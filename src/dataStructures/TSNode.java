package dataStructures;

import java.io.Serializable;

class TSNode<E> implements Serializable{

	static final long serialVersionUID = 0L;
	
	private E element;
	
	private TSNode<E> rightChild;
	
	private TSNode<E> leftChild;
	
	public TSNode(E element) {
		this.element = element;
		rightChild = null;
		leftChild = null;
	}
	
	public TSNode(E element, TSNode<E> leftChild, TSNode<E> rightChild) {
		this.element = element;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public E getElement() {
		return element;
	}
	
	public TSNode<E> getRight(){
		return rightChild;
	}
	
	public TSNode<E> getLeft(){
		return leftChild;
	}
	
	public void setElement(E element) {
		this.element = element;
	}
	
	public void setRight(TSNode<E> rightChild) {
		this.rightChild = rightChild;
	}
	
	public void setLeft(TSNode<E> leftChild) {
		this.leftChild = leftChild;
	}
	
	/**
     * Returns true iff the node is a leaf.
     * 
     * @return
     */
    public boolean isLeaf( )                                
    {    
        return leftChild == null && rightChild == null;          
    } 
}
