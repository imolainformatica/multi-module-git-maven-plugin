package it.mfm.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLTestUtil {
    
    /**
     * Metodo che dato un file xml in ingresso, restituisce il numero di entità di nome {@code entity}
     * @param entity - l'entita' di cui si vuole trovare il numero di occorrenze
     * @param xml - il {@link File} xml in cui cercarle
     * @return
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     * @throws XPathExpressionException 
     */
    public static int getEntityNumberFromXml(String entity, File xml) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException{
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xml);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Double count = (Double) xpath.evaluate("count(//"+entity+")", doc, XPathConstants.NUMBER);
        return count.intValue();
    }
}
