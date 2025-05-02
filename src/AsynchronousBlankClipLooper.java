import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

final class AsynchronousBlankClipLooper implements Runnable {
    public void run() {

        try (Clip clip = AudioSystem.getClip(); InputStream is = AsynchronousBlankClipLooper.class.getResourceAsStream("DEATH.wav")) {
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(is));
            clip.loop(-1);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            clip.close();
            throw new RuntimeException(e);
        }
    }
}