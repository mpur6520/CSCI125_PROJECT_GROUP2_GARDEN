// import Processing core features again
import processing.core.PApplet;
import processing.core.PImage;

// class representing a single plot of soil in the garden
public class Plot {

    // the plant currently growing in this plot (null if empty)
    public Plant plant = null;

    // whether the plot has been watered for the day
    public boolean watered = false;

    // whether the plot is dead and unusable
    public boolean dead = false;

    // draws the plot and plant sprites at the given position
    public void drawPlot(PApplet app, int x, int y, PImage plotBase, PImage plotWet, PImage plotDead, PImage[] plotImages) {

        // choose the correct soil image depending on state
        if(dead)
            app.image(plotDead, x, y);   // dead soil graphic
        else if(watered)
            app.image(plotWet, x, y);    // freshly watered graphic
        else
            app.image(plotBase, x, y);   // default dry soil graphic


        // draw plant only if there is one & plot is not occupied by dead
        if(plant != null && !dead){

            // convert the plant’s growth stage into an array index
            int stageIndex = switch(plant.stage){
                case "Seed" -> 0;
                case "Sprout" -> 1;
                case "Mature" -> 2;
                case "Harvest" -> 3;
                default -> 0; // fallback, should never happen
            };

            // each plant type occupies 4 consecutive images in the array
            app.image(plotImages[plant.type * 4 + stageIndex], x, y);
        }
    }
}
