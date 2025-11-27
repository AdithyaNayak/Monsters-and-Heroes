import java.util.*;

enum TileType { INACCESSIBLE, MARKET, COMMON }

class Tile {
    TileType type;
    Market market; // if market
    Tile(TileType t) { this.type = t; }
}

public class World {
    private final int rows, cols;
    private Tile[][] grid;
    private int partyR = 0, partyC = 0;
    private Market placedMarket = null;
    //private Score score;

    public World(int r, int c) {
        this.rows = r; this.cols = c;
        grid = new Tile[r][c];
        // default fill common
        for (int i=0;i<r;i++) for (int j=0;j<c;j++) grid[i][j] = new Tile(TileType.COMMON);
        partyR = r/2; partyC = c/2;
    }

    public void randomizeTiles(double inaccessibleFrac, double marketFrac, Random rnd) {
        int total = rows*cols;
        int inc = (int)(total * inaccessibleFrac);
        int mkt = (int)(total * marketFrac);
        // start with all common
        List<Integer> indices = new ArrayList<>();
        for (int i=0;i<total;i++) indices.add(i);
        Collections.shuffle(indices, rnd);
        for (int k=0;k<inc;k++) {
            int idx = indices.get(k);
            grid[idx/cols][idx%cols] = new Tile(TileType.INACCESSIBLE);
        }
        for (int k=inc;k<inc+mkt;k++) {
            int idx = indices.get(k);
            grid[idx/cols][idx%cols] = new Tile(TileType.MARKET);
        }
        // ensure starting tile accessible
        grid[partyR][partyC] = new Tile(TileType.COMMON);
    }

    /*
        public void placeMarketAtRandom(Market market, Random rnd) {
        // find a market tile
        List<int[]> candidates = new ArrayList<>();
        for (int i=0;i<rows;i++) for (int j=0;j<cols;j++) if (grid[i][j].type==TileType.MARKET) candidates.add(new int[]{i,j});
        if (candidates.isEmpty()) {
            // just place at 0,0
            grid[0][0] = new Tile(TileType.MARKET);
            grid[0][0].market = market;
            placedMarket = market;
            return;
        }
        int[] pick = candidates.get(rnd.nextInt(candidates.size()));
        grid[pick[0]][pick[1]].market = market;
        placedMarket = market;
    }
    
    
    */

    public void placeMarketAtRandom(Market market, Random rnd) {
        this.placedMarket = market;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].type == TileType.MARKET) {
                    grid[i][j].market = market;   // <-- FIX
                }
            }
        }
    }



    public boolean moveParty(List<Hero> party, char dir) {
        int nr = partyR, nc = partyC;
        if (dir=='w') nr--;
        else if (dir=='s') nr++;
        else if (dir=='a') nc--;
        else if (dir=='d') nc++;
        if (nr<0||nr>=rows||nc<0||nc>=cols) return false;
        if (grid[nr][nc].type == TileType.INACCESSIBLE) return false;
        partyR = nr; partyC = nc;
        return true;
    }

    public TileType getTileAtParty(List<Hero> party) {
        return grid[partyR][partyC].type;
    }

    public Market getMarketAtParty(List<Hero> party) {
        Tile t = grid[partyR][partyC];
        return t.market;
    }

    /*
    
    public void printMap(List<Hero> party) {
        System.out.println("WORLD MAP:");
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                if (i==partyR && j==partyC) System.out.print(" P ");
                else switch (grid[i][j].type) {
                    case INACCESSIBLE: System.out.print(" X "); break;
                    case MARKET: System.out.print(" M "); break;
                    case COMMON: System.out.print(" . "); break;
                }
            }
            System.out.println();
        }
    }


    */

    public void printMap(List<Hero> party) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        //final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";

        System.out.println("\n========== WORLD MAP ==========\n");
        System.out.print("    ");
        for (int j = 0; j < cols; j++) {
            //System.out.printf(" %2d ", j);
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            //System.out.printf("%2d ", i);
            for (int j = 0; j < cols; j++) {
                boolean partyHere = (i == partyR && j == partyC);

                String cell;
                Tile tile = grid[i][j];

                if (partyHere) {
                    cell = ANSI_BLUE + "[P]" + ANSI_RESET; // Party
                } else if (tile.type == TileType.INACCESSIBLE) {
                    cell = ANSI_RED + "[X]" + ANSI_RESET; // Inaccessible
                } else if (tile.type == TileType.MARKET) {
                    cell = ANSI_GREEN + "[M]" + ANSI_RESET; // Market
                } else {
                    cell = "[ ]"; // Common
                }

                System.out.print(" " + cell + " ");
            }
            System.out.println();
        }

        System.out.println("\nLegend:");
        System.out.println(ANSI_BLUE + "[P]" + ANSI_RESET + " = Party");
        System.out.println(ANSI_GREEN + "[M]" + ANSI_RESET + " = Market Tile");
        System.out.println(ANSI_RED + "[X]" + ANSI_RESET + " = Inaccessible");
        System.out.println("[ ] = Common Tile\n");
    }

}
