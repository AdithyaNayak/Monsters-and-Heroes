import java.util.*;

class MarketUI {

    //private static Score score = new Score();

    public static void marketMenu(Scanner sc, Market market, List<Hero> party, Score score) {
        boolean inMarket = true;

        while (inMarket) {
            //System.out.printf("=== Market: %s ===\n", market.getName());
            System.out.printf("=== Welcome to the Market! ===\n");
            System.out.println("1) View stock  2) Buy  3) Sell  4) Exit");
            String cmd = sc.nextLine().trim();
            if (!cmd.equals("1") && !cmd.equals("2") && !cmd.equals("3") && !cmd.equals("4")) {
                System.out.println("Invalid index.");
                continue;
            }

            switch (cmd) {
                case "1": showStock(market); break;
                case "2": buyFlow(sc, market, party, score); break;
                case "3": sellFlow(sc, market, party, score); break;
                default: inMarket = false;
            }
        }
    }

    private static void showStock(Market market) {
        List<Item> stock = market.getStock();
        if (stock.isEmpty()) {
            System.out.println("Market has no items!");
            return;
        }

        for (int i = 0; i < stock.size(); i++) {
            Item it = stock.get(i);
            System.out.printf("%d) %s (lvl %d) $%d\n",
                i + 1, it.getName(), it.getLevel(), it.getPrice());
        }
    }

    private static void buyFlow(Scanner sc, Market market, List<Hero> party, Score score) {
        System.out.println("Choose hero index:");
        for (int i = 0; i < party.size(); i++)
            System.out.printf("%d) %s ($%d)\n", i + 1, party.get(i).getName(), party.get(i).getGold());

        int hi = Integer.parseInt(sc.nextLine().trim()) - 1;
        if (hi < 0 || hi >= party.size()) { System.out.println("Invalid hero."); return; }

        Hero h = party.get(hi);
        List<Item> stock = market.getStock();

        showStock(market);
        System.out.println("Choose item index to buy:");
        int ii = Integer.parseInt(sc.nextLine().trim()) - 1;
        if (ii < 0 || ii >= stock.size()) { System.out.println("Invalid item."); return; }

        Item it = stock.get(ii);

        // Level check
        if (it.getLevel() > h.getLevel()) {
            System.out.println("Hero level too low.");
            return;
        }

        // Gold check
        if (it.getPrice() > h.getGold()) {
            System.out.println("Not enough gold.");
            return;
        }



        /*
        // Prevent duplicates for equipment/spells
        if (!(it instanceof Potion)) {
            if (h.getInventory().containsType(it)) {
                System.out.println("You already own this item!");
                return;
            }
        }        
        */


        if (!(it instanceof Potion)) {
            if (it instanceof Weapon) {
                Weapon w = (Weapon) it;

                // Count copies of this weapon
                int count = h.getInventory().countItemName(it.getName());

                if (w.getHands() == 1 && count >= 1) {
                    System.out.println("You already own this 1-handed weapon.");
                    return;
                }

                if (w.getHands() == 2 && count >= 1) {
                    System.out.println("You already own this 2-handed weapon.");
                    return;
                }
            }
            else {
                // Armor/Spell/etc → only 1 allowed
                if (h.getInventory().containsType(it)) {
                    System.out.println("You already own this item!");
                    return;
                }
            }
        }


        // Buy item
        h.addGold(-it.getPrice());
        h.getInventory().addItem(it);

        System.out.printf("%s bought %s\n", h.getName(), it.getName());
        score.addItemBought();

        // Option to equip immediately
        if (it instanceof Weapon || it instanceof Armor) {
            System.out.println("Equip now? (y/n)");
            String ans = sc.nextLine().trim().toLowerCase();

            if (ans.equals("y")) {
                if (it instanceof Weapon) {
                    h.equipWeapon(sc, (Weapon) it); // <- your 1-hand / 2-hand logic
                } else if (it instanceof Armor) {
                    h.equipArmor((Armor) it);
                }
            }
        }

    }

    private static void sellFlow(Scanner sc, Market market, List<Hero> party, Score score) {
        System.out.println("Choose hero index:");
        for (int i = 0; i < party.size(); i++)
            System.out.printf("%d) %s ($%d)\n", i + 1, party.get(i).getName(), party.get(i).getGold());

        int hi = Integer.parseInt(sc.nextLine().trim()) - 1;
        if (hi < 0 || hi >= party.size()) { System.out.println("Invalid hero."); return; }

        Hero h = party.get(hi);

        System.out.println("Inventory:");
        h.getInventory().print();

        List<Item> items = h.getInventory().getItems();
        if (items.isEmpty()) { System.out.println("Nothing to sell."); return; }

        System.out.println("Choose item to sell:");
        int ii = Integer.parseInt(sc.nextLine().trim()) - 1;

        if (ii < 0 || ii >= items.size()) { System.out.println("Invalid index."); return; }

        Item it = items.get(ii);

        int sellPrice = it.getPrice() / 2;
        h.addGold(sellPrice);
        h.getInventory().removeItem(it);

        System.out.printf("%s sold %s for $%d\n", h.getName(), it.getName(), sellPrice);
        score.addItemSold();
    }
}
