package cs345.deadwood.model;

import cs345.deadwood.view.SetSceneView;

import java.util.ArrayList;
import java.util.List;

public class SetScene extends Set implements ISetScene {
    private List<IArea> takes;
    private final List<IRole> roles = new ArrayList<>();
    private ICard sceneCard;
    private int numTakeIcons;
    private SetSceneView observer;

    public void setObserver(SetSceneView ob)
    {
        this.observer = ob;
    }

    public int getNumTakeIcons()
    {
        return numTakeIcons;
    }

    public void replaceTakeIcons()
    {
        observer.replaceShotIcons();
    }
    public void flipCard()
    {
        observer.flipCard();
    }

    @Override
    public List<IArea> getTakes() {
        return this.takes;
    }

    @Override
    public List<IRole> getRoles() {
        return this.roles;
    }

    @Override
    public ICard getSceneCard() {
        return this.sceneCard;
    }

    public void setSceneCard(ICard newSceneCard)
    {
        this.sceneCard = newSceneCard;
        numTakeIcons = getTakes().size();
        if(sceneCard == null)
        {
            observer.removeCard();
        }
        else
        {
            observer.displayBackOfCard();
        }
    }

    public void setTakesList(List<IArea> newTakes) {
        this.takes = newTakes;
    }

    public void addRole(Role newRole) {
        roles.add(newRole);
    }
    public void decrementNumTakeIcons()
    {
        numTakeIcons--;
        observer.decrementShotIcons();
    }
}
