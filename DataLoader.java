import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class DataLoader {
    private final Path dir;

    public DataLoader(String dataDir) {
        this.dir = Paths.get(dataDir);
    }

    public List<Hero> loadHeroes() throws IOException {
        Path p = dir.resolve("heroes.txt");
        if (!Files.exists(p)) return defaultHeroes();
        return Files.lines(p).filter(s->!s.trim().isEmpty() && !s.startsWith("#"))
            .map(this::parseHero).collect(Collectors.toList());
    }

    private Hero parseHero(String line) {
        // format: name,type,level,str,dex,agi,exp
        String[] t = line.split(",");
        String name = t[0].trim();
        //String type = t[1].trim().toUpperCase();
        int level = Integer.parseInt(t[2].trim());
        int str = Integer.parseInt(t[3].trim());
        int dex = Integer.parseInt(t[4].trim());
        int agi = Integer.parseInt(t[5].trim());
        int exp = Integer.parseInt(t[6].trim());
        Hero h = new Hero(name, level, str, dex, agi);
        h.setExperience(exp);
        return h;
    }

    public List<Weapon> loadWeapons() throws IOException {
        Path p = dir.resolve("weapons.txt");
        if (!Files.exists(p)) return defaultWeapons();
        return Files.lines(p).filter(s->!s.trim().isEmpty() && !s.startsWith("#"))
            .map(this::parseWeapon).collect(Collectors.toList());
    }

    private Weapon parseWeapon(String line) {
        // name,price,level,damage,hands
        String[] t = line.split(",");
        return new Weapon(t[0].trim(), Integer.parseInt(t[1].trim()), Integer.parseInt(t[2].trim()), Integer.parseInt(t[3].trim()), Integer.parseInt(t[4].trim()));
    }

    public List<Armor> loadArmors() throws IOException {
        Path p = dir.resolve("armors.txt");
        if (!Files.exists(p)) return defaultArmors();
        return Files.lines(p).filter(s->!s.trim().isEmpty() && !s.startsWith("#"))
            .map(this::parseArmor).collect(Collectors.toList());
    }

    private Armor parseArmor(String line){
        String[] t = line.split(",");
        return new Armor(t[0].trim(), Integer.parseInt(t[1].trim()), Integer.parseInt(t[2].trim()), Integer.parseInt(t[3].trim()));
    }

    public List<Potion> loadPotions() throws IOException {
        Path p = dir.resolve("potions.txt");
        if (!Files.exists(p)) return defaultPotions();
        return Files.lines(p).filter(s->!s.trim().isEmpty() && !s.startsWith("#"))
            .map(this::parsePotion).collect(Collectors.toList());
    }

    private Potion parsePotion(String line){
        // name,price,level,stat,amount
        String[] t = line.split(",");
        return new Potion(t[0].trim(), Integer.parseInt(t[1].trim()), Integer.parseInt(t[2].trim()), t[3].trim(), Integer.parseInt(t[4].trim()));
    }

    public List<Spell> loadSpells() throws IOException {
        Path p = dir.resolve("spells.txt");
        if (!Files.exists(p)) return defaultSpells();
        return Files.lines(p).filter(s->!s.trim().isEmpty() && !s.startsWith("#"))
            .map(this::parseSpell).collect(Collectors.toList());
    }

    private Spell parseSpell(String line){
        // name,price,level,baseDamage,manaCost,type
        String[] t = line.split(",");
        return new Spell(t[0].trim(), Integer.parseInt(t[1].trim()), Integer.parseInt(t[2].trim()), Integer.parseInt(t[3].trim()), Integer.parseInt(t[4].trim()), SpellType.valueOf(t[5].trim().toUpperCase()));
    }

    public List<MonsterTemplate> loadMonsterTemplates() throws IOException {
        Path p = dir.resolve("monsters.txt");
        if (!Files.exists(p)) return defaultMonsters();
        return Files.lines(p).filter(s->!s.trim().isEmpty() && !s.startsWith("#"))
            .map(this::parseMonster).collect(Collectors.toList());
    }

    private MonsterTemplate parseMonster(String line){
        // name,baseDamage,defense,dodge
        String[] t = line.split(",");
        return new MonsterTemplate(t[0].trim(), Integer.parseInt(t[1].trim()), Integer.parseInt(t[2].trim()), Double.parseDouble(t[3].trim()));
    }

    // default small sets if no files present
    private List<Hero> defaultHeroes() {
        return Arrays.asList(
            new Hero("Karl the Barbarian", 1, 30, 10, 18),
            new Hero("Merlin", 1, 8, 30, 12),
            new Hero("Lancelot", 1, 25, 12, 15)
        );
    }
    private List<Weapon> defaultWeapons() {
        return Arrays.asList(new Weapon("Short Sword",100,1,20,1), new Weapon("Long Bow",200,2,35,2));
    }
    private List<Armor> defaultArmors() {
        return Arrays.asList(new Armor("Leather",80,1,10), new Armor("Chain",200,2,25));
    }
    private List<Potion> defaultPotions() {
        return Arrays.asList(new Potion("Health Potion",50,1,"HP",100));
    }
    private List<Spell> defaultSpells() {
        return Arrays.asList(new Spell("Fireball",120,1,40,20,SpellType.FIRE));
    }
    private List<MonsterTemplate> defaultMonsters() {
        return Arrays.asList(new MonsterTemplate("Red Dragon",50,20,0.05), new MonsterTemplate("Giant Bug",20,5,0.02));
    }
}

