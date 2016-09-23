package marytts.io;

import java.util.ArrayList;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/* testng part */
import org.testng.Assert;
import org.testng.annotations.*;

/* Marytts needed packages */
import marytts.data.Utterance;
import marytts.data.Sequence;
import marytts.data.SupportedSequenceType;
import marytts.data.item.Item;
import marytts.io.XMLSerializer;
import marytts.features.FeatureProcessorFactory;
import marytts.features.ContextProcessorFactory;
import marytts.features.LevelProcessorFactory;
import marytts.features.Feature;
import marytts.features.FeatureComputer;

/* MaryData needed packages */
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import marytts.util.dom.MaryEntityResolver;
import marytts.util.string.StringUtils;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XMLAssert;

/**
 * Test class for the main hts label generation module
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class XMLSerializerIT
{

	protected String loadResourceIntoString(String resourceName)
        throws IOException
    {
		BufferedReader br = new BufferedReader
            (new InputStreamReader
             (this.getClass().getResourceAsStream(resourceName), "UTF-8"));
		StringBuilder buf = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			buf.append(line);
			buf.append("\n");
		}
		return buf.toString();
	}


    /**
     * Check load/save xml
     *
     */
	@Test
	public void loadSaveXML()
        throws Exception
    {
        XMLSerializer xml_ser = new XMLSerializer();
        String doc_str = loadResourceIntoString("basedoc.xml");
        doc_str.replaceAll(">\\s*<", "><");

        // 1. generate the doc from the string
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setExpandEntityReferences(false);
		factory.setNamespaceAware(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new MaryEntityResolver());

        doc_str = StringUtils.purgeNonBreakingSpaces(doc_str);
        Document doc1 = builder.parse(new InputSource(new StringReader(doc_str)));
        doc1.normalizeDocument();

        Utterance utt = xml_ser.fromString(doc_str);
        Assert.assertNotNull(utt);

        Document doc2 = builder.parse(new InputSource(new StringReader(xml_ser.toString(utt))));
        doc2.normalizeDocument();

        System.out.println("doc 1 = ");
        System.out.println(doc_str);
        System.out.println("\n\ndoc 2 = ");
        System.out.println(xml_ser.toString(utt));

        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(doc1, doc2);
    }
}
