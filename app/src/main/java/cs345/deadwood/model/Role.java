package cs345.deadwood.model;

import cs345.deadwood.view.RoleView;

public class Role implements IRole
{
    private String name;
    private int level;
    private String line;
    private Area area;
    private Player playerOnRole;
    private RoleView observer;
    public void setObserver(RoleView ob)
    {
        this.observer = ob;
    }
    private void notifyObserver(Player p)
    {
        observer.roleUpdated(p);
    }

    public Player getPlayerOnRole()
    {
        return playerOnRole;
    }

    public void setPlayerOnRole(Player playerOnRole)
    {
        this.playerOnRole = playerOnRole;
        notifyObserver(this.playerOnRole);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    @Override
    public int getLevel()
    {
        return this.level;
    }

    public void setLevel(int newLevel)
    {
        this.level = newLevel;
    }

    @Override
    public String getLine()
    {
        return this.line;
    }

    public void setLine(String newLine)
    {
        this.line = newLine;
    }

    @Override
    public IArea getArea()
    {
        return this.area;
    }

    public void setArea(Area newArea)
    {
        this.area = newArea;
    }
}
