package cs345.deadwood.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SetParser extends GameDataParser {

    public SetParser() {
        super("board.xml");
    }

    /**
     * Parse board.xml to create a list of set objects that implement the ISet or ISetScene interface.
     *
     * @return List of Sets in board.xml
     */
    public List<ISet> getSets() {
        List<ISet> board = new ArrayList<>();
        HashMap<String, ISet> setMap = new HashMap<>();

        Element rootNode = getRootNode();

        NodeList sets = rootNode.getChildNodes();
        for (int i = 0; i < sets.getLength(); i++) {

            Node set = sets.item(i);
            if ("set".equals(set.getNodeName())) {

                SetScene newScene = new SetScene();
                newScene.setName(set.getAttributes().getNamedItem("name").getNodeValue());

                NodeList children = set.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node sub = children.item(j);
                    if ("neighbors".equals(sub.getNodeName())) {
                        NodeList subChildren = sub.getChildNodes();
                        for (int k = 0; k < subChildren.getLength(); k++) {
                            Node subChild = subChildren.item(k);
                            if ("neighbor".equals(subChild.getNodeName())) {
                                newScene.addNeighborName(subChild.getAttributes().getNamedItem("name").getNodeValue());
                            }
                        }
                    } else if ("area".equals(sub.getNodeName())) {
                        newScene.setArea(parseArea(sub));
                    } else if ("takes".equals(sub.getNodeName())) {
                        NodeList subChildren = sub.getChildNodes();
                        List<IArea> takeList = new ArrayList();
                        for (int l = 0; l < subChildren.getLength(); l++) {
                            Node subChild = subChildren.item(l);
                            if ("take".equals(subChild.getNodeName())) {
                                NodeList takeChildren = subChild.getChildNodes();
                                for (int m = 0; m < takeChildren.getLength(); m++) {
                                    Node takeChild = takeChildren.item(m);
                                    if ("area".equals(takeChild.getNodeName())) {
                                        Area takeArea = new Area();
                                        takeArea.setX(Integer.parseInt(takeChild.getAttributes().getNamedItem("x").getNodeValue()));
                                        takeArea.setY(Integer.parseInt(takeChild.getAttributes().getNamedItem("y").getNodeValue()));
                                        takeArea.setH(Integer.parseInt(takeChild.getAttributes().getNamedItem("h").getNodeValue()));
                                        takeArea.setW(Integer.parseInt(takeChild.getAttributes().getNamedItem("w").getNodeValue()));
                                        takeList.add(takeArea);
                                    }
                                }
                            }
                        }
                        Collections.reverse(takeList);
                        newScene.setTakesList(takeList);
                    } else if ("parts".equals(sub.getNodeName())) {
                        NodeList partsChildren = sub.getChildNodes();
                        for (int n = 0; n < partsChildren.getLength(); n++) {
                            Node partsChild = partsChildren.item(n);
                            if ("part".equals(partsChild.getNodeName())) {
                                Role newRole = new Role();
                                newRole.setName(partsChild.getAttributes().getNamedItem("name").getNodeValue());
                                newRole.setLevel(Integer.parseInt(partsChild.getAttributes().getNamedItem("level").getNodeValue()));

                                NodeList partChildren = partsChild.getChildNodes();
                                for (int o = 0; o < partChildren.getLength(); o++) {
                                    Node partChild = partChildren.item(o);
                                    if ("area".equals(partChild.getNodeName())) {
                                        Area partArea = new Area();
                                        partArea.setX(Integer.parseInt(partChild.getAttributes().getNamedItem("x").getNodeValue()));
                                        partArea.setY(Integer.parseInt(partChild.getAttributes().getNamedItem("y").getNodeValue()));
                                        partArea.setH(Integer.parseInt(partChild.getAttributes().getNamedItem("h").getNodeValue()));
                                        partArea.setW(Integer.parseInt(partChild.getAttributes().getNamedItem("w").getNodeValue()));
                                        newRole.setArea(partArea);
                                    } else if ("line".equals(partChild.getNodeName())) {
                                        newRole.setLine(partChild.getTextContent());
                                    }
                                }
                                newScene.addRole(newRole);
                            }
                        }
                    } else if ("blanks".equals(sub.getNodeName())) {
                        NodeList blanksChildren = sub.getChildNodes();
                        List<BlankArea> blankAreaList = new ArrayList<>();
                        for (int p = 0; p < blanksChildren.getLength(); p++) {
                            Node blanksChild = blanksChildren.item(p);
                            if ("blank".equals(blanksChild.getNodeName())) {
                                NodeList blankChildren = blanksChild.getChildNodes();
                                for (int q = 0; q < blankChildren.getLength(); q++) {
                                    Node blankChild = blankChildren.item(q);
                                    if ("area".equals(blankChild.getNodeName())) {
                                        BlankArea blank = new BlankArea();
                                        Area blankArea = new Area();
                                        blankArea.setX(Integer.parseInt(blankChild.getAttributes().getNamedItem("x").getNodeValue()));
                                        blankArea.setY(Integer.parseInt(blankChild.getAttributes().getNamedItem("y").getNodeValue()));
                                        blankArea.setH(Integer.parseInt(blankChild.getAttributes().getNamedItem("h").getNodeValue()));
                                        blankArea.setW(Integer.parseInt(blankChild.getAttributes().getNamedItem("w").getNodeValue()));
                                        blank.setArea(blankArea);
                                        blankAreaList.add(blank);
                                    }
                                }
                            }
                        }
                        newScene.setBlankAreasList(blankAreaList);
                    }
                }
                board.add(newScene);
                setMap.put(newScene.getName(), newScene);
            }

            /* ***** Parse Office and trailer ********* */
            if ("office".equals(set.getNodeName()) || "trailer".equals(set.getNodeName())) {

                Node neighborsNode = null, areaNode = null, blankAreaNode = null;
                NodeList children = set.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node sub = children.item(j);

                    switch (sub.getNodeName()) {
                        case "neighbors":
                            neighborsNode = sub;
                            break;
                        case "area":
                            areaNode = sub;
                            break;
                        case "blanks":
                            blankAreaNode = sub;
                            break;
                    }
                }

                String setName = null;
                if ("office".equals(set.getNodeName())) {
                    setName = "Office";
                } else {
                    setName = "Trailer";
                }
                Set s = new Set(setName, parseArea(areaNode), parseNumberedAreas(blankAreaNode, "blank"), parseNeighborNames(neighborsNode));
                board.add(s);
                setMap.put(s.getName(), s);
            }
        }

        /* ********* Add neighbors as ISet *********** */
        for (String setName: setMap.keySet()) {
            Set s = (Set) setMap.get(setName);
            for (String neighborName: s.getNeighborNames()) {
                s.addNeighbor(setMap.get(neighborName));
            }
        }
        return board;
    }

    private IArea parseArea(Node node) {
        return new Area(getItemValueAsInt(node, "x"),
                getItemValueAsInt(node, "y"),
                getItemValueAsInt(node, "h"),
                getItemValueAsInt(node, "w"));
    }

    private List<BlankArea> parseNumberedAreas(Node node, String itemName) {

        /*
         * Use array instead of a list because we want to add blank areas at specific index in the list/array.
         * That is, blank area numbered 1 should be at index 0, area numbered 4 should be at index 3 (4-1), and so forth.
         */
        List<BlankArea> areas = new ArrayList<>();

        NodeList children = node.getChildNodes();

        for (int k = 0; k < children.getLength(); k++) {
            Node child = children.item(k);
            BlankArea blank = new BlankArea();
            if (itemName.equals(child.getNodeName())) {
                NodeList subChildren = child.getChildNodes();
                for (int i = 0; i < subChildren.getLength(); i++) {
                    Node sub = subChildren.item(i);
                    if ("area".equals(sub.getNodeName())) {
                        blank.setArea(parseArea(sub));
                        areas.add(blank);
                    }
                }
            }
        }
        return areas;
    }

    private List<String> parseNeighborNames(Node node) {
        List<String> names = new ArrayList<>();

        NodeList children = node.getChildNodes();

        for (int k = 0; k < children.getLength(); k++) {

            Node child = children.item(k);

            if ("neighbor".equals(child.getNodeName())) {
                names.add(getItemValueAsString(child, "name"));
            }
        }
        return names;
    }

    /**
     * Helper function to get item value as Integer
     *
     * @param node
     * @param item
     * @return
     */
    private int getItemValueAsInt(Node node, String item) {
        return Integer.parseInt(node.getAttributes().getNamedItem(item).getNodeValue());
    }

    /**
     * Helper function to get item value
     *
     * @param node
     * @param item
     * @return
     */
    private String getItemValueAsString(Node node, String item) {
        return node.getAttributes().getNamedItem(item).getNodeValue();
    }

}
