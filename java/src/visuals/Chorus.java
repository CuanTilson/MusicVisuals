package visuals;

import processing.core.*;

public class Chorus {

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    PVector[] tomatoPositions;
    int numTomatoes = 50; // Adjust this value to change the number of tomatoes
    float tomatoSpeed = 8; // Adjust this value to change the speed of the tomatoes
    float tomatoScale;

    // Tube properties
    float tubeRadius = 300; // Adjust this value to change the radius of the tube
    float tubeLength = 2000; // Adjust this value to change the length of the tube
    int numSegments = 100; // Adjust this value to change the number of segments in the tube

    // Constructor that accepts a MusicVisualiserProject object
    public Chorus(MusicVisualiserProject mvp) {
        this.mvp = mvp;

        mvp.tomato.rotateX(+260); // Rotate the model

        generateTomatoPositions();
    }

    // Method to render the tomato shape
    public void tomato(MusicVisualiserProject mvp) {
        mvp.pushMatrix();
        mvp.shape(mvp.tomato);
        mvp.popMatrix();
    }

    void generateTomatoPositions() {
        tomatoPositions = new PVector[numTomatoes];
        for (int i = 0; i < numTomatoes; i++) {
            float x = mvp.random(-tubeRadius, tubeRadius);
            float y = mvp.random(-tubeRadius, tubeRadius);
            float z = mvp.random(-tubeLength / 2, tubeLength / 2);
            tomatoPositions[i] = new PVector(x, y, z);
        }
    }

    void updateFloatingTomatoes() {
        if (mvp.getAudioPlayer().isPlaying()) { // Check if music is playing
            float amplitude = mvp.getSmoothedAmplitude();
            float mappedSpeed = PApplet.map(amplitude, 0, 1, 1, 10); // Map amplitude to tomato speed range
            for (int i = 0; i < numTomatoes; i++) {
                tomatoPositions[i].z += mappedSpeed; // Update z-coordinate based on mapped tomato speed
                // Reset tomato position if it reaches the end of the tube
                if (tomatoPositions[i].z > tubeLength / 2) {
                    tomatoPositions[i].z = -tubeLength / 2;
                }
            }
        }
    }

    // Adjust scaling logic to ensure proportional scaling based on amplitude
    void drawFloatingTomatoes() {
        float amplitude = mvp.getSmoothedAmplitude();
        float tomatoSize = PApplet.map(amplitude, 0, 1, 20, 350); // Map amplitude to larger tomato size range
        for (int i = 0; i < numTomatoes; i++) {
            mvp.pushMatrix();
            mvp.translate(tomatoPositions[i].x, tomatoPositions[i].y, tomatoPositions[i].z);
            mvp.scale(tomatoSize / 75.0f); // Scale the tomato size based on amplitude

            // Apply color to tomato
            mvp.fill(255);
            tomato(mvp);
            mvp.popMatrix();
        }
    }

    void drawCylinder() {
        mvp.noFill();
        mvp.stroke(255); // Darker grey stroke color
        mvp.strokeWeight(2);
        mvp.beginShape(PConstants.TRIANGLE_STRIP);
        for (int i = 0; i <= numSegments; i++) {
            float angle = PConstants.TWO_PI * i / numSegments;
            float offset = (float) (mvp.getSmoothedAmplitude() * 0.5); // Adjust multiplier as needed
            angle += offset; // Add offset to the angle
            float x = PApplet.cos(angle) * tubeRadius;
            float y = PApplet.sin(angle) * tubeRadius;
            float z1 = -tubeLength / 2;
            float z2 = tubeLength / 2;
            mvp.vertex(x, y, z1);
            mvp.vertex(x, y, z2);
        }
        mvp.endShape();
    }

    // Method to render the Chorus visual
    public void render() {
        mvp.lights();
        mvp.translate(mvp.width / 2, mvp.height / 2);
        drawCylinder();
        updateFloatingTomatoes();
        drawFloatingTomatoes();
    }
}