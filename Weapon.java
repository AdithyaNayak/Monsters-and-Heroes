// Weapon.java
public class Weapon extends Item {
    private int damage;
    private int hands;
    public Weapon(String name,int price,int level,int damage,int hands){ super(name,price,level); this.damage=damage; this.hands=hands;}
    public int getDamage(){return damage;}
    public int getHands(){return hands;}
}