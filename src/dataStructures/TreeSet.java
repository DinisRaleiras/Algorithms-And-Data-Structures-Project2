package dataStructures;

public class TreeSet<E extends Comparable<E>> implements OrderedSet<E> {
	/**
	 * Serial Version UID of the Class.
	 */
    static final long serialVersionUID = 0L;


    /**
     * The root of the tree.                                            
     * 
     */
    protected TSNode<E> root;                                

    /**
     * Number of entries in the tree.                                  
     * 
     */
    protected int currentSize;                   


    /**
     * Inner class to store path steps 
	 * @author AED team
	 * @version 1.0
     * @param <K> Generic type Key, must extend comparable
     * @param <V> Generic type Value 
     */
    protected static class PathStep<E>
    {

        /**
         * The parent of the node.
         */
        public TSNode<E> parent;

        /**
         * The node is the left or the right child of parent.
         */
        public boolean isLeftChild;

        /**
         * PathStep constructor
         * @param theParent - ancestor of the current node
         * @param toTheLeft - will be true of the current node is the left child of theParent
         */
        public PathStep( TSNode<E> theParent, boolean toTheLeft )
        {
            parent = theParent;
            isLeftChild = toTheLeft;
        }


        /**
         * Method to set node parent before moving in the tree
         * @param newParent - ancestor of the current node
         * @param toTheLeft - will be true of the current node is the left child of theParent
         */
        public void set( TSNode<E> newParent, boolean toTheLeft )
        {
            parent = newParent;
            isLeftChild = toTheLeft;
        }

    }


    /**
     * Tree Constructor - creates an empty tree.
     */
    public TreeSet( )                                    
    {    
        root = null;
        currentSize = 0;
    }


    @Override
    public boolean isEmpty( )                               
    {    
        return root == null;
    }


    @Override
    public int size( )                                      
    {    
        return currentSize;
    }


    @Override
    public boolean contains( E element )                             
    {    
        TSNode<E> node = this.findNode(root, element);
        if ( node == null )                                   
            return false;                                    
        else                                                     
            return true;
    }


    /**
     * Returns the node whose key is the specified key;
     * or null if no such node exists.        
     *                         
     * @param node where the search starts 
     * @param element to be searched
     * @return the found node, when the search is successful
     */
    protected TSNode<E> findNode( TSNode<E> node, E element )
    {                                                                   
        if ( node == null )
            return null;
        else
        {
            int compResult = element.compareTo( node.getElement() );
            if ( compResult == 0 )
                return node;                                         
            else if ( compResult < 0 )
                return this.findNode(node.getLeft(), element);
            else                                                     
                return this.findNode(node.getRight(), element); 
        }                 
    }


    @Override
    public E minEntry( ) throws EmptyDictionaryException
    {                                                                   
        if ( this.isEmpty() )                              
            throw new EmptyDictionaryException();           

        return this.minNode(root).getElement();                    
    }


    /**
     * Returns the node with the smallest key 
     * in the tree rooted at the specified node.
     * Requires: node != null.
     * @param node - node that roots the tree
     * @return node with the smallest key in the tree
     */
    protected TSNode<E> minNode( TSNode<E> node ) 
    {                                                                   
        if ( node.getLeft() == null )                             
            return node;                                             
        else                                                     
            return this.minNode( node.getLeft() );                        
    }                               


    @Override
    public E maxEntry( ) throws EmptyDictionaryException
    {                                                                   
        if ( this.isEmpty() )                              
            throw new EmptyDictionaryException();           

        return this.maxNode(root).getElement();                    
    }


    /**
     * Returns the node with the largest key 
     * in the tree rooted at the specified node.
     * Requires: node != null.
     * @param node that roots the tree
     * @return node with the largest key in the tree
     */
    protected TSNode<E> maxNode( TSNode<E> node )
    {                                                                   
        if ( node.getRight() == null )                            
            return node;                                             
        else                                                     
            return this.maxNode( node.getRight() );                       
    }                               


    /**
     * Returns the node whose key is the specified key;
     * or null if no such node exists.                                
     * Moreover, stores the last step of the path in lastStep.
     * @param element to be searched
     * @param lastStep - PathStep object referring to parent 
     * @return the found node, when the search is successful
     
     */
    protected TSNode<E> findNode( E element, PathStep<E> lastStep )
    {      
    	TSNode<E> node = root;
        while ( node != null )
        {
            int compResult = element.compareTo(node.getElement());
            if ( compResult == 0 )
                return node;
            else if ( compResult < 0 )
            {
                lastStep.set(node, true);
                node = node.getLeft();
            }
            else
            {
                lastStep.set(node, false);
                node = node.getRight();
            }
        }
        return null;                                                    
    }                               


    @Override
    public boolean insert( E element )
    {                                              
        PathStep<E> lastStep = new PathStep<E>(null, false);
        TSNode<E> node = this.findNode(element, lastStep);
        if ( node == null )
        {
        	TSNode<E> newLeaf = new TSNode<E>(element);
            this.linkSubtree(newLeaf, lastStep);
            currentSize++;
            return true;
        }                                 
        else 
        {
            return false;
        }
    }


    /**
     * Links a new subtree, rooted at the specified node, to the tree.
     * The parent of the old subtree is stored in lastStep.
     * @param node - root of the subtree
     * @param lastStep - PathStep object referring to the parent of the old subtree
     */
    protected void linkSubtree( TSNode<E> node, PathStep<E> lastStep )
    {
        if ( lastStep.parent == null )
            // Change the root of the tree.
            root = node;
        else
            // Change a child of parent. 
            if ( lastStep.isLeftChild )
                lastStep.parent.setLeft(node);
            else
                lastStep.parent.setRight(node);
    }


    /**
     * Returns the node with the smallest key 
     * in the tree rooted at the specified node.
     * Moreover, stores the last step of the path in lastStep.
     * Requires: theRoot != null.
     * @param theRoot - node that roots the tree
     * @param lastStep - Pathstep object to refer to the parent of theRoot
     * @return node containing the entry with the minimum key
     */
    protected TSNode<E> minNode( TSNode<E> theRoot, 
        PathStep<E> lastStep ) 
    {                                                                   
    	TSNode<E> node = theRoot;
        while ( node.getLeft() != null ) 
        {                      
            lastStep.set(node, true);
            node = node.getLeft();
        }                                       
        return node;                                                
    }


    @Override
    public boolean remove( E element )
    {
        PathStep<E> lastStep = new PathStep<E>(null, false);
        TSNode<E> node = this.findNode(element, lastStep);
        if ( node == null )
            return false;
        else
        {
            if ( node.getLeft() == null )
                // The left subtree is empty.
                this.linkSubtree(node.getRight(), lastStep);
            else if ( node.getRight() == null )
                // The right subtree is empty.
                this.linkSubtree(node.getLeft(), lastStep);
            else
            {
                // Node has 2 children. Replace the node's entry with
                // the 'minEntry' of the right subtree.
                lastStep.set(node, false);
                TSNode<E> minNode = this.minNode(node.getRight(), lastStep);
                node.setElement( minNode.getElement() );
                // Remove the 'minEntry' of the right subtree.
                this.linkSubtree(minNode.getRight(), lastStep);
            }
            currentSize--;
            return true;
        }                                 
    }                                


    /**
     * Returns an iterator of the entries in the dictionary 
     * which preserves the key order relation.
     * @return  key-order iterator of the entries in the dictionary
     */
    public Iterator<E> iterator( ) 
    {
        return new TSOrderIterator<>(root);
    }
}
