// Armor.java
public class Armor extends Item {
    private int damageReduction;
    public Armor(String name,int price,int level,int dr){ super(name,price,level); this.damageReduction=dr; }
    public int getDamageReduction(){return damageReduction;}
}