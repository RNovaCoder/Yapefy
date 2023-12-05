package com.novacoder.looptransaction.servicios;

import android.os.Handler;
import android.os.Looper;

public class TaskDiffer {

    @FunctionalInterface
    public interface TaskRun<T> {
        T run(boolean flag);
    }
    @FunctionalInterface
    public interface TaskUiRun<T> {
        void run (boolean flag, T params);
    }

    private Thread ThreadMain = new Thread();
    private Thread ThreadTask = new Thread();
    private Handler hiloUI = new Handler(Looper.getMainLooper());
    private TaskRun[] Task = {flag -> {
        return null;
    }};
    private TaskUiRun[] TaskUi = {(flag, param) -> {}};
    private boolean flagTask[] = {false};

    public <T> void setTask (TaskRun<T> task) {
        Task[0] = task;
        ThreadTask = new Thread(()->{
            T params = (T) Task[0].run(flagTask[0]);
            hiloUI.post(()->{
                TaskUi[0].run(flagTask[0], params);
            });
        });
    }

    public <T> void setTaskUi (TaskUiRun<T> task) {
        TaskUi[0] = task;
    }

    public TaskDiffer () {
        ThreadMain = new Thread(()->{
            try {ThreadTask.join();}
            catch (InterruptedException e) {}
            hiloUI.removeCallbacksAndMessages(null);
            ThreadTask.run();
        });
    }

    public void TaskRun () {
        flagTask[0] = false;
        ThreadMain.start();
        flagTask[0] = true;
    }
}
