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
        this.band = new PGraphics[bandCount];
        this.bandMask = new PGraphics[bandCount];

        for (int i = 0; i < bandCount; i++) {
            this.band[i] = mvp.createGraphics(width, height);
            this.bandMask[i] = mvp.createGraphics(width, height);
            createMask(i);
        }

        this.panOutDist = 0;
    }

    public void render() {

        if (cdPhase == 0) {
            // roll in phase
            updateCD();
            return;
        }

        // after rolling phase
        for (int i = 0; i < bandCount; i++) {
            updateBand(i);
            updateMask(i);
        }

        updateCD();
    }

    public void createMask(int i) {
        return;
    }

    public void updateMask(int i) {
        return;
    }

    public void updateBand(int i) {
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