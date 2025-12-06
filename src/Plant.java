import java.util.Random;

public class Plant {
    public int type;
    public String stage = "Seed";
    public boolean watered = false;
    Random rand = new Random();

    public Plant(int t){ type=t; }

    public void grow(String weather){
        switch(stage){
            case "Seed": stage="Sprout"; break;
            case "Sprout":
                if(weather.equals("Sunny") && rand.nextInt(100)<70) stage="Harvest";
                else stage="Mature";
                break;
            case "Mature":
                if(weather.equals("Dry") && !watered && rand.nextInt(100)<50) stage="Sprout";
                else stage="Harvest";
                break;
            case "Harvest": break;
        }
    }
}
