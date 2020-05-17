package PA3.map;

public class LinkedListMapFactory<K, V> implements MapFactory<K, V> {

	@Override
	public Map<K, V> getInstance(KeyExtractor<K, V> extractor) {
		return new LinkedListMap<K, V>(extractor);
	}

}