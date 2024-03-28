package visuals;

import processing.core.*;
import ie.tudublin.*;

public class Verse1 extends Visual {
    MusicVisualiserProject mvp;
    Planet sun;
    int planetCount;

    Verse1(MusicVisualiserProject mvp) {
        this.mvp = mvp;
        sun = new Planet(0, 100); // since zero distance, no translation
        planetCount = 6;
        sun.spawnOrbitingBodies(planetCount);
        sun.orbitingBody[planetCount - 2].spawnOrbitingBodies(1); // second last planet to have a moon
        sun.orbitingBody[planetCount - 2].orbitingBody[0].dist = 1; // moon distance
        sun.orbitSpeed = 0; // sun does not spin
        sun.position = 0;
    }

    public void render(int width, int height) {
        mvp.translate(width / 2, height / 2); // center the window
        mvp.colorMode(PApplet.HSB); // Set color mode to HSB
        mvp.lights();
        mvp.shapeMode(CENTER);
        animate(sun);
    }

    public void animate(Planet p) {
        mvp.pushMatrix();
        mvp.noStroke();
        mvp.fill(255);
        PVector rotationAxis;
        PVector perpendicular;
        if (p == sun.orbitingBody[planetCount - 1 - 2]) {
            rotationAxis = new PVector(0, 1, 5);
        } else if (p == sun.orbitingBody[planetCount - 1]) {
            rotationAxis = new PVector(0, -1, 6);
        } else if (p == sun.orbitingBody[planetCount - 2].orbitingBody[0]) {
            rotationAxis = new PVector(0, 2, 1);
        } else {
            rotationAxis = new PVector(0, 0, 1);
        }

        perpendicular = p.orbitVector.cross(rotationAxis);

        mvp.rotate(p.position, perpendicular.x, perpendicular.y, perpendicular.z);
        mvp.translate(p.orbitVector.x, p.orbitVector.y, p.orbitVector.z);
        mvp.sphere(p.size / 2);

        if (p.orbitingBody != null) {
            for (int i = 0; i < p.orbitingBody.length; i++) {
                animate(p.orbitingBody[i]); // animate all its orbiting bodies
            }
        }
        mvp.popMatrix();
        p.orbit();

    }

}
