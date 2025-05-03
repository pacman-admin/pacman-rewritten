import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
final class AsynchronousSoundPlayer implements Runnable {
    private final Sound s;

    AsynchronousSoundPlayer(Sound which) {
        s = which;
    }

    public void run() {
        try (InputStream is = switch (s) {
            case Sound.DEATH -> AsynchronousSoundPlayer.class.getResourceAsStream("DEATH.wav");
            case Sound.EXTRA_LIFE -> AsynchronousSoundPlayer.class.getResourceAsStream("EXTRA_LIFE.wav");
            case Sound.PAUSE -> AsynchronousSoundPlayer.class.getResourceAsStream("PAUSE.wav");
            case Sound.FRUIT -> AsynchronousSoundPlayer.class.getResourceAsStream("FRUIT.wav");
        }; Clip clip = AudioSystem.getClip()) {
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(is));
            while (!clip.isOpen()) ;
            clip.start();
            clip.drain();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}