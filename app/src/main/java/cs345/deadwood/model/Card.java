package cs345.deadwood.model;

import java.util.ArrayList;
import java.util.List;

public class Card implements ICard
{
    private String name;
    private String img;
    private String sceneDescription;
    private int budget;
    private int sceneNumber;
    private List<IRole> roles;

    public Card()
    {

    }

    public Card(String name, String img, String sceneDescription, int budget, int sceneNumber, List<IRole> roles)
    {
        this.name = name;
        this.img = img;
        this. sceneDescription = sceneDescription;
        this.budget = budget;
        this.sceneNumber = sceneNumber;
        this.roles = roles;
    }
    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    @Override
    public String getImageName()
    {
        return img;
    }

    public void setImageName(String newName)
    {
        this.img = newName;
    }

    public String getSceneDescription()
    {
        return this.sceneDescription;
    }

    public void setSceneDescription(String newSceneDescription)
    {
        this.sceneDescription = newSceneDescription;
    }


    @Override
    public int getBudget()
    {
        return budget;
    }

    public void setBudget(int newBudget)
    {
        this.budget = newBudget;
    }

    @Override
    public int getSceneNumber()
    {
        return sceneNumber;
    }

    public void setSceneNumber(int newSceneNumber)
    {
        this.sceneNumber = newSceneNumber;
    }

    @Override
    public List<IRole> getRoles()
    {
        return roles;
    }

    public void setRoles()
    {
        this.roles = new ArrayList<>();
    }
}
