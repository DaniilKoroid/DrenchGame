/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class Tile {
    private TileColor tileColor;
    private Choicer owner;
    private int row;
    private int column;
    
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        owner = null;
        tileColor = TileColor.getRandomTileColor();
    }

    public TileColor getTileColor() {
        return tileColor;
    }

    public Choicer getOwner() {
        return owner;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setTileColor(TileColor tileColor) {
        this.tileColor = tileColor;
    }

    public void setOwner(Choicer owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.tileColor != other.tileColor) {
            return false;
        }
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        if (this.row != other.row) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }
        return true;
    }
    
    
}
