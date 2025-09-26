package dataStructures;                                         

/**
 * AVL tree implementation
 * 
 * @author AED team
 * @version 1.0
 *
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public class AVLTree<K extends Comparable<K>, V> 
extends AdvancedBSTree<K,V> implements OrderedDictionary<K,V>
{                                                                   

	static final long serialVersionUID = 0L;

	AVLTree(AVLNode<Entry<K,V>> node) {
		root = node;
	}

	public AVLTree() {
		this(null);
	}

	/**
	 * Rebalance method called by insert and remove.  Traverses the path from
	 * zPos to the root. For each node encountered, we recompute its height
	 * and perform a trinode restructuring if it's unbalanced.
	 * the rebalance is completed with O(log n) running time
	 */
	void rebalance(AVLNode<Entry<K,V>> zPos) {
		if(zPos.isInternal())
			zPos.setHeight();
		// Improve if possible...
		while (zPos!=null) {  // traverse up the tree towards the root
			zPos.setHeight();
			if (!zPos.isBalanced()) {
				// perform a trinode restructuring at zPos's tallest grandchild
				//If yPos (zPos.tallerChild()) denote the child of zPos with greater height.
				//Finally, let xPos be the child of yPos with greater height
				AVLNode<Entry<K,V>> yPos = zPos.tallerChild();
				if(yPos == null) {
					break;
				}
				AVLNode<Entry<K,V>> xPos = yPos.tallerChild();
				if(xPos == null) {
					break;
				}
				zPos = (AVLNode<Entry<K, V>>) restructure(xPos); // tri-node restructure (from parent class)
				((AVLNode<Entry<K, V>>) zPos.getLeft()).setHeight();  // recompute heights
				((AVLNode<Entry<K, V>>) zPos.getRight()).setHeight();
				zPos.setHeight();
			}
			zPos = (AVLNode<Entry<K, V>>) zPos.getParent();
		}
	}


	@Override
	public V insert(K key, V value) {
		V valueToReturn = null;
		AVLNode<Entry<K,V>> newNode = null; // node where the new entry is being inserted (if find(key)==null)
		AVLNode<Entry<K,V>> node = (AVLNode<Entry<K,V>>) super.findNode(key);
		
		if ( node == null || node.getElement().getKey().compareTo(key) != 0 )
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

	@Override
	public V remove(K key) {
	    
	    V valueToReturn = null;
	    AVLNode<Entry<K,V>> node = null;
	    AVLNode<Entry<K,V>> oldNode = (AVLNode<Entry<K,V>>) super.findNode(key);
	    if ( oldNode == null || oldNode.getElement().getKey().compareTo(key) != 0 )
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

}

