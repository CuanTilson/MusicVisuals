package visuals;

import processing.core.*;

public class Chorus {

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    PVector[] tomatoPositions;
    int numTomatoes = 50; // Adjust this value to change the number of tomatoes

    // Tube properties
    float tubeRadius = 300; // Adjust this value to change the radius of the tube
    float tubeLength = 2000; // Initial length of the tube
    int numSegments = 25; // Adjust this value to change the number of segments in the tube

    float cameraSpeed = 2.0f; // Adjust this value to change the camera speed

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
        float buffer = tubeRadius * 0.5f; // Define a buffer size as half of the tube radius

        // Define the range for generating tomatoes within the buffer
        float minX = -tubeRadius + buffer;
        float maxX = tubeRadius - buffer;
        float minY = -tubeRadius + buffer;
        float maxY = tubeRadius - buffer;

        float step = tubeLength / numTomatoes; // Calculate the step size between tomatoes
        float backSpread = tubeLength / 4; // Adjust this value to control how far back tomatoes spread

        for (int i = 0; i < numTomatoes; i++) {
            float x = mvp.random(minX, maxX);
            float y = mvp.random(minY, maxY);
            // Spread out z values along the length of the tube, with more spread towards
            // the back
            float z = -tubeLength / 2 + i * step + mvp.random(-backSpread, backSpread);
            tomatoPositions[i] = new PVector(x, y, z);
        }
    }

    void updateFloatingTomatoes() {
        if (mvp.getAudioPlayer().isPlaying()) { // Check if music is playing
            float amplitude = mvp.getSmoothedAmplitude();
            float mappedSpeed = PApplet.map(amplitude, 0, 1, 1, 10);
            float tomatoDiameter = 50; // Diameter of the tomato model

            for (int i = 0; i < numTomatoes; i++) {
                tomatoPositions[i].z += mappedSpeed;
                if (tomatoPositions[i].z > tubeLength / 2) {
                    tomatoPositions[i].z = -tubeLength / 2;
                }

                // Check for collisions with other tomatoes
                for (int j = 0; j < numTomatoes; j++) {
                    if (i != j) { // Skip self-comparison
                        float distance = tomatoPositions[i].dist(tomatoPositions[j]);
                        if (distance < tomatoDiameter) {
                            // If a collision occurs, adjust the position of the current tomato
                            float angle = PApplet.atan2(tomatoPositions[j].y - tomatoPositions[i].y,
                                    tomatoPositions[j].x - tomatoPositions[i].x);
                            float newX = tomatoPositions[i].x - PApplet.cos(angle) * (tomatoDiameter - distance) / 2;
                            float newY = tomatoPositions[i].y - PApplet.sin(angle) * (tomatoDiameter - distance) / 2;
                            tomatoPositions[i].x = newX;
                            tomatoPositions[i].y = newY;
                        }
                    }
                }
            }
        }
    }

    void drawFloatingTomatoes() {
        float amplitude = mvp.getSmoothedAmplitude();
        float tomatoSize = PApplet.map(amplitude, 0, 1, 20, 1000) * 600; // Map amplitude to larger tomato size range
        for (int i = 0; i < numTomatoes; i++) {
            // Check if the tomato's position lies within the cylinder
            if (isInsideCylinder(tomatoPositions[i])) {
                mvp.pushMatrix();
                mvp.translate(tomatoPositions[i].x, tomatoPositions[i].y, tomatoPositions[i].z);
                mvp.scale(tomatoSize / 75.0f); // Scale the tomato size based on amplitude

                tomato(mvp);
                mvp.popMatrix();
            }
        }
    }

    boolean isInsideCylinder(PVector position) {
        float d = PApplet.dist(0, 0, position.x, position.y); // Distance from the center of the cylinder's base
        return d <= tubeRadius && position.z >= -tubeLength / 2 && position.z <= tubeLength / 2;
    }

    void drawCylinder() {
        mvp.noFill();
        mvp.stroke(255); // Darker grey stroke color

        // Base and maximum stroke weight
        float baseStrokeWeight = 3;
        float maxStrokeWeight = 12;

        // Calculate the increment angle for each segment
        float angleIncrement = PConstants.TWO_PI / numSegments;

        // Incremental rotation angle
        float rotationAngle = mvp.frameCount * 0.001f; // Adjust the rotation speed as needed

        // Begin drawing the lines for the cylinder
        mvp.beginShape(PConstants.LINES);

        // Calculate the length of the cylinder along the z-axis
        float zStart = -tubeLength / 2;
        float zEnd = tubeLength / 2;

        // Iterate through each segment
        for (float z = zStart; z < zEnd; z += tubeLength / numSegments) {
            for (int i = 0; i <= numSegments; i++) {
                // Calculate the angle for the current segment
                float angle1 = angleIncrement * i + rotationAngle; // Apply rotation to angle
                float angle2 = angleIncrement * (i + 1) + rotationAngle; // Apply rotation to angle

                // Wrap angles to ensure they are within [0, TWO_PI) range
                angle1 = angle1 % PConstants.TWO_PI;
                angle2 = angle2 % PConstants.TWO_PI;

                // Calculate the vertices for the current segment
                float x1 = PApplet.cos(angle1) * tubeRadius;
                float y1 = PApplet.sin(angle1) * tubeRadius;
                float x2 = PApplet.cos(angle2) * tubeRadius;
                float y2 = PApplet.sin(angle2) * tubeRadius;

                // Calculate the amplitude for the current segment
                int index = i % numSegments; // Ensure index wraps around for the last segment
                float amp1 = mvp.getFFT().getBand(index) * 50; // Adjust multiplier as needed
                float amp2 = mvp.getFFT().getBand((index + 1) % numSegments) * 50; // Adjust multiplier as needed

                // Modulate stroke color and weight based on amplitude
                mvp.stroke(255 - amp1, 255 - amp2, 255); // Set stroke color based on amplitude
                float mappedStrokeWeight = PApplet.map(amp2, 0, 255, baseStrokeWeight, maxStrokeWeight);
                mvp.strokeWeight(mappedStrokeWeight); // Set stroke weight based on amplitude

                // Draw a line segment connecting the vertices
                mvp.vertex(x1, y1, z);
                mvp.vertex(x2, y2, z);
            }
        }

        // End the shape
        mvp.endShape();
    }

    // Method to render the Chorus visual
    public void render() {
        mvp.noLights();
        mvp.lights();
        mvp.translate(mvp.width / 2, mvp.height / 2);
        drawCylinder();
        updateFloatingTomatoes();
        drawFloatingTomatoes();
    }
}
