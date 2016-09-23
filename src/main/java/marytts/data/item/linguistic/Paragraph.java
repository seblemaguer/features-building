package marytts.data.item.linguistic;

import java.util.ArrayList;

import marytts.data.item.Item;

/**
 * Class which represents a paragraph
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class Paragraph extends Item
{
	private String m_text; /*< For now the paragraph is just containing a text */

    /**
     *  Constructor of the paragrapj
     *
     *  @param text : the text of the paragraph
     */
	public Paragraph(String text)
	{
        super();
        setText(text);
    }

    /**
     *  Getter of the text of the Paragraph
     *
     *  @return the text of the paragraph
     */
    public String getText()
	{
		return m_text;
	}

    /**
     *  Setter of the text of the Paragraph
     *
     *  @param the new text of the Paragraph
     */
	protected void setText(String text)
	{
		m_text = text;
	}
}
