import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

final class AsynchronousBlankClipLooper implements Runnable {
    public void run() {
        System.out.println("Started white noise");
        //Constantly loop inaudible white noise to prevent buggy audio playing on Linux.
        try (Clip clip = AudioSystem.getClip(); InputStream is = AsynchronousBlankClipLooper.class.getResourceAsStream("SILENCE.wav")) {
            assert is != null;
            System.out.println("Looping white noise...");
            clip.open(AudioSystem.getAudioInputStream(is));
            clip.loop(-1);
            System.out.println("White noise is looping");
            clip.drain();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}