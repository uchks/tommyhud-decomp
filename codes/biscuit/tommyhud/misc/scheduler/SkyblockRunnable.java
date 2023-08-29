package codes.biscuit.tommyhud.misc.scheduler;

import codes.biscuit.tommyhud.*;

public abstract class SkyblockRunnable implements Runnable
{
    private ScheduledTask thisTask;
    
    public void cancel() {
        TommyHUD.getInstance().getScheduler().cancel(this.thisTask);
    }
    
    public void setThisTask(final ScheduledTask thisTask) {
        this.thisTask = thisTask;
    }
}
