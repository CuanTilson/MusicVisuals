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

        this.bandCount = 6;
        this.vertices = 9 * 4;

        this.band = new PGraphics[bandCount];
        this.bandMask = new PGraphics[bandCount];
        this.rotate = new float[bandCount];
        this.rot = new float[bandCount];

        for (int i = 0; i < bandCount; i++) {
            this.band[i] = mvp.createGraphics(width, height, mvp.P3D);
            this.bandMask[i] = mvp.createGraphics(width, height, mvp.P3D);
            createMask(i);
            rotate[i] = 0;
            rot[i] = mvp.random((float) -0.04, (float) 0.04);
        }

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
        mvp.blendMode(mvp.ADD);
        for (int i = 0; i < bandCount; i++) {
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
        diameterEnd = height * 2;

        x1 = mvp.random(-diameterEnd / 2, diameterEnd / 2) + width / 2;
        y1 = mvp.random(-diameterEnd / 2, diameterEnd / 2) + height / 2;

        x2 = width / 2;
        y2 = height / 2;

        step = 5;

        float transparency = 255;

        int colorStep = 50;

        start = curr = mvp.color(j * colorStep, 255, 255, transparency);
        end = mvp.color(colorStep + j * colorStep, 255, 255, transparency);

        bandMask[j].beginDraw();
        // band[j].background(255);
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
        return;
    }

    public void updateBand(int i) {
        float x, y, r;
        int realVertices = 9;
        float[] barLength = new float[realVertices];
        r = CDSize / 2;
        int k = 0;

        for (int j = realVertices * i; j < realVertices * (1 + i); j++) {
            barLength[k] = smoothedBands[j] * 10000;
            k++;
        }

        band[i].beginDraw();

        band[i].pushMatrix();
        band[i].translate(width / 2, height / 2);

        if (i > 1) {
            // blob shape
            band[i].noStroke();
        } else {
            // line
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

            band[i].curveVertex(x, y);
        }

        band[i].endShape();
        band[i].popMatrix();
        band[i].endDraw();

        rotate[i] = (rotate[i] + rot[i]) % mvp.TWO_PI;

        bandMask[i].mask(band[i]);
        mvp.image(bandMask[i], width / 2, height / 2, width, height);

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
        panOutDist += 3;
        return;
    }
}