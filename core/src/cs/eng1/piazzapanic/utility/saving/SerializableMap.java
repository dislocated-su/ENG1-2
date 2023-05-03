package cs.eng1.piazzapanic.utility.saving;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple container for a Map<K,V> that stores the values as arrays for
 * serialisation.
 * 
 * @param <K> the key type of the Map
 * @param <V> the value type of the Map
 *
 * @author Andrey Samoilov
 */
public class SerializableMap<K, V> {

    public K[] keys;
    public V[] values;

    /**
     * Convert a map into a serializable map
     */
    SerializableMap(Map<K, V> map) {
        keys = (K[]) map.keySet().toArray();
        values = (V[]) map.values().toArray();
    }

    public SerializableMap() {
    }

    /**
     * Construct a map from Serializable map
     */
    public Map<K, V> get() {
        Map<K, V> output = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            output.put(keys[i], values[i]);
        }
        return output;
    }
}
