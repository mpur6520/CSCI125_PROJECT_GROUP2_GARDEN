import processing.core.PApplet;
import processing.core.PImage;

public class Plot {
    public Plant plant = null;
    public boolean watered = false;
    public boolean dead = false;

    public void drawPlot(PApplet app, int x, int y, PImage plotBase, PImage plotWet, PImage deadImage, PImage[] plotImages) {
        if(dead) {
            app.image(deadImage, x, y);
        } else if(watered) {
            app.image(plotWet, x, y);
        } else {
            app.image(plotBase, x, y);
        }

        if(plant != null && !dead) {
            int stageIndex = 0;
            switch(plant.stage) {
                case "Seed": stageIndex = 0; break;
                case "Sprout": stageIndex = 1; break;
                case "Mature": stageIndex = 2; break;
                case "Harvest": stageIndex = 3; break;
            }
            app.image(plotImages[plant.type*4 + stageIndex], x, y);
        }
    }

    public void checkWatering() {
        if(plant != null && !watered && !dead) {
            dead = true;
            plant = null;
        }
    }

    public void resetWatering() {
        if(!dead) watered = false;
    }
}
