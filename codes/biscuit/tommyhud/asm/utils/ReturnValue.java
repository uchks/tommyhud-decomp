package codes.biscuit.tommyhud.asm.utils;

public class ReturnValue<R>
{
    private boolean cancelled;
    private R returnValue;
    
    public void cancel() {
        this.cancel(null);
    }
    
    public void cancel(final R returnValue) {
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
