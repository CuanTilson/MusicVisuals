package visuals;

import ie.tudublin.*;
import processing.core.PShape;

public class MusicVisualiserProject extends Visual {
    // Object instances
    Verse1 verse1;
    Verse2 verse2;
    Prechorus preChorus;
    Chorus chorus;
    Bridge bridge;

    float pausedPosition = 0;

    PShape tomato;

    // Rendering
    float x, y, z;

    // Define song section start times
    float[] songSectionStartTimes = {
            0, // Verse 1
            21, // Pre-Chorus 1
            37, // Chorus 1
            61, // Verse 2
            80, // Pre-Chorus 2 (Assuming 21-36 was a typo and should be 80-94)
            95, // Chorus 2
            120, // Bridge
            140 // Chorus 3 (Adjust these values as needed)
    };

    public void settings() {
        fullScreen(P3D);
        // size(1000, 1000, P3D);
        // x = width / 2;
        // y = height / 2;
        // z = 0;
    }

    public void setup() {
        colorMode(HSB);
        frameRate(60);

        startMinim();
        loadAudio("KetchupSongES.mp3");

        tomato = loadShape("tomato.obj");

        // Create objects
        verse1 = new Verse1(this);
        verse2 = new Verse2(this);
        preChorus = new Prechorus(this);
        chorus = new Chorus(this);
        bridge = new Bridge(this);
    }

    public void keyPressed() {
        if (key == ' ') {
            togglePlay();
        }
        if (key == 'r') {
            restartSong();
        }
    }

    private void togglePlay() {
        if (!getAudioPlayer().isPlaying()) {
            if (pausedPosition == 0) {
                getAudioPlayer().cue(0);
                getAudioPlayer().play();
            } else {
                getAudioPlayer().play((int) pausedPosition);
            }
        } else {
            getAudioPlayer().pause();
            pausedPosition = getAudioPlayer().position();
        }
    }

    private void restartSong() {
        getAudioPlayer().cue(0);
        getAudioPlayer().play();
    }

    int getCurrentSongSection(float songPosition) {
        for (int i = 0; i < songSectionStartTimes.length - 1; i++) {
            if (songPosition >= songSectionStartTimes[i] && songPosition < songSectionStartTimes[i + 1]) {
                return i;
            }
        }
        // Check if the current time is after the last section start time
        if (songPosition >= songSectionStartTimes[songSectionStartTimes.length - 1]) {
            return songSectionStartTimes.length - 1;
        }
        return -1; // If not in any known section
    }

    public void draw() {
        float currentTime = getAudioPlayer().position() / 1000.0f; // Convert milliseconds to seconds

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

        int currentSection = getCurrentSongSection(currentTime);
        // int currentSection = 2;

        switch (currentSection) {
            case 0: // Verse 1
                background(0);
                verse1.render(width, height);
                break;
            case 1: // Pre-Chorus 1
                background(0);
                preChorus.render();
                break;
            case 2: // Chorus 1
                background(0);
                chorus.render();
                break;
            case 3: // Verse 2
                background(0);
                verse2.render();
                break;
            case 4: // Pre-Chorus 2
                background(0);
                preChorus.render();
                break;
            case 5: // Chorus 2
                background(0);
                chorus.render();
                break;
            case 6: // Bridge
                background(0);
                bridge.render();
                break;
            case 7: // Chorus 3 (or any additional sections)
                background(0);
                chorus.render();
                break;
        }
    }
}