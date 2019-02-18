package fap.dssgui.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlHandler {

    private String xmlFileName;
    private Document xmlDom;

    public XmlHandler(String xmlFileName) {
        this.xmlFileName = xmlFileName;

        try {
            java.net.URL url = this.getClass().getResource("/" + xmlFileName);
            xmlDom = BasicDom.parseXmlFile(url.getPath(), false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void pushToCombo(String xPath, JComboBox comboBox) {
        int itemIndex;
        String itemTitle;
        String[] itemsList;

        itemsList = getNodeListTextValue(xPath);

        comboBox.removeAllItems();
        for (itemIndex = 0; itemIndex < itemsList.length; itemIndex++) {
            itemTitle = itemsList[itemIndex];
            comboBox.addItem(itemTitle);
        }
    }

    private String[] getNodesList(String parentNode) {
        String xpath;
        List nodesList = new ArrayList();
        String result[];

        xpath = parentNode;
        try {
            // Get the matching elements
            NodeList nodelist = org.apache.xpath.XPathAPI.selectNodeList(xmlDom, xpath);

            // Process the elements in the nodelist
            for (int i = 0; i < nodelist.getLength(); i++) {
                // Get element
                Element elem = (Element) nodelist.item(i);
                Text text1 = (Text) elem.getFirstChild();
                nodesList.add(text1.getData());
            }
        } catch (Exception e) {
        }

        if (nodesList != null) {
            int nodesCount = nodesList.size();
            result = new String[nodesCount];
            for (int nodeIndex = 0; nodeIndex < nodesCount; nodeIndex++) {
                result[nodeIndex] = ((String) nodesList.get(nodeIndex));
            }
            return result;
        } else {
            return null;
        }
    }

    public String getNodeTextValue(String xPath) {
        String result = "";
        try {
            Node node = org.apache.xpath.XPathAPI.selectSingleNode(xmlDom, xPath);
            Element elem = (Element) node;
            Text text1 = (Text) elem.getFirstChild();
            result = text1.getData();
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public int getNodeIntValue(String xPath) {
        int result = 0;
        try {
            Node node = org.apache.xpath.XPathAPI.selectSingleNode(xmlDom, xPath);
            Element elem = (Element) node;
            Text text1 = (Text) elem.getFirstChild();
            result = Integer.parseInt(text1.getData());
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public double getNodeDoubleValue(String xPath) {
        double result = 0;
        try {
            Node node = org.apache.xpath.XPathAPI.selectSingleNode(xmlDom, xPath);
            Element elem = (Element) node;
            Text text1 = (Text) elem.getFirstChild();
            result = Double.parseDouble(text1.getData());
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public boolean getNodeBooleanValue(String xPath, boolean defaultValue) {
        boolean result = defaultValue;
        try {
            Node node = org.apache.xpath.XPathAPI.selectSingleNode(xmlDom, xPath);
            Element elem = (Element) node;
            Text text1 = (Text) elem.getFirstChild();
            String string1 = text1.getData();
            if (string1.equalsIgnoreCase("TRUE")) {
                result = true;
            } else if (string1.equalsIgnoreCase("FALSE")) {
                result = false;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public int getNodeCount(String xpath) {
        int result;

        try {
            // Get the matching elements
            NodeList nodelist = org.apache.xpath.XPathAPI.selectNodeList(xmlDom, xpath);
            result = nodelist.getLength();

            // Process the elements in the nodelist to check if there are actual values in the nodes
            for (int i = 0; i < nodelist.getLength(); i++) {
                // Get element
                Element elem = (Element) nodelist.item(i);
                Text text1 = (Text) elem.getFirstChild();
                if (text1.getData().length() == 0) {
                    result -= 1; //reduce node count by 1 if no data found!
                }
            }
        } catch (javax.xml.transform.TransformerException e) {
            result = 0;
        }

        return result;
    }

    public String[] getNodeListTextValue(String xpath) {
        String[] result;

        try {
            // Get the matching elements
            NodeList nodelist = org.apache.xpath.XPathAPI.selectNodeList(xmlDom, xpath);
            int nodeCount = nodelist.getLength();
            result = new String[nodeCount];
            // Process the elements in the nodelist
            for (int i = 0; i < nodelist.getLength(); i++) {
                // Get element
                Element elem = (Element) nodelist.item(i);
                Text text1 = (Text) elem.getFirstChild();
                String string1 = text1.getData();
                result[i] = string1;
            }
        } catch (javax.xml.transform.TransformerException e) {
            result = null;
        }

        return result;
    }

    public boolean isNodeExists(String xPath) {
        boolean result = false;
        try {
            NodeList nodelist = org.apache.xpath.XPathAPI.selectNodeList(xmlDom, xPath);
            if (nodelist.getLength() > 0) {
                result = true;
            }
        } catch (javax.xml.transform.TransformerException e) {
            result = false;
        }
        return result;
    }
}
