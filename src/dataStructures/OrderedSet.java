package dataStructures;


public interface OrderedSet<E extends Comparable<E>> extends Set<E>{
	
	/**
     * Returns the entry with the smallest key in the dictionary.
     *
     * @return
     * @throws EmptyDictionaryException
     */
    E minEntry( ) throws EmptySetException;

    /**
     * Returns the entry with the largest key in the dictionary.
     *
     * @return
     * @throws EmptyDictionaryException
     */
    E maxEntry( ) throws EmptySetException;
}
