import java.util.*;

public class Game {
    public static void main(String[] args) throws Exception {
        System.out.println("Loading data...");
        DataLoader loader = new DataLoader("data");
        List<Hero> allHeroes = loader.loadHeroes();
        List<MonsterTemplate> monsterTemplates = loader.loadMonsterTemplates();
        List<Weapon> weapons = loader.loadWeapons();
        List<Armor> armors = loader.loadArmors();
        List<Potion> potions = loader.loadPotions();
        List<Spell> spells = loader.loadSpells();

        World world = new World(8, 8);
        world.randomizeTiles(0.20, 0.30, new Random());

        Score score = new Score();

        // create player party from available heroes (choose up to 3)
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose party size (1-3): ");
        int partySize = Math.max(1, Math.min(3, Integer.parseInt(sc.nextLine().trim())));
        List<Hero> party = new ArrayList<>();
        System.out.println("Available hero templates:");
        for (int i = 0; i < allHeroes.size(); i++) {
            System.out.printf("%d) %s (lvl %d)\n", i+1, allHeroes.get(i).getName(), allHeroes.get(i).getLevel());
        }
        for (int p = 0; p < partySize; p++) {
            System.out.printf("Pick hero %d index: ", p+1);
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            Hero template = allHeroes.get(idx);
            Hero hero = template.clone(); // deep copy
            // give starter gold
            hero.setGold(500);
            // give one starter weapon/armor if fits
            if (!weapons.isEmpty()) hero.getInventory().addItem(weapons.get(0));
            if (!armors.isEmpty()) hero.getInventory().addItem(armors.get(0));
            party.add(hero);
        }

        // create markets with data
        List<Market> markets = new ArrayList<>();
        Market defaultMarket = new Market("Market");
        weapons.forEach(defaultMarket::addItemForSale);
        armors.forEach(defaultMarket::addItemForSale);
        potions.forEach(defaultMarket::addItemForSale);
        spells.forEach(defaultMarket::addItemForSale);
        markets.add(defaultMarket);
        
        world.placeMarketAtRandom(markets.get(0), new Random());

        System.out.println("Starting game. Controls: W/A/S/D move, M enter market (if on market tile), I show info, Q quit");
        boolean running = true;
        while (running) {
            world.printMap(party);
            System.out.print("Command> ");
            String cmd = sc.nextLine().trim().toLowerCase();
            if (cmd.isEmpty()) continue;
            char c = cmd.charAt(0);
            switch (c) {
                case 'q':
                    running = false;
                    score.printFinalScore();
                    System.out.println("Goodbye!");
                    break;
                case 'w': case 'a': case 's': case 'd':
                    boolean moved = world.moveParty(party, c);
                    if (!moved) System.out.println("Can't move there.");
                    else {
                        score.moved();
                        TileType tile = world.getTileAtParty(party);
                        System.out.println("Moved to " + tile);
                        if (tile == TileType.COMMON) {
                            // random encounter
                            if (Math.random() < 0.4) { // 40% chance
                                System.out.println("A battle begins!");
                                // create monsters scaled to highest hero level
                                int highest = party.stream().mapToInt(Hero::getLevel).max().orElse(1);
                                List<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < party.size(); i++) {
                                    MonsterTemplate mt = monsterTemplates.get((int)(Math.random()*monsterTemplates.size()));
                                    monsters.add(new Monster(mt, highest));
                                }
                                Battle battle = new Battle(party, monsters, score);
                                boolean heroesWin = battle.runBattle(sc);
                                if (!heroesWin) {
                                    System.out.println("All heroes fainted. Game over.");
                                    score.printFinalScore();
                                    running = false;
                                } else {
                                    System.out.println("Heroes won!");
                                }
                            } else {
                                System.out.println("No monsters this time.");
                            }
                        } else if (tile == TileType.MARKET) {
                            System.out.println("You're on a market. Press M to enter market.");
                        } else {
                            // inaccessible won't happen because we block movement
                        }
                    }
                    break;
                case 'm':
                    if (world.getTileAtParty(party) == TileType.MARKET) {
                        Market market = world.getMarketAtParty(party);
                        if (market == null) {
                            System.out.println("Market not found.");
                        } else {
                            MarketUI.marketMenu(sc, market, party, score);
                        }
                    } else {
                        System.out.println("Not on a market tile.");
                    }
                    break;
                case 'i':
                    InfoPrinter.printPartyInfo(party);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }

        sc.close();
    }
}
