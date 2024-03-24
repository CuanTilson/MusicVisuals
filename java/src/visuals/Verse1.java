package visuals;

import processing.core.*;

public class Verse1 {
    // Add your FirstVisual-specific code here
    MusicVisualiserProject mvp;

    public Verse1(MusicVisualiserProject mvp) {
        this.mvp = mvp;
    }

    public void render() {
        mvp.colorMode(PApplet.HSB); // Set color mode to HSB

        // Draw circles
        mvp.strokeWeight(2);
        mvp.stroke(255);
        mvp.noFill();
        mvp.rect(100, 100, 100, 100);
    }
}
