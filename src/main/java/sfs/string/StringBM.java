package sfs.string;

import java.util.Arrays;
import java.util.Map;

import sfs.util.string.StringUtil;

public class StringBM {

	private String str;
	private String pattern;
	private Map<Character, Integer> shiftMapFromLast;

	public StringBM(String str, String pattern) {
		this.str = str;
		this.pattern = pattern;
		shiftMapFromLast = StringUtil.calculateShift( pattern.toCharArray() );
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public boolean searchByBM() {

		return StringUtil.doSearch( str.toCharArray(), pattern.toCharArray(), shiftMapFromLast );
	}

	public int searchFirstIndexOfByMB() {

		int index = StringUtil.doSearchIndexOf( str.toCharArray(), pattern.toCharArray(), shiftMapFromLast );
		return index >= 0 ? ( index - ( pattern.length() - 1 ) ) : -1;
	}

	public int searchFirstIndexOfByMB(int from) {
		if ( str.length() <= from ) {
			throw new IllegalArgumentException( "invalid from, " + from + " index is specified for str, " + str );
		}

		int index = StringUtil.doSearchIndexOf( Arrays.copyOfRange( str.toCharArray(), from, str.length() ),
				pattern.toCharArray(), shiftMapFromLast );
		return index >= 0 ? ( from + ( index - ( pattern.length() - 1 ) ) ) : -1;
	}

	public int searchLastIndexOfByMB() {

		int index = StringUtil.doSearchIndexOf( str.toCharArray(), pattern.toCharArray(), shiftMapFromLast );

		return index < str.length() ? index : -1;
	}

	public int searchLastIndexOfByMB(int from) {

		if ( str.length() <= from ) {
			throw new IllegalArgumentException( "invalid from, " + from + " index is specified for str, " + str );
		}

		int index = StringUtil.doSearchIndexOf( Arrays.copyOfRange( str.toCharArray(), from, str.length() ),
				pattern.toCharArray(), shiftMapFromLast );
		return index >= 0 ? ( from + index ) : -1;
	}

	public int startsWith() {
		if ( str.length() < pattern.length() ) {
			return -1;
		}

		int index = StringUtil.doSearchIndexOf( str.substring( 0, pattern.length() ).toCharArray(),
				pattern.toCharArray(), shiftMapFromLast );

		return index < str.length() ? index : -1;
	}

	public void clearShiftMap() {
		shiftMapFromLast.clear();
	}
}
