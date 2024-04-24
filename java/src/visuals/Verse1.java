// By Sagar Singh

package visuals;

import processing.core.*;
import ie.tudublin.*;

public class Verse1 extends Visual {
    MusicVisualiserProject mvp;
    Planet sun;
    int planetCount;
    PGraphics sunColor;

    float currentHue;
    float[] camPos;
    int camCounter;
    float[] camMovement;
    float camAngle;
    float camDist;

    float amp;

    Verse1(MusicVisualiserProject mvp) {
        this.mvp = mvp;

        sun = new Planet(0, 100, 0); // since zero distance, no translation

        planetCount = 7; // default 6
        sun.spawnOrbitingBodies(planetCount);

        sun.orbitingBody[planetCount - 2].spawnOrbitingBodies(1); // second last planet to have a moon
        sun.orbitingBody[planetCount - 2].orbitingBody[0].dist = 1; // moon distance
        sun.orbitingBody[planetCount - 2].orbitingBody[0].orbitSpeed = 0.040f; // moon orbit speed

        sun.orbitSpeed = 0; // sun does not orbit
        sun.rotationSpeed = 0; // sun does not rotate
        sun.position = 0;
        sun.tiltX = sun.tiltZ = sun.tiltOffset = 0; // complete symmertical to the XYZ field

        this.sunColor = mvp.createGraphics(width, height, mvp.P3D);
        this.currentHue = 0;

        this.camPos = new float[3];
        this.camPos[2] = 150;// infront of sun
        this.camPos[0] = this.camPos[1] = 0;
        this.camCounter = 0;
        this.camMovement = new float[2];
        this.camAngle = 0;

        // the total distance the camera will move to
        this.camDist = 0.8f * (mvp.height / 2.0f) / mvp.tan(PI * 30.0f / 180.0f);

        // step size to go from initial posZ to final in 5 seconds in 60 frame rate
        this.camMovement[0] = (camDist - camPos[2]) / (60 * 5);

    }

    public void render(int width, int height) {

        mvp.translate(width / 2, height / 2); // center the window
        mvp.colorMode(PApplet.HSB); // Set color mode to HSB

        amp = mvp.getSmoothedAmplitude() * 8000; // used scale the sun tomato
        currentHue = 35 + mvp.getSmoothedAmplitude() * 3500; // color change of sun

        // Camera
        camCounter++;

        // if 5 seconds have passed
        if (60 * 5 > camCounter) {
            camPos[2] += camMovement[0]; // pan out in Z
        } else if (camAngle < QUARTER_PI / 3) { // now pan above in Y
            camAngle += QUARTER_PI / (60 * 10);
            camPos[1] = -mvp.sin(camAngle) * camDist * 1.2f;
        }

        mvp.camera(camPos[0], camPos[1], camPos[2],
                0, 0, 0f,
                0f, 1f, 0f);

        // lighting
        PVector dLighVector = new PVector(-camPos[0], -camPos[1], -camPos[2]); // lighting towards origin from camera
        dLighVector.normalize();
        mvp.pointLight(currentHue, 255, 150, 0, 0, 0); // light from sun

        // planetTomato is Black and white color, this gives the illusion of them being
        // red with faint red light on all objects
        mvp.directionalLight(0, 200, 100, dLighVector.x, dLighVector.y, dLighVector.z);

        // color for sun
        sunColor.beginDraw();
        sunColor.colorMode(PApplet.HSB);
        sunColor.fill(currentHue, 150, 200);
        sunColor.box(1550);
        sunColor.endDraw();

        // animating solar system
        animate(sun);

    }

    public void animate(Planet p) {
        mvp.pushMatrix();
        mvp.noStroke();
        mvp.fill(255);
        PVector rotationAxis; // for orbits
        PVector perpendicular;

        // orbit paths tilting
        if (p == sun.orbitingBody[planetCount - 1 - 2]) {
            rotationAxis = new PVector(0, 1, 5);
        } else if (p == sun.orbitingBody[planetCount - 1]) {
            rotationAxis = new PVector(0, -1, 6);
        } else if (p == sun.orbitingBody[planetCount - 2].orbitingBody[0]) { // moon to have extreme tilt
            rotationAxis = new PVector(0, 2, 1);
        } else {
            rotationAxis = new PVector(0, 0, 1); // default
        }

        perpendicular = p.orbitVector.cross(rotationAxis);// direction of rotation

        mvp.rotate(p.position, perpendicular.x, perpendicular.y, perpendicular.z);
        mvp.translate(p.orbitVector.x, p.orbitVector.y, p.orbitVector.z);

        mvp.pushMatrix();

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
            mvp.emissive(150); // material property which ignores other light and emits own
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

}
