package visuals;

import ie.tudublin.*;

public class MusicVisualiserProject extends Visual {
    // Object instances
    Verse1 v1;
    Verse2 v2;
    Prechorus pc;
    Chorus c;
    Bridge b;

    // Rendering
    float x, y, z;

    public void settings() {
        fullScreen(P3D);
        // size(1000, 1000, P3D);
        // x = width / 2;
        // y = height / 2;
        // z = 0;
    }

    public void setup() {
        startMinim();
        loadAudio("KetchupSongES.mp3");

        // Create objects
        v1 = new Verse1(this);
        // v2 = new Verse2(this);
        // pc = new Prechorus(this);
        c = new Chorus(this);
        // b = new Bridge(this);
    }

    public void keyPressed() {
        if (key == ' ') {
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        }
    }

    public void draw() {
        background(0);
        try {
            // Call this if you want to use FFT data
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }
        // Call this is you want to use frequency bands
        calculateFrequencyBands();

        // Call this is you want to get the average amplitude
        calculateAverageAmplitude();

        c.render();

    }
}