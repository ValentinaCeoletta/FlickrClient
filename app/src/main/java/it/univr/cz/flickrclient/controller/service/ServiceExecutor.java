package it.univr.cz.flickrclient.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.concurrent.ExecutorService;

/**
 * ServiceExecutor class: it creates and manages worker threads
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public abstract class ServiceExecutor extends Service {

    private final Handler EDT = new Handler(Looper.getMainLooper());
    private boolean mRedelivery;
    private ExecutorService exec;
    private int runningTasks;

    public ServiceExecutor(String name) {
    }

    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        exec = mkExecutorService();
    }

    protected abstract ExecutorService mkExecutorService();

    @Override
    @UiThread
    public void onStart(@Nullable Intent intent, int startId) {
        runningTasks++;

        exec.submit(() -> {
            onHandleIntent(intent);
            EDT.post(this::endOfTask);
        });

    }

    @UiThread
    private void endOfTask() {
        if (--runningTasks == 0)
            stopSelf();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        exec.shutdown();
    }

    @Override
    public
    @Nullable
    IBinder onBind(Intent intent) {
        return null;
    }

    @WorkerThread
    protected abstract void onHandleIntent(@Nullable Intent intent);

}
