package cs345.deadwood.view;

import cs345.deadwood.model.BlankArea;
import cs345.deadwood.model.Player;

import javax.swing.*;

public class BlankAreaView extends JLabel
{
    private final BlankArea blank;

    public BlankAreaView(BlankArea blank)
    {
        this.blank = blank;
        blank.setObserver(this);
        this.setLocation(blank.getArea().getX(), blank.getArea().getY());
        this.setSize(blank.getArea().getW(), blank.getArea().getH());
    }
    public void blankUpdated(Player player)
    {
        renderPlayerOnBlank(player);
    }

    private void renderPlayerOnBlank(Player player)
    {
        if(player != null)
        {
            this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/dice_" + player.getDieColor() + player.getRank() + ".png").getPath()));
            this.setLocation(blank.getArea().getX(), blank.getArea().getY());
            this.setSize(blank.getArea().getW(), blank.getArea().getH());
        }
        else
        {
            this.setIcon(null);
        }
    }
}
