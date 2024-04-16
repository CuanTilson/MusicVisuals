package visuals;

import processing.core.*;

public class Bridge extends PApplet
{
    int cols, rows;
    int scl = 20;
    float[][] terrain;
    int w = 2048;
    int h  = 1500;
    float flying;
    float smoothedAmplitude = 0;

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    // Constructor that accepts a MusicVisualiserProject object
    public Bridge(MusicVisualiserProject mvProject) {
        this.mvp = mvProject;
    }

    // Method to render the Chorus visual
    public void render() {
        // Text saying Chorus centred on screen
        mvp.textSize(32);
        mvp.textAlign(PConstants.CENTER, PConstants.CENTER);
        mvp.fill(255);
        mvp.text("Bridge", mvp.width / 2, mvp.height / 2);
    }

    public void drawTerrain()
    {
        flying  -=  0.05;

        float amplitude = smoothedAmplitude * 6000;

        float yoff = flying;
        for (int y = 0;  y  < rows; y++){
            float xoff  = 0;
            for (int x = 0;  x < cols; x++){
                terrain[x][y] = map(noise(xoff, yoff) , 0, 1, -amplitude, amplitude);
                xoff += 0.1;
            }
            yoff +=  0.1;
        }

        
        background(0);
        stroke(255);
        noFill();
        
        translate(width/2, height/2);
        rotateX(PI/3);

        translate(-w/2, -h/2);
        for (int y = 0;  y  < rows-1; y++){
            beginShape(TRIANGLE_STRIP);
            for (int x = 0;  x < cols; x++){
                //rect(x *scl, y*scl, scl,scl);
                vertex(x*scl, y*scl, terrain[x][y]);
                vertex(x*scl, (y+1)*scl, terrain[x][y+1]);

            }
            endShape();
        }
    }
}