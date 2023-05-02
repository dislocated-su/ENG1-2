package cs.eng1.piazzapanic.utility.saving;

import java.util.HashMap;
import java.util.Map;

/**
 * SerializeableHashmap
 */
public class SerializeableMap<K, V> {

    public K[] keys;
    public V[] values;

    SerializeableMap(Map<K, V> map) {
        keys = (K[]) map.keySet().toArray();
        values = (V[]) map.values().toArray();
    }

    public SerializeableMap() {}

    public Map<K, V> get() {
        Map<K, V> output = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            output.put(keys[i], values[i]);
        }
        return output;
    }
}
