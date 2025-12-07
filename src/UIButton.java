// import processing so we can draw the button onto the screen
import processing.core.PApplet;

public class UIButton {

    // the text that appears on the button
    String label;

    // the button's position and size
    int x, y, w, h;

    // create a new button with a label and rectangle values
    public UIButton(String label, int x, int y, int w, int h){
        this.label = label;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // draw the button on the screen
    public void draw(PApplet app){

        // draw the button background rectangle
        app.fill(180);
        app.strokeWeight(2);
        app.stroke(161, 159, 157);
        app.rect(x, y, w, h);

        // draw the button label text on top
        app.fill(0);
        app.text(label, x + 10, y + h/2 + 10);
    }

    // check if the user clicked inside the button
    public boolean isClicked(int mx, int my){

        // return true if the mouse is inside the button area
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
