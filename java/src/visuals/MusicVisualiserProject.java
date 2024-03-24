package visuals;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ie.tudublin.Visual;
import visuals.AudioBandsVisual;
import visuals.WaveForm;

public class MusicVisualiserProject extends Visual {
    Minim minim;
    AudioPlayer ap;
    AudioInput ai;
    AudioBuffer ab;
    float x, y, z;

    int mode = 0;

    float[] lerpedBuffer;
    float smoothedAmplitude = 0;

    public void settings() {
        size(1000, 1000, P3D);
        x = width / 2;
        y = height / 2;
        z = 0;
    }

    public void setup() {
        minim = new Minim(this);
        ap = minim.loadFile("KetchupSong.mp3", 1024);
        ap.play();
        ab = ap.mix;
        colorMode(HSB);
        lerpedBuffer = new float[width];
    }

    public void keyPressed() {
        if (key == ' ') {
        }
    }

    public void draw() {
        background(0);

    }
}