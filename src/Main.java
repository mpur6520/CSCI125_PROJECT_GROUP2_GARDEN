// import core processing functions for drawing
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

// import pop-up message box stuff
import javax.swing.JOptionPane;

// import random number generator
import java.util.Random;

public class Main extends PApplet {

    // reference to intro screen overlay
    IntroScreen intro;

    // plot grid size and ui scale values
    int PLOT_SIZE = 128, GRID_ROWS = 3, GRID_COLS = 3;
    int INVENTORY_ICON = 64, WEATHER_ICON = 48;

    // main garden plot grid
    Plot[][] plots = new Plot[GRID_ROWS][GRID_COLS];

    // temporary plant storage
    Plant[] tempPlants;

    int[] seeds = {3,3,3,3,3}; // number of seeds in inventory
    int[] harvested = {0,0,0,0,0}; // harvest counter
    int[] died = {0,0,0,0,0}; // death counters

    // global score and day counter
    int points = 10;
    int days = 0;

    // plant types and growth stage names
    String[] PLANT_TYPES = {"Tomato","Carrot","Sunflower","Cabbage","Pumpkin"};
    String[] GROWTH_STAGES = {"Seed","Sprout","Mature","Harvest"};

    String weather = "Sunny"; // current weather name

    // arrays for icons and plant sprites
    PImage[] weatherIcons = new PImage[3];
    PImage[] seedImages = new PImage[5];
    PImage[] plotImages = new PImage[5*4];

    PImage plotBase, plotWet, plotDead; // soil textures

    int gridOffsetX, gridOffsetY; // offsets for centered plot grid

    UIManager ui; // ui manager reference

    Shop shop = new Shop(); // shop object reference

    UIButton btnEndDay, btnShop, btnEndGame; // buttons for gameplay actions

    Random rand = new Random(); // random object for weather and growth

    // main stuff
    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        // set window size using grid size
        size(GRID_COLS*PLOT_SIZE + 400, GRID_ROWS*PLOT_SIZE + 250);
    }

    public void setup() {
        // load custom fonts
        PFont pFont = createFont("Resources/fonts/RosesareFF0000.ttf",16);
        PFont tFont = createFont("Resources/fonts/Masaaki-Regular.otf",24);

        // create ui manager
        ui = new UIManager(pFont,tFont);

        // create intro screen handler
        intro = new IntroScreen(this);

        // load base plot textures
        plotBase = loadImage("Resources/images/plot.png"); plotBase.resize(PLOT_SIZE,PLOT_SIZE);
        plotWet  = loadImage("Resources/images/plot_wet.png"); plotWet.resize(PLOT_SIZE,PLOT_SIZE);
        plotDead = loadImage("Resources/images/dead.png"); plotDead.resize(PLOT_SIZE,PLOT_SIZE);

        // load seed icons
        for(int i=0;i<PLANT_TYPES.length;i++){
            seedImages[i] = loadImage("Resources/images/"+PLANT_TYPES[i].toLowerCase()+"_seed.png");
            seedImages[i].resize(INVENTORY_ICON,INVENTORY_ICON);
        }

        // load plant growth sprites
        for(int t=0;t<PLANT_TYPES.length;t++)
            for(int s=0;s<GROWTH_STAGES.length;s++){
                PImage img = loadImage("Resources/images/"+PLANT_TYPES[t].toLowerCase()+"_"+GROWTH_STAGES[s].toLowerCase()+".png");
                img.resize(PLOT_SIZE,PLOT_SIZE);
                plotImages[t*4+s] = img;
            }

        // load weather icons
        weatherIcons[0] = loadImage("Resources/images/weather_sunny.png"); weatherIcons[0].resize(WEATHER_ICON,WEATHER_ICON);
        weatherIcons[1] = loadImage("Resources/images/weather_rainy.png"); weatherIcons[1].resize(WEATHER_ICON,WEATHER_ICON);
        weatherIcons[2] = loadImage("Resources/images/weather_dry.png");   weatherIcons[2].resize(WEATHER_ICON,WEATHER_ICON);

        // create plot objects for grid
        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++)
                plots[r][c] = new Plot();

        // calculate centered grid offset
        gridOffsetX = (width - GRID_COLS*PLOT_SIZE)/2;
        gridOffsetY = 100;

        // create ui buttons
        btnEndDay = new UIButton("End Day", width/4-60, height-60, 120, 30);
        btnShop = new UIButton("Shop", width/2-60, height-60, 120, 30);
        btnEndGame = new UIButton("End Game", 3*width/4-60, height-60, 120, 30);
    }

    public void draw() {
        // background color for main screen
        background(227,219,204);

        // auto-water plots if raining
        if(weather.equals("Rainy"))
            for(int r=0;r<GRID_ROWS;r++)
                for(int c=0;c<GRID_COLS;c++)
                    if(!plots[r][c].dead)
                        plots[r][c].watered = true;

        // draw soil plots and plants
        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++)
                plots[r][c].drawPlot(this,
                        gridOffsetX+50+c*PLOT_SIZE,
                        gridOffsetY+r*PLOT_SIZE+20,
                        plotBase, plotWet, plotDead, plotImages);

        // draw all ui elements
        ui.drawTitle(this);

        // pick weather icon based on condition
        PImage wIcon = weather.equals("Sunny")?weatherIcons[0]:
                weather.equals("Rainy")?weatherIcons[1]:
                        weatherIcons[2];

        // draw weather box, score box, and inventory
        ui.drawWeatherBox(this, weather, wIcon);
        ui.drawPointsBox(this, points, days);
        ui.drawInventory(this, seedImages, PLANT_TYPES, seeds, 20, gridOffsetY+25, INVENTORY_ICON);

        // draw ui buttons
        btnEndDay.draw(this);
        btnShop.draw(this);
        btnEndGame.draw(this);

        // draw shop overlay if open
        if(shop.open) shop.draw(this, seedImages, PLANT_TYPES, seeds);

        // draw intro overlay on top of everything
        if(intro.showIntro){
            intro.draw();
            return;
        }
    }

    public void mousePressed() {
        int mx = mouseX, my = mouseY;

        // handle shop clicks first when open
        if(shop.open){
            for(int i=0;i<PLANT_TYPES.length;i++){
                int x=215, y=120+i*80;
                if(mx>=x && mx<=x+340 && my>=y && my<=y+70){
                    int cost = i+1;
                    if(points>=cost){
                        points-=cost;
                        seeds[i]++;
                        JOptionPane.showMessageDialog(null,"Bought 1 "+PLANT_TYPES[i]+"!");
                    } else {
                        JOptionPane.showMessageDialog(null,"Not enough points for "+PLANT_TYPES[i]+"!");
                    }
                }
            }
            if(shop.clickExit(mx,my,this)) shop.open=false;
            return;
        }

        // check plot clicks for watering or harvesting
        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++){
                Plot p = plots[r][c];
                int px = gridOffsetX + 50 + c*PLOT_SIZE;
                int py = gridOffsetY + r*PLOT_SIZE + 20;

                if(mx>=px && mx<px+PLOT_SIZE && my>=py && my<py+PLOT_SIZE){

                    // revive dead plot
                    if(p.dead){ p.dead=false; p.plant=null; }

                    // interact with planted crop
                    else if(p.plant!=null){
                        if(p.plant.stage.equals("Harvest")){
                            harvested[p.plant.type]++;
                            points += p.plant.type+1;
                            p.plant=null;
                            p.watered=false;
                        } else {
                            p.watered=true;
                        }
                    }
                }
            }

        // check inventory seed clicking
        for(int i=0;i<PLANT_TYPES.length;i++){
            int x=20;
            int y=gridOffsetY+25+i*(INVENTORY_ICON+10);

            if(mx>=x && mx<=x+INVENTORY_ICON+100 && my>=y && my<=y+INVENTORY_ICON){
                if(seeds[i]>0){

                    // find empty plot to plant seed
                    outer: for(int r=0;r<GRID_ROWS;r++)
                        for(int c=0;c<GRID_COLS;c++)
                            if(plots[r][c].plant==null && !plots[r][c].dead){
                                plots[r][c].plant = new Plant(i);
                                seeds[i]--;
                                break outer;
                            }

                } else {
                    JOptionPane.showMessageDialog(null,"Not enough seeds!");
                }
            }
        }

        // check button presses
        if(btnEndDay.isClicked(mx,my)) endDay();
        if(btnShop.isClicked(mx,my)) shop.open=true;
        if(btnEndGame.isClicked(mx,my)) endGame();

        // intro overlay click check
        if(intro.showIntro){
            intro.mousePressed(mouseX, mouseY);
            return;
        }
    }

    void endDay(){
        // advance one day
        days++;

        // update each plot and plant
        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++){
                Plot p = plots[r][c];

                // grow plant if alive
                if(p.plant!=null && !p.dead){
                    p.plant.grow(weather);

                    // if plant not watered, it dies
                    if(!p.watered){
                        int type=p.plant.type;
                        p.dead=true;
                        p.plant=null;
                        died[type]++;
                    }
                }

                // rainy weather auto-waters
                if(weather.equals("Rainy") && !p.dead) p.watered=true;

                // reset watering on non-rainy days
                if(!weather.equals("Rainy")) p.watered=false;
            }

        // pick next day's random weather
        int w = rand.nextInt(3);
        weather = w==0?"Sunny":w==1?"Rainy":"Dry";
    }

    void endGame(){
        // build summary text
        StringBuilder sb = new StringBuilder();
        sb.append("Game Over!\nDays: ").append(days).append("\nPoints: ").append(points).append("\n\n");

        for(int i=0;i<PLANT_TYPES.length;i++)
            sb.append(PLANT_TYPES[i]).append(" Harvested: ").append(harvested[i])
                    .append("! Died: ").append(died[i]).append("\n");

        // show results
        JOptionPane.showMessageDialog(null,sb.toString());

        // close program
        exit();
    }
}
