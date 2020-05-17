package PA3.hashtable;

import PA3.map.Map;


public interface HashTableFactory<K, V> {

	Map<K, V> getInstance(int initialCapacity);

}