package dataStructures;

/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 1
 */
public class ConcatenableQueueInList<E> extends QueueInList<E> implements ConcatenableQueue<E>{

	/**
	 * Serial Version UID of the Class
	 */
	static final long serialVersionUID = 0L;

	
	/**
     * Constructor of an empty concatenable queue.
     * list is initialized as a double linked list.
     */
	public ConcatenableQueueInList() {
		super();
	}
	
	@Override
	public void append(ConcatenableQueue<E> queue) {
		this.list.append((DoubleList<E>) ((ConcatenableQueueInList<E>) queue).list);
	}

}
