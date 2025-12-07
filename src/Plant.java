// import for generating random numbers
import java.util.Random;

// class representing a single plant in the garden
public class Plant {

    // type of plant
    public int type;

    // current growth stage, starts at seed
    public String stage = "Seed";

    // whether the plant was watered for the current day
    public boolean watered = false;

    // rng used for probability-based growth (sunny, dry)
    Random rand = new Random();

    // sets plant type when created
    public Plant(int t){
        type = t;
    }

    // main growth logic, changes stage depending on weather + randomness
    public void grow(String weather){
        switch(stage){

            // if the plant is a seed
            case "Seed" -> {
                if(weather.equals("Sunny") && rand.nextInt(100) < 25)
                    stage = "Mature";  // high sunshine -> skip stage
                else
                    stage = "Sprout";   // otherwise just normal growth
            }

            // if the plant is a sprout
            case "Sprout" -> {
                if(weather.equals("Dry") && !watered && rand.nextInt(100) < 25)
                    stage = "Seed";   // drought causes plant to regress
                else if(weather.equals("Sunny") && rand.nextInt(100) < 25)
                    stage = "Harvest"; // skips a stage if sunny
                else
                    stage = "Mature"; // otherwise just normal growth
            }

            // if the plant is mature
            case "Mature" -> {
                if(weather.equals("Dry") && !watered && rand.nextInt(100) < 25)
                    stage = "Sprout";   // drought causes plant to regress
                else
                    stage = "Harvest"; // otherwise just normal growth
            }

            // if the plant is at harvest
            case "Harvest" -> {
                if(weather.equals("Dry") && !watered && rand.nextInt(100) < 25)
                    stage = "Mature";   // drought causes plant to regress
            }
        }
    }
}
