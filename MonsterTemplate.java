public class MonsterTemplate {
    private final String name;
    private final int baseDamage;
    private final int defense;
    private final double dodge;

    public MonsterTemplate(String name, int baseDamage, int defense, double dodge) {
        this.name = name; this.baseDamage = baseDamage; this.defense = defense; this.dodge = dodge;
    }
    public String getName(){return name;}
    public int getBaseDamage(){return baseDamage;}
    public int getDefense(){return defense;}
    public double getDodge(){return dodge;}
}