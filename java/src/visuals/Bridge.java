package visuals;

import processing.core.*;

public class Bridge extends PApplet
{
    MusicVisualiserProject mvp;
    int cols, rows; //cols and rows of terrain
    float[][] terrain; 
    float flying; //speed terrain is flown over
    float amplitude; //amplitude of song
    float smoothedAmplitude; //smoothed amplitude of song
    int w, h; //used to alter terrain to span over width and height
    int scl = 20; //size of terrain triangles
    int increase = 600; //amount used to increase width/height

    // Constructor that accepts a MusicVisualiserProject object
    public Bridge(MusicVisualiserProject mvp) {
        this.mvp = mvp;

    }

    // Method to render the Chorus visual
    public void render(int width, int height) {

        //resets camera when called
        mvp.camera(mvp.width / 2, mvp.height / 2, (mvp.height / 2) / PApplet.tan(PApplet.PI / 6), mvp.width / 2, mvp.height / 2, 0, 0, 1, 0);
        
        //code used on terrain to increase width and height
        w = width + increase;
        h = height + increase;

        //how column and rows are sized
        cols = w / scl;
        rows = h / scl;
        terrain = new float[cols][rows];

        //amplitude code to get desired amplitdue
        smoothedAmplitude = mvp.getSmoothedAmplitude();
        amplitude = smoothedAmplitude * 6000;

        // Rotate the tomato model
        mvp.tomato.rotateX(+50);
        mvp.tomato.rotateY(+50);

        //Calling methods
        drawTerrain();
        drawTomatoes();
    }

    //Method used to draw terrain to screen
    public void drawTerrain()
    {
        //speed terrain is flown through at
        flying  -=  0.1;

        //Utilising pearl noise to create vertex movement
        float yoff = flying;
        for (int y = 0;  y  < rows; y++){
            float xoff  = 0;
            for (int x = 0;  x < cols; x++){
                terrain[x][y] = map(noise(xoff, yoff) , 0, 1, -amplitude, amplitude);
                xoff += 0.1;
            }
            yoff +=  0.1;
        }

        //Resets background and unfills terrain
        mvp.background(0);
        mvp.stroke(255);
        mvp.noFill();
        
        //Translates terrain to flat terrain
        mvp.translate(w/2, h/2);
        mvp.rotateX(PI/2);
        mvp.translate(-w/2-increase/2, -h/2);

        //Draws terrain to sceen, and colours in terrain
        for (int y = 0;  y  < rows-1; y++){
            mvp.beginShape(TRIANGLE_STRIP);
            for (int x = 0;  x < cols; x++){
                float vertHue = map(x, 0, mvp.getAudioBuffer().size(), 10, 165);
                mvp.stroke(vertHue, 255,255);
                mvp.vertex(x*scl, y*scl, terrain[x][y]);
                mvp.vertex(x*scl, (y+1)*scl, terrain[x][y+1]);

            }
            mvp.endShape();
        }
    }

    //Draws Tomatos to screen
    public void drawTomatoes() {
        mvp.pushMatrix();
    
        float tomatoDiameter = (float) (min(w, h) * 0.0000004); // Adjust the size of the circle
        float tomatoY = (float) ((mvp.height / 2) * 2.25); // Y-coordinate of the circle
    
        // Translate the coordinates to match the terrain translation
        mvp.translate(w/2, h/2);
        mvp.rotateX(PI/2);
        mvp.translate(-w/2, -h/2);
        
        // Array to store tomato positions
        float[][] tomatoPositions = {
            {mvp.width / 2, tomatoY},
            {mvp.width / 2 + 1000, tomatoY + 300},
            {mvp.width / 2 - 700, tomatoY + 200},
            {mvp.width / 2 - 400, tomatoY - 200},
            {mvp.width / 2 + 500, tomatoY - 100},
            {mvp.width / 2 + 1200, tomatoY - 250},
        };
    
        // Draw the tomatoes in a loop
        for (float[] pos : tomatoPositions) {
            float tomatoX = pos[0];
            mvp.shape(mvp.tomato, tomatoX, pos[1], tomatoDiameter + (amplitude - 160), tomatoDiameter + (amplitude - 160));
        }
    
        mvp.popMatrix();
    }
}