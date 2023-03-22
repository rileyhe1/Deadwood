package cs345.deadwood.controller;

import cs345.deadwood.model.*;
import cs345.deadwood.view.GameLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameController implements IController
{
    private final GameEngine model;
    private final GameLog log = GameLog.getInstance();
    private int numCardsRemaining = 10;
    private int currentDay = 1;
    private boolean playerCurrentlyMoving = false;
    private boolean playerCurrentlyRehearsing = false;
    private boolean playerCurrentlyActing = false;
    private boolean playerHasMovedThisTurn = false;
    private boolean playerHasUpgradedThisTurn = false;
    private boolean playerCurrentlyUpgrading = false;
    private boolean playerTakingRole = false;
    private Set moveToThisSet;
    private Role playerNewRole;

    private int activePlayerNum;

    public GameController(GameEngine model)
    {
        this.model = model;
        activePlayerNum = 0;
    }
    public void distributeCards()
    {
        ArrayList<Integer> takenCards = new ArrayList<>();

        for(ISet s: model.getSetList())
        {
            if(s instanceof ISetScene)
            {
                Random rand = new Random();
                int cardIdx = rand.nextInt(39);
                while(takenCards.contains(cardIdx))
                {
                    cardIdx = rand.nextInt(39);
                }
                ((SetScene)s).setSceneCard(model.getCardList().get(cardIdx));
                takenCards.add(cardIdx);
            }
        }
    }
    public void printCards()
    {
        for(ISet s: model.getSetList())
        {
            if(s instanceof ISetScene)
            {
                for(IRole r : ((ISetScene) s).getSceneCard().getRoles())
                {
                    System.out.println(r.getName());
                }
            }
        }
    }
    private void upgrade()
    {
        // upgrade
        playerHasUpgradedThisTurn = true;
        playerCurrentlyUpgrading = false;
    }
    private void act()
    {
        String playerCurrentSetName = getActivePlayer().getCurrentSet().getName();
        int budget = 0;
        int dieResult = rollDie();
        log.log("Die Result: " + dieResult + "\n");
        String currentRoleName = getActivePlayer().getCurrentRole().getName();
        boolean onTheCard = false;
        for(ISet set: model.getSetList())
        {
            if(set instanceof ISetScene)
            {
                if(set.getName().equals(playerCurrentSetName))
                {
                    budget = ((ISetScene) set).getSceneCard().getBudget();
                }
            }
        }
        for(ICard c: model.getCardList())
        {
            for(IRole r: c.getRoles())
            {
                if(r.getName().equals(currentRoleName))
                {
                    onTheCard = true;
                }
            }
        }
        // successfully acted:
        if((dieResult + getActivePlayer().getPracticeChips()) >= budget)
        {
            log.log("Successfully acted! Removing a shot counter...\n");
            // remove shot counter
            SetScene currentScene = (SetScene)getActivePlayer().getCurrentSet();
            currentScene.decrementNumTakeIcons();
            log.log("Distributing money/credits!\n");
            if(onTheCard)
            {
                // award two credits
                getActivePlayer().setCredits(getActivePlayer().getCredits() + 2);
            }
            else
            {
                // award one credit and one dollar
                getActivePlayer().setCredits(getActivePlayer().getCredits() + 1);
                getActivePlayer().setMoney(getActivePlayer().getMoney() + 1);
            }
            if(currentScene.getNumTakeIcons() == 0)
            {
                sceneWrap(currentScene);
            }
        }
        // failed to act:
        else
        {
            log.log("Acting attempt failed!\n");
            if(!onTheCard)
            {
                // award one dollar
                getActivePlayer().setMoney(getActivePlayer().getMoney() + 1);
            }
        }
        playerCurrentlyActing = false;
        endTurn();
    }
    private void endTurn()
    {
        incrementTurn();
        playerHasMovedThisTurn = false;
        playerHasUpgradedThisTurn = false;
    }
    private int rollDie()
    {
        Random rand = new Random();
        return rand.nextInt(5)+1;
    }
    private void rehearse()
    {
        int budget = ((SetScene)getActivePlayer().getCurrentSet()).getSceneCard().getBudget();
        if(getActivePlayer().getPracticeChips() != budget)
        {
            int currentChips = getActivePlayer().getPracticeChips();
            getActivePlayer().setPracticeChips(currentChips + 1);
            log.log("Player " + getActivePlayer().getPlayerNum() + "'s Current practice chip count: " + getActivePlayer().getPracticeChips()+ "\n");
            playerCurrentlyRehearsing = false;
            endTurn();
        }
        else
        {
            log.log("You have enough chips to ensure acting success, you must act!\n");
        }
    }
    private int takeRole(Role newRole)
    {
        if(newRole.getPlayerOnRole() != null)
        {
            return 2;
        }
        // return 1 if the role is not valid:
        if((getActivePlayer().getCurrentSet() instanceof ISetScene))
        {
            if ((!((SetScene) getActivePlayer().getCurrentSet()).getRoles().contains(newRole) && !((SetScene) getActivePlayer().getCurrentSet()).getSceneCard().getRoles().contains(newRole)) || getActivePlayer().getRank() < newRole.getLevel())
            {
                return 1;
            }
        }
        // assign newRole to player and render player's die on that role:
        getActivePlayer().getBlankArea().setPlayerOnSpace(null);
        getActivePlayer().setBlankArea(null);
        getActivePlayer().setCurrentRole(null);
        getActivePlayer().setCurrentRole(newRole);
        playerTakingRole = false;
        return 0;
    }
    // returns 1 if move is invalid, 0 otherwise
    private int moveToSet(Set newSet)
    {
        if(!((Set)getActivePlayer().getCurrentSet()).getNeighborNames().contains(newSet.getName()))
        {
            return 1;
        }
        if(getActivePlayer().getBlankArea() != null)
        {
            getActivePlayer().getBlankArea().setPlayerOnSpace(null);
        }
        if(getActivePlayer().getCurrentRole() != null)
        {
            ((Role)getActivePlayer().getCurrentRole()).setPlayerOnRole(null);
            getActivePlayer().setCurrentRole(null);
        }
        getActivePlayer().setBlankArea(newSet.getNextAvailableBlank());
        getActivePlayer().getBlankArea().setPlayerOnSpace(getActivePlayer());
        getActivePlayer().setCurrentSet(((ISet)newSet));

        // flip the scene card
        if(newSet instanceof SetScene)
        {
            ((SetScene)newSet).flipCard();
        }

        if(getActivePlayer().getBlankArea() == null)
        {
            System.out.println("Error moving player to set: no available spaces");
        }
        playerCurrentlyMoving = false;
        return 0;
    }
    private void sceneWrap(SetScene currentScene)
    {
        log.log("Wrapping Scene...\n");
        boolean playersOnCard = false;

        for(IRole s: currentScene.getSceneCard().getRoles())
        {
            if(((Role) s).getPlayerOnRole() != null)
            {
                playersOnCard = true;
                break;
            }
        }
        // if there was a player on the card, distribute bonus money
        if(playersOnCard)
        {
            log.log("Distributing bonus money!\n");
            // bonus money for on the card roles
            int numDie = currentScene.getSceneCard().getBudget();
            int result;
            List<Integer> dieResults = new ArrayList<>();
            for(int i = 0; i < numDie; i++)
            {
                result = ((Integer)rollDie());
                dieResults.add(result);
            }
            Collections.sort(dieResults, Collections.reverseOrder());
            int currentRole = 0;
            for(int i = 0; i < numDie; i++)
            {
                if(getCurrentRole(currentScene, currentRole).getPlayerOnRole()!= null)
                {
                    // award the player on current role money equal to the die result assigned to this role:
                    getCurrentRole(currentScene, currentRole).getPlayerOnRole().setMoney(getCurrentRole(currentScene, currentRole).getPlayerOnRole().getMoney() + dieResults.get(i));
                    getCurrentRole(currentScene, currentRole).getPlayerOnRole().setPracticeChips(0);
                }

                if(currentRole == 2)
                {
                    currentRole = 0;
                }
                else
                {
                    currentRole++;
                }
            }
            // bonus money for off the card roles
            for(int i = 0; i < currentScene.getRoles().size(); i++)
            {
                if(getCurrentRole(currentScene, i).getPlayerOnRole()!= null)
                {
                    int currentMoney = getCurrentRole(currentScene, i).getPlayerOnRole().getMoney();
                    getCurrentRole(currentScene, i).getPlayerOnRole().setMoney(currentMoney + getCurrentRole(currentScene, i).getLevel());
                    getCurrentRole(currentScene, i).getPlayerOnRole().setPracticeChips(0);
                }
            }
        }
        // remove card
        currentScene.setSceneCard(null);
        numCardsRemaining--;

        // for each player on this scene, set role completed boolean to true & rest practice chips:
        for(IRole r : currentScene.getRoles())
        {
            if(((Role)r).getPlayerOnRole()!= null)
            {
                ((Role) r).getPlayerOnRole().setRoleCompleted(true);
                ((Role)r).getPlayerOnRole().setPracticeChips(0);
            }

        }


        // check if that was the second to last scene, if so end the day
        if(numCardsRemaining == 9)
        {
            log.log("Second to last scene card was wrapped!\n");
            log.log("Ending the day...\n");
            endDay();
        }
    }
    private void endDay()
    {
        // remove all cards, return all players to the trailers and increment current day, check if there are remaining days,
        log.log("Ending the day!\n");
        for(ISet s: model.getSetList())
        {
            if(s instanceof ISetScene)
            {
                ((SetScene)s).setSceneCard(null);
            }
        }
        Set trailer = null;
        for(ISet s: model.getSetList())
        {
            if(s.getName().equals("Trailer"))
            {
                trailer = ((Set)s);
            }
        }
        for(Player p: model.getPlayerList())
        {
            if(p.getBlankArea() != null)
            {
                p.getBlankArea().setPlayerOnSpace(null);
                p.setBlankArea(null);
            }
            else if(p.getCurrentRole() != null)
            {
                ((Role)p.getCurrentRole()).setPlayerOnRole(null);
                p.setCurrentRole(null);
            }

            if(trailer != null)
            {
                p.setCurrentSet(trailer);
                p.setBlankArea(trailer.getNextAvailableBlank());
                p.getBlankArea().setPlayerOnSpace(p);
            }
        }
        currentDay++;
        if(currentDay == 5)
        {
            // game over proceed to scoring
            log.log("Game over, proceeding to scoring!\n");
            scoring();
        }
        else
        {
            System.out.println("beginning new day!");
            log.log("Beginning day " + currentDay + "!\n");
            //replace shot counters
            for(ISet s: model.getSetList())
            {
                if(s instanceof ISetScene)
                {
                    ((SetScene)s).replaceTakeIcons();
                }
            }
            // deal 10 new cards
            distributeCards();
            // reset number of scenes
            numCardsRemaining = 10;
        }
    }

    private void scoring()
    {
        int highestScore = 0;
        List<Player> winners = new ArrayList<>();
        // iterate to find the highest score
        for(Player p: model.getPlayerList())
        {
            int score = p.getMoney();
            score += p.getCredits();
            score += (5 * p.getRank());
            p.setScore(score);
            if(score > highestScore)
            {
                highestScore = score;
            }
        }
        // add players with the highest score to the list of winners
        for(Player p: model.getPlayerList())
        {
            if(p.getScore() >= highestScore)
            {
                winners.add(p);
            }
        }
        log.log("Winner(s):");
        for(Player winner: winners)
        {
            log.log(" Player " + winner.getPlayerNum());
        }
        log.log("\n");
    }


    private Role getCurrentRole(SetScene currentScene, int idx)
    {
        return ((Role)currentScene.getSceneCard().getRoles().get(idx));
    }
    private Role getCurrentSceneRole(SetScene currentScene, int idx)
    {
        return ((Role)currentScene.getRoles().get(idx));
    }

    private void incrementTurn()
    {
        if(activePlayerNum == model.getPlayerList().size() -1)
        {
            getActivePlayer().setInactivePlayer();
            activePlayerNum = 0;
           getActivePlayer().setActivePlayer();
        }
        else
        {
            getActivePlayer().setInactivePlayer();
            activePlayerNum++;
            getActivePlayer().setActivePlayer();
        }
    }
    private Player getActivePlayer()
    {
        return model.getPlayerList().get(activePlayerNum);
    }

    @Override
    public void clicked(IRole role)
    {
        if(playerTakingRole)
        {
            int result = takeRole((Role)role);
            if(result == 1)
            {
             log.log("Invalid role... select a role that is <= your rank and on current set!\n");
            }
            else if(result == 2)
            {
                log.log("Invalid role... selected role is occupied!\n");
            }
            else
            {
                endTurn();
            }
        }

    }

    @Override
    public void clicked(ISet set)
    {
        if(playerCurrentlyMoving)
        {
            moveToThisSet = ((Set)set);
            if(moveToSet(((Set) set)) == 1)
            {
                // move was invalid, print error and wait for set input again
                log.log("Invalid move, select a neighboring set!\n");
            }
        }
    }

    @Override
    public void clickedMove(String action)
    {
        switch(action)
        {
            case "Move":
                // move
                System.out.println("Move clicked");
                // player can move if they're not currently moving, and if they're not currently on a role, and if they haven't already moved this turn.
                if(!playerCurrentlyMoving && (getActivePlayer().getCurrentRole() == null || (getActivePlayer().getCurrentRole() != null && getActivePlayer().isRoleCompleted())) && !playerHasMovedThisTurn)
                {
                    log.log("Click on the set you'd like to move to\n");
                    playerHasMovedThisTurn = true;
                    playerCurrentlyMoving = true;
                }
                else
                {
                    log.log("Cannot currently move!\n");
                }

                break;
            case "Take Role":
                // take role
                System.out.println("Take Role clicked");
                // players can take a role if they don't have one, are at a setscene, and aren't currently moving
                if(getActivePlayer().getCurrentRole() == null && getActivePlayer().getCurrentSet() instanceof SetScene && !playerCurrentlyMoving)
                {
                    log.log("Select the role you would like to take\n");
                    playerTakingRole = true;
                }
                else
                {
                    log.log("Cannot take role currently!\n");
                }
                break;
            case "Act":
                // act
                System.out.println("Act clicked");
                // players can act if they have a role, and aren't currently rehearsing
                if(getActivePlayer().getCurrentRole() != null && !playerCurrentlyMoving && !getActivePlayer().isRoleCompleted())
                {
                    System.out.println("acting!");
                    playerCurrentlyActing = true;
                    act();
                }
                else
                {
                    log.log("Cannot currently act!\n");
                }
                break;
            case "Rehearse":
                // rehearse
                System.out.println("Rehearse clicked");
                // players can rehearse if they have a role, and aren't currently acting
                if(getActivePlayer().getCurrentRole() != null && !playerCurrentlyActing && !playerCurrentlyMoving && !getActivePlayer().isRoleCompleted())
                {
                    playerCurrentlyRehearsing = true;
                    rehearse();
                }
                else
                {
                    log.log("Cannot currently rehearse!\n");
                }
                break;
            case "Upgrade":
                // upgrade
                System.out.println("Upgrade clicked");
                // players can upgrade if they are at the casting office, and haven't upgraded this turn and aren't currently upgrading
                if(getActivePlayer().getCurrentSet().getName().equals("Office") && !playerHasUpgradedThisTurn && !playerCurrentlyUpgrading && !playerCurrentlyMoving)
                {
                    playerCurrentlyUpgrading = true;
                    // upgrade
                    log.log("upgrading!\n");
                    upgrade();
                    playerHasUpgradedThisTurn = true;
                }
                else
                {
                    log.log("Cannot currently upgrade!\n");
                }
                break;
            case "End Turn":
                System.out.println("End Turn clicked");
                // end turn
                if(!playerCurrentlyMoving)
                {
                    endTurn();
                }
                else
                {
                    log.log("Cannot currently end turn, you are in the middle of an action!\n");
                }
                break;
            default:
                System.out.println("hit default...\n");
                break;

        }
    }

}
