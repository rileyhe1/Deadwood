package cs345.deadwood.view;

public class GameLog
{
    private static GameLog uniqueInstance;
    private BoardView observer;


    private GameLog() { }
    public static GameLog getInstance()
    {
        if(uniqueInstance == null)
        {
            uniqueInstance = new GameLog();
        }
        return uniqueInstance;
    }
    public void registerObserver(BoardView boardView)
    {
        this.observer = boardView;
    }
    private void notifyObservers(String message)
    {
        observer.subjectUpdated(message);
    }

    public void log(String message)
    {
        System.out.println(message);
        notifyObservers(message);
    }
}
