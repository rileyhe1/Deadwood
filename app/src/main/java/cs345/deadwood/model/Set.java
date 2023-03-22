package cs345.deadwood.model;

import java.util.ArrayList;
import java.util.List;

public class Set implements ISet {
    private String name;
    private IArea area;
    private List<BlankArea> blankAreas;
    private List<String> neighborNames = new ArrayList<>();
    private final List<ISet> neighbors = new ArrayList<>();

    public Set(String name, IArea area, List<BlankArea> blankAreas, List<String> neighborNames) {
        this.name = name;
        this.area = area;
        this.blankAreas = blankAreas;
        this.neighborNames = neighborNames;
    }

    public Set() {
    }
    public BlankArea getNextAvailableBlank()
    {
        for(BlankArea ba: blankAreas)
        {
            if(ba.getPlayerOnSpace() == null)
            {
                return ba;
            }
        }
        return null;
    }

    public List<String> getNeighborNames() {
        return neighborNames;
    }

    public void addNeighborName(String name) {
        this.neighborNames.add(name);
    }

    public void addNeighbor(ISet s) {
        neighbors.add(s);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public List<ISet> getNeighbors() {
        return neighbors;
    }

    @Override
    public IArea getArea() {
        return this.area;
    }

    public void setArea(IArea newArea) {
        this.area = newArea;
    }

    @Override
    public List<BlankArea> getBlankAreas() {
        return this.blankAreas;
    }

    public void setBlankAreasList(List<BlankArea> blankAreasList) {
        this.blankAreas = blankAreasList;
    }

}
