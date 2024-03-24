package ie.tudublin;

import example.AudioBandsVisual;
import example.WaveForm;

public class MusicVisualiserProject extends Visual {

    public void settings() {
        size(1024, 500);
    }

    public void keyPressed() {
        if (key == ' ') {
        }
    }

    public void draw() {
        background(0);

        Verse1 v1 = new Verse1();
        v1.draw();
    }
}
