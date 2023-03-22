package cs345.deadwood.model;

import java.util.List;

public class SetSceneBuilder
{
    private String name;
    private List<ISet> neighbors;
    private IArea area;
    private List<IArea> blankAreas;
    private List<IArea> takes;
    private List<IRole> roles;
    private ICard sceneCard;

    public SetSceneBuilder addNeighbors(List<ISet> neighbors)
    {
        this.neighbors = neighbors;
        return this;
    }

    public SetSceneBuilder addArea(IArea area)
    {
        this.area = area;
        return this;
    }

    public SetSceneBuilder addBlankAreas(List<IArea> blanks)
    {
        this.blankAreas = blanks;
        return this;
    }

    public SetSceneBuilder addTakes(List<IArea> takes)
    {
        this.takes = takes;
        return this;
    }

    public SetSceneBuilder addRoles(List<IRole> roles)
    {
        this.roles = roles;
        return this;
    }

    public SetSceneBuilder addName(String name)
    {
        this.name = name;
        return this;
    }

    public SetSceneBuilder addSceneCard(ICard sceneCard)
    {
        this.sceneCard = sceneCard;
        return this;
    }

    public SetScene getSetScene()
    {
//        return new SetScene(name, neighbors, area, blankAreas, takes, roles, sceneCard);
        // TODO: 7/18/22 Implement
        return new SetScene();
    }
}
