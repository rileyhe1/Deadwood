package cs345.deadwood.model;

import cs345.deadwood.view.BlankAreaView;

public class BlankArea
{
    private Player playerOnSpace;
    private IArea area;
    private BlankAreaView observer;

    public void notifyObserver()
    {
        observer.blankUpdated(playerOnSpace);
    }

    public void initializePlayerOnSpace(Player p)
    {
        this.playerOnSpace = p;
        playerOnSpace.setBlankArea(this);
    }

    public void setObserver(BlankAreaView ob)
    {
        this.observer = ob;
    }

    public Player getPlayerOnSpace()
    {
        return playerOnSpace;
    }

    public void setPlayerOnSpace(Player playerOnSpace)
    {
        this.playerOnSpace = playerOnSpace;
        notifyObserver();
    }

    public IArea getArea()
    {
        return area;
    }

    public void setArea(IArea area)
    {
        this.area = area;
    }
}
