/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class Pole {
    public static final int MAX_HEIGHT = 50;
    public static final int MAX_WIDTH = 50;
    public static final int MIN_HEIGHT = 2;
    public static final int MIN_WIDTH = 2;
    
    private Tile[][] pole;
    private int poleWidth;
    private int poleHeight;
    
    public Pole(int rowCount, int columnCount) {
        checkRowCount(rowCount);
        checkColumnCount(columnCount);
        createPole();
    }
    
    private void checkRowCount(int rowCount) {
        if(rowCount > MAX_HEIGHT) {
            poleHeight = MAX_HEIGHT;
        } else if (rowCount < MIN_HEIGHT) {
            poleHeight = MIN_HEIGHT;
        } else {
            poleHeight = rowCount;
        }
    }
    
    private void checkColumnCount(int columnCount) {
        if(columnCount > MAX_WIDTH) {
            poleWidth = MAX_WIDTH;
        } else if (columnCount < MIN_WIDTH) {
            poleWidth = MIN_WIDTH;
        } else {
            poleWidth = columnCount;
        }
    }
    
    private void createPole() {
        pole = new Tile[poleHeight][poleWidth];
        for(int row = poleHeight - 1; row >= 0; row--) {
            for(int column = poleWidth - 1; column >= 0; column--) {
                pole[row][column] = new Tile(row, column);
            }
        }
        checkDifferentColorForCornerTiles();
    }

    private void checkDifferentColorForCornerTiles() {
        ArrayList<TileColor> usedColors;
        ArrayList<TileColor> unusedColors;
        
        usedColors = new ArrayList<TileColor>();
        unusedColors = new ArrayList<TileColor>();
        
        TileColor[] values;
        TileColor tileColor;
        values = TileColor.values();
        tileColor = pole[0][0].getTileColor();
        usedColors.add(tileColor);
        for(int i = values.length - 1; i >= 0; i--) {
            if(values[i] != tileColor) {
                unusedColors.add(values[i]);
            }
        }
        
        //Check upper right
        if(pole[0][poleWidth - 1].getTileColor() == tileColor) {
            int randomNumber;
            randomNumber = (int) (Math.random() * unusedColors.size());
            tileColor = unusedColors.get(randomNumber);
            pole[0][poleWidth - 1].setTileColor(tileColor);
            usedColors.add(tileColor);
            unusedColors.remove(randomNumber);
        } else {
            tileColor = pole[0][poleWidth - 1].getTileColor();
            usedColors.add(tileColor);
            unusedColors.remove(tileColor);
        }
        
        //Check lower right
        for(int i = usedColors.size() - 1; i >= 0; i--) {
            if(usedColors.get(i) == pole[poleHeight - 1][poleWidth - 1].getTileColor()) {
                int randomNumber;
                randomNumber = (int) (Math.random() * unusedColors.size());
                tileColor = unusedColors.get(randomNumber);
                pole[poleHeight - 1][poleWidth - 1].setTileColor(tileColor);
                usedColors.add(tileColor);
                unusedColors.remove(randomNumber);
                break;
            }
        }
        if(usedColors.size() == 2) {
            tileColor = pole[poleHeight - 1][poleWidth - 1].getTileColor();
            usedColors.add(tileColor);
            unusedColors.remove(tileColor);
        }
        
        //Check lower left
        for(int i = usedColors.size() - 1; i >= 0; i--) {
            if(usedColors.get(i) == pole[poleHeight - 1][0].getTileColor()) {
                int randomNumber;
                randomNumber = (int) (Math.random() * unusedColors.size());
                tileColor = unusedColors.get(randomNumber);
                pole[poleHeight - 1][0].setTileColor(tileColor);
                usedColors.add(tileColor);
                unusedColors.remove(randomNumber);
                break;
            }
        }
        if(usedColors.size() == 3) {
            tileColor = pole[poleHeight - 1][0].getTileColor();
            usedColors.add(tileColor);
            unusedColors.remove(tileColor);
        }
    }

    public int getPoleWidth() {
        return poleWidth;
    }

    public int getPoleHeight() {
        return poleHeight;
    }
    
    public Tile getTile(int row, int column) {
        return pole[row][column];
    }

    public ArrayList<Tile> getAllTilesAround(int row, int column) {
        ArrayList<Tile> result;
        Tile up;
        Tile down;
        Tile right;
        Tile left;
        
        up = getUpperTile(row, column);
        left = getLeftTile(row, column);
        down = getLowerTile(row, column);
        right = getRightTile(row, column);
        
        result = new ArrayList<Tile>();
        
        if(up != null) {
            result.add(up);
        }
        if(down != null) {
            result.add(down);
        }
        if(right != null) {
            result.add(right);
        }
        if(left != null) {
            result.add(left);
        }
        
        return result;
    }
    
    private Tile getUpperTile(int row, int col) {
        if(row == 0) {
            return null;
        } else {
            return pole[row - 1][col];
        }
    }
    
    private Tile getLowerTile(int row, int col) {
        if (row == poleHeight - 1) {
            return null;
        } else {
            return pole[row + 1][col];
        }
    }
    
    private Tile getRightTile(int row, int col) {
        if(col == poleWidth - 1) {
            return null;
        } else {
            return pole[row][col + 1];
        }
    }
    
    private Tile getLeftTile(int row, int col) {
        if(col == 0) {
            return null;
        } else {
            return pole[row][col - 1];
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pole other = (Pole) obj;
        if (!Arrays.deepEquals(this.pole, other.pole)) {
            return false;
        }
        if (this.poleWidth != other.poleWidth) {
            return false;
        }
        if (this.poleHeight != other.poleHeight) {
            return false;
        }
        return true;
    }
    
    
}
