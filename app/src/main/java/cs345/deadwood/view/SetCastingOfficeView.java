package cs345.deadwood.view;

import cs345.deadwood.controller.GameController;
import cs345.deadwood.model.BlankArea;
import cs345.deadwood.model.ISet;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SetCastingOfficeView
{

    private final JFrame board;
    private ISet set;
    private GameController controller;
    private JPanel cardPanel;
    private final GameLog log = GameLog.getInstance();

    public SetCastingOfficeView(JFrame parentContainer, ISet set, GameController controller)
    {
        board = parentContainer;
        this.set = set;
        this.controller = controller;

    }

    public void drawSet()
    {
        cardPanel = new JPanel();
        cardPanel.setLocation(set.getArea().getX(), set.getArea().getY());
        cardPanel.setSize(set.getArea().getW(), set.getArea().getH());// height and width from board.xml, set name "Train Station", area element
        cardPanel.setLayout(null); // set layout to null, so we can render roles on the card (x-y values in roles in cards.xml). The x-y values for roles in cards.xml are relative to the card.
        cardPanel.setOpaque(false);
        board.add(cardPanel);

        for (BlankArea blank : set.getBlankAreas())
        {
            BlankAreaView bView = new BlankAreaView(blank);
            bView.blankUpdated(blank.getPlayerOnSpace());
            board.add(bView);
        }

        cardPanel.addMouseListener(new CastingOfficeButtonListener()); // uncomment this to list clicks on this set
    }
    private class CastingOfficeButtonListener implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("Set " + set.getName() + " clicked.");
            log.log("Set " + set.getName() + " clicked.\n");
        }

        @Override
        public void mousePressed(MouseEvent e)
        {

        }

        @Override
        public void mouseReleased(MouseEvent e)
        {

        }

        @Override
        public void mouseEntered(MouseEvent e)
        {

        }

        @Override
        public void mouseExited(MouseEvent e)
        {

        }
    }
}
