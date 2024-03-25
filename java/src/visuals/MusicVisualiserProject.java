package visuals;

import ie.tudublin.*;
import ddf.minim.AudioBuffer;
// import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class MusicVisualiserProject extends Visual {
    // Minim and Audio
    Minim minim;
    AudioPlayer ap;
    AudioBuffer ab;
    // AudioInput ai;

    // Object instances
    Verse1 v1;
    Verse2 v2;
    Prechorus pc;
    Chorus c;
    Bridge b;

    // Rendering
    float x, y, z;

    // Audio analysis
    float[] lerpedBuffer;
    float smoothedAmplitude = 0;

    public void settings() {
        fullScreen(P3D);
        // size(1000, 1000, P3D);
        // x = width / 2;
        // y = height / 2;
        // z = 0;

        // Use this to make fullscreen
        // fullScreen();
    }

    public void setup() {
        minim = new Minim(this);
        ap = minim.loadFile("KetchupSong.mp3", 1024);
        ap.play();
        ab = ap.mix;
        colorMode(HSB);
        lerpedBuffer = new float[width];

        // Create objects
        v1 = new Verse1(this);
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

        v1.render();

    }
}