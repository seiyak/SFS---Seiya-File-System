package sfs.response.statuscode;

public class Status {

	private final int number;
	private final String string;

	public Status(int number, String string) {
		this.number = number;
		this.string = string;
	}

	public int getNumber() {
		return number;
	}

	public String getString() {
		return string;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof Status ) {

			Status status = (Status) obj;
			return ( number == status.getNumber() ) && string.equals( status.getString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return "{number=" + number + " string=" + string + "}";
	}
}
