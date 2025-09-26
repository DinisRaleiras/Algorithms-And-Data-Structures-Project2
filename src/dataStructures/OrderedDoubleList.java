package dataStructures;


/**
 * Doubly linked list Implementation 
 * @author AED  Team
 * @version 1.0
 * @param <K, V> Generics - K extends Comparable
 * 
 */
class OrderedDoubleList<K extends Comparable<K>, V> 
	implements OrderedDictionary<K , V> {

    /**
	 * Serial Version UID of the Class
	 */
    static final long serialVersionUID = 0L;
    
	/**
     *  Node at the head of the list.
     */
	protected DoubleListNode<Entry<K,V>> head;

    /**
     * Node at the tail of the list.
     */
	protected DoubleListNode<Entry<K,V>> tail;

    /**
     * Number of elements in the list.
     */
	protected int currentSize;
	
    /**
     * Constructor of an empty ordered double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
	public OrderedDoubleList() {
		head=null;
		tail=null;
		currentSize=0;
	}

    /**
     * Inserts the Entry element before node after.
     * Precondition: after is not the head of the ordered double list.
     * @param element - Entry to be inserted
     * @param after - Node to be next to the new node  
     */
	protected void addBeforeNode(Entry<K,V> element, DoubleListNode<Entry<K,V>> after){
		DoubleListNode<Entry<K,V>> before = after.getPrevious();
		DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(element, before, after);
		before.setNext(newNode);
		after.setPrevious(newNode);
		currentSize++;
	}
	
    /**
     * Inserts the Entry element at the first position in the list.
     * @param element - Entry to be inserted
     */
    protected void addFirst( Entry<K,V> element )
    {
        DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(element, null, head);
        if ( this.isEmpty() )
            tail = newNode;
        else
            head.setPrevious(newNode);
        head = newNode;
        currentSize++;
    }


    /**
     * Inserts the Entry element at the last position in the list.
     * @param element - Entry to be inserted
     */
    protected void addLast( Entry<K,V> element )
    {
    	 DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(element, tail, null);
         if ( this.isEmpty() )
             head = newNode;
         else
             tail.setNext(newNode);
         tail = newNode;
         currentSize++;
    }

	@Override
    public Entry<K, V> maxEntry() throws EmptyDictionaryException {
		if( this.isEmpty())
			throw new EmptyDictionaryException();
        return tail.getElement();
	}

    @Override
	public Entry<K, V> minEntry() throws EmptyDictionaryException {
    	if( this.isEmpty())
			throw new EmptyDictionaryException();
        return head.getElement();
	}

    /**
     * Returns the node with the Entry with Key key
     * in the list, if the list contains this entry.
     * Otherwise, returns null.
     * @param key - Key of type K to be searched
     * @return DoubleListNode<E> where the Entry with key was found, or the one with the key immediately after 
     */
	protected DoubleListNode<Entry<K,V>> findNode (K key){
		DoubleListNode<Entry<K,V>> currNode = head;
		while(currNode != null && currNode.getElement().getKey().compareTo(key) < 0) {
			currNode = currNode.getNext();
		}
		return currNode;
	}
	
    @Override
	public V find(K key) {
		DoubleListNode<Entry<K,V>> node = findNode(key);
		if(node == null || node.getElement().getKey().compareTo(key) != 0) {
			return null;
		}else {
			return node.getElement().getValue();
		}
	}


	@Override
	public V insert(K key, V value) {
		DoubleListNode<Entry<K,V>> node = findNode(key);
		if ((node!=null) && (node.getElement().getKey().compareTo(key)==0)){
			V oldValue = node.getElement().getValue();
			node.getElement().setKey(key);
			node.getElement().setValue(value);
			return oldValue;
		}
		else { 
			Entry<K,V> newNode=new EntryClass<K,V> (key, value);
			if(node == null) {
				this.addLast(newNode);
			}else if(node.getElement().getKey().compareTo(head.getElement().getKey()) == 0) {
				this.addFirst(newNode);
			}else {
				this.addBeforeNode(newNode, node);
			}
			return null;
		}
	}
	
	@Override
    public boolean isEmpty() {
	
		return currentSize==0;
	}

    @Override
	public Iterator<Entry<K, V>> iterator() {
		return new DoubleListIterator<Entry<K,V>>(head,tail);
	}

    /**
     * Removes the first node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeFirstNode( )
    {
        head = head.getNext();
        if ( head == null )
            tail = null;
        else
            head.setPrevious(null);
        currentSize--;
    }


    /**
     * Removes and returns the value at the first entry in the list.
     */
    protected V removeFirst( ) throws EmptyDictionaryException
    {
        if( this.isEmpty() )
        	throw new EmptyDictionaryException();
        DoubleListNode<Entry<K,V>> node = head;
        this.removeFirstNode();
    	return node.getElement().getValue();
    }


    /**
     * Removes the last node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeLastNode( )
    {
    	tail = tail.getPrevious();
        if ( tail == null )
            head = null;
        else
            tail.setNext(null);
        currentSize--;
    }


    /**
     * Removes and returns the value at the last entry in the list.
     */
    protected V removeLast( ) throws EmptyDictionaryException
    {
        if ( this.isEmpty() )
            throw new EmptyDictionaryException();

        V value = tail.getElement().getValue();
        this.removeLastNode();
        return value;
    }

    /**
     * Removes the specified node from the list.
     * Pre-condition: the node is neither the head nor the tail of the list.
     * @param node - middle node to be removed
     */
    protected void removeMiddleNode( DoubleListNode<Entry<K,V>> node )
    {
    	DoubleListNode<Entry<K,V>> before = node.getPrevious();
    	DoubleListNode<Entry<K,V>> after = node.getNext();
    	before.setNext(after);
    	after.setPrevious(before);

    }

    @Override
    public V remove(K key) {
		DoubleListNode<Entry<K,V>> node = findNode(key);
		if ((node == null) || (node.getElement().getKey().compareTo(key)!=0))
			return null;
		else {
			if(node.getElement().getKey().compareTo(head.getElement().getKey()) == 0) {
				this.removeFirst();
			}else if(node.getElement().getKey().compareTo(tail.getElement().getKey()) == 0) {
				this.removeLast();
			}else {
				this.removeMiddleNode(node);
			}
			return node.getElement().getValue();
		}
	}

    @Override
	public int size() {
		return currentSize;
	}

	/**
	 * Writes the object to the output stream. We rewrite this method due to serialization problems
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
    	s.writeInt(currentSize);
    	for(DoubleListNode<Entry<K,V>> x = head; x != null; x = x.getNext()) {
    		s.writeObject(x.getElement().getKey());
    		s.writeObject(x.getElement().getValue());
    	}
    }
    
    @SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
    	int size = s.readInt();
    	for(int i = 0; i < size; i++) {
    		this.insert((K)s.readObject(), (V)s.readObject());
    	}
    }
	
	
}
