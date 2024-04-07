package visuals;

import processing.core.*;

public class Chorus {

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    Tomato[] tomatoes; // Array to hold tomatoes
    int numTomatoes = 5; // Adjust this value to change the number of tomatoes

    int timeInterval = 1000; // Add new tomatoes every second
    int lastUpdateTime = 0;

    // Tube properties
    float tubeRadius = 300; // Adjust this value to change the radius of the tube
    float tubeLength = 2000; // Adjust this value to change the length of the tube
    int numSegments = 25; // Adjust this value to change the number of segments in the tube

    // Constructor that accepts a MusicVisualiserProject object
    public Chorus(MusicVisualiserProject mvp) {
        this.mvp = mvp;

        mvp.tomato.rotateX(+260); // Rotate the model

        generateTomatoes();
    }

    // Method to render the tomato shape
    public void tomato(MusicVisualiserProject mvp) {
        mvp.pushMatrix();
        mvp.shape(mvp.tomato);
        mvp.popMatrix();
    }

    // Method to generate tomatoes
    void generateTomatoes() {
        tomatoes = new Tomato[numTomatoes];
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
            float speed = mvp.random(1, 5); // Random speed between 1 and 10
            tomatoes[i] = new Tomato(x, y, z, speed);
        }
    }

    void updateFloatingTomatoes() {
        if (mvp.getAudioPlayer().isPlaying()) { // Check if music is playing
            int currentTime = mvp.millis();
            if (currentTime - lastUpdateTime > timeInterval) {
                addNewTomatoes(10); // Add 10 new tomatoes
                lastUpdateTime = currentTime;
            }
            for (int i = 0; i < numTomatoes; i++) {
                float tomatoSpeed = tomatoes[i].speed;
                tomatoes[i].position.z += tomatoSpeed;

                // Check for collisions with other tomatoes
                for (int j = 0; j < numTomatoes; j++) {
                    if (i != j) { // Avoid self-collision check
                        float distance = PVector.dist(tomatoes[i].position, tomatoes[j].position);
                        float minDistance = 50; // Adjust this value as needed
                        if (distance < minDistance) {
                            // Apply a force to push tomatoes away from each other
                            PVector direction = PVector.sub(tomatoes[i].position, tomatoes[j].position);
                            direction.normalize();
                            float forceMagnitude = (float) 0.1; // Adjust this value to control the force strength
                            PVector force = direction.copy().mult(forceMagnitude);
                            tomatoes[i].applyForce(force);
                            tomatoes[j].applyForce(force.copy().mult(-1)); // Apply force in opposite direction
                        }
                    }
                }

                if (tomatoes[i].position.z > tubeLength / 2) {
                    tomatoes[i].position.z = -tubeLength / 2;
                }

                // Update tomato velocity and position based on applied forces
                tomatoes[i].update();
            }
        }
    }

    void drawFloatingTomatoes() {
        float amplitude = mvp.getSmoothedAmplitude();
        float tomatoSize = PApplet.map(amplitude, 0, 1, 20, 1000) * 600; // Map amplitude to larger tomato size range
        for (int i = 0; i < numTomatoes; i++) {
            // Check if the tomato's position lies within the cylinder
            if (isInsideCylinder(tomatoes[i].position)) {
                mvp.pushMatrix();
                mvp.translate(tomatoes[i].position.x, tomatoes[i].position.y, tomatoes[i].position.z);
                mvp.scale(tomatoSize / 75.0f); // Scale the tomato size based on amplitude

                tomato(mvp);
                mvp.popMatrix();
            }
        }
    }

    void addNewTomatoes(int numNewTomatoes) {
        Tomato[] newTomatoes = new Tomato[numTomatoes + numNewTomatoes];
        System.arraycopy(tomatoes, 0, newTomatoes, 0, numTomatoes);

        float buffer = tubeRadius * 0.5f; // Define a buffer size as half of the tube radius

        // Define the range for generating new tomatoes within the buffer
        float minX = -tubeRadius + buffer;
        float maxX = tubeRadius - buffer;
        float minY = -tubeRadius + buffer;
        float maxY = tubeRadius - buffer;

        float step = tubeLength / numNewTomatoes; // Calculate the step size between new tomatoes
        float backSpread = tubeLength / 4; // Adjust this value to control how far back tomatoes spread

        for (int i = numTomatoes; i < numTomatoes + numNewTomatoes; i++) {
            float x = mvp.random(minX, maxX);
            float y = mvp.random(minY, maxY);
            // Spread out z values along the length of the tube, with more spread towards
            // the back
            float z = tubeLength / 2 + i * step + mvp.random(-backSpread, backSpread); // Adding at the back
            float speed = mvp.random(1, 5); // Random speed between 1 and 10
            newTomatoes[i] = new Tomato(x, y, z, speed);
        }

        tomatoes = newTomatoes;
        numTomatoes += numNewTomatoes;
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

        // Iterate through each segment
        for (int i = 0; i <= numSegments; i++) { // Change condition to include the last segment
            // Calculate the angle for the current segment
            float angle1 = angleIncrement * i + rotationAngle; // Apply rotation to angle
            float angle2 = angleIncrement * (i + 1) + rotationAngle; // Apply rotation to angle

            // Wrap angles to ensure they are within [0, TWO_PI) range
            angle1 = angle1 % PConstants.TWO_PI;
            angle2 = angle2 % PConstants.TWO_PI;

            // Calculate the vertices for the current segment
            float x1 = PApplet.cos(angle1) * tubeRadius;
            float y1 = PApplet.sin(angle1) * tubeRadius;
            float z1 = -tubeLength / 2;

            float x2 = PApplet.cos(angle2) * tubeRadius;
            float y2 = PApplet.sin(angle2) * tubeRadius;
            float z2 = tubeLength / 2;

            // Calculate the amplitude for the current segment
            int index = i % numSegments; // Ensure index wraps around for the last segment
            float amp1 = mvp.getFFT().getBand(index) * 50; // Adjust multiplier as needed
            float amp2 = mvp.getFFT().getBand((index + 1) % numSegments) * 50; // Adjust multiplier as needed

            // Modulate stroke color and weight based on amplitude
            mvp.stroke(255 - amp1, 255 - amp2, 255); // Set stroke color based on amplitude

            // Calculate the stroke weight based on amplitude
            float mappedStrokeWeight = PApplet.map(amp2, 0, 255, baseStrokeWeight,
                    maxStrokeWeight);
            mvp.strokeWeight(mappedStrokeWeight); // Set stroke weight based on amplitude

            // Draw a line segment connecting the vertices
            mvp.vertex(x1, y1, z1);
            mvp.vertex(x2, y2, z2);
        }

        // End the shape
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

class Tomato {
    PVector position;
    PVector velocity;
    float speed;

    Tomato(float x, float y, float z, float speed) {
        position = new PVector(x, y, z);
        velocity = new PVector();
        this.speed = speed;
    }

    // Method to apply force to the tomato
    void applyForce(PVector force) {
        velocity.add(force);
    }

    // Method to update the tomato's position based on velocity
    void update() {
        position.add(velocity);
    }
}