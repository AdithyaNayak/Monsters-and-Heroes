import java.util.*;


/*
public class Market {
    private String name;
    private List<Item> stock = new ArrayList<>();
    public Market(String name){ this.name = name; }
    public void addItemForSale(Item it){ stock.add(it); }
    public List<Item> getStock(){ return stock; }
    public String getName(){ return name; }
}
*/

public class Market {
    private String name;
    private List<Item> stock = new ArrayList<>();

    public Market(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    // Accept ANY item type because all extend Item
    public void addItemForSale(Item it) {
        stock.add(it);
    }

    // Market stock is infinite, so do NOT remove items when bought
    public List<Item> getStock() {
        return stock;
    }
}




