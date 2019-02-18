package fap.dssgui.plugins;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BasicDom {
//        public static void main(String[] args) {
//            Document doc = parseXmlFile("infilename.xml", false);
//        }
   // Parses an XML file and returns a DOM document.
   // If validating is true, the contents is validated against the DTD
   // specified in the file.
   public static Document parseXmlFile(String filename, boolean validating) {
      try {
         // Create a builder factory
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setValidating(validating);

         // Create the builder and parse the file
         Document doc = factory.newDocumentBuilder().parse(new File(filename));
         return doc;
      } catch (SAXException e) {
      // A parsing error occurred; the xml input is not valid
      } catch (ParserConfigurationException e) {
      } catch (IOException e) {
      }
      return null;
   }
}
