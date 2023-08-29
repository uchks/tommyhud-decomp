package codes.biscuit.tommyhud.config;

public enum AnchorPoint
{
    TOP_LEFT, 
    TOP_RIGHT, 
    BOTTOM_LEFT, 
    BOTTOM_RIGHT, 
    BOTTOM_MIDDLE;
    
    public int getX(final int maxX) {
        int x = 0;
        switch (this) {
            case TOP_RIGHT:
            case BOTTOM_RIGHT: {
                x = maxX;
                break;
            }
            case BOTTOM_MIDDLE: {
                x = maxX / 2;
                break;
            }
        }
        return x;
    }
    
    public int getY(final int maxY) {
        int y = 0;
        switch (this) {
            case BOTTOM_RIGHT:
            case BOTTOM_MIDDLE:
            case BOTTOM_LEFT: {
                y = maxY;
                break;
            }
        }
        return y;
    }
}
