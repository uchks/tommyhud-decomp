package codes.biscuit.tommyhud.misc.scheduler;

import codes.biscuit.tommyhud.misc.scheduler.ScheduledTask;
import codes.biscuit.tommyhud.misc.scheduler.SkyblockRunnable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class Scheduler {
    private final List<ScheduledTask> queuedTasks = new ArrayList();
    private final List<ScheduledTask> pendingTasks = new ArrayList();
    private final Object anchor = new Object();
    private volatile long currentTicks = 0L;
    private volatile long totalTicks = 0L;

    public synchronized long getCurrentTicks() {
        return this.currentTicks;
    }

    public synchronized long getTotalTicks() {
        return this.totalTicks;
    }

    @SubscribeEvent
    public void ticker(ClientTickEvent event) {
        if (event.phase == Phase.START) {
            synchronized(this.anchor) {
                ++this.totalTicks;
                ++this.currentTicks;
            }

            if (Minecraft.getMinecraft() != null) {
                this.pendingTasks.removeIf(ScheduledTask::isCanceled);
                this.pendingTasks.addAll(this.queuedTasks);
                this.queuedTasks.clear();

                try {
                    for(ScheduledTask scheduledTask : this.pendingTasks) {
                        if (this.getTotalTicks() >= scheduledTask.getAddedTicks() + (long)scheduledTask.getDelay()) {
                            scheduledTask.start();
                            if (scheduledTask.isRepeating()) {
                                if (!scheduledTask.isCanceled()) {
                                    scheduledTask.setDelay(scheduledTask.getPeriod());
                                }
                            } else {
                                scheduledTask.cancel();
                            }
                        }
                    }
                } catch (Throwable var5) {
                    var5.printStackTrace();
                }
            }
        }

    }

    public synchronized void cancel(int id) {
        this.pendingTasks.forEach((scheduledTask) -> {
            if (scheduledTask.getId() == id) {
                scheduledTask.cancel();
            }

        });
    }

    public void cancel(ScheduledTask task) {
        task.cancel();
    }

    public ScheduledTask repeat(SkyblockRunnable task) {
        return this.scheduleRepeatingTask(task, 0, 1);
    }

    public ScheduledTask repeatAsync(SkyblockRunnable task) {
        return this.runAsync(task, 0, 1);
    }

    public ScheduledTask runAsync(SkyblockRunnable task) {
        return this.runAsync(task, 0);
    }

    public ScheduledTask runAsync(SkyblockRunnable task, int delay) {
        return this.runAsync(task, delay, 0);
    }

    public ScheduledTask runAsync(SkyblockRunnable task, int delay, int period) {
        ScheduledTask scheduledTask = new ScheduledTask(task, delay, period, true);
        this.pendingTasks.add(scheduledTask);
        return scheduledTask;
    }

    public ScheduledTask scheduleTask(SkyblockRunnable task) {
        return this.scheduleDelayedTask(task, 0);
    }

    public ScheduledTask scheduleDelayedTask(SkyblockRunnable task, int delay) {
        return this.scheduleRepeatingTask(task, delay, 0);
    }

    public ScheduledTask scheduleRepeatingTask(SkyblockRunnable task, int delay, int period) {
        return this.scheduleRepeatingTask(task, delay, period, false);
    }

    public ScheduledTask scheduleRepeatingTask(SkyblockRunnable task, int delay, int period, boolean queued) {
        ScheduledTask scheduledTask = new ScheduledTask(task, delay, period, false);
        if (queued) {
            this.queuedTasks.add(scheduledTask);
        } else {
            this.pendingTasks.add(scheduledTask);
        }

        return scheduledTask;
    }

    public void schedule(ScheduledTask scheduledTask) {
        this.pendingTasks.add(scheduledTask);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var3) {
        }

    }
}
 