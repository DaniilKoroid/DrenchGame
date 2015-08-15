/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class Bot extends Choicer {

    protected Game game;
    
    public Bot(String name, Pole pole, Game game) {
        super(name, pole);
        this.game = game;
    }
    
    public TileColor chooseColor() {
        if(neighbourTiles == null || neighbourTiles.isEmpty()) {
            return randomAllowedColor(game.getAllowedColors());
        }
        boolean[] allowedColors;
        allowedColors = game.getAllowedColors();
        int[] availableTilesToAdd;
        
        availableTilesToAdd = new int[allowedColors.length];
        for(int i = 0; i < availableTilesToAdd.length; i++) {
            availableTilesToAdd[i] = 0;
        }
        
        Iterator<Tile> iter;
        Tile curr;
        
        iter = neighbourTiles.iterator();
        
        while (iter.hasNext()) {
            curr = iter.next();
            availableTilesToAdd[TileColor.getTileColorIndex(curr.getTileColor())]++;
        }
        
        return getMaxNumberOfAvailableTiles(availableTilesToAdd, allowedColors);
    }

    private TileColor getMaxNumberOfAvailableTiles(
            int[] availableTilesToAdd, boolean[] allowedColors) {
        int maximumIndex = -1;
        
        for(int i = 0; i < availableTilesToAdd.length; i++) {
            if(allowedColors[i]) {
                if(availableTilesToAdd[i] > 0) {
                    maximumIndex = i;
                }
            }
        }
        
        if (maximumIndex != -1) {
            return TileColor.values()[maximumIndex];
        } else {
            return randomAllowedColor(allowedColors);
        }
    }
    
    private TileColor randomAllowedColor(boolean[] allowedColors) {
        int numberOfAllowed = 0;
        for(int i = 0; i < allowedColors.length; i++) {
            if(allowedColors[i]) {
                numberOfAllowed++;
            }
        }
        TileColor[] colors;
        int counter = 0;
        colors = new TileColor[numberOfAllowed];
        for(int i = 0; i < allowedColors.length; i++) {
            if(allowedColors[i]) {
                colors[counter++] = TileColor.values()[i];
            }
        }
        
        return colors[(int) (Math.random() * colors.length)];
    }
    
    
}
