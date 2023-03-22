package cs345.deadwood.view;

import cs345.deadwood.controller.GameController;
import cs345.deadwood.model.BlankArea;
import cs345.deadwood.model.IRole;
import cs345.deadwood.model.ISetScene;
import cs345.deadwood.model.SetScene;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class SetSceneView
{
    private final JFrame board;
    private JLabel card;
    private JLabel role1;
    private List<JLabel> shotIcons = new ArrayList<>();
    private JPanel cardPanel;
    private JLabel cardLabel;
    private ISetScene set;
    private final GameLog log = GameLog.getInstance();

    private GameController controller;

    public SetSceneView(JFrame parentContainer, ISetScene set, GameController controller)
    {
        board = parentContainer;
        this.set = set;
        ((SetScene) set).setObserver(this);
        this.controller = controller;
    }

    public List<JLabel> getShotIcons()
    {
        return this.shotIcons;
    }
    public void removeCard()
    {
        cardLabel.setIcon(null);
    }

    public void drawSet() {

        /*
         * Create a JPanel to render things on the card.
         */
        cardPanel = new JPanel();
        cardPanel.setLocation(set.getArea().getX(), set.getArea().getY());
        cardPanel.setSize(set.getArea().getW(), set.getArea().getH());// height and width from board.xml, set name "Train Station", area element
        cardPanel.setLayout(null); // set layout to null, so we can render roles on the card (x-y values in roles in cards.xml). The x-y values for roles in cards.xml are relative to the card.
        cardPanel.setOpaque(false);
        board.add(cardPanel);

        cardPanel.addMouseListener(new setButtonListener()); // uncomment this to list clicks on this set

        cardLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("img/cardback.png").getPath()));
        cardLabel.setLocation(0,0);
        cardLabel.setSize(set.getArea().getW(), set.getArea().getH()); // height and width from board.xml, set name "Train Station", area element
        cardPanel.add(cardLabel);

        // sample code showing how to place the shot icon on a take
        // loop to add all the shot icons on each take on a scene
        for(int i = 0; i < set.getTakes().size(); i++)
        {
            JLabel shotIcon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("img/shot.png").getPath()));
            shotIcon.setLocation(set.getTakes().get(i).getX(), set.getTakes().get(i).getY()); // x,y values from board.xml, set name "Train Station", take 1
            shotIcon.setSize(set.getTakes().get(i).getH(), set.getTakes().get(i).getW()); // height and width from board.xml, set name "Train Station", take 1
            shotIcons.add(shotIcon);
            board.add(shotIcons.get(i));
        }

        for (IRole role : set.getRoles())
        {
            RoleView rView = new RoleView(role, controller);
            board.add(rView);
        }
        for (BlankArea blank : set.getBlankAreas())
        {
            BlankAreaView bView = new BlankAreaView(blank);
            bView.blankUpdated(blank.getPlayerOnSpace());
            board.add(bView);
        }

    }
    public void replaceShotIcons()
    {
        for(int i = 0; i < set.getTakes().size(); i++)
        {
            shotIcons.get(i).setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/shot.png").getPath()));
        }
    }

    public void decrementShotIcons()
    {
        shotIcons.get(((SetScene)set).getNumTakeIcons()).setIcon(null);
    }

    public void flipCard()
    {
        int number = set.getSceneCard().getSceneNumber();
        String numberAddress;
        if(number < 10)
        {
            numberAddress = ("0" + set.getSceneCard().getSceneNumber());
        }
        else
        {
            numberAddress = ("" + set.getSceneCard().getSceneNumber());
        }
//        cardLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/card_" + numberAddress + ".png").getPath()));
        cardLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/" + set.getSceneCard().getImageName())));
        for(IRole role: set.getSceneCard().getRoles())
        {
            RoleView rView = new RoleView(role, controller);
            board.add(rView);
        }
    }

    public void displayBackOfCard()
    {
        cardLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/cardback.png").getPath()));
    }

    private class setButtonListener implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Set " + set.getName() + " clicked.");
            controller.clicked(set);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
