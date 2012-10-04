package sfs.header.http.separator;

import java.util.LinkedList;
import java.util.List;

public abstract class Separator {

	private final List<Separator> separators;
	private final String separator;

	protected Separator(String separator) {
		separators = new LinkedList<Separator>();
		this.separator = separator;
	}

	/**
	 * Adds Separator object to make composite separators.
	 * 
	 * @param separator
	 *            To be composite with this object.
	 * @return this object.
	 */
	public Separator add(Separator separator) {

		separators.add( separator );

		return this;
	}

	/**
	 * Removes Separator object.
	 * 
	 * @param separator
	 *            Separator object to be removed.
	 * @return
	 */
	public Separator remove(Separator separator) {

		separators.remove( separator );

		return this;
	}

	/**
	 * Gets separator as String.
	 * 
	 * @return String representation of separator.
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Iterates and make composite with all the separators.
	 * 
	 * @return All the separators as String.
	 */
	private String getSeparators() {

		String all = separator;

		for ( Separator sep : separators ) {
			all += sep.getSeparator();
		}

		return all;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof Separator ) {

			Separator sep = (Separator) obj;
			return toString().equals( sep.toString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return getSeparators();
	}
}
