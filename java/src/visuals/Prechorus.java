package visuals;

import processing.core.*;

public class Prechorus extends PApplet
{
    MusicVisualiserProject mvp;
    int cols, rows;
    float[][] terrain;
    float flying;
    float amplitude;
    float smoothedAmplitude;
    int w;
    int h;
    int increase = 600;
    int scl = 20;

    // Reference to the main MusicVisualiserProject object

    // Constructor that accepts a MusicVisualiserProject object
    public Prechorus(MusicVisualiserProject mvp) {
        this.mvp = mvp;

    }

    // Method to render the Chorus visual
    public void render(int width, int height) {

        mvp.camera(mvp.width / 2, mvp.height / 2, (mvp.height / 2) / PApplet.tan(PApplet.PI / 6), mvp.width / 2, mvp.height / 2, 0, 0, 1, 0);

        w = width + increase;
        h = height + increase;
        cols = w / scl;
        rows = h / scl;
        terrain = new float[cols][rows];
        smoothedAmplitude = mvp.getSmoothedAmplitude();
        amplitude = smoothedAmplitude * 6000;

        mvp.tomato.rotateX(+50); // Rotate the model
        mvp.tomato.rotateY(+50); // Rotate the model


        drawTerrain();
        drawTomatoes();
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