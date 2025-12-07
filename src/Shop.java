// import processing to draw rectangles, text, and images
import processing.core.PApplet;
import processing.core.PImage;

public class Shop {

    // boolean for if the shop window is open
    public boolean open = false;

    // draw everything that appears inside the shop window
    public void draw(PApplet app, PImage[] seedImages, String[] plantTypes, int[] seeds) {

        // draw the big background box for the shop window
        app.strokeWeight(5);
        app.stroke(143, 123, 103);
        app.fill(194, 183, 165);
        app.rect(150, 50, app.width - 300, app.height - 100);

        // draw a smaller rectangle behind the shop title
        app.fill(180);
        app.strokeWeight(2);
        app.stroke(161, 159, 157);
        app.rect(app.width/2 - 200, 68, 400, 35);

        // draw the shop title text
        app.fill(0);
        app.text("SHOP - Click a seed to buy", app.width/2 - 150, 95);

        // loop through all seed types and draw each seed option
        for (int i = 0; i < plantTypes.length; i++) {

            // position of each seed item box
            int x = 215;
            int y = 120 + i * 80;

            // draw the background box behind each seed option
            app.fill(180);
            app.rect(x, y, 340, 70);

            // draw the seed image next to the name
            app.image(seedImages[i], x + 5, y + 5);

            // draw plant name and cost label
            app.fill(0);
            app.text(plantTypes[i] + " - Cost: " + (i + 1) + " pts",
                    x + 80, y + 45);
        }

        // draw the button to exit the shop
        app.fill(180);
        app.rect(app.width/2 - 75, app.height - 100, 150, 40);

        // draw the "exit shop" text
        app.fill(0);
        app.text("Exit Shop", app.width/2 - 50, app.height - 70);
    }

    // check if the player clicked the "exit shop" button
    public boolean clickExit(int mx, int my, PApplet app) {

        // get the button's position
        int exitX = app.width/2 - 75;
        int exitY = app.height - 100;

        // return true if the mouse click is inside the exit button
        return mx >= exitX &&
                mx <= exitX + 150 &&
                my >= exitY &&
                my <= exitY + 40;
    }
}
