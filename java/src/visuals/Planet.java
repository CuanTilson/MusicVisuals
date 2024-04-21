
// By Sagar Singh
package visuals;

import ie.tudublin.Visual;
import processing.core.PVector;

public class Planet extends Visual {

    float size; // diameter
    float dist; // distance from orbiting body
    float position; // in radians
    Planet[] orbitingBody;
    float orbitSpeed; // in radians
    PVector orbitVector; // direction of orbit path

    float rotation; // current rotation angle of planet
    float rotationSpeed; // in radians
    float tiltX, tiltZ; // planet tilt orientation for spin on axis
    float tiltOffset; // offset from spinning

    public Planet(float dist, float size, float orbitSpeed) {
        this.dist = dist;
        this.orbitVector = new PVector(1, 0, 0);
        this.orbitVector.mult(this.dist);

        this.size = size;

        // orbit rotation
        this.position = random(TWO_PI);
        this.orbitSpeed = orbitSpeed;

        // planet rotation and positioning
        this.rotation = 0;
        this.rotationSpeed = random((float) 0.01, (float) 0.05);
        this.tiltX = random(-QUARTER_PI, 0);
        this.tiltZ = random(-QUARTER_PI, QUARTER_PI);
        this.tiltOffset = random(QUARTER_PI);
    }

    public void spawnOrbitingBodies(int nBodies) {
        this.orbitingBody = new Planet[nBodies];

        for (int i = 0; i < nBodies; i++) {
            float newSize = random(size / 6, size / 4) + i * 2;
            float newDist = 80 * (i + 1) + 30;
            float newOrbitSpeed = map(i, 0, nBodies, 0.015f, 0.001f);
            if (nBodies == 1) { // moon dist closer
                newDist = 40;
            }
            orbitingBody[i] = new Planet(newDist, newSize, newOrbitSpeed);
        }
    }

    public void orbit() {
        position += orbitSpeed;
    }

    public void rotate() {
        rotation += rotationSpeed;
    }

}
