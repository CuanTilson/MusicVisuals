package visuals;

import processing.core.*;

public class Bridge extends PApplet
{
    MusicVisualiserProject mvp;
    int cols, rows;
    float[][] terrain;
    float flying;
    float amplitude;
    float smoothedAmplitude;
    int w;
    int h;
    int increase;
    int scl = 20;
    int cx,cy;

    // Reference to the main MusicVisualiserProject object

    // Constructor that accepts a MusicVisualiserProject object
    public Bridge(MusicVisualiserProject mvp) {
        this.mvp = mvp;
        increase = 600;

    }

    // Method to render the Chorus visual
    public void render(int width, int height) {
        w = width + increase;
        h = height + increase;
        cols = w / scl;
        rows = h / scl;
        terrain = new float[cols][rows];
        cx = w/4;
        cy = h/4;
        smoothedAmplitude = mvp.getSmoothedAmplitude();
        amplitude = smoothedAmplitude * 6000;
        // Text saying Chorus centred on screen
        // mvp.textSize(32);
        // mvp.textAlign(PConstants.CENTER, PConstants.CENTER);
        // mvp.fill(255);
        // mvp.text("Bridge", mvp.width / 2, mvp.height / 2);
        drawTerrain();
        drawTomatos();
    }

    public void drawTerrain()
    {
        flying  -=  0.05;

        float yoff = flying;
        for (int y = 0;  y  < rows; y++){
            float xoff  = 0;
            for (int x = 0;  x < cols; x++){
                terrain[x][y] = map(noise(xoff, yoff) , 0, 1, -amplitude, amplitude);
                xoff += 0.1;
            }
            yoff +=  0.1;
        }

        
        mvp.background(0);
        mvp.stroke(255);
        mvp.noFill();
        
        mvp.translate(w/2, h/2);
        mvp.rotateX(PI/2);

        mvp.translate(-w/2-increase/2, -h/2);
        for (int y = 0;  y  < rows-1; y++){
            mvp.beginShape(TRIANGLE_STRIP);
            for (int x = 0;  x < cols; x++){
                //rect(x *scl, y*scl, scl,scl);
                float vertHue = map(x, 0, mvp.getAudioBuffer().size(), 10, 165);
                mvp.stroke(vertHue, 255,255);
                mvp.vertex(x*scl, y*scl, terrain[x][y]);
                mvp.vertex(x*scl, (y+1)*scl, terrain[x][y+1]);

            }
            mvp.endShape();
        }
    }

    public void drawTomatos() {
        mvp.pushMatrix();

        float tomatoDiameter = (float) (min(w, h) * 0.2); // Adjust the size of the circle
        float tomatoX = mvp.width / 2; // X-coordinate of the circle
        float tomatoY = (float) ((mvp.height / 2) * 2.25); // Y-coordinate of the circle
        
        // Translate the coordinates to match the terrain translation
        mvp.translate(w/2, h/2);
        mvp.rotateX(PI/2);

        mvp.translate(-w/2, -h/2);
        
        // Draw the giant circle
        mvp.noFill();
        mvp.stroke(255); // Adjust the stroke color
        mvp.shape(mvp.tomato, tomatoX, tomatoY, tomatoDiameter + amplitude, tomatoDiameter + amplitude);

        mvp.popMatrix();

    }
}