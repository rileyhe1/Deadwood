package cs345.deadwood.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class CardParser extends GameDataParser
{

    public CardParser() {
        super("cards.xml");
    }

    /**
     * Parse cards.xml to create a list of Card objects that implement the ICard interface.
     *
     * @return List of cards in cards.xml
     */
    public List<ICard> getCards()
    {
        List <ICard> cardArrayList= new ArrayList<>();

        Element rootNode = getRootNode();

        NodeList cards = rootNode.getElementsByTagName("card");
        for(int i = 0; i < cards.getLength(); i++)
        {
            Card newCard = new Card();
            newCard.setRoles();
            Node card = cards.item(i);
            newCard.setName(card.getAttributes().getNamedItem("name").getNodeValue());
            newCard.setImageName(card.getAttributes().getNamedItem("img").getNodeValue());
            newCard.setBudget(Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue()));

            NodeList children = card.getChildNodes();
            for(int j = 0; j < children.getLength(); j++)
            {
                Node sub = children.item(j);
                if("scene".equals(sub.getNodeName()))
                {
                    newCard.setSceneNumber(Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue()));
                    newCard.setSceneDescription(sub.getTextContent());
                }
                else if("part".equals(sub.getNodeName()))
                {
                    Role newRole = new Role();
                    newRole.setName(sub.getAttributes().getNamedItem("name").getNodeValue());
                    newRole.setLevel(Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue()));
                    NodeList partChildren = sub.getChildNodes();
                    for(int k = 0; k < partChildren.getLength(); k++)
                    {
                        Node partSub = partChildren.item(k);
                        if("area".equals(partSub.getNodeName()))
                        {
                            Area newArea = new Area();
                            newArea.setX(Integer.parseInt(partSub.getAttributes().getNamedItem("x").getNodeValue()));
                            newArea.setY(Integer.parseInt(partSub.getAttributes().getNamedItem("y").getNodeValue()));
                            newArea.setH(Integer.parseInt(partSub.getAttributes().getNamedItem("h").getNodeValue()));
                            newArea.setW(Integer.parseInt(partSub.getAttributes().getNamedItem("w").getNodeValue()));
                            newRole.setArea(newArea);
                        }
                        else if("line".equals(partSub.getNodeName()))
                        {
                            newRole.setLine(partSub.getTextContent());
                        }
                    }
                    newCard.getRoles().add(newRole);
                }
            }
            cardArrayList.add(newCard);
        }
        return cardArrayList;
    }


}
