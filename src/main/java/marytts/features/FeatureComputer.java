package marytts.features;


import marytts.data.Utterance;
import marytts.data.item.Item;
import java.util.Hashtable;

/**
 *
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class FeatureComputer
{
    private Hashtable<String, String[]> m_features;

    public FeatureComputer()
    {
        m_features = new Hashtable<String, String[]>();
    }

    public void addFeature(String name, String level, String context, String feature)
    {
        m_features.put(name, new String[]{level, context, feature});
    }

    public Feature compute(Utterance utt, Item item, String level, String context, String feature)
    {
        return null;
    }

    public FeatureMap process(Utterance utt, Item item)
    {
        FeatureMap feature_map = new FeatureMap();
        for (String feature_name: m_features.keySet())
        {
            String[] infos = m_features.get(feature_name);
            Feature feature =  compute(utt, item, infos[0], infos[1], infos[2]); // FIXME: using constants instead of hardcoded indexes
            feature_map.put(feature_name, feature);
        }

        return feature_map;
    }
}
