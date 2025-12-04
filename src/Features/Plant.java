package Features;

public class Plant {
    // Arrays defined here, not changing
    public static final String[] PLANT_TYPES = {"Tomato", "Carrot", "Sunflower"};
    public static final String[] GROWTH_STAGES = {"Seed", "Sprout", "Mature", "Harvest-Ready"};

    private String type;
    private String stage;
    private boolean watered;

    // Constructor (?) Is that the right word...
    public Plant(String type) {
        this.type = type;
        this.stage = GROWTH_STAGES[0]; // Start as "Seed"
        this.watered = false;
    }

    // Takes information
    public String getType() {
        return type;
    }
    public String getStage() {
        return stage;
    }
    public boolean isWatered() {
        return watered;
    }

    // Water the plant
    public void water() {
        watered = true;
    }

    // Grow the plant
    public void grow() {
        if (!watered) {
            stage = "Dead";
            return;
        }

        int currentIndex = java.util.Arrays.asList(GROWTH_STAGES).indexOf(stage);
        if (currentIndex < GROWTH_STAGES.length - 1) {
            stage = GROWTH_STAGES[currentIndex + 1];
        }

        watered = false;
    }

    // Check harvest
    public boolean isHarvestable() {
        return stage.equals(GROWTH_STAGES[GROWTH_STAGES.length - 1]);
    }

    public boolean isDead() {
        return stage.equals("Dead");
    }
}

// If this doesn't work no one's gonna see me on Monday.