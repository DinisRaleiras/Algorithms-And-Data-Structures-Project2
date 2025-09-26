package dataStructures;

/**
 * ConcatenableQueue Abstract Data Type
 * Includes description of general methods for ConcatenableQueue with FIFO discipline.
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public interface ConcatenableQueue<E> extends Queue<E>{
	
	/*
	 * Removes all of the elements from the specified queue and
	 * inserts them all at the end of the queue (in proper order).
	 * @param queue: queue to be appended to the end of this
	 * Complexity: BestCase - O(n) WorstCase - O(n) ExpectedCase - O(n)
	 */
	void append(ConcatenableQueue<E> queue);
}
