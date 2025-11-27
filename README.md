
# Monsters-and-Heroes
## CS611 – Assignment 4
- Name: Adithya Darshan Nayak
- Email: adithyad@bu.edu
- Student ID: U48974468

## Project structure

Top-level files (important classes):

- `Game.java` — Main entry point.
  - Loads all data (heroes, monsters, weapons, armors, spells, potions) from text files.
  - Creates the world, initializes party selection, manages movement, encounters, and market access.
  - Handles the global game loop, including quitting and triggering battles or markets.
    
- `World.java` — Generates and manages the game map.
  - Creates an 8×8 world with common, inaccessible, and market tiles.
  - Handles party positioning, movement validation, and tile lookups.
  - Prints a stylized ASCII map of the world.
    
- `Character.java` — base class for characters (heroes & monsters).
  
- `Hero.java` — Represents a playable hero.
  - Tracks HP, MP, strength, dexterity, agility, gold, level, experience.
  - Manages inventory, equipping, leveling, damage calculation, and stat bonuses.
  - Supports cloning for template-based hero selection.
  
- `Monster.java` & `MonsterTemplate.java` — Defines enemy monster classes.
  - Stats scale to match the highest-level hero.
  - Supports dodge, defense, damage reduction, and stat debuffs from spells.
  
- `Battle.java` — Implements the full party-vs-full party battle system.
  - Turn-based combat loop.
  - Heroes choose: Attack, Cast Spell, Use Potion, Equip Gear, or View Info.
  - Monsters attack one random hero per turn.
  - Supports spell status reductions, dodge chance, defense reduction, and post-round regen.
  - Rewards distributed after victory.
  
- `Market.java` - Represents a store within the world.
  - Stores items available for purchase (weapons, armor, spells, potions).
  - Allows infinite stock for all items.
  - Potions can be bought multiple times; heroes cannot buy duplicate weapons/armor/spells.
    
- `MarketUI.java` - Handles all market interactions.
  - Viewing store stock
  - Buying items
  - Selling items
  - Ensures heroes only buy duplicate-allowed items (potions)
  
- `StockEntry.java` — market/shop system.
  
- `Item.java` — Base class for items.
  
- `Weapon.java`, `Armor.java`, `Potion.java`, `Spell.java`, `SpellType.java` — Defines all purchasable and usable items.
  - Weapon: damage, hands required
  - Armor: defense
  - Potion: stat boosts (HP/MP/Strength/Dexterity/Agility)
  - Spell: base damage, mana cost, spell type (Fire/Ice/Lightning)
  
- `Inventory.java` — Stores and manages hero items.
  - Prevents duplicates for weapons/armor/spells.
  - Allows stacking potions.
  - Supports adding/removing items and printing inventory contents.
  
- `Score.java` — Tracks and prints the final score at game over.
  - Tracks metrics such as number of battles won, moves taken, gold earned, etc.
  - Printed independently (not called from Game.java).
  
- `DataLoader.java` — Reads all game assets from text files in the data/ directory.
  - Parses heroes, monsters, weapons, armors, potions, spells.
  
- `InfoPrinter.java` — Utility for printing hero & monster stats cleanly.
  - Used during exploration and in battle context.
    
Data files are stored in the `data/` folder and loaded at runtime. 

## How to compile and run

Open a terminal in the project root (where `Game.java` is located).

1. Compile all Java sources:

```bash
javac *.java
```

2. Run the game (entrypoint `Game`):

```bash
java Game
```

## Data files

The `data/` directory contains plain text data files used to populate weapons, armors, potions, spells, monsters and heroes:

- `weapons.txt`
- `armors.txt`
- `potions.txt`
- `spells.txt`
- `monsters.txt`
- `heroes.txt`

These files are parsed by `DataLoader.java`.

## Design Patterns

- Factory Pattern
  - DataLoader:
      - It loads heroes, monsters, weapons, armors, potions, and spells from text files.
      - DataLoader acts like a factory because it creates fully constructed objects based on input data.
        
  - Prototype Pattern
    - Hero.clone()
    - Maintains hero templates (allHeroes) and clones them to create actual playable heroes:

    ```bash
    Hero template = allHeroes.get(idx);
    Hero hero = template.clone();
    ```
    
    - Not constructing a new hero from scratch.
    - You duplicate an existing one.


## Quick gameplay summary

- Choose your party size.
- Select your hero(s).
- Travel the world and encounter markets and monster battles.
- Buy/sell items in markets.
- Equip weapons and armor, use potions, and cast spells in battle.
- Level up heroes, increase stats, and check your score at the end of the game.

## Gameplay tips

- Each Hero has starting gear, so don't forget to equip them on your first monster encounter.
- It's a good idea to make it to the nearest Market and stock up on health potions (and mana potions if you're a Sorcerer).
- Always better to two-hand a one hand weapon if its the only one you have.
- Sorcerers are pretty useless without they're spells, so make sure to purchase them when you get to a market.


