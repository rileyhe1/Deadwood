/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cs345.deadwood;

import cs345.deadwood.controller.GameController;
import cs345.deadwood.model.*;
import cs345.deadwood.view.*;

public class Deadwood
{
    private static GameLog log = GameLog.getInstance();

    public static void main(String[] args)
    {
        /* Get number of players from command line arg
         * Usage: $ ./graldew run --args="2"
         */
        int numberOfPlayers = 3;  // default number of players
        if (args.length > 0) {
            try {
                numberOfPlayers = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Usage: ./gradlew run --args \"NUM\", where NUM should be an integer between 2 and 8.");
                return;
            }
            if (numberOfPlayers > 8 || numberOfPlayers < 2) {
                System.out.println("Usage: ./gradlew run --args \"NUM\", where NUM should be an integer between 2 and 8.");
                return;
            }
        }
        SetParser setParser = new SetParser();
        CardParser cardParser = new CardParser();
        GameEngine model = new GameEngine(numberOfPlayers, setParser.getSets(), cardParser.getCards());
        GameController controller = new GameController(model);
        BoardView view = new BoardView(model, controller);

        view.init();
        controller.distributeCards();
        log.log("Cards distributed using a random strategy.\n");
    }
}