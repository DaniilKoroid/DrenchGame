/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class Choicer {
    protected String name;
    protected Pole pole;
    protected ArrayList<Tile> myTiles;
    protected ArrayList<Tile> boundTiles;
    protected ArrayList<Tile> neighbourTiles;
    
    public Choicer(String name, Pole pole) {
        this.name = name;
        this.pole = pole;
        myTiles = new ArrayList<Tile>();
        boundTiles = new ArrayList<Tile>();
        neighbourTiles = new ArrayList<Tile>();
    }
    
    public void setStartingTile(Tile tile) {
        addTile(tile);
        expandWhileCan(tile.getTileColor());
    }
    
    public void expandWhileCan(TileColor tileColor) {
        repaintMyTiles(tileColor);
        while(canExpandWithColor(tileColor)) {
            expandWithColor(tileColor);
        }
    }
    
    private void expandWithColor(TileColor tileColor) {
        if(neighbourTiles == null || neighbourTiles.isEmpty()) {
            return;
        }
        Iterator<Tile> iter;
        Tile curr;
        
        iter = neighbourTiles.iterator();
        
        while(iter.hasNext()) {
            curr = iter.next();
            if(tileColor == curr.getTileColor()) {
                if(!tileIsAdded(curr)) {
                    addTileWithoutUpdate(curr);
                }
            }
        }
        repaintMyTiles(tileColor);
        updateBoundTiles();
        updateNeighbourTiles();
    }
    
    private boolean tileIsAdded(Tile tile) {
        Iterator<Tile> iter;
        iter = myTiles.iterator();
        
        while(iter.hasNext()) {
            if(tile.equals(iter.next())) {
                return true;
            }
        }
        return false;
    }
    
    private void repaintMyTiles(TileColor tileColor) {
        Iterator<Tile> iter;
        iter = myTiles.iterator();
        
        while(iter.hasNext()) {
            iter.next().setTileColor(tileColor);
        }
    }
    
    public boolean canExpandWithColor(TileColor tileColor) {
        if(neighbourTiles == null || neighbourTiles.isEmpty()) {
            return false;
        }
        Iterator<Tile> iter;
        iter = neighbourTiles.iterator();
        
        while(iter.hasNext()) {
            if(tileColor == iter.next().getTileColor()) {
                return true;
            }
        }
        
        return false;
    }
    
    private void addTile(Tile tile) {
        myTiles.add(tile);
        tile.setOwner(this);
        updateBoundTiles();
        updateNeighbourTiles();
    }
    
    private void addTileWithoutUpdate(Tile tile) {
        myTiles.add(tile);
        tile.setOwner(this);
    }
    
    private void updateMyTiles() {
        myTiles = new ArrayList<Tile>();
        
        for(int row = pole.getPoleHeight() - 1; row >= 0; row--) {
            for(int col = pole.getPoleWidth() - 1; col >= 0; col--) {
                if(this.equals(pole.getTile(row, col).getOwner())) {
                    myTiles.add(pole.getTile(row, col));
                }
            }
        }
    }
    
    private void updateBoundTiles() {
        boundTiles = new ArrayList<Tile>();
        
        if(myTiles == null || myTiles.isEmpty()) {
            return;
        }
        
        Iterator<Tile> iter;
        Tile curr;
        
        iter = myTiles.iterator();
        
        while(iter.hasNext()) {
            curr = iter.next();
            if(isBoundTile(curr, pole.getAllTilesAround(curr.getRow(), curr.getColumn()))) {
                boundTiles.add(curr);
            }
        }
    }
    
    private void updateNeighbourTiles() {
        neighbourTiles = new ArrayList<Tile>();
        
        if(boundTiles == null || boundTiles.isEmpty()) {
            return;
        }
        
        Iterator<Tile> mainIter;
        Iterator<Tile> secIter;
        ArrayList<Tile> aroundCurrent;
        Tile current;
        
        mainIter = boundTiles.iterator();
        
        while(mainIter.hasNext()) {
            current = mainIter.next();
            aroundCurrent = pole.getAllTilesAround(current.getRow(), current.getColumn());
            if(aroundCurrent != null) {
                secIter = aroundCurrent.iterator();
                while(secIter.hasNext()) {
                    current = secIter.next();
                    if(current.getOwner() == null) {
                        neighbourTiles.add(current);
                    }
                }
            }
        }
    }

    private boolean isBoundTile(Tile center, ArrayList<Tile> aroundTiles) {
        Iterator<Tile> iter;
        Tile curr;
        
        iter = aroundTiles.iterator();
        
        while(iter.hasNext()) {
            curr = iter.next();
            if(!center.getOwner().equals(curr.getOwner())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Choicer other = (Choicer) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.pole, other.pole)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }
    
    public int myTilesSize() {
        return myTiles.size();
    }
    
    public boolean canExpand() {
        if(neighbourTiles == null || neighbourTiles.isEmpty()) {
            return false;
        }    
        return true;
    }
    
    public TileColor getCurrentTileColor() {
        return myTiles.get(0).getTileColor();
    }
    
    public int getNumberOfMyTiles() {
        return myTiles.size();
    }
}
