package cs345.deadwood.view;

import cs345.deadwood.controller.GameController;
import cs345.deadwood.model.GameEngine;
import cs345.deadwood.model.ISet;
import cs345.deadwood.model.ISetScene;
import cs345.deadwood.model.Player;
import cs345.deadwood.view.GameLog;
import org.checkerframework.checker.units.qual.A;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;


public class BoardView implements MouseListener
{

    private final GameController controller;
    private final GameEngine model;
    private JFrame frame;
    private final int VERTICAL_PADDING = 5;
    private final int HORIZONTAL_PADDING = 5;
    private JTextArea comment = new JTextArea();
    private GameLog log = GameLog.getInstance();
    private final ArrayList<PlayerView> playerViews = new ArrayList<>();

    public BoardView(GameEngine model, GameController controller)
    {
        this.model = model;
        this.controller = controller;
        log.registerObserver(this);
    }

    public void init()
    {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 930));
        // board img is 1200 x 900. The control panel is 300 x 900, so we want the frame to be 1500 x 900
        // The top bar on the frame is about 30 pixels in height. To account for that, we increase frame height by 30, so 930.

        // Set layout to null, so we can place widgets based on x-y coordinates.
        frame.setLayout(null);

        for(ISet set: model.getSetList())
        {
            if(set instanceof ISetScene)
            {
                SetSceneView setView = new SetSceneView(frame, (ISetScene) set, controller);
                setView.drawSet();
            }
            else if("Trailer".equals(set.getName()))
            {
                SetTrailorView setView = new SetTrailorView(frame, set, controller);
                setView.drawSet();
            }
            else if("Office".equals(set.getName()))
            {
                SetCastingOfficeView setView = new SetCastingOfficeView(frame, set, controller);
                setView.drawSet();
            }
            else
            {
                throw new RuntimeException("Found unexpected set name");
            }
        }


        URL boardImg = getClass().getClassLoader().getResource("img/board.png");
        JLabel board = new JLabel(new ImageIcon(boardImg.getPath()));
        board.setLocation(0, 0);
        board.setSize(1200, 900);
        frame.add(board);

        JPanel controlPanel = createControlPanel();
        controlPanel.setLocation(1200, 0);
        controlPanel.setSize(300, 900);
        frame.add(controlPanel);

        frame.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
        log.log("Game setup complete\n");
    }

    private JPanel createControlPanel()
    {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(300, 900));
        // Set height same as the board image. board image dimensions are 1200 x 900

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add padding around edges

        JLabel team = new JLabel("Team Name", SwingConstants.CENTER);
        team.setFont(new Font("TimesRoman", Font.BOLD, 20));
        controlPanel.add(team);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        JLabel playerInfoLabel = new JLabel("Players", SwingConstants.CENTER);
        playerInfoLabel.setFont(new Font("TimesRoman", Font.BOLD, 18));
        controlPanel.add(playerInfoLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        // Show players
        int playerNum = 1;
        for(Player p: model.getPlayerList())
        {
            PlayerView pv = new PlayerView(p);
            playerViews.add(pv);
            if(playerNum == 1)
                pv.setActivePlayer();
            controlPanel.add(pv);
            controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
            playerNum++;
        }


        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(getMovePanel());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(miscInteraction());


        return controlPanel;
    }


    private JPanel getMovePanel()
    {
        JPanel movePanel = new JPanel();
        movePanel.setPreferredSize(new Dimension(300 - HORIZONTAL_PADDING*2, 200));

        JLabel panelTitle = new JLabel("Move options");
        panelTitle.setFont(new Font("TimesRoman", Font.BOLD, 18));
        movePanel.add(panelTitle);

        JButton move = new JButton("Move");
        move.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.clickedMove("Move");
            }
        });
        movePanel.add(move);

        JButton takeRole = new JButton("Take Role");
        takeRole.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.clickedMove("Take Role");
            }
        });
        movePanel.add(takeRole);

        JButton act = new JButton("Act");
        act.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.clickedMove("Act");
            }
        });
        movePanel.add(act);
        JButton rehearse = new JButton("Rehearse");
        rehearse.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.clickedMove("Rehearse");
            }
        });
        movePanel.add(rehearse);
        JButton upgrade = new JButton("Upgrade");
        upgrade.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.clickedMove("Upgrade");
            }
        });
        movePanel.add(upgrade);
        JButton endTurn = new JButton("End Turn");
        endTurn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.clickedMove("End Turn");
            }
        });
        movePanel.add(endTurn);
//        JTextArea comment = new JTextArea("Player interaction space. E.g., Ask what the player wants to do, show valid moves");
//        comment.setLineWrap(true);
//        comment.setPreferredSize(movePanel.getPreferredSize());
//        movePanel.add(comment);

        return movePanel;
    }


    private JPanel miscInteraction()
    {
        // free space to use for comments or any game related stuff. E.g., show rolling die or show game log.

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300 - HORIZONTAL_PADDING*2, 250));

        JLabel panelTitle = new JLabel("Game Log");
        panelTitle.setFont(new Font("TimesRoman", Font.BOLD, 18));
        panel.add(panelTitle);

        comment = new JTextArea();
        comment.setLineWrap(true);
        comment.setPreferredSize(panel.getPreferredSize());
        panel.add(comment);
        return panel;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // The top bar of the frame is about 30 pixels in height. So to get the x,y values on the board, subtract 30 from the y value.
        System.out.println("Mouse clicked at X = " + e.getX() + ", Y = " + (e.getY() - 30));
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

    public void subjectUpdated(String message)
    {
        comment.append(message);
    }
}
