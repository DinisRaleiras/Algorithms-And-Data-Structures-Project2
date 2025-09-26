package dataStructures;

/**
 * Insensitive Doubly linked list Implementation
 * @version 1.0
 * 
 */
public class InsensitiveOrderedDoubleList<V> extends OrderedDoubleList<String, V>{

	/**
	 * Serial Version UID of the Class
	 */
	static final long serialVersionUID = 0L;
	
	public InsensitiveOrderedDoubleList() {
		super();
	}
	
	/**
     * Returns the node with the Entry with Key key
     * in the list, if the list contains this entry.
     * Otherwise, returns null.
     * @param key - Key of type K to be searched
     * @return DoubleListNode<E> where the Entry with key was found, or the one with the key immediately after 
     */
	protected DoubleListNode<Entry<String,V>> findNode (String key){
		DoubleListNode<Entry<String,V>> currNode = head;
		while(currNode != null && currNode.getElement().getKey().toUpperCase().compareTo(key.toUpperCase()) < 0) {
			currNode = currNode.getNext();
		}
		return currNode;
	}
	
	@Override
	public V find(String key) {
		DoubleListNode<Entry<String,V>> node = findNode(key);
		if(node == null || node.getElement().getKey().toUpperCase().compareTo(key.toUpperCase()) != 0) {
			return null;
		}else {
			return node.getElement().getValue();
		}
	}
	
	@Override
    public V remove(String key) {
		DoubleListNode<Entry<String,V>> node = findNode(key);
		if ((node == null) || (node.getElement().getKey().toUpperCase().compareTo(key.toUpperCase())!=0))
			return null;
		else {
			if(node.getElement().getKey().toUpperCase().compareTo(head.getElement().getKey().toUpperCase()) == 0) {
				this.removeFirst();
			}else if(node.getElement().getKey().toUpperCase().compareTo(tail.getElement().getKey().toUpperCase()) == 0) {
				this.removeLast();
			}else {
				this.removeMiddleNode(node);
			}
			return node.getElement().getValue();
		}
	}
	
	@Override
	public V insert(String key, V value) {
		DoubleListNode<Entry<String,V>> node = findNode(key);
		if ((node!=null) && (node.getElement().getKey().toUpperCase().compareTo(key.toUpperCase())==0)){
			V oldValue = node.getElement().getValue();
			node.getElement().setKey(key);
			node.getElement().setValue(value);
			return oldValue;
		}
		else { 
			Entry<String,V> newNode=new EntryClass<String,V> (key, value);
			if(node == null) {
				this.addLast(newNode);
			}else if(node.getElement().getKey().toUpperCase().compareTo(head.getElement().getKey().toUpperCase()) == 0) {
				this.addFirst(newNode);
			}else {
				this.addBeforeNode(newNode, node);
			}
			return null;
		}
	}
}
