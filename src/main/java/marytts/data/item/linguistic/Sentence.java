package marytts.data.item.linguistic;

import java.util.ArrayList;

import marytts.data.item.Item;
import marytts.data.item.prosody.Phrase;

/**
 * Class which represents a sentence
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class Sentence extends Item
{
	private String m_text; /*< For now the sentence is just containing a text */

    /**
     *  Constructor of the paragrapj
     *
     *  @param text : the text of the sentence
     */
	public Sentence(String text)
	{
        super();
		setText(text);
	}

    /**
     *  Getter of the text of the Sentence
     *
     *  @return the text of the sentence
     */
	public String getText()
	{
		return m_text;
	}

    /**
     *  Setter of the text of the Sentence
     *
     *  @param the new text of the Sentence
     */
	protected void setText(String text)
	{
		m_text = text;
	}
}
