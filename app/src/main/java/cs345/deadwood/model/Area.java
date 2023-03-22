package cs345.deadwood.model;

public class Area implements IArea
{
    private int x;
    private int y;
    private int h;
    private int w;

    public Area() {
    }

    public Area(int x, int y, int h, int w) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    public void setX(int newX)
    {
        this.x = newX;
    }

    @Override
    public int getY()
    {
        return this.y;
    }

    public void setY(int newY)
    {
        this.y = newY;
    }

    @Override
    public int getH()
    {
        return this.h;
    }

    public void setH(int newH)
    {
        this.h = newH;
    }

    @Override
    public int getW()
    {
        return this.w;
    }

    public void setW(int newW)
    {
        this.w = newW;
    }
}
