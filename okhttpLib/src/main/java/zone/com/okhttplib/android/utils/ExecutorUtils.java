package zone.com.retrofitlib.android.utils;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ExecutorUtils {

    public static final Executor SERIAL_EXECUTOR = AsyncTask.SERIAL_EXECUTOR;
    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;
    //线程池申请那么大 如果没有线程执行也不会有那么大资源；所以不怕；
    public static ScheduledExecutorService scheduled =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    /**
     * 并行
     *
     * @param command
     */
    public static void execute(Runnable command) {
        THREAD_POOL_EXECUTOR.execute(command);
    }

    /**
     * 串行
     *
     * @param command
     */
    public static void executeSerial(Runnable command) {
        SERIAL_EXECUTOR.execute(command);
    }

    public static  void schedule(Runnable command, long delay, TimeUnit unit) {
        scheduled.schedule(command, delay, unit);
    }

    public static void scheduleAtFixedRate(Runnable command,
                                    long initialDelay,
                                    long period,
                                    TimeUnit unit) {
        scheduled.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static void scheduleWithFixedDelay(Runnable command,
                                       long initialDelay,
                                       long delay,
                                       TimeUnit unit) {
        scheduled.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}