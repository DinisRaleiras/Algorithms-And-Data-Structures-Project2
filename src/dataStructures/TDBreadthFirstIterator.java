package dataStructures;

public class TDBreadthFirstIterator<E extends Comparable<E>> implements Iterator<E>{

	static final long serialVersionUID = 1L;
	
	protected TSNode<E> root;
	// espacial 1 n n
	protected Queue<TSNode<E>> q;
	
	// 1 1 1
	public  TDBreadthFirstIterator(TSNode<E> root){
		this.root=root;
		rewind();
		
	}
	
	// 1 1 1
	public boolean hasNext( ){
		return !q.isEmpty();
	}
	
	// 1 1 1
	private void enqueueNode(TSNode<E> node){
		if (node!=null) q.enqueue(node);
	}
	
	// 1 1 1
    public E next( ) throws NoSuchElementException{
    	if (!hasNext()) throw new NoSuchElementException();
    	TSNode<E> node=q.dequeue();
    	enqueueNode(node.getLeft());
    	enqueueNode(node.getRight());
    	return node.getElement();
    }

    // 1 1 1
    public void rewind( ){
    	q=new QueueInList<TSNode<E>>();
    	enqueueNode(root);
    	
    }
}
