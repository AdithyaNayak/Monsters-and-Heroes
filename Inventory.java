import java.util.*;



/*
public class Inventory {
    private final List<Item> items = new ArrayList<>();
    public void addItem(Item i){ items.add(i); }
    public void removeItem(Item i){ items.remove(i); }
    public List<Item> getItems(){ return items; }
    public void print() {
        if (items.isEmpty()) { System.out.println("Empty."); return; }
        for (int i=0;i<items.size();i++) System.out.printf("%d) %s\n", i+1, items.get(i));
    }
}
*/

public class Inventory {

    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() { return items; }

    public void addItem(Item it) {
        items.add(it);
    }

    public void removeItem(Item it) {
        items.remove(it);
    }

    public void print() {
        for (int i = 0; i < items.size(); i++)
            System.out.printf("%d) %s\n", i + 1, items.get(i));
    }

    // NEW: prevents duplicates of equipment/spells
    public boolean containsType(Item it) {
        for (Item inv : items) {
            if (inv.getClass() == it.getClass()
                && !(inv instanceof Potion)
                && inv.getName().equals(it.getName()))
                return true;
        }
        return false;
    }

    public int countItemName(String name) {
        int count = 0;
        for (Item i : items)
            if (i.getName().equalsIgnoreCase(name))
                count++;
        return count;
    }


}


