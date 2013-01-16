package sfs.stat.message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import sfs.header.http.HeaderEntry;
import sfs.list.WrappedList;
import sfs.request.http.RequestMessage;
import sfs.util.string.StringUtil;

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
	private Map<HeaderEntry,String> header = null;
	private String boundary = "";
	private boolean fileUploadable = false;
	private int currentContentDispositionIndex = -1;
	private int currentContentStartIndex = -1;
	private boolean contentDispositionSet = false;
	private final WrappedList<ContentDisposition> contentDispositions;
	private final static String BOUNDARY_PREFIX = "--";

	public MessageStat() {
		message = "";
		contentDispositions = new WrappedList<ContentDisposition>( new LinkedList<ContentDisposition>() );
	}

	public MessageStat(int length, String message, boolean endOfMessage) {
		this.setLength( length );
		this.message = message;
		this.endOfMessage = endOfMessage;
		contentDispositions = new WrappedList<ContentDisposition>( new LinkedList<ContentDisposition>() );
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

	public void setHeader(Map<HeaderEntry, String> header) {
		this.header = header;
	}

	public Map<HeaderEntry, String> getHeader() {
		return header;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = BOUNDARY_PREFIX + boundary;
	}

	public boolean isFileUploadable() {
		return fileUploadable;
	}

	/**
	 * Checks if the header property is already set or not. If and only if not set,
	 * the method sets the header property extracting the header part using the specified RequestMessage.
	 * 
	 * @param requestMessage
	 *            Used to extract the header.
	 */
	public void checkAndSetHeader(RequestMessage requestMessage) {
		if ( header == null ) {
			setHeader( requestMessage.extractMessage( message ).getHeader() );
		}
	}

	/**
	 * Convenient method to check and set boundary.
	 * 
	 * @param index
	 *            Where 'boundary' key word starts within the header.
	 */
	public void checkAndSetBoundary(int index) {

		if ( index != -1 && boundary == "" ) {

			setBoundary( header.get( HeaderEntry.CONTENT_TYPE ).substring( index + 1 ) );
			fileUploadable = true;
		}
	}

	/**
	 * Convenient method to check and set header and boundary.
	 * 
	 * @param boundaryKey
	 *            Boundary Used to separator form data and attached data.
	 * @param requestMessage
	 *            Used to generate header.
	 */
	public void checkAndSetHeaderAndBoundary(String boundaryKey, RequestMessage requestMessage) {
		checkAndSetHeader( requestMessage );
		checkAndSetBoundary( StringUtil
				.searchLastIndexOfByMB( getHeader().get( HeaderEntry.CONTENT_TYPE ), boundaryKey ) );
	}

	public boolean isContentDispositionHasBeenSet() {
		return currentContentDispositionIndex != -1 && currentContentStartIndex != -1;
	}

	public int getCurrentContentDispositionIndex() {
		return currentContentDispositionIndex;
	}

	public boolean isContentDispositionSet() {
		return contentDispositionSet;
	}

	public void setContentDispositionSet(boolean contentDispositionSet) {
		this.contentDispositionSet = contentDispositionSet;
	}

	public void setCurrentContentDispositionIndex(int currentContentDispositionIndex) {
		this.currentContentDispositionIndex = currentContentDispositionIndex;
	}

	public boolean addContentDisposition(ContentDisposition contentDisposition){
		return contentDispositions.add( contentDisposition );
	}
	
	public boolean removeContentDisposition(ContentDisposition contentDisposition){
		return contentDispositions.remove( contentDisposition );
	}
	
	public void clearContentDispositions(){
		contentDispositions.clear();
	}
	
	public Iterator<ContentDisposition> getIteratorForContentDispositions(){
		return contentDispositions.getIterator();
	}
	
	public int getSizeOfContentDispositions(){
		return contentDispositions.getSize();
	}
	
	public boolean isEmptyContentDispositions(){
		return contentDispositions.isEmpty();
	}

	public int getCurrentContentStartIndex() {
		return currentContentStartIndex;
	}

	public void setCurrentContentStartIndex(int currentContentStartIndex) {
		this.currentContentStartIndex = currentContentStartIndex;
	}
	
	public boolean isEndOfMessage(int index, int offset){
		return index == message.length() - offset;
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
		clearHeader();
		boundary = "";
		fileUploadable = false;
		currentContentDispositionIndex = -1;
		contentDispositions.clear();
		currentContentStartIndex = -1;
	}

	/**
	 * Clears header and sets it null if and only if header peroperty is not null.
	 */
	private void clearHeader() {

		if ( header != null ) {
			header.clear();
			header = null;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof MessageStat ) {

			MessageStat stat = (MessageStat) obj;
			return length == stat.getLength() && message.equals( stat.getMessage() )
					&& endOfMessage == stat.isEndOfMessage() && hasBeenSet == stat.isHasHeenSet()
					&& messageBodyContained == stat.isMessageBodyContained()
					&& messageBodyType.equals( stat.getMessageBodyType() )
					&& messageBodyLength == stat.getMessageBodyStartIndex()
					&& messageBodyStartIndex == stat.getMessageBodyStartIndex()
					&& headerHasBeenSet == stat.isHeaderHasBeenSet() && boundary.equals( stat.getBoundary() )
					&& fileUploadable == stat.isFileUploadable()
					&& currentContentDispositionIndex == stat.getCurrentContentDispositionIndex()
					&& currentContentStartIndex == stat.getCurrentContentStartIndex()
					&& contentDispositionSet == stat.isContentDispositionSet()
					&& equals( stat.getIteratorForContentDispositions(), stat.getSizeOfContentDispositions() );
		}

		return false;
	}

	/**
	 * Checks equality of contentDispositions property.
	 * 
	 * @param contentDispositions
	 *            Used to be compared with contentDispositions property.
	 * @param size
	 *            Size of contentDispositions property.
	 * @return True if contentDispositions property is equals to the contentDispositions property
	 *         represented by the parameter, contentDispositions, false otherwise.
	 */
	private boolean equals(Iterator<ContentDisposition> contentDispositions, int size) {

		if ( this.contentDispositions.getSize() == size ) {

			Iterator<ContentDisposition> itr = this.contentDispositions.getIterator();

			int count = 0;
			while ( itr.hasNext() ) {

				ContentDisposition contentDisposition = itr.next();
				ContentDisposition contentDisp = contentDispositions.next();

				if ( !contentDisposition.equals( contentDisp ) ) {
					break;
				}

				count++;
			}

			if ( count == this.contentDispositions.getSize() ) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public String toString() {
		return "{length=" + length + " message=" + message + " endOfMessage=" + endOfMessage + " hasBeenSet="
				+ hasBeenSet + " header size=" + header.size() + "}";
	}
}
