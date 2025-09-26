package dataStructures;

public class InsensitiveSepChainHashTable<V> extends SepChainHashTable<String, V> {
	private static final long serialVersionUID = 0L;
	
	
	@SuppressWarnings("unchecked")
	public InsensitiveSepChainHashTable( int capacity ) {
		int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
        // Compiler gives a warning.
        table = (Dictionary<String,V>[]) new Dictionary[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = new InsensitiveOrderedDoubleList<V>();
        maxSize = capacity;
        currentSize = 0;
	}
	
	
	public InsensitiveSepChainHashTable( )
    {
        this(DEFAULT_CAPACITY);
    }

	/**
     * Returns the hash value of the specified key.
     * @param key to be encoded
     * @return hash value of the specified key
     */
    protected int hash( String key )
    {
        return Math.abs( key.toUpperCase().hashCode() ) % table.length;
    }
    
    @SuppressWarnings("unchecked")
	protected void rehash() {
    	int capacity = maxSize * 2;
    	int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
    	Dictionary<String,V>[] newTable = (Dictionary<String,V>[]) new Dictionary[arraySize];
    	
    	for ( int i = 0; i < arraySize; i++ )
    		newTable[i] = new InsensitiveOrderedDoubleList<V>();
    	
    	Iterator<Entry<String,V>> it = this.iterator();
    	
    	table = newTable;
    	maxSize = capacity;
    	currentSize = 0;
    	
    	while(it.hasNext()) {
    		Entry<String,V> entry = it.next();
    		this.insert(entry.getKey(), entry.getValue());
    	}
    }
}
