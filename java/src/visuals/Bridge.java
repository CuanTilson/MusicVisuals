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

    // Reference to the main MusicVisualiserProject object

    // Constructor that accepts a MusicVisualiserProject object
    public Bridge(MusicVisualiserProject mvp) {
        this.mvp = mvp;
        increase = 1000;


        
    }

    // Method to render the Chorus visual
    public void render() {
        w = mvp.width + increase;
        h = mvp.height + increase;
        cols = w / scl;
        rows = h / scl;
        terrain = new float[cols][rows];
        // Text saying Chorus centred on screen
        // mvp.textSize(32);
        // mvp.textAlign(PConstants.CENTER, PConstants.CENTER);
        // mvp.fill(255);
        // mvp.text("Bridge", mvp.width / 2, mvp.height / 2);
        drawTerrain();
    }

    public void drawTerrain()
    {
        flying  -=  0.05;

        smoothedAmplitude = mvp.getSmoothedAmplitude();
        amplitude = smoothedAmplitude * 6000;

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
        mvp.rotateX(PI/3);

        mvp.translate(-w/2-increase/2, -h/2);
        for (int y = 0;  y  < rows-1; y++){
            mvp.beginShape(TRIANGLE_STRIP);
            for (int x = 0;  x < cols; x++){
                //rect(x *scl, y*scl, scl,scl);
                mvp.vertex(x*scl, y*scl, terrain[x][y]);
                mvp.vertex(x*scl, (y+1)*scl, terrain[x][y+1]);

            }
            mvp.endShape();
        }
    }
}