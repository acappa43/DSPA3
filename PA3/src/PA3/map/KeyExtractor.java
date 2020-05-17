package PA3.map;

public interface KeyExtractor<K, V> {

	K getKey(V value);
}