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
	public void generateWordText()
        throws Exception
    {
        String[] list_previous_text_word =
            {"Welcome", "to", "the", "world", "of", "speech", "synthesis", "!"};

        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("text", "marytts.features.featureprocessor.TextFeature");

        //
        int i = 0;
        for (Item item : utt.getSequence(SupportedSequenceType.WORD))
        {
            Feature feat = feat_fact.createFeatureProcessor("text").generate(utt, item);
            Assert.assertEquals(feat.getValue(), list_previous_text_word[i]);
            i++;
        }
    }


    /**
     * Check baseline for german
     *
     */
	@Test
	public void generateContextWordText()
        throws Exception
    {
        String[] list_previous_text_word =
            {null, "Welcome", "to", "the", "world", "of", "speech", "synthesis"};

        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("text", "marytts.features.featureprocessor.TextFeature");

        ContextProcessorFactory ctx_fact = new ContextProcessorFactory();
        ctx_fact.addContextProcessor("previous", "marytts.features.contextprocessor.Previous");

        //
        int i = 0;
        for (Item item : utt.getSequence(SupportedSequenceType.WORD))
        {
            Item prev_item = ctx_fact.createContextProcessor("previous").generate(utt, item);
            if (prev_item != null)
            {
                Feature feat = feat_fact.createFeatureProcessor("text").generate(utt, prev_item);
                Assert.assertEquals(feat.getValue(), list_previous_text_word[i]);
            }
            else
            {
                Assert.assertEquals(prev_item, null);
            }

            i++;
        }
    }


    /**
     * Asserting phone features
     *
     */
	@Test
	public void assertPhoneContext()
        throws Exception
    {
        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("string", "marytts.features.featureprocessor.StringFeature");
        feat_fact.addFeatureProcessor("nb_syl", "marytts.features.featureprocessor.NbFromSyllableStart");

        ContextProcessorFactory ctx_fact = new ContextProcessorFactory();
        ctx_fact.addContextProcessor("previousprevious", "marytts.features.contextprocessor.PreviousPrevious");
        ctx_fact.addContextProcessor("previous", "marytts.features.contextprocessor.Previous");
        ctx_fact.addContextProcessor("current", "marytts.features.contextprocessor.Current");
        ctx_fact.addContextProcessor("next", "marytts.features.contextprocessor.Next");
        ctx_fact.addContextProcessor("nextnext", "marytts.features.contextprocessor.NextNext");

        LevelProcessorFactory lvl_fact = new LevelProcessorFactory();
        lvl_fact.addLevelProcessor("current", "marytts.features.levelprocessor.CurrentLevel");

        // Populate feature computer
        FeatureComputer fc = new FeatureComputer(lvl_fact, ctx_fact, feat_fact);
        fc.addFeature("previousprevious_phone_string", "current", "previousprevious", "string");
        fc.addFeature("previous_phone_string", "current", "previous", "string");
        fc.addFeature("current_phone_string", "current", "current", "string");
        fc.addFeature("next_phone_string", "current", "next", "string");
        fc.addFeature("nextnext_phone_string", "current", "nextnext", "string");
        fc.addFeature("nbfromsylstart", "current", "current", "nb_syl");

        String[] feature_names =
            {"previousprevious_phone_string", "previous_phone_string", "current_phone_string", "next_phone_string", "nextnext_phone_string", "nbfromsylstart"};
        String generated_labels = "";
        for (Item item : utt.getSequence(SupportedSequenceType.PHONE))
        {
            FeatureMap map = fc.process(utt, item);
            for (int i=0; i<feature_names.length; i++)
            {
                String value = map.get(feature_names[i]).getValue();
                if (value == null)
                    generated_labels += "x;";
                else
                    generated_labels += value + ";";
            }
            generated_labels += "\n";
        }


        String original_labels = loadResourceIntoString("welcome_phone.lab");

        Assert.assertEquals(generated_labels, original_labels);
    }



    /**
     * Asserting word features
     *
     */
	@Test
	public void assertSyllableContext()
        throws Exception
    {
        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("nbfromphrase", "marytts.features.featureprocessor.NbFromPhraseStart");
        feat_fact.addFeatureProcessor("nbtophrase", "marytts.features.featureprocessor.NbToPhraseEnd");
        feat_fact.addFeatureProcessor("nbfromword", "marytts.features.featureprocessor.NbFromWordStart");
        feat_fact.addFeatureProcessor("nbtoword", "marytts.features.featureprocessor.NbToWordEnd");
        feat_fact.addFeatureProcessor("nbphones", "marytts.features.featureprocessor.NbPhonesRelated");
        feat_fact.addFeatureProcessor("accented", "marytts.features.featureprocessor.AccentedFeature");
        feat_fact.addFeatureProcessor("string", "marytts.features.featureprocessor.StringFeature");


        ContextProcessorFactory ctx_fact = new ContextProcessorFactory();
        ctx_fact.addContextProcessor("previous", "marytts.features.contextprocessor.Previous");
        ctx_fact.addContextProcessor("current", "marytts.features.contextprocessor.Current");
        ctx_fact.addContextProcessor("next", "marytts.features.contextprocessor.Next");

        LevelProcessorFactory lvl_fact = new LevelProcessorFactory();
        lvl_fact.addLevelProcessor("current", "marytts.features.levelprocessor.CurrentLevel");
        lvl_fact.addLevelProcessor("syllable", "marytts.features.levelprocessor.SyllableLevel");

        // Populate feature computer
        FeatureComputer fc = new FeatureComputer(lvl_fact, ctx_fact, feat_fact);
        fc.addFeature("previous_syl_accented", "syllable", "previous", "accented");
        fc.addFeature("current_syl_accented", "syllable", "current", "accented");
        fc.addFeature("next_syl_accented", "syllable", "next", "accented");
        fc.addFeature("current_syl_nbfromphrase", "current", "current", "nbfromphrase");
        fc.addFeature("current_syl_nbtophrase", "current", "current", "nbtophrase");
        fc.addFeature("current_syl_nbfromword", "syllable", "current", "nbfromword");
        fc.addFeature("current_syl_nbtoword", "current", "current", "nbtoword");
        fc.addFeature("current_syl_nbphones", "syllable", "current", "nbphones");
        fc.addFeature("current_phone", "current", "current", "string");

        String[] feature_names =
            {"current_phone", "previous_syl_accented", "current_syl_accented", "next_syl_accented",
             "current_syl_nbfromphrase", "current_syl_nbtophrase",
             "current_syl_nbfromword", "current_syl_nbtoword",
             "current_syl_nbphones"};
        String generated_labels = "";
        for (Item item : utt.getSequence(SupportedSequenceType.PHONE))
        {
            FeatureMap map = fc.process(utt, item);
            for (int i=0; i<feature_names.length; i++)
            {
                String value = map.get(feature_names[i]).getValue();
                if (value == null)
                    generated_labels += "x;";
                else
                    generated_labels += value + ";";
            }
            generated_labels += "\n";
        }


        String original_labels = loadResourceIntoString("welcome_syl.lab");

        Assert.assertEquals(generated_labels, original_labels);
    }


    /**
     * Asserting word features
     *
     */
	@Test
	public void assertWordFeature()
        throws Exception
    {
        // Loading the XML
        XMLSerializer xml_ser = new XMLSerializer();
        Utterance utt = xml_ser.fromString(loadResourceIntoString("basedoc.xml"));

        // Generate the category
        FeatureProcessorFactory feat_fact = new FeatureProcessorFactory();
        feat_fact.addFeatureProcessor("text", "marytts.features.featureprocessor.TextFeature");
        feat_fact.addFeatureProcessor("pos", "marytts.features.featureprocessor.POSFeature");
        feat_fact.addFeatureProcessor("nbfromphrase", "marytts.features.featureprocessor.NbFromPhraseStart");

        ContextProcessorFactory ctx_fact = new ContextProcessorFactory();
        ctx_fact.addContextProcessor("previous", "marytts.features.contextprocessor.Previous");
        ctx_fact.addContextProcessor("current", "marytts.features.contextprocessor.Current");
        ctx_fact.addContextProcessor("next", "marytts.features.contextprocessor.Next");

        LevelProcessorFactory lvl_fact = new LevelProcessorFactory();
        lvl_fact.addLevelProcessor("word", "marytts.features.levelprocessor.WordLevel");

        // Populate feature computer
        FeatureComputer fc = new FeatureComputer(lvl_fact, ctx_fact, feat_fact);
        fc.addFeature("previous_word_text", "word", "previous", "text");
        fc.addFeature("current_word_text", "word", "current", "text");
        fc.addFeature("next_word_text", "word", "next", "text");
        fc.addFeature("previous_word_pos", "word", "previous", "pos");
        fc.addFeature("current_word_pos", "word", "current", "pos");
        fc.addFeature("next_word_pos", "word", "next", "pos");
        fc.addFeature("previous_word_nbfromphrase", "word", "previous", "nbfromphrase");
        fc.addFeature("current_word_nbfromphrase", "word", "current", "nbfromphrase");
        fc.addFeature("next_word_nbfromphrase", "word", "next", "nbfromphrase");

        String[] feature_names =
            {"previous_word_text", "current_word_text", "next_word_text",
             "previous_word_pos", "current_word_pos", "next_word_pos",
             "previous_word_nbfromphrase", "current_word_nbfromphrase", "next_word_nbfromphrase"};
        String generated_labels = "";
        for (Item item : utt.getSequence(SupportedSequenceType.PHONE))
        {
            FeatureMap map = fc.process(utt, item);
            for (int i=0; i<feature_names.length; i++)
            {
                String value = map.get(feature_names[i]).getValue();
                if (value == null)
                    generated_labels += "x;";
                else
                    generated_labels += value + ";";
            }
            generated_labels += "\n";
        }


        String original_labels = loadResourceIntoString("welcome_word.lab");

        Assert.assertEquals(generated_labels, original_labels);
    }
}
