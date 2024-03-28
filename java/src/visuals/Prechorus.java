package visuals;

import processing.core.*;

public class Prechorus {

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    // Constructor that accepts a MusicVisualiserProject object
    public Prechorus(MusicVisualiserProject mvProject) {
        this.mvp = mvProject;
    }

    // Method to render the Chorus visual
    public void render() {
        // Text saying Chorus centred on screen
        mvp.textSize(32);
        mvp.textAlign(PConstants.CENTER, PConstants.CENTER);
        mvp.fill(255);
        mvp.text("Prechorus", mvp.width / 2, mvp.height / 2);
    }
}