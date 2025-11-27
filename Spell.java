public class Spell extends Item {
    private int baseDamage;
    private int manaCost;
    private SpellType type;
    public Spell(String name,int price,int level,int baseDamage,int manaCost,SpellType type){ super(name,price,level); this.baseDamage=baseDamage; this.manaCost=manaCost; this.type=type;}
    public int getBaseDamage(){return baseDamage;}
    public int getManaCost(){return manaCost;}
    public SpellType getType(){return type;}
}