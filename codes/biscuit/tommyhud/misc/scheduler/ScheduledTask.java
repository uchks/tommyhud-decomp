package codes.biscuit.tommyhud.misc.scheduler;

import codes.biscuit.tommyhud.*;

public class ScheduledTask
{
    private static volatile int currentId;
    private static final Object anchor;
    private final long addedTime;
    private long addedTicks;
    private final int id;
    private int delay;
    private final int period;
    private final boolean async;
    private boolean running;
    private boolean canceled;
    private boolean repeating;
    private Runnable task;
    
    public ScheduledTask(final int delay, final int period, final boolean async) {
        this.addedTime = System.currentTimeMillis();
        this.addedTicks = TommyHUD.getInstance().getScheduler().getTotalTicks();
        synchronized (ScheduledTask.anchor) {
            this.id = ScheduledTask.currentId++;
        }
        this.delay = delay;
        this.period = period;
        this.async = async;
        this.repeating = (this.period > 0);
    }
    
    public ScheduledTask(final SkyblockRunnable task, final int delay, final int period, final boolean async) {
        this.addedTime = System.currentTimeMillis();
        this.addedTicks = TommyHUD.getInstance().getScheduler().getTotalTicks();
        synchronized (ScheduledTask.anchor) {
            this.id = ScheduledTask.currentId++;
        }
        this.delay = delay;
        this.period = period;
        this.async = async;
        this.repeating = (this.period > 0);
        task.setThisTask(this);
        this.task = (() -> {
            this.running = true;
            task.run();
            this.running = false;
        });
    }
    
    public final void cancel() {
        this.repeating = false;
        this.running = false;
        this.canceled = true;
    }
    
    public final long getAddedTime() {
        return this.addedTime;
    }
    
    public final long getAddedTicks() {
        return this.addedTicks;
    }
    
    public final int getId() {
        return this.id;
    }
    
    public final int getDelay() {
        return this.delay;
    }
    
    public final int getPeriod() {
        return this.period;
    }
    
    public boolean isAsync() {
        return this.async;
    }
    
    public boolean isCanceled() {
        return this.canceled;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    void setDelay(final int delay) {
        this.addedTicks = TommyHUD.getInstance().getScheduler().getTotalTicks();
        this.delay = delay;
    }
    
    public void start() {
        if (this.isAsync()) {
            new Thread(this.task).start();
        }
        else {
            this.task.run();
        }
    }
    
    public void setTask(final SkyblockRunnable task) {
        this.task = (() -> {
            this.running = true;
            task.run();
            this.running = false;
        });
    }
    
    public boolean isRepeating() {
        return this.repeating;
    }
    
    static {
        ScheduledTask.currentId = 1;
        anchor = new Object();
    }
}
