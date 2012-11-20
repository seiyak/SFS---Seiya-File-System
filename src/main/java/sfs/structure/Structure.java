package sfs.structure;

public interface Structure<T> {

	public T add(T t);

	public T delete(T t);

	public boolean find(T t);

	public int getSize();
	
	public T[] toArray();
	
	public T getRoot();
}
