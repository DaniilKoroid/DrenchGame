/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class Game {
    private Pole pole;
    private GameMode gameMode;
    private Choicer[] choicers;
    private Choicer currentChoicer;
    private int currentChoicerIndex;
    private JFrame frame;
    private MyPanel panel;
    private boolean canAnyoneExpand;
    private boolean[] allowedColors;
    private int singleCounterOfMoves;
    
    public Game() {
        frame = new JFrame("Drench game by Daniil Koroid.");
        pole = new Pole(askForRowCount(), askForColumnCount());
        gameMode = askForGameMode();
        setChoicers();
        allowedColors = new boolean[TileColor.values().length];
        for(int i = 0; i < allowedColors.length; i++) {
            allowedColors[i] = true;
        }
        canAnyoneExpand = true;
        updateAllowedColors();
        if(gameMode == GameMode.PLAY_VS_BOTS) {
            playVsBotsMessage();
        }
        addPanelToFrame();
    }
    
    private void updateAllowedColors() {
        TileColor currentColor;
        for(int i = 0; i < allowedColors.length; i++) {
            allowedColors[i] = true;
        }
        for(int i = 0; i < choicers.length; i++) {
            currentColor = choicers[i].getCurrentTileColor();
            int currentColorIndex = TileColor.getTileColorIndex(currentColor);
            allowedColors[currentColorIndex] = false;
        }
    }
    
    private void addPanelToFrame() {
        panel = new MyPanel(this, pole);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    private int askForRowCount() {
        String answer;
        int result;
        try {
            answer = JOptionPane.showInputDialog("Please, input number of rows ("
                    + "from " + Pole.MIN_HEIGHT + " to " + Pole.MAX_HEIGHT + ")");
            result = Integer.parseInt(answer);
            return result;
        } catch (NumberFormatException nfe) {
            return askForRowCount();
        }
    }
    
    private int askForColumnCount() {
        String answer;
        int result;
        try {
            answer = JOptionPane.showInputDialog("Please, input number of "
                    + "columns (from " + Pole.MIN_WIDTH + " to "
                    + Pole.MAX_WIDTH + ")");
            result = Integer.parseInt(answer);
            return result;
        } catch (NumberFormatException nfe) {
            return askForColumnCount();
        }
    }
    
    private GameMode askForGameMode() {
        GameMode gm = null;
        Object[] values;
        Object[] gameModes;
        
        values = GameMode.values();
        gameModes = new Object[values.length];
        for(int i = values.length - 1; i >= 0; i--) {
            gameModes[i] = values[i].toString();
        }
        
        String result = (String) JOptionPane.showInputDialog(
                frame,
                "Game mode:",
                "Choose game mode:",
                JOptionPane.PLAIN_MESSAGE,
                null,
                gameModes,
                gameModes[0]);
        
        for(int i = values.length - 1; i >= 0; i--) {
            if(result.equals(values[i].toString())) {
                gm = GameMode.values()[i];
            }
        }
        
        return gm;
    }
    
    private int askForBotCount() {
        Object[] values = {"1", "2", "3"};
        
        String result = (String) JOptionPane.showInputDialog(
                frame,
                "Number of bots:",
                "Choose number of bots to play against:",
                JOptionPane.PLAIN_MESSAGE,
                null,
                values,
                values[0]);
        
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException nfe) {
            return 0;
        }

    }
    
    private String askForPlayerName() {
        return JOptionPane.showInputDialog("Please, enter your name.");
    }
    
    private void setChoicers() {
        if(gameMode == GameMode.SINGLEPLAYER) {
            choicers = new Choicer[1];
            choicers[0] = new Player(askForPlayerName(), pole);
            choicers[0].setStartingTile(pole.getTile(0, 0));
            singleCounterOfMoves = 0;
            
        } else {
            choicers = new Choicer[askForBotCount() + 1];
            
            choicers[0] = new Player(askForPlayerName(), pole);
            choicers[0].setStartingTile(pole.getTile(0, 0));
            
            for(int i = choicers.length - 1; i > 0; i--) {
                choicers[i] = new Bot("Bot #" + i, pole, this);
            }
            
            if(choicers.length > 3) {
                choicers[3].setStartingTile(
                    pole.getTile(pole.getPoleHeight() - 1, 0));
            }
            
            if(choicers.length > 2) {
                choicers[2].setStartingTile(
                    pole.getTile(0, pole.getPoleWidth() - 1));
            }
            
            if(choicers.length > 1) {
                choicers[1].setStartingTile(
                        pole.getTile(   pole.getPoleHeight() - 1,
                                        pole.getPoleWidth() - 1));
            }
            singleCounterOfMoves = -1;
        }
        currentChoicerIndex = 0;
        currentChoicer = choicers[currentChoicerIndex];
    }

    private void getNextChoicer() {
        if(++currentChoicerIndex == choicers.length) {
            currentChoicerIndex = 0;
        }
        currentChoicer = choicers[currentChoicerIndex];
        
        if(gameMode == GameMode.PLAY_VS_BOTS) {
            if((currentChoicer) instanceof Bot) {
                Bot bot;
                bot = (Bot) currentChoicer;
                updateAllowedColors();
                makeMoveWithCurrentChoicer(bot.chooseColor());
                updateAllowedColors();
            }
        }
    }
    
    public void makeMoveWithCurrentChoicer(TileColor colorChoose) {
        if(gameMode == GameMode.SINGLEPLAYER) {
            singleCounterOfMoves++;
        }
        currentChoicer.expandWhileCan(colorChoose);
        updateExpandingAbility();
        getNextChoicer();
        panel.repaint();
        updateAllowedColors();
    }
    
    public Choicer getCurrentChoicer() {
        return currentChoicer;
    }
    
    public void updateExpandingAbility() {
        int summ = 0;
        for (Choicer choicer : choicers) {
            summ += choicer.getNumberOfMyTiles();
        }
        if(summ == pole.getPoleHeight() * pole.getPoleWidth()) {
            canAnyoneExpand = false;
            endGame();
        }
        canAnyoneExpand = true;
    }
    
    public GameMode getGameMode() {
        return gameMode;
    }
    
    public boolean[] getAllowedColors() {
        return allowedColors;
    }

    public boolean isCanAnyoneExpand() {
        return canAnyoneExpand;
    }
    
    public void endGame() {
        panel.repaint();
        frame.repaint();
        getFinalStats();
        frame.removeAll();
        frame.dispose();
        System.exit(0);
    }

    private void getFinalStats() {
        if(gameMode == GameMode.SINGLEPLAYER) {
            String gameInfo = "";
            gameInfo += "Game summary:\n";
            gameInfo += "Congratulations to " + choicers[0].getName() + " for "
                    + "capturing the whole pole in " + singleCounterOfMoves
                    + " moves!";
            JOptionPane.showMessageDialog(new JFrame(), gameInfo);
            return;
        }
        String gameInfo = "";
        int maximumTiles = -1;
        int currentTiles;
        int winners = 0;
        gameInfo += "Game summary:\n";
        for (Choicer choicer : choicers) {
            currentTiles = choicer.getNumberOfMyTiles();
            gameInfo += choicer.getName() + " controls " + currentTiles + " ";
            if(currentTiles == 1) {
                gameInfo += "tile.";
            } else {
                gameInfo += "tiles.";
            }
            gameInfo +="\n";
            if(currentTiles > 1) {
                if(currentTiles > maximumTiles) {
                    maximumTiles = currentTiles;
                }
            }
        }
        gameInfo += "Congratulations to ";
        for (Choicer choicer : choicers) {
            if (maximumTiles == choicer.getNumberOfMyTiles()) {
                if(winners > 0) {
                    gameInfo += ", ";
                }
                gameInfo += choicer.getName();
                winners++;
            }
        }
        gameInfo += " for capturing the maximum tiles (" + maximumTiles + ")!";
                
        JOptionPane.showMessageDialog(new JFrame(), gameInfo);
                
    }

    private void playVsBotsMessage() {
        String info = "";
        info += "You now play versus " + (int) (choicers.length - 1) + " bots:";
        if(choicers.length > 1) {
            info += "\n" + choicers[1].getName() + "(lower right corner)";
        }
        if(choicers.length > 2) {
            info += "\n" + choicers[2].getName() + "(upper right corner)";
        }
        if(choicers.length > 3) {
            info += "\n" + choicers[3].getName() + "(lower left corner)";
        }
        JOptionPane.showMessageDialog(new JFrame(), info);
    }
}
