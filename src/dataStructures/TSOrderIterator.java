package dataStructures;

public class TSOrderIterator<E extends Comparable<E>> implements Iterator<E> {

	/**
	 * Serial Version UID of the Class.
	 */
	static final long serialVersionUID = 0L;
	
	/**
	 * The stack that stores the nodes by crescent order.
	 */
	protected Stack<TSNode<E>> stack;
	
	/**
     * The root of the tree.
     */
	protected TSNode<E> root;
	
	
	/**
     * BSTKeyOrderIterator constructor
     * @param root - The root of the tree
     */
	public TSOrderIterator(TSNode<E> root) {
		this.root = root;
		stack = new StackInList<>();
		this.pushLeftNodes(root);
	}
	
	/**
	 * Adds to the stack every left node of each left subtree 
	 * @param root - the root of the subtree
	 */
	protected void pushLeftNodes(TSNode<E> root) {
		TSNode<E> node = root;
		while(node != null) {
			stack.push(node);
			node = node.getLeft();
		}
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public E next() throws NoSuchElementException {
		if(!this.hasNext()) {
			throw new NoSuchElementException();
		}
		TSNode<E> node = stack.pop();
		if(node.getRight() != null) {
			this.pushLeftNodes(node.getRight());
		}
		return node.getElement();
	}

	@Override
	public void rewind() {
		this.clearStack();
		pushLeftNodes(root);		
	}
	
	/**
	 * Clears the stack that contains the nodes of the tree
	 */
	protected void clearStack() {
		while(!stack.isEmpty()) {
			stack.pop();
		}
	}


}
