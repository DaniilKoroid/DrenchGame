/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class MyPanel extends JPanel {
    private static final int PANEL_POLE_HEIGHT = 500;
    private static final int PANEL_POLE_WIDTH = 500;
    private static final int PANEL_COLOR_CHOOSE_WIDTH = 150;
    private static final int TEXT_POLE_HEIGHT = 20;
    private static final Dimension size = new Dimension(
            PANEL_POLE_WIDTH + PANEL_COLOR_CHOOSE_WIDTH,
            PANEL_POLE_HEIGHT + TEXT_POLE_HEIGHT);
    private Pole pole;
    private Game game;
    private int tileHeight;
    private int tileWidth;
    private int colorChooseHeight;
    private int colorChooseWidth;
    private int verticalIndent;
    private int horizontalPoleIndent;
    private int horizontalColorChooseIndent = 10;
    public boolean canClick;
    
    public MyPanel(final Game game, Pole pole) {
        super();
        this.pole = pole;
        this.game = game;
        canClick = true;
        setBorder(BorderFactory.createLineBorder(Color.black));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(game.isCanAnyoneExpand()) {
                    if(game.getGameMode() == GameMode.SINGLEPLAYER) {
                        if(canClick) {
                            canClick = false;
                            TileColor colorChoose;
                            int mouseX;
                            int mouseY;
                    
                            mouseX = e.getX();
                            mouseY = e.getY();
                            colorChoose = getColorChoose(mouseX, mouseY);
                            if(colorChoose == null) {
                                canClick = true;
                                return;
                            }
                            if(!game.getAllowedColors()[TileColor.getTileColorIndex(colorChoose)]) {    
                                canClick = true;
                                return;
                            }
                            game.makeMoveWithCurrentChoicer(colorChoose);
                            repaint();
                            canClick = true;  
                        }
                    } else if (game.getGameMode() == GameMode.PLAY_VS_BOTS) {
                        if((game.getCurrentChoicer()) instanceof Player) {
                            canClick  = true;
                        } else {
                            canClick = false;
                        }
                        if(canClick) {
                            canClick = false;
                            TileColor colorChoose;
                            int mouseX;
                            int mouseY;
                    
                            mouseX = e.getX();
                            mouseY = e.getY();
                            colorChoose = getColorChoose(mouseX, mouseY);
                            if(colorChoose == null) {
                                canClick = true;
                                return;
                            }
                            if(!game.getAllowedColors()[TileColor.getTileColorIndex(colorChoose)]) {
                                canClick = true;
                                return;
                            }
                             game.makeMoveWithCurrentChoicer(colorChoose);
                            repaint();
                            canClick = true;
                        }
                    }
                }
            }
        });
        setPreferredSize(size);
        calculateTileHeightAndWidth();
        calculateIndents();
        calculateColorChooseHeightAndWidth();
    }
    
    private TileColor getColorChoose(int mouseX, int mouseY) {
        if(mouseX < (PANEL_POLE_WIDTH + horizontalColorChooseIndent)
                || mouseX > (PANEL_POLE_WIDTH + PANEL_COLOR_CHOOSE_WIDTH
                            - horizontalColorChooseIndent)) {
            return null;
        }
        
        if(mouseY < (verticalIndent + TEXT_POLE_HEIGHT)
                || mouseY > (PANEL_POLE_HEIGHT + TEXT_POLE_HEIGHT- verticalIndent)) {
            return null;
        }
        
        return TileColor.values()
                [(int) ((mouseY - verticalIndent - TEXT_POLE_HEIGHT) / colorChooseHeight)];
    }
    
    private void calculateTileHeightAndWidth() {
        tileHeight = (size.height - TEXT_POLE_HEIGHT) / pole.getPoleHeight();
        tileWidth = (size.width - PANEL_COLOR_CHOOSE_WIDTH) / pole.getPoleWidth();
    }
    
    private void calculateIndents() {
        verticalIndent = (PANEL_POLE_HEIGHT
                - tileHeight * pole.getPoleHeight()) / 2;
        horizontalPoleIndent = (PANEL_POLE_WIDTH
                - tileWidth * pole.getPoleWidth()) / 2;
    }
    
    private void calculateColorChooseHeightAndWidth() {
        colorChooseHeight = (PANEL_POLE_HEIGHT - (2 * verticalIndent))
                / TileColor.values().length;
        colorChooseWidth = PANEL_COLOR_CHOOSE_WIDTH - (2 * horizontalColorChooseIndent);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int poleWidth;
        int poleHeight;
        
        poleWidth = pole.getPoleWidth();
        poleHeight = pole.getPoleHeight();
        
        //draw pole string
        g.setColor(Color.BLACK);
        g.drawString("Current disposition of tiles:",
                    2 * PANEL_POLE_WIDTH / 5,
                    3 * TEXT_POLE_HEIGHT / 4);
        //draw pole
        for(int row = 0; row < poleHeight; row++) {
            for(int col = 0; col < poleWidth; col++) {
                g.setColor(pole.getTile(row, col).getTileColor().getColor());
                g.fillRect(horizontalPoleIndent + (tileWidth * col),
                        TEXT_POLE_HEIGHT + verticalIndent + (tileHeight * row),
                        tileWidth,
                        tileHeight);
            }
        }
        
        //draw color choose string
        g.setColor(Color.BLACK);
        g.drawString("Choose your color:",
                    PANEL_POLE_WIDTH + 2 * horizontalColorChooseIndent,
                    3 * TEXT_POLE_HEIGHT / 4);
        
        //draw color choose
        TileColor[] values;
        int length;
        values = TileColor.values();
        length = values.length;
        for(int i = 0; i < length; i++) {
            g.setColor(values[i].getColor());
            g.fillRect(PANEL_POLE_WIDTH + horizontalColorChooseIndent,
                    TEXT_POLE_HEIGHT + verticalIndent + (i * colorChooseHeight),
                    colorChooseWidth,
                    colorChooseHeight);
        }
    }
    
    
}
