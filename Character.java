abstract class Character {
    protected String name;
    protected int level;
    protected double hp;
    protected double mp;
    protected double strength;
    protected double dexterity;
    protected double agility;
    protected boolean fainted = false;

    public Character(String name, int level, double str, double dex, double agi) {
        this.name = name; this.level = level; this.strength = str; this.dexterity = dex; this.agility = agi;
        this.hp = level * 100;
        this.mp = 50 + level*10;
    }

    public String getName(){return name;}
    public int getLevel(){return level;}
    public double getHP(){return hp;}
    public boolean isFainted(){return fainted;}
    public void receiveDamage(double dmg) {
        if (Math.random() < getDodgeChance()) {
            System.out.printf("%s dodged the attack!\n", name);
            return;
        }
        hp -= dmg;
        if (hp <= 0) { hp = 0; fainted = true; System.out.printf("%s fainted!\n", name); }
    }
    public double getDodgeChance() { return agility * 0.002; } // heroes
    public void endRoundRegen() {
        if (!fainted) {
            hp = hp * 1.1;
            mp = mp * 1.1;
        }
    }
}