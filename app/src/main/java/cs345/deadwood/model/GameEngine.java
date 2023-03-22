package cs345.deadwood.model;

import java.util.ArrayList;
import java.util.List;

public class GameEngine
{
    private final int numberOfPlayers;
    private final List<ISet> setList;
    private final List<ICard> cardList;
    private final List<Player> playerList = new ArrayList<>();

    public GameEngine(int numberOfPlayers, List<ISet> setList, List<ICard> cardList)
    {
        ISet trailer = new Set();
        for(ISet s: setList)
        {
            if(s.getName().equals("Trailer"))
            {
                trailer = s;
            }
        }
        this.numberOfPlayers = numberOfPlayers;
        this.setList = setList;
        this.cardList = cardList;
        for(int i = 0; i < numberOfPlayers; i++)
        {
            Player newPlayer = new Player(i+1, trailer, 0, 0, 0, 0, 2);
            for(ISet s: setList)
            {
               if(s.getName().equals("Trailer"))
               {
                    s.getBlankAreas().get(i).initializePlayerOnSpace(newPlayer);
               }
            }
            playerList.add(newPlayer);
        }
    }

    public List<Player> getPlayerList()
    {
        return playerList;
    }

    public List<ISet> getSetList()
    {
        return setList;
    }

    public List<ICard> getCardList()
    {
        return cardList;
    }
}
