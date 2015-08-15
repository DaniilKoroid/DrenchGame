/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.awt.Color;

/**
 *
 * @author Administrator
 */
public enum TileColor {
    RED(Color.RED),
    BLACK(Color.BLACK),
    YELLOW(Color.YELLOW),
    PINK(Color.PINK),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN);
    
    private final Color color;
    
    TileColor(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
    public static TileColor getRandomTileColor() {
        return TileColor.values()[(int) (Math.random() * TileColor.values().length)];
    }

    @Override
    public String toString() {
        return "" + this.name();
    }
    
    public static int getTileColorIndex(TileColor tileColor) {
        TileColor[] values;
        values = TileColor.values();
        for(int i = 0; i < values.length; i++) {
            if(values[i] == tileColor) {
                return i;
            }
        }
        return -1;
    }
    
}
