package visuals;

import ie.tudublin.Visual;
import processing.core.PVector;

public class Planet extends Visual {

    float size; // diameter
    float dist; // distance from orbiting body
    float position; // in radians
    Planet[] orbitingBody;
    float orbitSpeed; // in radians
    float rotation; // current rotation angle of planet
    float rotationSpeed; // in radians
    PVector orbitVector; // direction of orbit path
    PVector planetTiltVector; // direction of planet spin

    public Planet(float dist, float size) {
        this.dist = dist;
        this.orbitVector = new PVector(1, 0, 0);
        this.orbitVector.mult(this.dist);

        this.size = size;
        this.position = random(TWO_PI);
        this.orbitSpeed = random((float) 0.001, (float) 0.01);
        this.rotation = 0;
        this.rotationSpeed = random((float) 0.05, (float) 0.1);
    }

    public void spawnOrbitingBodies(int nBodies) {
        this.orbitingBody = new Planet[nBodies];

        for (int i = 0; i < nBodies; i++) {
            float newSize = size / 3;
            float newDist = 60 * (i + 1);
            if (nBodies == 1) {
                newDist = 30;
            }
            orbitingBody[i] = new Planet(newDist, newSize);
        }
    }

    public void orbit() {
        position += orbitSpeed;
    }

    public void rotate() {
        rotation += rotationSpeed;
    }

}
