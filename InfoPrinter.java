import java.util.*;

public class InfoPrinter {
    public static void printPartyInfo(List<Hero> party) {
        for (Hero h : party) {
            System.out.printf("Hero: %s lvl%d HP:%.1f MP:%.1f STR:%.1f DEX:%.1f AGI:%.1f Gold:%d Exp:%d\n",
                h.getName(), h.getLevel(), h.getHP(), h.mp, h.strength, h.dexterity, h.agility, h.getGold(), h.getExperience());
            System.out.println(" Inventory:");
            h.getInventory().print();
        }
    }
    public static void printPartyInfo(List<Hero> party, boolean compact) { printPartyInfo(party); }

    public static void printMonsterInfo(List<Monster> monsters) { 
        for (Monster m : monsters) {
            System.out.printf("Monster: %s lvl%d HP:%.1f STR:%.1f DEF:%d Doge:%.1f\n",
                m.getName(), m.getLevel(), m.getHP(), m.attackDamage(), m.getDefense(), m.getDodgeChance());
        }
    }
}

