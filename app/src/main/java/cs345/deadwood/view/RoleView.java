package cs345.deadwood.view;

import cs345.deadwood.controller.GameController;
import cs345.deadwood.model.IRole;
import cs345.deadwood.model.Player;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoleView extends JLabel implements MouseListener {
    private final IRole model;
    private GameController controller;
    private final GameLog log = GameLog.getInstance();

    public RoleView(IRole model, GameController controller)
    {
        this.model = model;
        model.setObserver(this);
        this.controller = controller;

        setLocation(model.getArea().getX(), model.getArea().getY());
        setSize(model.getArea().getW(), model.getArea().getH());
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        System.out.println("Role " + model.getName() + " clicked.");
        controller.clicked(model);
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

    public void roleUpdated(Player p)
    {
        if(p == null)
        {
            this.setIcon(null);
        }
        else
        {
            this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/dice_" + p.getDieColor() + p.getRank() + ".png").getPath()));
            this.setLocation(model.getArea().getX(), model.getArea().getY());
            this.setSize(model.getArea().getW(), model.getArea().getH());
        }
    }
}