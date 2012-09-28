package sfs.writer;

import sfs.entry.Entry;

public interface Writer {
	
	public void writeFrom(Entry entry);
	public String get();
}
