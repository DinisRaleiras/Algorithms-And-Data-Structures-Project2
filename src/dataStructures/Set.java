package dataStructures;

import java.io.Serializable;

public interface Set<E> extends Serializable{
	

    boolean isEmpty( );
    

    int size( );
    

    boolean contains( E element );
    

    boolean insert( E element );
    

    boolean remove( E element );
    

    Iterator<E> iterator( );  
}
