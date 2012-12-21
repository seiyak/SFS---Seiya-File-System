package sfs.stat.message;

public class MessageStat {

	private int length;
	private String message;
	private boolean endOfMessage;
	private boolean hasBeenSet = false;
	private boolean messageBodyContained = false;
	private String messageBodyType = "";
	private int messageBodyLength;
	private int messageBodyStartIndex = -1;
	private boolean headerHasBeenSet;

	public MessageStat() {
		message = "";
	}

	public MessageStat(int length, String message, boolean endOfMessage) {
		this.setLength( length );
		this.message = message;
		this.endOfMessage = endOfMessage;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {

		if ( !hasBeenSet ) {
			this.length = length;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {

		if ( !hasBeenSet ) {
			this.message = message;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public boolean isEndOfMessage() {
		return endOfMessage;
	}

	public void setEndOfMessage(boolean endOfMessage) {
		this.endOfMessage = endOfMessage;

		if ( endOfMessage ) {
			hasBeenSet = true;
		}
		else {
			if ( hasBeenSet ) {
				hasBeenSet = false;
			}
		}
	}

	public boolean isMessageBodyContained() {
		return messageBodyContained;
	}

	public void setMessageBodyContained(boolean messageBodyContained) {

		if ( !hasBeenSet ) {
			this.messageBodyContained = messageBodyContained;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public String getMessageBodyType() {
		return messageBodyType;
	}

	public void setMessageBodyType(String messageBodyType) {

		if ( !hasBeenSet ) {
			this.messageBodyType = messageBodyType;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public int getMessageBodyLength() {
		return messageBodyLength;
	}

	public void setMessageBodyLength(int messageBodyLength) {

		if ( !hasBeenSet ) {
			this.messageBodyLength = messageBodyLength;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public int getMessageBodyStartIndex() {
		return messageBodyStartIndex;
	}

	public void setMessageBodyStartIndex(int messageBodyStartIndex) {

		if ( !hasBeenSet ) {
			this.messageBodyStartIndex = messageBodyStartIndex;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public boolean isHeaderHasBeenSet() {
		return headerHasBeenSet;
	}

	public void setHeaderHasBeenSet(boolean headerHasBeenSet) {

		if ( !hasBeenSet ) {
			this.headerHasBeenSet = headerHasBeenSet;
		}
		else {
			throw new IllegalStateException( "current this stat is in read only." );
		}
	}

	public boolean isHasHeenSet() {
		return hasBeenSet;
	}

	public void clearStat() {

		length = 0;
		message = "";
		endOfMessage = false;
		hasBeenSet = false;
		messageBodyLength = 0;
		messageBodyType = "";
		messageBodyContained = false;
		messageBodyStartIndex = 0;
		headerHasBeenSet = false;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof MessageStat ) {

			MessageStat stat = (MessageStat) obj;
			return length == stat.getLength() && message.equals( stat.getMessage() )
					&& endOfMessage == stat.isEndOfMessage() && hasBeenSet == stat.isHasHeenSet();
		}

		return false;
	}

	@Override
	public String toString() {
		return "{length=" + length + " message=" + message + " endOfMessage=" + endOfMessage + " hasBeenSet="
				+ hasBeenSet + "}";
	}
}
