import java.util.Scanner;

class Hero extends Character implements Cloneable {
    private String heroType;
    private int experience = 0;
    private int gold = 0;
    private Inventory inventory = new Inventory();
    private Armor equippedArmor = null;

    private Weapon weaponSlot1 = null;
    private Weapon weaponSlot2 = null;
    private boolean twoHanding = false;

    public Hero(String name, int level, double str, double dex, double agi) {
        super(name, level, str, dex, agi);
        this.heroType = "WARRIOR";
    }
    public Hero(String name, int level, double str, double dex, double agi, String type) {
        super(name, level, str, dex, agi);
        this.heroType = type;
    }

    public void setExperience(int e) { experience = e; }
    public void setGold(int g) { gold = g; }
    public int getGold() { return gold; }
    public void addGold(int g) { gold += g; }
    public int getExperience() { return experience; }
    public Inventory getInventory() { return inventory; }

    public double attackDamage() {
        double base = strength;
        double weaponDamage = 0;

        if (twoHanding && weaponSlot1 != null && weaponSlot2 != null) {
            weaponDamage = weaponSlot1.getDamage() * 1.5;
        } else {
            if (weaponSlot1 != null) weaponDamage += weaponSlot1.getDamage();
            if (weaponSlot2 != null) weaponDamage += weaponSlot2.getDamage();
        }

        return (base + weaponDamage) * 0.5;
    }

    public double spellDamage(Spell s) {
        return s.getBaseDamage() + (dexterity/10000.0)*s.getBaseDamage();
    }


    // ****************************************************************
    // **                   FIXED EQUIP LOGIC BELOW                   **
    // ****************************************************************
    public void equipWeapon(Scanner sc, Weapon w) {
        // === Step 1: If two-handing and new weapon is one-handed, free both slots ===
        if (twoHanding && w.getHands() == 1) {
            System.out.println("You stop two-handing " + weaponSlot1.getName() + ".");
            unequipTwoHanded();
        }

        if (w == null) return;

        // === NEW: refuse to equip the same weapon instance twice ===
        if (weaponSlot1 == w && weaponSlot2 == w) {
            System.out.printf("%s is already two-handed by %s.\n", w.getName(), name);
            System.out.printf("Unequip it? (y/n)\n");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y")) {
                unequipTwoHanded();
            }
            return;
        }
        if (weaponSlot1 == w && weaponSlot2 != w) {
            System.out.printf("%s is already equipped in slot 1 for %s.\n", w.getName(), name);
            System.out.printf("Unequip it? (y/n)\n");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y")) {
                unequipTwoHanded();
            }
            return;
        }
        if (weaponSlot2 == w && weaponSlot1 != w) {
            System.out.printf("%s is already equipped in slot 2 for %s.\n", w.getName(), name);
            System.out.printf("Unequip it? (y/n)\n");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y")) {
                unequipTwoHanded();
            }
            return;
        }

        boolean slot1Empty = (weaponSlot1 == null);
        boolean slot2Empty = (weaponSlot2 == null);

        // CASE: this is a two-handed weapon
        if (w.getHands() == 2) {
            // If the same instance is already two-handed, we already returned above.
            weaponSlot1 = w;
            weaponSlot2 = w;
            twoHanding = true;
            System.out.printf("%s equipped %s (two-handed weapon)\n", name, w.getName());
            return;
        }

        // BOTH EMPTY
        if (slot1Empty && slot2Empty) {
            System.out.println("Equip weapon in:");
            System.out.println("1) Slot 1");
            System.out.println("2) Slot 2");
            System.out.println("3) Two-hand it (take both slots)");
            String cmd = sc.nextLine().trim();

            switch (cmd) {
                case "1":
                    weaponSlot1 = w;
                    twoHanding = false;
                    break;
                case "2":
                    weaponSlot2 = w;
                    twoHanding = false;
                    break;
                case "3":
                    weaponSlot1 = w;
                    weaponSlot2 = w;
                    twoHanding = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            System.out.printf("%s equipped %s\n", name, w.getName());
            return;
        }

        // EXACTLY ONE EMPTY
        if (!slot1Empty && slot2Empty) {
            handleOneSlotOccupied(sc, w, "slot1");
            return;
        }

        if (slot1Empty && !slot2Empty) {
            handleOneSlotOccupied(sc, w, "slot2");
            return;
        }

        // BOTH FULL
        if (!slot1Empty && !slot2Empty) {

            // If currently two-handing the same weapon instance we'd have returned already.
            if (twoHanding) {
                System.out.println("You are currently two-handing " + weaponSlot1.getName() + ".");
                System.out.println("What do you want to do?");
                System.out.println("1) Replace slot 1");
                System.out.println("2) Replace slot 2");
                System.out.println("3) Stop two-handing and free slot 2");
                System.out.println("4) Two-hand this new weapon instead");

                String cmd = sc.nextLine().trim();

                switch (cmd) {
                    case "1":
                        // prevent replacing slot1 with the same instance already in slot2 (rare but safe)
                        if (weaponSlot2 == w) {
                            System.out.println("That weapon is already equipped in slot 2.");
                            return;
                        }
                        weaponSlot1 = w;
                        twoHanding = false;
                        break;

                    case "2":
                        if (weaponSlot1 == w) {
                            System.out.println("That weapon is already equipped in slot 1.");
                            return;
                        }
                        weaponSlot2 = w;
                        twoHanding = false;
                        break;

                    case "3":
                        // stop two-handing and equip the new weapon into slot1 (free slot2)
                        weaponSlot2 = null;
                        weaponSlot1 = w;
                        twoHanding = false;
                        break;

                    case "4":
                        // two-hand new weapon
                        weaponSlot1 = w;
                        weaponSlot2 = w;
                        twoHanding = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                        return;
                }

                System.out.printf("%s equipped %s\n", name, w.getName());
                return;
            }

            // Normal both-full case (not currently two-handing)
            System.out.println("Both weapon slots are full.");
            System.out.println("1) Replace slot 1");
            System.out.println("2) Replace slot 2");
            System.out.println("3) Two-hand this weapon (replace both)");

            String cmd = sc.nextLine().trim();

            switch (cmd) {
                case "1":
                    // if replacing slot1 with the same instance already in slot2, block
                    if (weaponSlot2 == w) {
                        System.out.println("That weapon is already equipped in slot 2.");
                        return;
                    }
                    weaponSlot1 = w;
                    twoHanding = false;
                    break;
                case "2":
                    if (weaponSlot1 == w) {
                        System.out.println("That weapon is already equipped in slot 1.");
                        return;
                    }
                    weaponSlot2 = w;
                    twoHanding = false;
                    break;
                case "3":
                    weaponSlot1 = w;
                    weaponSlot2 = w;
                    twoHanding = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            System.out.printf("%s equipped %s\n", name, w.getName());
        }
    }


    private void handleOneSlotOccupied(Scanner sc, Weapon w, String occupiedSlot) {
        // occupiedSlot is the one that already contains a weapon (either "slot1" or "slot2")
        System.out.printf("You have a weapon in %s. What do you want to do?\n", occupiedSlot);
        System.out.println("1) Equip in the empty slot");
        System.out.println("2) Replace the existing weapon");
        System.out.println("3) Two-hand this weapon (replace both)");

        String cmd = sc.nextLine().trim();

        switch (cmd) {
            case "1":  // Equip in empty slot
                if (occupiedSlot.equals("slot1")) {
                    // if weapon is same instance as in slot1, prevent equipping into slot2
                    if (weaponSlot1 == w) {
                        System.out.println("That weapon is already equipped in slot 1; can't equip same instance in slot 2.");
                        return;
                    }
                    weaponSlot2 = w;
                } else {
                    if (weaponSlot2 == w) {
                        System.out.println("That weapon is already equipped in slot 2; can't equip same instance in slot 1.");
                        return;
                    }
                    weaponSlot1 = w;
                }
                twoHanding = false;
                break;

            case "2":  // Replace existing slot
                if (occupiedSlot.equals("slot1")) {
                    weaponSlot1 = w;
                } else {
                    weaponSlot2 = w;
                }
                twoHanding = false;
                break;

            case "3":  // Two-hand
                weaponSlot1 = w;
                weaponSlot2 = w;
                twoHanding = true;
                break;

            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.printf("%s equipped %s\n", name, w.getName());
    }


/*
    public void unequipTwoHanded() {
        if (twoHanding) {
            weaponSlot1 = null;
            weaponSlot2 = null;
            twoHanding = false;
        }
    }
*/
    public void unequipTwoHanded() {
        weaponSlot1 = null;
        weaponSlot2 = null;
        twoHanding = false;
    }



    public void equipArmor(Armor a) {
        if (a==null) return;
        equippedArmor = a;
        System.out.printf("%s equipped %s\n", name, a.getName());
    }

    public double reduceDamage(double incoming) {
        double red = equippedArmor==null?0:equippedArmor.getDamageReduction();
        return Math.max(0, incoming - red*0.4);
    }

    public void gainExperience(int e) {
        experience += e;
        while (experience >= level * 10) {
            experience -= level*10;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        strength *= 1.05;
        dexterity *= 1.05;
        agility *= 1.05;

        if (heroType.equalsIgnoreCase("WARRIOR")) {
            strength *= 1.05;
            agility *= 1.05;
        }
        if (heroType.equalsIgnoreCase("SORCERER")) {
            dexterity *= 1.05;
            agility *= 1.05;
        }
        if (heroType.equalsIgnoreCase("PALADIN")) {
            strength *= 1.05;
            dexterity *= 1.05;
        }

        this.hp = level * 100;
        this.mp = this.mp * 1.1;
        System.out.printf("%s leveled up to %d!\n", name, level);
    }

    @Override
    public Hero clone() {
        Hero h = new Hero(this.name, this.level, this.strength, this.dexterity, this.agility, this.heroType);
        h.setExperience(this.experience);
        h.setGold(this.gold);
        return h;
    }
}
