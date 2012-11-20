package sfs.structure.list;

import org.apache.log4j.Logger;

public class SinglyLinkedList<T extends SinglyNode> extends AbstractList<T> {

	private int count;
	private final T head;
	private static Logger log = Logger.getLogger( SinglyLinkedList.class );

	public SinglyLinkedList(T head) {
		this.head = head;
	}

	public T add(T t) {

		T node = null;
		try {
			node = head;
			while ( node.getNext() != null ) {

				node = (T) node.getNext();
			}
			node.setNext( t );

			count++;
		}
		catch ( Exception ex ) {
			log.error( ex );
			return null;
		}
		return node;
	}

	public T delete(T t) {

		T node = head;
		while ( node != null ) {

			if ( node.getNext().getId() == t.getId() ) {
				break;
			}

			node = (T) node.getNext();
		}

		if ( node == null ) {
			return null;
		}

		SinglyNode sn = node.getNext().getNext();
		node.setNext( null );
		node.setNext( sn );

		count--;
		return node;
	}

	public boolean find(T t) {

		T node = head;
		while ( node != null ) {

			if ( node.getId() == t.getId() ) {
				break;
			}

			node = (T) node.getNext();
		}

		if ( node == null ) {
			return false;
		}

		return true;
	}

	public int getSize() {
		return count;
	}

	public T get(int index) {

		T node = head;
		int count = 0;
		while ( node != null ) {

			if ( count == index ) {
				break;
			}

			node = (T) node.getNext();
			count++;
		}

		if ( count == index ) {
			return node;
		}

		return null;
	}

	public T[] toArray() {
		
		Object[] nodes = new Object[count];
		T node = head;
		int index = 0;
		while(node != null){
			nodes[index++] = node;
			node = (T) node.getNext();
		}
		
		return (T[]) nodes;
	}

	public T getRoot() {
		return head;
	}
}
