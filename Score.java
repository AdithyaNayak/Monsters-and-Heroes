import java.time.Duration;
import java.time.Instant;

public class Score {

    private int battlesWon = 0;
    private int monstersDefeated = 0;
    private int goldCollected = 0;
    private int stepsTaken = 0;
    private int itemsBought = 0;
    private int itemsSold = 0;
    private int heroFaints = 0;

    private final Instant startTime;

    public Score() {
        this.startTime = Instant.now();
    }

    // === Increment Methods ===
    public void addBattleWin() { battlesWon++; }
    public void addMonsterDefeat() { monstersDefeated++; }
    public void addGold(int g) { goldCollected += g; }
    public void moved() { stepsTaken++; }
    public void addItemBought() { itemsBought++; }
    public void addItemSold() { itemsSold++; }
    public void addHeroFaint() { heroFaints++; }

    // === Calculate total score
    public int calculateFinalScore() {
        return battlesWon * 50
                + monstersDefeated * 20
                + goldCollected / 10
                + stepsTaken * 2
                + itemsBought * 5
                + itemsSold * 3
                - heroFaints * 15;
    }

    // === Print final score screen ===
    public void printFinalScore() {
        Duration playTime = Duration.between(startTime, Instant.now());

        System.out.println("\n===============================");
        System.out.println("         GAME OVER");
        System.out.println("===============================");

        System.out.println("Time Played: " + formatDuration(playTime));
        System.out.println("Battles Won: " + battlesWon);
        System.out.println("Monsters Defeated: " + monstersDefeated);
        System.out.println("Gold Collected: " + goldCollected);
        System.out.println("Steps Taken: " + stepsTaken);
        System.out.println("Items Bought: " + itemsBought);
        System.out.println("Items Sold: " + itemsSold);
        System.out.println("Hero Faints: " + heroFaints);

        System.out.println("-------------------------------");
        System.out.println("FINAL SCORE: " + calculateFinalScore());
        System.out.println("===============================\n");
    }

    private String formatDuration(Duration d) {
        long h = d.toHours();
        long m = d.toMinutesPart();
        long s = d.toSecondsPart();
        return String.format("%02dh:%02dm:%02ds", h, m, s);
    }
}
