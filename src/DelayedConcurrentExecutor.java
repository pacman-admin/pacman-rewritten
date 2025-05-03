import java.util.Timer;
import java.util.TimerTask;

abstract class DelayedConcurrentExecutor {
    DelayedConcurrentExecutor(String desc, int delay) {
        new Timer(desc).schedule(new TimerTask() {
            @Override
            public void run() {
                task();
            }
        }, delay);
    }

    abstract void task();
}
