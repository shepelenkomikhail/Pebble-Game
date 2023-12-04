public class Player {
    private final String name;
    private final String pebbleColour;          //Initializing variables to store a data about player
    private int pebblesCount;

    public Player(String name, String pebbleColour) {          //Class constructor
        this.name = name;
        this.pebbleColour = pebbleColour;
        this.pebblesCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getPebbleColour() {                  //Getters and setters
        return pebbleColour;
    }
    
    public int getPebblesCount() {
        return pebblesCount;
    }
}
