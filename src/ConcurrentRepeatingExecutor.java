import java.util.Timer;
import java.util.TimerTask;

abstract class ConcurrentRepeatingExecutor {
    ConcurrentRepeatingExecutor(String desc, int delay, int period) {
        new Timer(desc).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                task();
            }
        }, delay, period);
    }

    abstract void task();
}