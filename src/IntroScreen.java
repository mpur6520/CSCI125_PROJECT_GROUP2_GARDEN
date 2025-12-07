// import processing tools for images, text, and drawing
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class IntroScreen {

    // store a reference to the main app (so it can be "drawn" over)
    PApplet app;

    // sunflower image shown on the intro screen
    PImage sunflower;

    // start button that the user clicks
    UIButton btnStart;

    // fonts used for the title and description text
    PFont titleFont, bodyFont;

    // this controls whether the intro screen should be visible
    public boolean showIntro = true;

    // constructor that prepares images, fonts, and the button
    public IntroScreen(PApplet p){
        this.app = p;

        // load the sunflower picture for decoration
        sunflower = app.loadImage("Resources/images/sunflower_harvest.png");
        sunflower.resize(64, 64); // resize the icon so it fits nicely

        // load the custom fonts for the intro text
        titleFont = app.createFont("Resources/fonts/Masaaki-Regular.otf", 50);
        bodyFont = app.createFont("Resources/fonts/RosesareFF0000.ttf", 18);

        // create the start button centered horizontally
        btnStart = new UIButton("Start Game",
                app.width/2 - 75,      // x position
                app.height - 100,      // y position
                150,                   // width
                40                     // height
        );
    }

    // draw everything that appears on the intro screen
    public void draw() {

        // if the intro screen is not active, don't draw anything
        if (!showIntro) return;

        // background color of the whole screen
        app.background(227, 219, 204);

        // draw the decorative sunflower icons on both sides of the title
        app.image(sunflower, app.width/2 - 300, 100);
        app.image(sunflower, app.width/2 + 225, 100);

        // draw the main game title text
        app.textFont(titleFont);
        app.fill(0);
        app.text("VIRTUAL GARDEN", app.width/2 - 175, 150);

        // draw the explanation lines under the title
        app.textFont(bodyFont);
        app.fill(0);
        app.text("Plant seeds, water them each day, deal with weather,", 75, 220);
        app.text("harvest crops, and earn points!", 75, 250);

        // draw the weather effect explanations
        app.text("Rainy -> Automatically Watered 2x", 75, 300);
        app.text("Sunny -> Chance to Skip Stage", 75, 330);
        app.text("Dry -> Chance to Regress Stage", 75, 360);

        // draw plant-related rules
        app.text("Plants are ready to harvest once they sparkle.", 75, 410);
        app.text("Plants will die if you forget to water them.", 75, 440);
        app.text("Have fun...!", 75, 490);

        // draw the start button at the bottom
        btnStart.draw(app);
    }

    // check if the user clicked the start button
    public void mousePressed(int mx, int my){

        // ignore clicks if the intro isn't visible
        if (!showIntro) return;

        // if the start button was clicked, hide the intro screen
        if (btnStart.isClicked(mx, my)) {
            showIntro = false;
        }
    }
}
