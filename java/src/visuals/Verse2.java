package visuals;

import ie.tudublin.*;

import processing.core.*;

public class Verse2 {
    MusicVisualiserProject mvp;
    PGraphics CD; // circle for masking album cover
    PImage CDCover; // album cover

    PGraphics[] band; // curved line around the CD
    PGraphics[] bandMask;
    int bandCount;
    int vertices;
    float[] rotate;
    float[] rot;
    float[] multiplier;
    int[] colorShift;
    int[] shiftCounter;
    int[] shiftDir;

    int width;
    int height;
    int centerX;
    int centerY;
    int CDpositionX;
    int CDpositionY;
    int CDSize;
    float rotation;
    float r1;
    float r2;
    float[] smoothedBands;

    int cdPhase;
    int moveBandCounter;
    float[] mbX;
    float[] mbY;
    float currentDist;
    float currentRotation;

    float panOutDist;

    public Verse2(MusicVisualiserProject mvProject) {
        this.mvp = mvProject;
        this.width = mvp.width;
        this.centerX = this.width / 2;
        this.height = mvp.height;
        this.centerY = this.height / 2;

        this.rotation = 0;
        this.r1 = mvp.PI / 40;
        this.r2 = mvp.PI / 80;

        this.CDSize = height / 3;
        this.CD = mvp.createGraphics(CDSize, CDSize);
        this.CDCover = new PImage();
        this.CDCover = mvp.loadImage("cover.jpg");
        CDCover.resize(CDSize, CDSize);
        this.cdPhase = 0;
        CDpositionX = -CDSize - 10; // CD spawned out of frame
        CDpositionY = centerY;
        createCD();

        this.bandCount = 5;
        this.vertices = 9 * 4;

        this.band = new PGraphics[bandCount];
        this.bandMask = new PGraphics[bandCount];
        this.rotate = new float[bandCount];
        this.rot = new float[bandCount];
        this.multiplier = new float[bandCount];
        this.colorShift = new int[bandCount];
        this.shiftCounter = new int[bandCount];
        this.shiftDir = new int[bandCount];

        this.moveBandCounter = 0;
        this.mbX = new float[bandCount];
        this.mbY = new float[bandCount];
        this.currentDist = this.currentRotation = 0;

        for (int i = 0; i < bandCount; i++) {
            this.band[i] = mvp.createGraphics(width, height, mvp.P3D);
            this.bandMask[i] = mvp.createGraphics(width, height, mvp.P3D);
            rotate[i] = 0;
            rot[i] = mvp.random((float) -0.04, (float) 0.04);
            this.shiftCounter[i] = this.colorShift[i] = 0;
            this.shiftDir[i] = 1;

            if (i == 0) {
                multiplier[i] = 100000;
            } else {
                multiplier[i] = mvp.map(bandCount - i, 1, bandCount, 30000, 60000);
            }

        }

        rot[0] = (float) 0.02;

        this.panOutDist = 0;

    }

    public void render() {

        this.smoothedBands = mvp.calculateSmoothedBufferInSections((vertices / 4) * bandCount);

        if (cdPhase == 0) {
            // roll in phase
            updateCD();
            return;
        }

        // after rolling phase
        for (int i = bandCount - 1; i >= 0; i--) {
            updateMask(i);
            updateBand(i);
        }

        mvp.blendMode(mvp.BLEND);
        updateCD();
    }

    public void createMask(int j) {

        float x1, x2, y1, y2;
        int start, end, curr;
        float step;
        float diameterEnd, diameterStart;

        diameterStart = 10;
        diameterEnd = (float) height * (float) 1.5;
        if (j == 0) {
            diameterEnd = width;

        }

        x1 = (centerY) * (-mvp.cos(rotate[j])) + centerX;
        y1 = (centerY) * (mvp.sin(rotate[j])) + centerY;

        x2 = width / 2;
        y2 = height / 2;

        step = 10;
        float h = mvp.map(j, 0, bandCount - 1, 0, 240);

        shiftCounter[j] += shiftDir[j] * j;

        if ((h + shiftCounter[j]) > 230) {
            shiftDir[j] = -1;
        } else if ((h + shiftCounter[j]) < 5) {
            shiftDir[j] = 1;
        }
        h += shiftCounter[j];

        float transparency = 255;
        float brightness = 150; // 150 for ADD mode

        if (j > 0) {
            start = curr = mvp.color(255 - h, 255, brightness, transparency);
            end = mvp.color(255 - (h + 255 / 3), 240, brightness, transparency);
        } else {
            start = curr = mvp.color(40, 255, 255, 255);
            end = mvp.color(20, 240, 255, 255);
        }

        bandMask[j].beginDraw();
        bandMask[j].noStroke();
        bandMask[j].noFill();

        for (int i = 0; i < (diameterEnd / step); i++) {
            float x = mvp.map(i, 0, (diameterEnd / step), x2, x1);
            float y = mvp.map(i, 0, (diameterEnd / step), y2, y1);
            float diameter = mvp.map(i, 0, (diameterEnd / step), diameterEnd, diameterStart);

            curr = mvp.lerpColor(start, end, mvp.map(i, 0, (diameterEnd / step), 0, 1), mvp.HSB);

            bandMask[j].fill(curr);
            bandMask[j].circle(x, y, diameter);
        }
        bandMask[j].endDraw();

        return;
    }

    public void updateMask(int i) {

        bandMask[i].rotate(rotate[i]);
        return;
    }

    public void updateBand(int i) {
        float x, y, r;
        int realVertices = 9;
        float[] barLength = new float[realVertices];
        r = CDSize / 2;
        int k = 0;

        for (int j = realVertices * i; j < realVertices * (1 + i); j++) {
            barLength[k] = smoothedBands[j] * multiplier[i];
            k++;
        }

        band[i].beginDraw();

        band[i].pushMatrix();
        band[i].translate(width / 2, height / 2);

        mvp.blendMode(mvp.ADD);

        if (i > 0) {
            // blob shape
            band[i].noStroke();
            r *= mvp.map(i, 1, bandCount, 1, (float) 2.5);

        } else {
            // line strips
            band[i].rotate(mvp.QUARTER_PI / 4);
            band[i].translate(width / 8, 0);
            band[i].strokeWeight(5);
            band[i].stroke(150, 255, 255);
            band[i].noFill();
        }

        band[i].background(0);

        band[i].beginShape();

        int q = 0;

        for (int j = 0; j <= vertices + 5; j++) {
            float angle = mvp.map(j % (vertices), 0, vertices, 0, mvp.TWO_PI) + rotate[i];

            if (j % 4 == 1) {
                x = -mvp.cos(angle) * (barLength[q % realVertices] + r);
                y = mvp.sin(angle) * (barLength[q % realVertices] + r);
                q++;
            } else if (j % 4 == 0 || j % 4 == 2) {
                continue;

            } else {
                x = -mvp.cos(angle) * (barLength[q % realVertices] / 2 + r);
                y = mvp.sin(angle) * (barLength[q % realVertices] / 2 + r);
            }

            if (i == 0) {
                x *= 3;
                y *= 1.5;
            }
            band[i].curveVertex(x, y);
        }

        band[i].endShape();
        band[i].popMatrix();
        band[i].endDraw();

        createMask(i);

        rotate[i] = (rotate[i] + rot[i]) % mvp.TWO_PI;

        bandMask[i].mask(band[i]);
        mvp.image(bandMask[i], width / 2 + mbX[i], height / 2 + mbY[i], width, height);

        return;
    }

    public void createCD() {
        CD.beginDraw();
        CD.noStroke();
        CD.circle(CDSize / 2, CDSize / 2, height / 3);
        CD.endDraw();
        return;
    }

    public void updateCD() {
        // if CD is in middle
        if (CDpositionX >= centerX) {
            cdPhase++;
            CDpositionX = centerX;
            rotation = (rotation + r2) % mvp.TWO_PI; // new rotate CD
        } else {
            rotation = (rotation + r1) % mvp.TWO_PI; // new rotate CD
        }

        CDCover.mask(CD); // mask the album cover image with the shape CD (circle)

        mvp.pushMatrix();
        mvp.translate(CDpositionX, CDpositionY + panOutDist); // position CD
        mvp.rotate(rotation); // rotate CD
        mvp.fill(0, 255, 0);
        mvp.circle(0, 0, CDSize);
        mvp.image(CDCover, 0, 0); // load CD
        mvp.fill(0, 255, 0);
        mvp.noStroke();
        mvp.circle(0, 0, 50); // CD hole
        mvp.popMatrix();

        CDpositionX += 5; // move CD to the right
        return;
    }

    public void panOut() {
        // move CD out of frame
        panOutDist += 6;
        return;
    }

    public void moveBand() {
        moveBandCounter++;

        float bands = bandCount - 1;
        float angle = mvp.TWO_PI / bands;
        float dist = width / 4;
        float distChange = 5;
        float rotationSpeed = 0.02f;

        if (60 * 5 > moveBandCounter) {
            return;
        }

        for (int i = 0; i < bands; i++) {
            float newAngle = (angle * i + currentRotation) % mvp.TWO_PI;
            mbX[i + 1] = currentDist * mvp.sin(newAngle);
            mbY[i + 1] = currentDist * mvp.cos(newAngle);

        }

        currentRotation += rotationSpeed;

        if (currentDist < dist) {
            currentDist += distChange;
        }

        currentDist += panOutDist;
        mbY[0] += panOutDist;

    }
}
