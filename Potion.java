public class Potion extends Item {
    private String stat;
    private int amount;
    //private int quantity = 1; 
    public Potion(String name,int price,int level,String stat,int amount){ super(name,price,level); this.stat=stat; this.amount=amount;}
    public String getStat(){return stat;}
    public int getAmount(){return amount;}
}