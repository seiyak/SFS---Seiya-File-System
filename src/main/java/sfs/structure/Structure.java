package sfs.structure;

public interface Structure<T> {

	public boolean add(T t);

	public boolean delete(T t);

	public boolean find(T t);

	public int getSize();
	
	public T[] toArray();
}
