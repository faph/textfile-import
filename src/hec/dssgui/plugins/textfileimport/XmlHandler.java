/* 
 * The MIT License
 *
 * Copyright 2008-2019 Florenz A. P. Hollebrandse.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hec.dssgui.plugins.textfileimport;

import hec.dssgui.plugins.textfileimport.BasicDom;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XmlHandler {

    private String xmlFileName;
    private Document xmlDom;
    private XPath xpath;

    public XmlHandler(String xmlFileName) {
        this.xmlFileName = xmlFileName;

        try {
            URI path = this.getClass().getResource("/" + xmlFileName).toURI();
            xmlDom = BasicDom.parseXmlFile(path, false);
            XPathFactory xpathfactory = XPathFactory.newInstance();
            xpath = xpathfactory.newXPath();
        } catch (Exception e) {
            Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void pushToCombo(String path, JComboBox comboBox) {
        int itemIndex;
        String itemTitle;
        String[] itemsList;

        itemsList = getNodeListTextValue(path);

        comboBox.removeAllItems();
        for (itemIndex = 0; itemIndex < itemsList.length; itemIndex++) {
            itemTitle = itemsList[itemIndex];
            comboBox.addItem(itemTitle);
        }
    }

    private String[] getNodesList(String parentNode) {
        List nodesList = new ArrayList();
        String result[];

        try {
            // Get the matching elements
            XPathExpression expr = xpath.compile(parentNode);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.NODESET);
            NodeList nodelist = (NodeList) resultObj;

            // Process the elements in the nodelist
            for (int i = 0; i < nodelist.getLength(); i++) {
                // Get element
                Element elem = (Element) nodelist.item(i);
                Text text1 = (Text) elem.getFirstChild();
                nodesList.add(text1.getData());
            }
        } catch (Exception e) {
        }

        int nodesCount = nodesList.size();
        result = new String[nodesCount];
        for (int nodeIndex = 0; nodeIndex < nodesCount; nodeIndex++) {
            result[nodeIndex] = ((String) nodesList.get(nodeIndex));
        }
        return result;
    }

    public String getNodeTextValue(String path) {
        String result;
        try {
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.STRING);
            result = (String) resultObj;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null, ex);
            result = "";
        }
        return result;
    }

    public int getNodeIntValue(String path) {
        int result;
        try {
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.NUMBER);
            result = ((Double) resultObj).intValue();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null, ex);
            result = 0;
        }
        return result;
    }

    public double getNodeDoubleValue(String path) {
        double result;
        try {
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.NUMBER);
            result = (Double) resultObj;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null, ex);
            result = 0;
        }
        return result;
    }

    public boolean getNodeBooleanValue(String path, boolean defaultValue) {
        boolean result = defaultValue;
        try {
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.BOOLEAN);
            result = (Boolean) resultObj;
        } catch (Exception e) {
        }
        return result;
    }

    public int getNodeCount(String path) {
        int result;

        try {
            // Get the matching elements
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.NODESET);
            NodeList nodelist = (NodeList) resultObj;
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
        } catch (XPathExpressionException e) {
            result = 0;
        }

        return result;
    }

    public String[] getNodeListTextValue(String path) {
        String[] result;

        try {
            // Get the matching elements
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.NODESET);
            NodeList nodelist = (NodeList) resultObj;
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
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        return result;
    }

    public boolean isNodeExists(String path) {
        boolean result = false;
        try {
            XPathExpression expr = xpath.compile(path);
            Object resultObj = expr.evaluate(xmlDom, XPathConstants.NODESET);
            NodeList nodelist = (NodeList) resultObj;
            if (nodelist.getLength() > 0) {
                result = true;
            }
        } catch (XPathExpressionException e) {
            result = false;
        }
        return result;
    }
}
