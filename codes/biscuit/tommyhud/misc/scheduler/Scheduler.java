package codes.biscuit.tommyhud.misc.scheduler;

import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Scheduler
{
    private final List<ScheduledTask> queuedTasks;
    private final List<ScheduledTask> pendingTasks;
    private final Object anchor;
    private volatile long currentTicks;
    private volatile long totalTicks;
    
    public Scheduler() {
        this.queuedTasks = new ArrayList<ScheduledTask>();
        this.pendingTasks = new ArrayList<ScheduledTask>();
        this.anchor = new Object();
        this.currentTicks = 0L;
        this.totalTicks = 0L;
    }
    
    public synchronized long getCurrentTicks() {
        return this.currentTicks;
    }
    
    public synchronized long getTotalTicks() {
        return this.totalTicks;
    }
    
    @SubscribeEvent
    public void ticker(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            synchronized (this.anchor) {
                ++this.totalTicks;
                ++this.currentTicks;
            }
            if (Minecraft.func_71410_x() != null) {
                this.pendingTasks.removeIf(ScheduledTask::isCanceled);
                this.pendingTasks.addAll(this.queuedTasks);
                this.queuedTasks.clear();
                try {
                    for (final ScheduledTask scheduledTask : this.pendingTasks) {
                        if (this.getTotalTicks() >= scheduledTask.getAddedTicks() + scheduledTask.getDelay()) {
                            scheduledTask.start();
                            if (scheduledTask.isRepeating()) {
                                if (scheduledTask.isCanceled()) {
                                    continue;
                                }
                                scheduledTask.setDelay(scheduledTask.getPeriod());
                            }
                            else {
                                scheduledTask.cancel();
                            }
                        }
                    }
                }
                catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public synchronized void cancel(final int id) {
        this.pendingTasks.forEach(scheduledTask -> {
            if (scheduledTask.getId() == id) {
                scheduledTask.cancel();
            }
        });
    }
    
    public void cancel(final ScheduledTask task) {
        task.cancel();
    }
    
    public ScheduledTask repeat(final SkyblockRunnable task) {
        return this.scheduleRepeatingTask(task, 0, 1);
    }
    
    public ScheduledTask repeatAsync(final SkyblockRunnable task) {
        return this.runAsync(task, 0, 1);
    }
    
    public ScheduledTask runAsync(final SkyblockRunnable task) {
        return this.runAsync(task, 0);
    }
    
    public ScheduledTask runAsync(final SkyblockRunnable task, final int delay) {
        return this.runAsync(task, delay, 0);
    }
    
    public ScheduledTask runAsync(final SkyblockRunnable task, final int delay, final int period) {
        final ScheduledTask scheduledTask = new ScheduledTask(task, delay, period, true);
        this.pendingTasks.add(scheduledTask);
        return scheduledTask;
    }
    
    public ScheduledTask scheduleTask(final SkyblockRunnable task) {
        return this.scheduleDelayedTask(task, 0);
    }
    
    public ScheduledTask scheduleDelayedTask(final SkyblockRunnable task, final int delay) {
        return this.scheduleRepeatingTask(task, delay, 0);
    }
    
    public ScheduledTask scheduleRepeatingTask(final SkyblockRunnable task, final int delay, final int period) {
        return this.scheduleRepeatingTask(task, delay, period, false);
    }
    
    public ScheduledTask scheduleRepeatingTask(final SkyblockRunnable task, final int delay, final int period, final boolean queued) {
        final ScheduledTask scheduledTask = new ScheduledTask(task, delay, period, false);
        if (queued) {
            this.queuedTasks.add(scheduledTask);
        }
        else {
            this.pendingTasks.add(scheduledTask);
        }
        return scheduledTask;
    }
    
    public void schedule(final ScheduledTask scheduledTask) {
        this.pendingTasks.add(scheduledTask);
    }
    
    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {}
    }
}
