package sfs.async.handler.http.reader;

import java.util.Arrays;

import sfs.header.http.HeaderEntry;
import sfs.header.http.ending.Ending;
import sfs.stat.message.MessageStat;
import sfs.util.string.StringUtil;

public class HTTPMessageReader extends AbstractHTTPReader {

	private final String TRANSFER_ENCODING_HEADER_KEY = HeaderEntry.TRANSFER_ENCODING.toString() + ": chunked"
			+ Ending.CRLF;
	private final String CONTENT_LENGTH_HEADER_KEY = HeaderEntry.CONTENT_LENGTH.toString() + ": ";

	public HTTPMessageReader() {
		super();
	}

	public HTTPMessageReader(int bufferCapacity) {
		super( bufferCapacity );
	}

	@Override
	public boolean findEndOfMessage(String message, MessageStat messageStat) {

		if ( messageStat.isHeaderHasBeenSet() ) {
			if ( messageStat.getMessageBodyType().equals( HeaderEntry.CONTENT_LENGTH.toString() ) ) {

				// TODO should read by string or one by one as char ?
				for ( char c : message.toCharArray() ) {

					if ( messageStat.getMessageBodyLength() == 0 ) {
						break;
					}

					messageStat.setMessage( messageStat.getMessage() + c );
					messageStat.setLength( messageStat.getLength() + 1 );
					messageStat.setMessageBodyLength( messageStat.getMessageBodyLength() - 1 );
				}

				if ( messageStat.getMessageBodyLength() == 0 ) {
					messageStat.setMessageBodyLength( messageStat.getLength() - messageStat.getMessageBodyStartIndex() );
					messageStat.setEndOfMessage( true );

					return true;
				}

				return false;
			}
			else if ( messageStat.getMessageBodyType().equals( "chunked" ) ) {
				// TODO chunked type to read message body here.
			}
		}

		messageStat.setMessage( messageStat.getMessage() + message );
		messageStat.setLength( messageStat.getMessage().length() );

		int separatorIndex = StringUtil.searchLastIndexOfByMB( messageStat.getMessage(), Ending.CRLF.toString()
				+ Ending.CRLF.toString() );

		if ( separatorIndex == -1 ) {
			return false;
		}

		int contentHeaderIndex = StringUtil.searchLastIndexOfByMB( messageStat.getMessage(), CONTENT_LENGTH_HEADER_KEY );
		int chunkedHeaderIndex = StringUtil.searchLastIndexOfByMB( messageStat.getMessage(),
				TRANSFER_ENCODING_HEADER_KEY );

		if ( ( contentHeaderIndex == -1 ) && ( chunkedHeaderIndex == -1 ) ) {
			// no message body attached
			messageStat.setMessageBodyLength( 0 );
			messageStat.setMessageBodyContained( false );
			messageStat.setEndOfMessage( true );

			return true;
		}

		messageStat.setMessageBodyContained( true );
		messageStat.setMessageBodyStartIndex( separatorIndex + 1 );
		messageStat.setHeaderHasBeenSet( true );

		if ( contentHeaderIndex != -1 ) {
			messageStat.setMessageBodyType( HeaderEntry.CONTENT_LENGTH.toString() );
			int contentLengthIndex = StringUtil.searchFirstIndexOfByMB( messageStat.getMessage(),
					Ending.CRLF.toString(), contentHeaderIndex );

			messageStat.setMessageBodyLength( Integer.valueOf( String.valueOf( Arrays.copyOfRange( messageStat
					.getMessage().toCharArray(), contentHeaderIndex + 1, contentLengthIndex ) ) )
					- ( messageStat.getLength() - messageStat.getMessageBodyStartIndex() ) );

			if ( messageStat.getMessageBodyLength() == 0 ) {
				messageStat.setMessageBodyLength( messageStat.getLength() - messageStat.getMessageBodyStartIndex() );
				messageStat.setEndOfMessage( true );

				return true;
			}
		}

		if ( chunkedHeaderIndex != -1 ) {
			messageStat.setMessageBodyType( "chunked" );
		}

		return false;
	}
}
