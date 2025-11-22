import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (c) 2025 Langdon Staab <pacman@langdonstaab.ca>
 * <p>
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 * @author Langdon Staab
 */
record AsynchronousSoundPlayer(Sound s) implements Runnable {

    public void run() {
        try (InputStream is = switch (s) {
            case Sound.DEATH -> AsynchronousSoundPlayer.class.getResourceAsStream("DEATH.wav");
            case Sound.EXTRA_LIFE -> AsynchronousSoundPlayer.class.getResourceAsStream("EXTRA_LIFE.wav");
            case Sound.PAUSE -> AsynchronousSoundPlayer.class.getResourceAsStream("PAUSE.wav");
            case Sound.FRUIT -> AsynchronousSoundPlayer.class.getResourceAsStream("FRUIT.wav");
        }) {
            assert is != null;
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(is)));
            while (!clip.isOpen()) ;
            is.close();
            clip.start();
            clip.drain();
            //clip.close();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}