package cs345.deadwood.view;

import cs345.deadwood.model.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerView extends JPanel
{
    private static final int HORIZONTAL_PADDING = 5;
    private final Player player;
    private JLabel dice;
    private JLabel playerInfo;

    private static int numberOfPlayers = 0;

    public PlayerView (Player player)
    {
        numberOfPlayers++;
        this.player = player;
        player.setObserver(this);
        this.setPreferredSize(new Dimension(300 - HORIZONTAL_PADDING*2, 50));
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        // use this label for player dice
        renderPlayerDie();

        // use this label for a string with the rest of the information
        renderPlayerInfo();
    }

    private void renderPlayerDie()
    {
        dice = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("img/dice_" + player.getDieColor() + player.getRank() + ".png").getPath()));
        this.add(dice);
        this.add(Box.createRigidArea(new Dimension(HORIZONTAL_PADDING,0))); // Add padding
    }
    private void renderPlayerInfo()
    {
        playerInfo = new JLabel();
        playerInfo.setText(getPlayerInfo());
        this.add(playerInfo);
    }

    public void setActivePlayer()
    {
        this.setBackground(Color.YELLOW);
    }

    public void setInactivePlayer()
    {
        this.setBackground(null);
    }

    public void playerUpdated()
    {
        // refresh Player Die
        dice.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/dice_" + player.getDieColor() + player.getRank() + ".png").getPath()));
        playerInfo.setText("P" + player.getPlayerNum() + " " + player.getLocation() + "; $" + player.getMoney()
        + " C" + player.getCredits() + " Pc" + player.getPracticeChips() + "; S=" + player.getScore());
    }
    public String getPlayerInfo()
    {
       return ("P" + player.getPlayerNum() + " " + player.getLocation() + "; $" + player.getMoney()
               + " C" + player.getCredits() + " Pc" + player.getPracticeChips() + "; S=" + player.getScore());
    }
}
