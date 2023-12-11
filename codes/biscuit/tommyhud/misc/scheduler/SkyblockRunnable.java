package codes.biscuit.tommyhud.misc.scheduler;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.misc.scheduler.ScheduledTask;
import java.util.Map;

public abstract class SkyblockRunnable implements Runnable {
    private ScheduledTask thisTask;

    public void cancel() {
        TommyHUD.getInstance().getScheduler().cancel(this.thisTask);
    }

    public void setThisTask(ScheduledTask thisTask) {
        this.thisTask = thisTask;
    }
}
 