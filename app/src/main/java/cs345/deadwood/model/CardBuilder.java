package cs345.deadwood.model;

import java.util.List;

public class CardBuilder
{
    private String name;
    private String img;
    private String sceneDescription;
    private int budget;
    private int sceneNumber;
    private List<IRole> roles;

    public CardBuilder addName(String name)
    {
        this.name = name;
        return this;
    }

    public CardBuilder addImg(String img)
    {
        this.img = img;
        return this;
    }

    public CardBuilder addSceneDescription(String description)
    {
        this.sceneDescription= description;
        return this;
    }

    public CardBuilder addBudget(int budget)
    {
        this.budget= budget;
        return this;
    }
    public CardBuilder addSceneNumber(int number)
    {
        this.sceneNumber = number;
        return this;
    }
   public CardBuilder addRoles(List<IRole> roles)
   {
       this.roles = roles;
       return this;
   }

   public Card getCard()
   {
       return new Card(name, img, sceneDescription, budget, sceneNumber, roles);
   }

}
