package marytts.data;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.sound.sampled.AudioInputStream;

import marytts.data.item.linguistic.Paragraph;
import marytts.data.item.linguistic.Word;
import marytts.data.item.linguistic.Sentence;
import marytts.data.item.prosody.Phrase;

import marytts.data.item.Item;

/**
 *
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class Utterance
{
    private String m_voice_name;
	private String m_text;
    private Locale m_locale;
    private ArrayList<AudioInputStream> m_list_streams;
    private Hashtable<SupportedSequenceType, Sequence<? extends Item>> m_sequences;
    private RelationGraph m_relation_graph;

    public Utterance(String text, Locale locale)
    {
        setVoice(null);
        setText(text);
        setLocale(locale);

        m_sequences = new Hashtable<SupportedSequenceType, Sequence<? extends Item>>();
        m_relation_graph = new RelationGraph();
    }

    public String getText()
    {
        return m_text;
    }


    /**
     *  FIXME: authorized just for the serializer for now... however needs to be more robust
     *
     */
    public void setText(String text)
    {
        m_text = text;
    }

    public Locale getLocale()
    {
        return m_locale;
    }

    protected void setLocale(Locale locale)
    {
        m_locale = locale;
    }

    public String getVoiceName()
    {
        return m_voice_name;
    }

    public void setVoice(String voice_name)
    {
        m_voice_name = voice_name;
    }

    public Sequence<Phrase> getAllPhrases()
    {
        return (Sequence<Phrase>) getSequence(SupportedSequenceType.PHRASE);
    }

    public Sequence<Word> getAllWords()
    {
        return (Sequence<Word>) getSequence(SupportedSequenceType.WORD);
    }

    /**
     * Adding a sequence. If the label is already existing, the corresponding sequence is replaced
     *
     *  @param type the type of the sequence
     *  @param sequence the sequence
     */
    public void addSequence(SupportedSequenceType type, Sequence<? extends Item> sequence)
    {
        m_sequences.put(type, sequence);
    }

    public Sequence<? extends Item> getSequence(SupportedSequenceType type)
    {
        if (m_sequences.containsKey(type))
            return m_sequences.get(type);

        return new Sequence<Item>();
    }

    /**
     * FIXME: not really efficient right now
     *
     */
    public Relation getRelation(SupportedSequenceType source, SupportedSequenceType target)
    {
        return m_relation_graph.getRelation(getSequence(source), getSequence(target));
    }

    public Relation getRelation(Sequence<? extends Item> source, Sequence<? extends Item> target)
    {
        return m_relation_graph.getRelation(source, target);
    }

    /**
     * FIXME: have to check !
     */
    public void setRelation(SupportedSequenceType source, SupportedSequenceType target, Relation rel)
    {
        m_relation_graph.addRelation(rel);
    }
}
