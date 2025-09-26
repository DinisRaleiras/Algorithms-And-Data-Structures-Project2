package dataStructures;

public class AVLTreeByValue<K extends Comparable<K>, V extends Comparable<V> > extends AVLTree<K, V> implements ValueDictionary<K,V>{

    // Serial Version UID
    private static final long serialVersionUID = 0L;

    public AVLTreeByValue(){
        super();
    }

    /**
     * Compares two entries by their values and then by their keys.
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @return
     */
    private int compare(K key1, V value1, K key2, V value2){
        int cmp = value1.compareTo(value2);
        if(cmp == 0)
            return key1.compareTo(key2);
        return cmp;
    }

    /**
     * Returns the node whose key is the specified key;
     * or the parent of the node where the key should exist if no such node exists.
     * @param key to be searched
     * @return see above
     
     */
    BSTNode<Entry<K,V>> findNode( K key, V value)
    {      
        BSTNode<Entry<K,V>> node = root;
        BSTNode<Entry<K,V>> current = null;
        while ( node != null )
        {
            int compResult = this.compare(key, value, node.getElement().getKey(), node.getElement().getValue());
            if ( compResult == 0 )
                return node;
            else if ( compResult < 0 ) {
                current = node;
                node = node.getLeft();
            }
            else {
                current = node;
                node = node.getRight();
            }
        }
        return current;
    } 
    
    @Override
    public V insert(K key, V value) {
        V valueToReturn = null;
        AVLNode<Entry<K,V>> newNode = null; // node where the new entry is being inserted (if find(key)==null)
        AVLNode<Entry<K,V>> node = (AVLNode<Entry<K,V>>) this.findNode(key,value);

        if ( node == null || this.compare(key, value, node.getElement().getKey(), node.getElement().getValue()) != 0 )
        { // Key does not exist, node is "parent"
            newNode = new AVLNode<>(new EntryClass<>(key, value), node, null, null);
            this.linkSubtreeInsert(newNode, node);
            currentSize++;
        }
        else
        {
            valueToReturn = node.getElement().getValue();
            node.setElement(new EntryClass<>(key, value));
        }

        if(newNode != null)
            rebalance(newNode);
        return valueToReturn;
    }
    
    
	public V remove(K key, V value) {
	    
	    V valueToReturn = null;
	    AVLNode<Entry<K,V>> node = null;
	    AVLNode<Entry<K,V>> oldNode = (AVLNode<Entry<K,V>>) this.findNode(key, value);
	    if ( oldNode == null || this.compare(key, value, oldNode.getElement().getKey(), oldNode.getElement().getValue()) != 0 )
            return null;
        else
        {
        	valueToReturn = oldNode.getElement().getValue();

	        if ( oldNode.getLeft() == null )
                // The left subtree is empty.
                this.linkSubtreeRemove(oldNode.getRight(), oldNode.getParent(),oldNode);
            else if ( oldNode.getRight() == null )
                // The right subtree is empty.
                this.linkSubtreeRemove(oldNode.getLeft(), oldNode.getParent(),oldNode);
            else
            {
                // Node has 2 children. Replace the node's entry with
                // the 'minEntry' of the right subtree.
                BSTNode<Entry<K,V>> minNode = this.minNode(oldNode.getRight());
                oldNode.setElement( minNode.getElement() );
                // Remove the 'minEntry' of the right subtree.
                this.linkSubtreeRemove(minNode.getRight(), minNode.getParent(),minNode);
            }
	        
	        node = (oldNode.getParent() != null) ? (AVLNode<Entry<K,V>>) oldNode.getParent() : (AVLNode<Entry<K,V>>) root;
	        
            currentSize--;
        }
	    

	    if (node != null) {
	        rebalance(node);
	    }

	    return valueToReturn;
	}
	
	
	/**
     * Links a new subtree, rooted at the specified node, to the tree.
     *
     * @param node - root of the subtree
     * @param parent - parent node for the new subtree
     */
    void linkSubtreeInsert(BSTNode<Entry<K,V>> node, BSTNode<Entry<K,V>> parent) {
        if ( parent == null )
            // Change the root of the tree.
            root = node;
        else {
            if (node != null) {
                node.setParent(parent);
                // Change child of parent.
                if (this.compare(node.getElement().getKey(), node.getElement().getValue(), parent.getElement().getKey(), parent.getElement().getValue()) <= 0)
                    parent.setLeft(node);
                else
                    parent.setRight(node);
            }
        }
    }
}
