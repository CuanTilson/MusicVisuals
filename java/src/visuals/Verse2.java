package visuals;

import java.util.ArrayList;

import processing.core.*;

public class Verse2 {
    MusicVisualiserProject mvp;
    PGraphics CD;
    PGraphics[] band;
    PGraphics[] bandMask;
    int bandCount;

    int width;
    int height;
    int centerX;
    int centerY;

    int cdPhase;

    public Verse2(MusicVisualiserProject mvProject) {
        this.mvp = mvProject;
        this.width = mvp.width;
        this.centerX = this.width / 2;
        this.height = mvp.height;
        this.centerY = this.height / 2;

        this.CD = new PGraphics();
        this.cdPhase = 0;
        createCD();

        this.bandCount = 6;
        this.band = new PGraphics[bandCount];
        this.bandMask = new PGraphics[bandCount];
        for (int i = 0; i < bandCount; i++) {
            this.band[i] = new PGraphics();
            this.bandMask[i] = new PGraphics();
            createMask(i);
        }
    }

    public void render() {

        for (int i = 0; i < bandCount; i++) {
            updateBand(i);
            updateMask(i);
        }

        updateCD();
    }

    public void createMask(int i) {

    }

    public void updateMask(int i) {

    }

    public void updateBand(int i) {

    }

    public void createCD() {
    }

    public void updateCD() {
        if (cdPhase == 0) {

        } else if (cdPhase == 1) {

        } else {

        }
    }
}