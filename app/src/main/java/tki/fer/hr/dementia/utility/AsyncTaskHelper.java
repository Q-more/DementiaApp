package tki.fer.hr.dementia.utility;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lucija on 08.04.18..
 */

public class AsyncTaskHelper {

    private final ExecutorService workers;

    private AsyncTaskHelper(ExecutorService workers) {
        this.workers = workers;
    }

    private static class InstanceHolder {
        private final static AsyncTaskHelper INSTANCE = new AsyncTaskHelper(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    public static AsyncTaskHelper instance() {
        return InstanceHolder.INSTANCE;
    }

    public void runOnBackground(Runnable job) {
        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                job.run();
                return null;
            }
        };

        task.executeOnExecutor(workers);
    }
}
