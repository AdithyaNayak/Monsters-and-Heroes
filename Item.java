public abstract class Item {
    protected String name;
    protected int price;
    protected int level;

    public Item(String name, int price, int level) {
        this.name = name;
        this.price = price;
        this.level = level;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getLevel() { return level; }

    @Override
    public String toString() {
        return name + " (lvl " + level + ") $" + price;
    }
}
