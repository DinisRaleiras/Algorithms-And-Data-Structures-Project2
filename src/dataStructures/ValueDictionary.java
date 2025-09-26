package dataStructures;


public interface ValueDictionary<K extends Comparable<K>,V extends Comparable<V>> extends Dictionary<K,V>{

	V remove(K key, V value);

}
