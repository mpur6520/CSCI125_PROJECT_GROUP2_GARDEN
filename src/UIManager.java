// import processing for graphics and text
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class UIManager {

    public PFont bodyFont; // store the font used for normal text
    public PFont titleFont; // store the font used for titles and headers

    // this allows all ui text to use consistent fonts
    public UIManager(PFont bodyFont, PFont titleFont) {
        this.bodyFont = bodyFont;
        this.titleFont = titleFont;
    }

    // draw the main title bar at the top of the screen
    public void drawTitle(PApplet app) {

        // draw the outline or border of the title rectangle
        app.stroke(74, 45, 24);
        app.strokeWeight(7);

        // fill the title bar background
        app.fill(117, 84, 60);
        app.rect(50, 20, 450, 60);

        // apply font styling for the title text
        app.fill(0);
        app.textFont(titleFont);
        app.textSize(30);

        // draw the actual title text on the bar
        app.text("VIRTUAL GARDENING GAME", 115, 57);
    }

    // draw the weather box with an icon and description
    public void drawWeatherBox(PApplet app, String weather, PImage icon) {

        // draw the box behind the weather text and icon
        app.fill(117, 84, 60);
        app.rect(app.width - 250, 20, 200, 60);

        // draw the small weather icon inside the box
        app.image(icon, app.width - 240, 26);

        // set the font and styling for the weather label
        app.fill(0);
        app.textFont(titleFont);

        // draw the weather text (like "sunny", "rainy", etc)
        app.text(weather, app.width - 160, 55);
    }

    // draw the points and days counter box
    public void drawPointsBox(PApplet app, int points, int days) {

        // draw the rectangular background for points and days
        app.fill(117, 84, 60);
        app.rect(app.width - 135, app.height/2 - 60, 120, 100);

        // set the color and font for the counter text
        app.fill(0);
        app.textFont(bodyFont);

        // draw the current points
        app.text("Points: " + points, app.width - 125, app.height/2 - 20);

        // draw the current day number
        app.text("Days: " + days, app.width - 125, app.height/2 + 20);
    }

    // draw the inventory sidebar showing seeds, names, and costs
    public void drawInventory(PApplet app, PImage[] seedImages, String[] plantTypes, int[] seeds,
                              int invX, int invY, int iconSize) {

        // select the body font since inventory uses normal text
        app.textFont(bodyFont);

        // draw the background for the inventory panel
        app.fill(117, 84, 60);
        app.rect(invX - 5, invY - 15, 218, 400);

        // loop through each plant type to draw a row in the inventory
        for (int i = 0; i < plantTypes.length; i++) {

            // calculate each entry's y position so they stack vertically
            int x = invX;
            int y = invY + i * (iconSize + 10);

            app.image(seedImages[i], x, y); // draw the plant or seed icon

            app.fill(0); // set text color for labels

            // draw the plant name and how many seeds the player owns
            app.text(plantTypes[i] + " x" + seeds[i], x + iconSize + 10, y + iconSize / 2);

            // draw the cost of planting this plant type
            app.text("Cost: " + (i + 1) + " pts", x + iconSize + 10, y + iconSize / 2 + 18);
        }
    }
}
