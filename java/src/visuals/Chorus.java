package visuals;

import processing.core.*;

public class Chorus {

    // Reference to the main MusicVisualiserProject object
    MusicVisualiserProject mvp;

    // Constructor that accepts a MusicVisualiserProject object
    public Chorus(MusicVisualiserProject mvProject) {
        this.mvp = mvProject;
    }

    // Method to render the Chorus visual
    public void render() {
        float gap = mvp.width / (float) mvp.getBands().length;
        mvp.noStroke();
        for (int i = 0; i < mvp.getBands().length; i++) {
            mvp.fill(PApplet.map(i, 0, mvp.getBands().length, 255, 0), 255, 255);
            mvp.rect(i * gap, mvp.height, gap, -mvp.getSmoothedBands()[i] * 10f);
        }
    }
}
