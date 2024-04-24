package visuals;

import processing.core.*;

public class Chorus {

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    PVector[] tomatoPositions;
    int numTomatoes = 60; // Adjust this value to change the number of tomatoes
    float[] tomatoSpeeds; // Array to store the speed of each tomato

    // Tube properties
    float tubeRadius = 300; // Adjust this value to change the radius of the tube
    float tubeLength = 2000; // Initial length of the tube
    int numSegments = 25; // Adjust this value to change the number of segments in the tube

    // Constructor that accepts a MusicVisualiserProject object
    public Chorus(MusicVisualiserProject mvp) {
        this.mvp = mvp;
        mvp.tomato.rotateX(+260); // Rotate the model
        initialiseTomatoes();
    }

    // Method to render the tomato shape
    public void tomato(MusicVisualiserProject mvp) {
        mvp.pushMatrix();
        mvp.shape(mvp.tomato);
        mvp.popMatrix();
    }

    void initialiseTomatoes() {
        tomatoPositions = new PVector[numTomatoes];
        tomatoSpeeds = new float[numTomatoes];
        float buffer = tubeRadius * 0.4f; // Define a buffer size as half of the tube radius
        float minX = -tubeRadius + buffer;
        float maxX = tubeRadius - buffer;
        float minY = -tubeRadius + buffer;
        float maxY = tubeRadius - buffer;

        for (int i = 0; i < numTomatoes; i++) {
            float x = mvp.random(minX, maxX);
            float y = mvp.random(minY, maxY);
            float z = mvp.random(-tubeLength / 2, tubeLength / 2);
            tomatoPositions[i] = new PVector(x, y, z);
            tomatoSpeeds[i] = mvp.random(1, 8); // Random speed between 1 and 8
        }
    }

    void updateFloatingTomatoes() {
        if (mvp.getAudioPlayer().isPlaying()) {
            for (int i = 0; i < numTomatoes; i++) {
                tomatoPositions[i].z += tomatoSpeeds[i];
                if (tomatoPositions[i].z > tubeLength / 2) {
                    tomatoPositions[i].z = -tubeLength / 2;
                }

                // Apply repulsive force between tomatoes
                for (int j = 0; j < numTomatoes; j++) {
                    if (i != j) { // Avoid self-collision check
                        float distance = tomatoPositions[i].dist(tomatoPositions[j]);
                        float minDistance = 50; // Adjust this value as needed
                        if (distance < minDistance) {
                            // Calculate repulsive force
                            PVector direction = PVector.sub(tomatoPositions[i], tomatoPositions[j]).normalize();
                            float repulsionMagnitude = 0.1f; // Adjust this value to control the repulsion strength
                            float repulsionFactor = 2.0f; // Adjust this value to control the effect of repulsion
                            PVector repulsionForce = direction.mult(repulsionMagnitude).mult(repulsionFactor);

                            // Apply repulsive force to both tomatoes
                            tomatoPositions[i].add(repulsionForce);
                            tomatoPositions[j].sub(repulsionForce);

                            // Reverse velocities upon collision
                            // Reverse velocities upon collision
                            float velocityDiff = tomatoSpeeds[j] - tomatoSpeeds[i];
                            tomatoSpeeds[i] += velocityDiff;
                            tomatoSpeeds[j] -= velocityDiff;

                        }
                    }
                }

                // Ensure tomatoes stay within the cylinder's sides
                if (!isInsideCylinder(tomatoPositions[i])) {
                    // Calculate correction vector to move tomato back inside the cylinder
                    PVector correction = PVector.sub(new PVector(0, 0, tomatoPositions[i].z), tomatoPositions[i])
                            .normalize().mult(tubeRadius - 50);
                    tomatoPositions[i].add(correction);
                }
            }
            generateNewTomatoes();
        }
    }

    void generateNewTomatoes() {
        for (int i = 0; i < numTomatoes; i++) {
            if (tomatoPositions[i].z < -tubeLength / 2) {
                float x = mvp.random(-tubeRadius, tubeRadius);
                float y = mvp.random(-tubeRadius, tubeRadius);
                float z = -tubeLength / 2 + mvp.random(0, tubeLength / 4);
                tomatoPositions[i] = new PVector(x, y, z);
            }
        }
    }

    void drawFloatingTomatoes() {
        float amplitude = mvp.getSmoothedAmplitude();
        float tomatoSize = PApplet.map(amplitude, 0, 1, 20, 1000) * 600; // Map amplitude to larger tomato size range
        float rotationSpeed = 0.02f; // Adjust the rotation speed

        for (int i = 0; i < numTomatoes; i++) {
            if (isInsideCylinder(tomatoPositions[i])) {
                mvp.pushMatrix();
                mvp.translate(tomatoPositions[i].x, tomatoPositions[i].y, tomatoPositions[i].z);
                mvp.scale(tomatoSize / 75.0f); // Scale the tomato size based on amplitude
                float tomatoRotationY = mvp.frameCount * rotationSpeed + i * 0.1f;
                float tomatoRotationZ = mvp.frameCount * rotationSpeed + i * 0.2f;
                mvp.rotateY(tomatoRotationY);
                mvp.rotateZ(tomatoRotationZ);
                tomato(mvp);
                mvp.popMatrix();
            }
        }
    }

    boolean isInsideCylinder(PVector position) {
        float d = PApplet.dist(0, 0, position.x, position.y);
        return d <= tubeRadius && position.z >= -tubeLength / 2 && position.z <= tubeLength / 2;
    }

    void drawCylinder() {
        mvp.noFill();
        mvp.stroke(255); // Darker grey stroke color
        float baseStrokeWeight = 3;
        float maxStrokeWeight = 12;
        float angleIncrement = PConstants.TWO_PI / numSegments;
        float rotationAngle = mvp.frameCount * 0.001f;

        mvp.beginShape(PConstants.LINES);
        float zStart = -tubeLength / 2;
        float zEnd = tubeLength / 2;

        for (float z = zStart; z < zEnd; z += tubeLength / numSegments) {
            for (int i = 0; i <= numSegments; i++) {
                float angle1 = angleIncrement * i + rotationAngle;
                float angle2 = angleIncrement * (i + 1) + rotationAngle;
                angle1 = angle1 % PConstants.TWO_PI;
                angle2 = angle2 % PConstants.TWO_PI;
                float x1 = PApplet.cos(angle1) * tubeRadius;
                float y1 = PApplet.sin(angle1) * tubeRadius;
                float x2 = PApplet.cos(angle2) * tubeRadius;
                float y2 = PApplet.sin(angle2) * tubeRadius;
                int index = i % numSegments;
                float amp1 = mvp.getFFT().getBand(index) * 50;
                float amp2 = mvp.getFFT().getBand((index + 1) % numSegments) * 50;
                mvp.stroke(255 - amp1, 255 - amp2, 255);
                float mappedStrokeWeight = PApplet.map(amp2, 0, 255, baseStrokeWeight, maxStrokeWeight);
                mvp.strokeWeight(mappedStrokeWeight);
                mvp.vertex(x1, y1, z);
                mvp.vertex(x2, y2, z);
            }
        }

        mvp.endShape();
    }

    // Method to render the Chorus visual
    public void render() {
        mvp.camera(mvp.width / 2, mvp.height / 2, (mvp.height / 2) / PApplet.tan(PApplet.PI / 6), mvp.width / 2,
                mvp.height / 2, 0, 0, 1, 0);

        // mvp.ambientLight(128, 128, 128);
        // mvp.directionalLight(192, 192, 192, -1, -1, -1);

        mvp.lights();
        mvp.translate(mvp.width / 2, mvp.height / 2);
        drawCylinder();
        updateFloatingTomatoes();
        drawFloatingTomatoes();
    }
}