package dataStructures;

public class InsensitiveTreeSet extends TreeSet<String> {
	static final long serialVersionUID = 0L;
	
	public InsensitiveTreeSet() {
		super();
	}
	
	protected TSNode<String> findNode( TSNode<String> node, String element )
    {                                                                   
        if ( node == null )
            return null;
        else
        {
            int compResult = element.toUpperCase().compareTo( node.getElement().toUpperCase() );
            if ( compResult == 0 )
                return node;                                         
            else if ( compResult < 0 )
                return this.findNode(node.getLeft(), element);
            else                                                     
                return this.findNode(node.getRight(), element); 
        }                 
    }
	
	protected TSNode<String> findNode( String element, PathStep<String> lastStep )
    {      
    	TSNode<String> node = root;
        while ( node != null )
        {
            int compResult = element.toUpperCase().compareTo( node.getElement().toUpperCase() );
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
}
