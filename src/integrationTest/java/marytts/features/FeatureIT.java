package marytts.features;

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



/**
 * Test class for the main hts label generation module
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class FeatureIT
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
     * Check baseline for german
     *
     */
	@Test
	public void loadXML()
        throws Exception
    {
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        Assert.assertNotNull(utt);
    }

    /**
     * Check baseline for german
     *
     */
	@Test
	public void generateWordText()
        throws Exception
    {
        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("text", "marytts.features.featureprocessor.Text");

        //
        for (Item item : utt.getSequence(SupportedSequenceType.WORD))
        {
            Feature feat = feat_fact.createFeatureProcessor("text").generate(utt, item);
            System.out.println("val = " + feat.getValue());
        }

        Assert.assertTrue(false);
    }


    /**
     * Check baseline for german
     *
     */
	@Test
	public void generateContextWordText()
        throws Exception
    {
        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("text", "marytts.features.featureprocessor.Text");

        ContextProcessorFactory ctx_fact = new ContextProcessorFactory();
        ctx_fact.addContextProcessor("previous", "marytts.features.contextprocessor.Previous");

        //
        for (Item item : utt.getSequence(SupportedSequenceType.WORD))
        {
            Item prev_item = ctx_fact.createContextProcessor("previous").generate(utt, item);
            if (prev_item != null)
            {
                Feature feat = feat_fact.createFeatureProcessor("text").generate(utt, prev_item);
                System.out.println("val2 = " + feat.getValue());
            }
            else
            {
                System.out.println("val2 = null");
            }
        }

        Assert.assertTrue(false);
    }


    /**
     * Check baseline for german
     *
     */
	@Test
	public void testFeatureComputer()
        throws Exception
    {
        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("text", "marytts.features.featureprocessor.Text");

        ContextProcessorFactory ctx_fact = new ContextProcessorFactory();
        ctx_fact.addContextProcessor("previous", "marytts.features.contextprocessor.Previous");
        ctx_fact.addContextProcessor("current", "marytts.features.contextprocessor.Current");
        ctx_fact.addContextProcessor("next", "marytts.features.contextprocessor.Next");

        LevelProcessorFactory lvl_fact = new LevelProcessorFactory();
        lvl_fact.addLevelProcessor("current", "marytts.features.levelprocessor.CurrentLevel");
        lvl_fact.addLevelProcessor("word", "marytts.features.levelprocessor.WordLevel");

        // Populate feature computer
        FeatureComputer fc = new FeatureComputer(lvl_fact, ctx_fact, feat_fact);
        fc.addFeature("previous_word_text", "word", "previous", "text");
        fc.addFeature("current_word_text", "word", "current", "text");
        fc.addFeature("next_word_text", "word", "next", "text");

        //
        int i = 0;
        for (Item item : utt.getSequence(SupportedSequenceType.SYLLABLE))
        {
            System.out.println("item_idx = " + i);
            FeatureMap map = fc.process(utt, item);
            System.out.print("item " + i + " = {");
            for (String key: map.keySet())
            {
                System.out.print(key + ": " + map.get(key).getValue() + ", ");
            }
            System.out.println("}");
            i++;
        }

        Assert.assertTrue(false);
    }
}
