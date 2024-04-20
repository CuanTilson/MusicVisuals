package visuals;

import processing.core.*;
import ie.tudublin.*;

public class Verse1 extends Visual {
    MusicVisualiserProject mvp;
    Planet sun;
    int planetCount;
    PGraphics sunColor;

    float move = 0;
    float[] camPos;
    int camCounter;
    float[] camMovement;
    float camAngle;
    float camDist;

    float amp;

    Verse1(MusicVisualiserProject mvp) {
        this.mvp = mvp;

        sun = new Planet(0, 100, 0); // since zero distance, no translation

        planetCount = 7; // 6
        sun.spawnOrbitingBodies(planetCount);

        sun.orbitingBody[planetCount - 2].spawnOrbitingBodies(1); // second last planet to have a moon
        sun.orbitingBody[planetCount - 2].orbitingBody[0].dist = 1; // moon distance
        sun.orbitingBody[planetCount - 2].orbitingBody[0].orbitSpeed = 0.040f;

        sun.orbitSpeed = 0; // sun does not orbit
        sun.rotationSpeed = 0; // sun does not rotate
        sun.position = 0;
        sun.tiltX = sun.tiltZ = sun.tiltOffset = 0;

        this.sunColor = mvp.createGraphics(width, height, mvp.P3D);

        this.camPos = new float[3];
        this.camPos[2] = 150;// infront of sun
        this.camPos[0] = this.camPos[1] = 0;
        this.camCounter = 0;
        this.camMovement = new float[2];
        this.camAngle = 0;
        this.camDist = 1.2f * (mvp.height / 2.0f) / mvp.tan(PI * 30.0f / 180.0f);

        // step size to go from initial posZ to final in 5 seconds in 60 frame rate
        this.camMovement[0] = (camDist - camPos[2]) / (60 * 5);

    }

    public void render(int width, int height) {

        mvp.translate(width / 2, height / 2); // center the window
        mvp.colorMode(PApplet.HSB); // Set color mode to HSB

        move = (float) ((move + 0.5) % 255); // 0.01 good
        amp = mvp.getSmoothedAmplitude() * 8000;
        move = 35 + mvp.getSmoothedAmplitude() * 3500;

        // Camera
        camCounter++;

        if (60 * 5 > camCounter) {
            camPos[2] += camMovement[0];
        } else if (camAngle < QUARTER_PI / 3) {
            camAngle += QUARTER_PI / (60 * 10);
            camPos[1] = -mvp.sin(camAngle) * camDist;
            camPos[2] = camDist - mvp.sin(camAngle) * camDist + 1;
        }

        mvp.camera(camPos[0], camPos[1], camPos[2],
                0, 0, 0f,
                0f, 1f, 0f);

        // lighting
        PVector dLighVector = new PVector(-camPos[0], -camPos[1], -camPos[2]);
        dLighVector.normalize();
        mvp.pointLight(move, 255, 150, 0, 0, 0); // light from sun
        mvp.directionalLight(0, 200, 150, dLighVector.x, dLighVector.y, dLighVector.z); // faint light on all objects

        // color
        sunColor.beginDraw();
        sunColor.colorMode(PApplet.HSB);
        sunColor.fill(move, 150, 200);
        sunColor.box(width);
        sunColor.endDraw();

        // animating solar system
        animate(sun);

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

        float scale = 30 * (p.size / 2);

        // texture the sun
        if (p == sun) {
            mvp.scale(scale + amp);
            mvp.sunTomato.setTexture(sunColor);
            mvp.emissive(150);
            mvp.shape(mvp.sunTomato);
        } else {
            mvp.scale(scale);
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
        mvp.noStroke();

        mvp.pushMatrix();
        mvp.translate(0, 0);
        mvp.fill(0, 255, 255);
        mvp.box(100);
        mvp.popMatrix();

        mvp.pushMatrix();

        mvp.translate(300, 0);
        mvp.fill(150, 255, 255);
        mvp.box(50);
        mvp.popMatrix();

        mvp.pushMatrix();
        mvp.translate(0, 300);
        mvp.fill(100, 255, 255);
        mvp.box(50);
        mvp.popMatrix();

        mvp.pushMatrix();
        mvp.translate(-300, 0);
        mvp.fill(200, 255, 255);
        mvp.box(50);
        mvp.popMatrix();

        mvp.pushMatrix();
        mvp.translate(0, -300);
        mvp.fill(50, 255, 255);
        mvp.box(50);
        mvp.popMatrix();

        // camera
    }

}
