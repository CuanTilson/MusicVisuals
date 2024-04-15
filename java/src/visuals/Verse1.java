package visuals;

import processing.core.*;
import ie.tudublin.*;

public class Verse1 extends Visual {
    MusicVisualiserProject mvp;
    Planet sun;
    int planetCount;
    PGraphics sunColor;

    float move = 0;

    Verse1(MusicVisualiserProject mvp) {
        this.mvp = mvp;

        sun = new Planet(0, 100); // since zero distance, no translation

        planetCount = 6;
        sun.spawnOrbitingBodies(planetCount);

        sun.orbitingBody[planetCount - 2].spawnOrbitingBodies(1); // second last planet to have a moon
        sun.orbitingBody[planetCount - 2].orbitingBody[0].dist = 1; // moon distance
        sun.orbitingBody[planetCount - 2].orbitingBody[0].orbitSpeed = 0.040f;

        sun.orbitSpeed = 0; // sun does not orbit
        sun.rotationSpeed = 0; // sun does not rotate
        sun.position = 0;
        sun.tiltX = sun.tiltZ = sun.tiltOffset = 0;

        this.sunColor = mvp.createGraphics(width, height, mvp.P3D);

    }

    public void render(int width, int height) {
        mvp.translate(width / 2, height / 2); // center the window
        mvp.colorMode(PApplet.HSB); // Set color mode to HSB

        move = (float) ((move + 0.5) % 255); // 0.01 good

        // lighting
        // mvp.lights();
        mvp.pointLight(move, 255, 150, 0, 0, 0); // light from sun
        mvp.directionalLight(0, 150, 150, 0, 1, 0); // faint light on all objects

        // color
        sunColor.beginDraw();
        sunColor.colorMode(PApplet.HSB);
        sunColor.fill(move, 150, 200);
        sunColor.box(width);
        sunColor.endDraw();

        // animating solar system
        animate(sun);

        // camera
        // top down view
        // mvp.camera(width / 2, -300,
        // (300), // eye position
        // width / 2, height / 2, (float) 0, // center of the scene
        // (float) 0, (float) 1, (float) 0);
    }

    public void animate(Planet p) {
        mvp.pushMatrix();
        mvp.noStroke();
        mvp.fill(255);
        PVector rotationAxis;
        PVector perpendicular;

        // orbit paths
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

        mvp.pushMatrix();

        // mvp.sphere(p.size / 2);

        // orientation
        mvp.rotateX(p.tiltX);
        mvp.rotateZ(p.tiltZ);

        // rotation
        mvp.rotateY(p.rotation);

        // rotation tilt
        mvp.rotateX(p.tiltOffset);

        // tomato tilt correction
        mvp.rotateX(PI);

        mvp.scale(30 * (p.size / 2));

        // texture the sun
        if (p == sun) {
            mvp.sunTomato.setTexture(sunColor);
            mvp.emissive(150);
            mvp.shape(mvp.sunTomato);
        } else {
            mvp.emissive(0);
            mvp.shape(mvp.planetTomato);
        }

        mvp.popMatrix();

        if (p.orbitingBody != null) {
            for (int i = 0; i < p.orbitingBody.length; i++) {
                animate(p.orbitingBody[i]); // animate all its orbiting bodies
            }
        }

        mvp.popMatrix();
        p.orbit();
        p.rotate();

    }

    public void test(int width, int height) {
        mvp.background(0);

        mvp.translate(width / 2, height / 2); // center the window
        mvp.colorMode(PApplet.HSB); // Set color mode to HSB

        mvp.lights();
        // mvp.pointLight(move * 10, 100, 255, 0, 0, 0); // color change
        // mvp.pointLight(move * 10, 0, 255, 0, 0, 0); // color change

        // mvp.ambientLight(0, 0, 50);
        // mvp.directionalLight(0, 0, 150, 0, 0, -1);

        move = (float) ((move + 0.1) % 25); // 0.01 good

        mvp.pushMatrix();
        mvp.translate(0, 0);

        // orientation
        mvp.rotateX(-QUARTER_PI);// -PI/4 to 0
        mvp.rotateZ(-QUARTER_PI);// -PI/4 to PI/4

        // rotation
        mvp.rotateY(move);// 0.005 to 0.03

        // rotation tilt
        mvp.rotateX(QUARTER_PI / 3); // 0 to PI/4

        // object tilt correction
        mvp.rotateX(QUARTER_PI);

        // color;
        sunColor.beginDraw();
        sunColor.colorMode(PApplet.HSB);
        sunColor.fill(move * 10, 150, 200);
        sunColor.box(width);
        sunColor.endDraw();

        mvp.tomato.disableStyle();
        // mvp.tomato.setTexture(sunColor);
        mvp.tomato.setFill(color(200, 255, 255));

        // mvp.tomato.setEmissive(1);
        mvp.emissive(150); // 150
        mvp.scale(1000);
        mvp.shape(mvp.tomato);
        mvp.popMatrix();

        mvp.translate(300, 0, 0);
        mvp.noStroke();
        mvp.fill(0, 255, 255);
        mvp.emissive(0);
        mvp.sphere(50);

    }

}
