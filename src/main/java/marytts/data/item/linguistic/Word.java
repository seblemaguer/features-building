package marytts.data.item.linguistic;

import marytts.data.item.phonology.Syllable;
import marytts.data.item.phonology.Phoneme;
import marytts.data.item.phonology.Accent;

import marytts.data.item.Item;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Class which represents a word
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class Word extends Item
{
	private String m_POS; /*< The POS tag of the word */
	private String m_text; /*< The text of the word */
    private String m_sounds_like; /*< */
	private String m_g2p_method;
    private Locale m_alternative_locale;
    private Accent m_accent; /*< The accent information */

	public Word(String text)
    {
        super();
        setText(text);
        setAlternativeLocale(null);
        soundsLike(null);
        setG2PMethod(null);
        setAccent(null);
    }

    public Word(String text, Locale alternative_locale)
	{
        super();
		setText(text);
        setAlternativeLocale(alternative_locale);
        soundsLike(null);
        setG2PMethod(null);
        setAccent(null);
    }

	public Word(String text, String sounds_like)
	{
        super();
		setText(text);
        setAlternativeLocale(null);
        setG2PMethod(null);
        soundsLike(sounds_like);
        setAccent(null);
    }

    /***************************************************************************************
     ** Getters / Setters
     ***************************************************************************************/
    public String getPOS()
    {
        return m_POS;
    }

    public void setPOS(String POS)
    {
        m_POS = POS;
    }

    public String getText()
    {
        return m_text;
    }

    public void setText(String text)
    {
        m_text = text;
    }

    public Locale getAlternativeLocale()
    {
        return m_alternative_locale;
    }

    public void setAlternativeLocale(Locale alternative_locale)
    {
        m_alternative_locale = alternative_locale;
    }

    public String getG2PMethod()
    {
        return m_g2p_method;
    }

    public void setG2PMethod(String g2p_method)
    {
        m_g2p_method = g2p_method;
    }


    public String soundsLike()
    {
        return m_sounds_like;
    }

    public void soundsLike(String sounds_like)
    {
        m_sounds_like = sounds_like;
    }

    public Accent getAccent()
    {
        return m_accent;
    }

    public void setAccent(Accent accent)
    {
        m_accent = accent;
    }
}
