package ie.tudublin;

import visuals.MusicVisualiserProject;

public class Main {

    public void startUI() {
        String[] a = { "MAIN" };
        processing.core.PApplet.runSketch(a, new MusicVisualiserProject());
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.startUI();
    }
}