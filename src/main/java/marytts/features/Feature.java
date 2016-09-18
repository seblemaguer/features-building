package marytts.features;

import marytts.data.item.Item;

/**
 * For now just extend string but technically should be a little bit more accurate
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class Feature extends Item
{
    String m_value;
    public Feature(String value)
    {
        m_value = value;
    }

    public String getValue()
    {
        return m_value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Feature))
            return false;

        return (getValue() == ((Feature) o).getValue());
    }

    public static final Feature UNDEF_FEATURE = new Feature(null);
}
