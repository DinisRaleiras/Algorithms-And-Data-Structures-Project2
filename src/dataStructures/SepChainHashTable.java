package dataStructures;  

/**
 * Separate Chaining Hash table implementation
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key, must extend comparable
 * @param <V> Generic Value 
 */

public class SepChainHashTable<K extends Comparable<K>, V> 
    extends HashTable<K,V> 
{ 
	/**
	 * Serial Version UID of the Class.
	 */
    static final long serialVersionUID = 0L;

	/**
	 * The array of dictionaries.
	 */
    protected Dictionary<K,V>[] table;


    /**
     * Constructor of an empty separate chaining hash table,
     * with the specified initial capacity.
     * Each position of the array is initialized to a new ordered list
     * maxSize is initialized to the capacity.
     * @param capacity defines the table capacity.
     */
    @SuppressWarnings("unchecked")
    public SepChainHashTable( int capacity )
    {
        int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
        // Compiler gives a warning.
        table = (Dictionary<K,V>[]) new Dictionary[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = new OrderedDoubleList<K,V>();
        maxSize = capacity;
        currentSize = 0;
    }                                      


    public SepChainHashTable( )
    {
        this(DEFAULT_CAPACITY);
    }                                                                

    /**
     * Returns the hash value of the specified key.
     * @param key to be encoded
     * @return hash value of the specified key
     */
    protected int hash( K key )
    {
        return Math.abs( key.hashCode() ) % table.length;
    }
    
    @SuppressWarnings("unchecked")
	protected void rehash() {
    	int capacity = maxSize * 2;
    	int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
    	Dictionary<K,V>[] newTable = (Dictionary<K,V>[]) new Dictionary[arraySize];
    	
    	for ( int i = 0; i < arraySize; i++ )
    		newTable[i] = new OrderedDoubleList<K,V>();
    	
    	Iterator<Entry<K,V>> it = this.iterator();
    	
    	table = newTable;
    	maxSize = capacity;
    	currentSize = 0;
    	
    	while(it.hasNext()) {
    		Entry<K,V> entry = it.next();
    		this.insert(entry.getKey(), entry.getValue());
    	}
    }

    @Override
    public V find( K key )
    {
        return table[ this.hash(key) ].find(key);
    }

    @Override
    public V insert( K key, V value )
    {
        if ( this.isFull() )
            this.rehash();
        
        V oldValue = table[this.hash(key)].insert(key, value);
        if(oldValue == null) {
        	currentSize++;
        }
        return oldValue;
    }

    @Override
    public V remove( K key )
    {
    	V value = table[this.hash(key)].remove(key);
    	if(value != null) {
    		currentSize--;
    	}
        return value;
    }

    @Override
    public Iterator<Entry<K,V>> iterator( )
    {
        return new SepChainHashTableIterator<>(table);
    }

    /**
     * Writes the contents of the hash table. We rewrite this method due to serialization problems
     * @param s
     * @throws java.io.IOException
     */
    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
    	s.writeInt(maxSize);
    	Iterator<Entry<K,V>> it = this.iterator();
    	s.writeInt(currentSize);
    	while(it.hasNext()) {
    		Entry<K,V> entry = it.next();
    		s.writeObject(entry.getKey());
    		s.writeObject(entry.getValue());
    	}
    }

    /**
     * Reads the contents of the hash table. We rewrite this method due to serialization problems
     * @param s
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
    	maxSize = s.readInt();
    	int arraySize = HashTable.nextPrime((int) (1.1 * maxSize));
    	table = (Dictionary<K,V>[]) new Dictionary[arraySize];
    	int size = s.readInt();
    	for(int i = 0; i < size; i++) {
    		this.insert((K)s.readObject(), (V)s.readObject());
    	}
    }
}
































