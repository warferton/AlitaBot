
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RunnableTest {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    static int i = 0;

    public static void runEverySec() {
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                System.out.println("A second passed.");
                i += 1;
            }
        };
        ScheduledFuture updaterHandlerAtFixedRate =
                scheduler.scheduleAtFixedRate(updater, 0, 1, TimeUnit.SECONDS);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                updaterHandlerAtFixedRate.cancel(true);
                System.exit(0);
            }
        }, 4, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        runEverySec();
        assert i == 5;
    }
}