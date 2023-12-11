package codes.biscuit.tommyhud.asm.utils;

import java.util.Map;

public class ReturnValue<R> {
    private boolean cancelled;
    private R returnValue;

    public void cancel() {
        this.cancel((R)null);
    }

    public void cancel(R returnValue) {
        this.cancelled = true;
        this.returnValue = returnValue;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public R getReturnValue() {
        return this.returnValue;
    }
}
