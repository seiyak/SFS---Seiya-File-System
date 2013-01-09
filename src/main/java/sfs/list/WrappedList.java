package sfs.list;

import java.util.Iterator;
import java.util.List;

public class WrappedList<T> {

	private final List<T> list;

	public WrappedList(List<T> list) {
		this.list = list;
	}

	public boolean add(T t) {
		return list.add( t );
	}

	public boolean remove(T t) {
		return list.remove( t );
	}

	public boolean remove(int index) {

		checkBoundary( index );
		T t = list.remove( index );

		return t == null ? false : true;
	}

	public T get(int index) {

		checkBoundary( index );

		return list.get( index );
	}

	private void checkBoundary(int index) {

		if ( index < 0 || index > list.size() ) {
			throw new IllegalArgumentException( "invalid index, " + index + " was specified." );
		}
	}

	public void clear() {
		list.clear();
	}

	public Iterator<T> getIterator() {
		return list.iterator();
	}

	public int getSize() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
}
