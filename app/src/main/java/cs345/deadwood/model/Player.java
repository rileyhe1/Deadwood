package cs345.deadwood.model;

import cs345.deadwood.view.PlayerView;

public class Player
{
    private int playerNum;
    private ISet currentSet;

    private String location;
    private int money;
    private int practiceChips;
    private int score;
    private int credits;
    private int rank = 1;
    private char dieColor = 'b';
    private static char currentDieColor = 'b';
    private PlayerView observer;
    private BlankArea blank;
    private Role currentRole;
    private boolean hasRole = false;
    private boolean roleCompleted = false;

    public IRole getCurrentRole()
    {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole)
    {
        if(this.currentRole != null && currentRole == null)
        {
            this.currentRole.setPlayerOnRole(null);
        }

        this.currentRole = currentRole;
        if(currentRole != null)
        {
            hasRole = true;
            roleCompleted = false;
            currentRole.setPlayerOnRole(this);
        }
        else
        {
            hasRole = false;
        }
    }

    public boolean isHasRole()
    {
        return hasRole;
    }

    public void setHasRole(boolean hasRole)
    {
        this.hasRole = hasRole;
    }

    public char getDieColor()
    {
        return dieColor;
    }

    public void setDieColor(char dieColor)
    {
        this.dieColor = dieColor;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
        notifyObserver();
    }

    public Player(int playerNum, ISet location, int money, int practiceChips, int score, int credits, int rank)
    {
        this.playerNum = playerNum;
        this.currentSet = location;
        this.location = location.getName();
        this.money = money;
        this.practiceChips = practiceChips;
        this.score = score;
        this.credits = credits;
        this.rank = rank;
        this.dieColor = currentDieColor;
        incrementCurrentDieColor();
    }

    private void incrementCurrentDieColor()
    {
        switch(currentDieColor)
        {
            case 'b':
                currentDieColor = 'c';
                break;
            case 'c':
                currentDieColor = 'g';
                break;
            case 'g':
                currentDieColor = 'o';
                break;
            case 'o':
                currentDieColor = 'p';
                break;
            case 'p':
                currentDieColor = 'r';
                break;
            case 'r':
                currentDieColor = 'v';
                break;
            case 'v':
                currentDieColor = 'w';
                break;
            case 'w':
                currentDieColor = 'y';
                break;
            case 'y':
                break;
            default:
                System.out.println("Error: invalid die color...");
        }
    }

    public void setObserver(PlayerView observer)
    {
        this.observer = observer;
    }
    public void notifyObserver()
    {
        observer.playerUpdated();
    }

    public void setPlayerNum(int playerNum)
    {
        this.playerNum = playerNum;
    }

    public void setLocation(String location)
    {
        this.location = location;
        notifyObserver();
    }

    public void setMoney(int money)
    {
        this.money = money;
        notifyObserver();
    }

    public void setPracticeChips(int practiceChips)
    {
        this.practiceChips = practiceChips;
        notifyObserver();
    }

    public void setScore(int score)
    {
        this.score = score;
        notifyObserver();
    }

    public void setCredits(int credits)
    {
        this.credits = credits;
        notifyObserver();
    }

    public int getPlayerNum()
    {
        return playerNum;
    }

    public String getLocation()
    {
        return location;
    }

    public int getMoney()
    {
        return money;
    }

    public int getPracticeChips()
    {
        return practiceChips;
    }

    public int getScore()
    {
        return score;
    }

    public int getCredits()
    {
        return credits;
    }

    public void setActivePlayer()
    {
        observer.setActivePlayer();
        notifyObserver();
    }
    public void setInactivePlayer()
    {
        observer.setInactivePlayer();
        notifyObserver();
    }

    public void setBlankArea(BlankArea blankArea)
    {
        this.blank = blankArea;
        if(this.observer != null)
        {
            notifyObserver();
        }
    }
    public BlankArea getBlankArea()
    {
        return this.blank;
    }
    public ISet getCurrentSet()
    {
        return currentSet;
    }

    public void setCurrentSet(ISet currentSet)
    {
        this.currentSet = currentSet;
    }

    public boolean isRoleCompleted()
    {
        return roleCompleted;
    }

    public void setRoleCompleted(boolean roleCompleted)
    {
        this.roleCompleted = roleCompleted;
    }
}
