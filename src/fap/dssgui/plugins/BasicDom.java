package fap.dssgui.plugins;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BasicDom {
    // Parses an XML file and returns a DOM document.
    // If validating is true, the contents is validated against the DTD
    // specified in the file.

    public static Document parseXmlFile(URI filename, boolean validating) {
        try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validating);

            // Create the builder and parse the file
            File file = new File(filename);
            Document doc = factory.newDocumentBuilder().parse(file);
            return doc;
        } catch (SAXException e) {
            // A parsing error occurred; the xml input is not valid
            Logger.getLogger(BasicDom.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParserConfigurationException e) {
            Logger.getLogger(BasicDom.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(BasicDom.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
