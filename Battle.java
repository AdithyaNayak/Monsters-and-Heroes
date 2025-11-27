import java.util.*;
import java.util.stream.*;

public class Battle {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private Score score;


    public Battle(List<Hero> heroes, List<Monster> monsters, Score score) {
        // clone hero references (battle uses same hero objects)
        this.heroes = heroes;
        this.monsters = monsters;
        this.score = score;
        System.out.printf("Encounter: %d heroes vs %d monsters\n", heroes.size(), monsters.size());
        for (Monster m : monsters) System.out.printf(" - %s (lvl %d)\n", m.getName(), m.getLevel());
    }

    

    public boolean runBattle(Scanner sc) {
        // revive fainted status for battle start? assume no
        while (true) {
            // check end
            if (monsters.stream().allMatch(Monster::isFainted)) {
                allocateRewards();
                score.addBattleWin();
                // revive fainted heroes with half HP+MP, but they don't gain XP/gold
                heroes.forEach(h -> { if (h.isFainted()) { h.fainted = false; h.hp = h.getLevel()*100/2; h.mp = 20; }});
                return true;
            }
            if (heroes.stream().allMatch(Hero::isFainted)) {
                List<Monster> killedMonsters = monsters.stream().filter(m->m.isFainted()).collect(Collectors.toList()); //validate
                for (Monster m : killedMonsters) {
                    score.addMonsterDefeat();
                }
                return false;
            }

            // HERO TURN: for each alive hero choose action
            for (int hi=0; hi<heroes.size(); hi++) {
                Hero h = heroes.get(hi);
                if (h.isFainted()) continue;
                System.out.printf("Hero %s's turn. HP: %.1f MP: %.1f\n", h.getName(), h.getHP(), h.mp);
                boolean acted = false;
                while (!acted) {
                    System.out.println("Choose action: 1) Attack 2) Cast Spell 3) Use Potion 4) Equip (weapon/armor) 5) Info 6) Quit");
                    String line = sc.nextLine().trim();
                    if (line.equals("1")) {

                        if (monsters.stream().allMatch(Monster::isFainted)) { 
                            allocateRewards();
                            score.addBattleWin();
                            //continue;
                            return true;
                        }

                        // list monsters
                        List<Monster> targets = monsters.stream().filter(m->!m.isFainted()).collect(Collectors.toList());
                        for (int i=0;i<targets.size();i++) System.out.printf("%d) %s HP:%.1f\n", i+1, targets.get(i).getName(), targets.get(i).getHP());
                        System.out.print("Choose target index: ");
                        int ti = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (ti < 0 || ti >= targets.size()) { System.out.println("Invalid index."); continue; } 
                        Monster target = targets.get(ti);
                        double dmg = h.attackDamage();
                        //System.out.printf("damage = %.1f\n", dmg);
                        dmg = Math.max(0, dmg - target.getDefense()*0.05); // simplistic defense
                        System.out.printf("%s attacked %s for %.1f damage!\n", h.getName(), target.getName(), dmg);
                        target.receiveDamage(dmg);
                        System.out.printf("%s's HP is now %.1f\n", target.getName(), target.getHP());
                        acted = true;
                    } else if (line.equals("2")) {

                        if (monsters.stream().allMatch(Monster::isFainted)) { 
                            allocateRewards();
                            score.addBattleWin();
                            //continue;
                            return true;
                        }

                        // list spells in inventory
                        List<Spell> spells = h.getInventory().getItems().stream().filter(it->it instanceof Spell).map(it->(Spell)it).collect(Collectors.toList());
                        if (spells.isEmpty()) { System.out.println("No spells."); continue; }
                        for (int i=0;i<spells.size();i++) System.out.printf("%d) %s (DMG %d MP %d)\n", i+1, spells.get(i).getName(), spells.get(i).getBaseDamage(), spells.get(i).getManaCost());
                        System.out.print("Choose spell index: ");
                        int si = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (si < 0 || si >= spells.size()) { System.out.println("Invalid index."); continue; } 
                        Spell s = spells.get(si);
                        if (h.mp < s.getManaCost()) { System.out.println("Not enough MP."); continue; }
                        // choose target
                        List<Monster> targets = monsters.stream().filter(m->!m.isFainted()).collect(Collectors.toList());
                        for (int i=0;i<targets.size();i++) System.out.printf("%d) %s HP:%.1f\n", i+1, targets.get(i).getName(), targets.get(i).getHP());
                        int ti = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (ti < 0 || ti >= targets.size()) { System.out.println("Invalid index."); continue; } 
                        Monster target = targets.get(ti);
                        double dmg = h.spellDamage(s);
                        System.out.printf("%s cast %s on %s for %.1f damage!\n", h.getName(), s.getName(), target.getName(), dmg);
                        target.receiveDamage(dmg);
                        // apply spell effect
                        applySpellEffect(s, target);
                        h.mp -= s.getManaCost();
                        // remove spell if single-use
                        //h.getInventory().removeItem(s);
                        acted = true;
                    } else if (line.equals("3")) {
                        // potions
                        List<Potion> potions = h.getInventory().getItems().stream().filter(it->it instanceof Potion).map(it->(Potion)it).collect(Collectors.toList());
                        if (potions.isEmpty()) { System.out.println("No potions."); continue; }
                        for (int i=0;i<potions.size();i++) System.out.printf("%d) %s (%s +%d)\n", i+1, potions.get(i).getName(), potions.get(i).getStat(), potions.get(i).getAmount());
                        int pi = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (pi < 0 || pi >= potions.size()) { System.out.println("Invalid index."); continue; } 
                        Potion p = potions.get(pi);
                        usePotion(h, p);
                        h.getInventory().removeItem(p);
                        acted = true;
                    } else if (line.equals("4")) {
                        // equip
                        List<Weapon> ws = h.getInventory().getItems().stream().filter(it->it instanceof Weapon).map(it->(Weapon)it).collect(Collectors.toList());
                        List<Armor> as = h.getInventory().getItems().stream().filter(it->it instanceof Armor).map(it->(Armor)it).collect(Collectors.toList());
                        if (!ws.isEmpty()) {
                            System.out.println("Weapons:");
                            for (int i=0;i<ws.size();i++) System.out.printf("%d) %s\n", i+1, ws.get(i));
                            int wi = Integer.parseInt(sc.nextLine().trim()) - 1;
                            if (wi < 0 || wi >= ws.size()) { System.out.println("Invalid index."); continue; } 
                            //sc.nextLine(); // validate
                            h.equipWeapon(sc, ws.get(wi));
                        }
                        if (!as.isEmpty()) {
                            System.out.println("Armors:");
                            for (int i=0;i<as.size();i++) System.out.printf("%d) %s\n", i+1, as.get(i));
                            int ai = Integer.parseInt(sc.nextLine().trim()) - 1;
                            if (ai < 0 || ai >= as.size()) { System.out.println("Invalid index."); continue; } 
                            //sc.nextLine(); // validate
                            h.equipArmor(as.get(ai));
                        }
                        //acted = true;
                    } else if (line.equals("5")) {
                        InfoPrinter.printPartyInfo(heroes);
                        InfoPrinter.printMonsterInfo(monsters);
                    } else if (line.equals("6")) {
                        System.out.println("Goodbye!");
                        return false;
                    } else {
                        System.out.println("Invalid.");
                    }
                } // end hero action loop
            } // end all hero turns

            // MONSTER TURN
            for (Monster m : monsters) {
                if (m.isFainted()) continue;

                if (heroes.stream().allMatch(Hero::isFainted)) { 
                    continue;
                    //return false;
                }

                // choose alive hero target randomly
                List<Hero> alive = heroes.stream().filter(h->!h.isFainted()).collect(Collectors.toList());
                if (alive.isEmpty()) break;
                Hero target = alive.get(new Random().nextInt(alive.size()));
                double dmg = m.attackDamage();
                dmg = Math.max(0, target.reduceDamage(dmg));
                //System.out.printf("damage = %.1f\n", dmg);
                System.out.printf("%s attacked %s for %.1f damage!\n", m.getName(), target.getName(), dmg);
                //double after = target.reduceDamage(dmg);
                //target.receiveDamage(after);
                target.receiveDamage(dmg);
            }

            // end of round: regen for heroes that haven't fainted, revive rules handled at end
            //for (Hero h : heroes) h.endRoundRegen();
            //for (Monster m : monsters) { if (!m.isFainted()) m.endRoundRegen(); }
        }
    }

    private void usePotion(Hero h, Potion p) {
        String stat = p.getStat().toLowerCase();
        int amt = p.getAmount();

        switch (stat) {
            case "hp":
                h.hp += amt;
                System.out.printf("%s used %s and gained +%d HP!\n", h.getName(), p.getName(), amt);
                break;

            case "mp":
                h.mp += amt;
                System.out.printf("%s used %s and gained +%d MP!\n", h.getName(), p.getName(), amt);
                break;

            case "strength":
                h.strength += amt;
                System.out.printf("%s used %s and gained +%d Strength!\n", h.getName(), p.getName(), amt);
                break;

            case "dexterity":
                h.dexterity += amt;
                System.out.printf("%s used %s and gained +%d Dexterity!\n", h.getName(), p.getName(), amt);
                break;

            case "agility":
                h.agility += amt;
                System.out.printf("%s used %s and gained +%d Agility!\n", h.getName(), p.getName(), amt);
                break;

            default:
                System.out.println("Potion stat not recognized: " + stat);
        }
    }



    private void allocateRewards() {
        int highestHeroLevel = heroes.stream().mapToInt(Hero::getLevel).max().orElse(1);
        int numMonsters = monsters.size();

        for (int i = 0; i < numMonsters; i++) {
            score.addMonsterDefeat();
        }

        for (Hero h : heroes) {
            if (!h.isFainted()) {
                // gold = monster_level * 100 per monster
                int goldGain = highestHeroLevel * 100;
                int expGain = numMonsters * 4;
                score.addGold(goldGain);
                h.addGold(goldGain);
                h.gainExperience(expGain);
                System.out.printf("%s gained %d gold and %d exp.\n", h.getName(), goldGain, expGain);
            } else {
                System.out.printf("%s fainted and gains no reward.\n", h.getName());
            }
        }
    }

    private void applySpellEffect(Spell s, Monster target) {
        double pct = 0.1; // 10% effect
        switch (s.getType()) {
            case ICE:
                target.reduceStatByPercent(pct); // reduce damage
                break;
            case FIRE:
                // reduce defense: modeled same
                target.reduceStatByPercent(pct);
                break;
            case LIGHTNING:
                target.reduceStatByPercent(pct);
                break;
        }
    }
}
