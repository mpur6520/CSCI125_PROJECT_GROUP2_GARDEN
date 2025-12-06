import processing.core.PApplet;
import processing.core.PImage;
import javax.swing.JOptionPane;
import java.util.Random;

public class Main extends PApplet {

    int PLOT_SIZE = 128;
    int GRID_ROWS = 3, GRID_COLS = 3;
    int INVENTORY_ICON = 64, WEATHER_ICON = 48;

    Plot[][] plots = new Plot[GRID_ROWS][GRID_COLS];
    int[] seeds = {3,3,3,3,3};
    int[] harvested = {0,0,0,0,0};
    int[] died = {0,0,0,0,0};
    int points = 10;
    int days = 0;

    String[] PLANT_TYPES = {"Tomato","Carrot","Sunflower","Cabbage","Pumpkin"};
    String[] GROWTH_STAGES = {"Seed","Sprout","Mature","Harvest"};

    String weather = "Sunny";
    String nextWeather = "Sunny";
    PImage[] weatherIcons = new PImage[3];
    Random rand = new Random();

    PImage[] seedImages = new PImage[5];
    PImage[] plotImages = new PImage[5*4];
    PImage plotBase, plotWet, plotDead;

    int btnEndDayX, btnEndDayY, btnEndDayW, btnEndDayH;
    int btnEndGameX, btnEndGameY, btnEndGameW, btnEndGameH;
    int btnShopX, btnShopY, btnShopW, btnShopH;

    boolean inShop = false;

    //  offsets for centering (or something thanks stackoverflow)
    int gridOffsetX, gridOffsetY;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(GRID_COLS*PLOT_SIZE + 400, GRID_ROWS*PLOT_SIZE + 250);
    }

    public void setup() {
        surface.setTitle("Garden Game");

        plotBase = loadImage("Resources/images/plot.png"); plotBase.resize(PLOT_SIZE,PLOT_SIZE);
        plotWet  = loadImage("Resources/images/plot_wet.png"); plotWet.resize(PLOT_SIZE,PLOT_SIZE);
        plotDead = loadImage("Resources/images/dead.png"); plotDead.resize(PLOT_SIZE,PLOT_SIZE);

        for(int i=0;i<PLANT_TYPES.length;i++){
            seedImages[i] = loadImage("Resources/images/"+PLANT_TYPES[i].toLowerCase()+"_seed.png");
            seedImages[i].resize(INVENTORY_ICON,INVENTORY_ICON);
        }

        for(int t=0;t<PLANT_TYPES.length;t++){
            for(int s=0;s<GROWTH_STAGES.length;s++){
                PImage img = loadImage("Resources/images/"+PLANT_TYPES[t].toLowerCase()+"_"+GROWTH_STAGES[s].toLowerCase()+".png");
                img.resize(PLOT_SIZE,PLOT_SIZE);
                plotImages[t*4+s] = img;
            }
        }

        weatherIcons[0] = loadImage("Resources/images/weather_sunny.png"); weatherIcons[0].resize(WEATHER_ICON,WEATHER_ICON);
        weatherIcons[1] = loadImage("Resources/images/weather_rainy.png"); weatherIcons[1].resize(WEATHER_ICON,WEATHER_ICON);
        weatherIcons[2] = loadImage("Resources/images/weather_dry.png"); weatherIcons[2].resize(WEATHER_ICON,WEATHER_ICON);

        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++)
                plots[r][c] = new Plot();

        // center the grid
        gridOffsetX = (width - GRID_COLS*PLOT_SIZE) / 2;
        gridOffsetY = 100; // leave space for weather at top

        // buttons at bottom
        btnEndDayW=120; btnEndDayH=30;
        btnEndDayX = width/4 - btnEndDayW/2; btnEndDayY = height - 60;
        btnShopW=120; btnShopH=30;
        btnShopX = width/2 - btnShopW/2; btnShopY = height - 60;
        btnEndGameW=120; btnEndGameH=30;
        btnEndGameX = 3*width/4 - btnEndGameW/2; btnEndGameY = height - 60;

        textSize(16);
    }

    public void draw() {
        background(200,255,200);

        // immetiate rain effect wet plots
        if(weather.equals("Rainy")){
            for(int r=0;r<GRID_ROWS;r++)
                for(int c=0;c<GRID_COLS;c++)
                    if(!plots[r][c].dead)
                        plots[r][c].watered = true;
        }

        //  plots centered
        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++)
                plots[r][c].drawPlot(this, gridOffsetX + c*PLOT_SIZE, gridOffsetY + r*PLOT_SIZE, plotBase, plotWet, plotDead, plotImages);

        // yipppeeee triangle for weather display
        fill(180,220,255);
        rect(50,20,width-100,60);
        PImage wIcon = weather.equals("Sunny")?weatherIcons[0]:(weather.equals("Rainy")?weatherIcons[1]:weatherIcons[2]);
        image(wIcon, 60, 25);
        fill(0); textSize(24); text("Weather: "+weather, 120, 55);
        textSize(16);

        // user interface at the bottom or something
        int invX = 50, invY = gridOffsetY + GRID_ROWS*PLOT_SIZE + 20;
        for(int i=0;i<PLANT_TYPES.length;i++){
            int x = invX;
            int y = invY + i*(INVENTORY_ICON+10);
            image(seedImages[i], x, y);
            fill(0);
            text(PLANT_TYPES[i]+" x"+seeds[i], x + INVENTORY_ICON + 10, y + INVENTORY_ICON/2);
            text("Cost: "+(i+1)+" pts", x + INVENTORY_ICON + 10, y + INVENTORY_ICON/2 + 18);
        }

        fill(0);
        text("Points: "+points, width-150, height-120);
        text("Days: "+days, width-150, height-100);

        // buttons.
        fill(200);
        rect(btnEndDayX,btnEndDayY,btnEndDayW,btnEndDayH);
        fill(0); text("End Day",btnEndDayX+20,btnEndDayY+20);

        fill(200);
        rect(btnShopX,btnShopY,btnShopW,btnShopH);
        fill(0); text("Shop",btnShopX+30,btnShopY+20);

        fill(200);
        rect(btnEndGameX,btnEndGameY,btnEndGameW,btnEndGameH);
        fill(0); text("End Game",btnEndGameX+10,btnEndGameY+20);

        // shop overlay (it's ugly IGNORE IT FOR NOW)
        if(inShop){
            fill(100,200,255,220);
            rect(50,50,width-100,height-100);
            fill(0);
            text("SHOP - Click a seed to buy", width/2-80, 80);

            for(int i=0;i<PLANT_TYPES.length;i++){
                int x = 100;
                int y = 120 + i*80;
                fill(180);
                rect(x,y,200,60);
                image(seedImages[i], x+5, y+5);
                fill(0);
                text(PLANT_TYPES[i]+" - Cost: "+(i+1)+" pts", x+70, y+35);
            }

            // exit the shop
            fill(180);
            rect(width-150, height-100, 100, 40);
            fill(0);
            text("Exit Shop", width-140, height-75);
        }
    }

    public void mousePressed() {
        int mx = mouseX, my = mouseY;

        if(inShop){
            for(int i=0;i<PLANT_TYPES.length;i++){
                int x=100, y=120 + i*80;
                if(mx>=x && mx<=x+200 && my>=y && my<=y+60){
                    int cost = i+1;
                    if(points>=cost){
                        points -= cost;
                        seeds[i]++;
                        JOptionPane.showMessageDialog(null,"Bought 1 "+PLANT_TYPES[i]+" seed!");
                    } else {
                        JOptionPane.showMessageDialog(null,"Not enough points for "+PLANT_TYPES[i]+"!");
                    }
                }
            }
            if(mx >= width-150 && mx <= width-50 && my >= height-100 && my <= height-60){
                inShop = false;
            }
            return;
        }

        // plot clicks
        for(int r=0;r<GRID_ROWS;r++)
            for(int c=0;c<GRID_COLS;c++){
                Plot p = plots[r][c];
                int px = gridOffsetX + c*PLOT_SIZE;
                int py = gridOffsetY + r*PLOT_SIZE;
                if(mx>=px && mx<px+PLOT_SIZE && my>=py && my<py+PLOT_SIZE){
                    if(p.dead){
                        p.dead = false; p.plant=null;
                    } else if(p.plant!=null){
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

        // say click take a pause take your dream ?? (inventory click)
        for(int i=0;i<PLANT_TYPES.length;i++){
            int x=50, y=gridOffsetY + GRID_ROWS*PLOT_SIZE + 20 + i*(INVENTORY_ICON+10);
            if(mx>=x && mx<=x+INVENTORY_ICON && my>=y && my<=y+INVENTORY_ICON){
                if(seeds[i]>0){
                    outer: for(int r=0;r<GRID_ROWS;r++){
                        for(int c=0;c<GRID_COLS;c++){
                            if(plots[r][c].plant==null && !plots[r][c].dead){
                                plots[r][c].plant = new Plant(i);
                                seeds[i]--;
                                break outer;
                            }
                        }
                    }
                } else JOptionPane.showMessageDialog(null,"Not enough seeds!");
            }
        }

        // butonnns
        if(mx>=btnEndDayX && mx<=btnEndDayX+btnEndDayW && my>=btnEndDayY && my<=btnEndDayY+btnEndDayH) endDay();
        if(mx>=btnEndGameX && mx<=btnEndGameX+btnEndGameW && my>=btnEndGameY && my<=btnEndGameY+btnEndGameH) endGame();
        if(mx>=btnShopX && mx<=btnShopX+btnShopW && my>=btnShopY && my<=btnShopY+btnShopH) inShop=true;
    }

    void endDay(){
        days++;
        for(int r=0;r<GRID_ROWS;r++){
            for(int c=0;c<GRID_COLS;c++){
                Plot p = plots[r][c];

                if(p.plant!=null && !p.dead){
                    p.plant.grow(weather);
                    if(!p.watered){
                        int type = p.plant.type;
                        p.dead=true;
                        p.plant = null;
                        died[type]++;
                    }
                }

                if(weather.equals("Rainy") && !p.dead) p.watered=true;
                if(!weather.equals("Rainy")) p.watered=false;
            }
        }

        // Randomize next weather
        int w = rand.nextInt(3);
        nextWeather = w==0?"Sunny":w==1?"Rainy":"Dry";
        weather = nextWeather;
    }

    void endGame(){
        StringBuilder sb = new StringBuilder();
        sb.append("Game Over!\nDays: ").append(days).append("\nPoints: ").append(points).append("\n\n");
        for(int i=0;i<PLANT_TYPES.length;i++)
            sb.append(PLANT_TYPES[i]).append(" harvested: ").append(harvested[i]).append(", died: ").append(died[i]).append("\n");
        JOptionPane.showMessageDialog(null,sb.toString());
        exit();
    }
}
