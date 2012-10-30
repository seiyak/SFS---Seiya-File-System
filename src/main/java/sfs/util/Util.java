package sfs.util;

import sfs.structure.StructureNode;

public class Util {

	public static <T extends StructureNode> boolean isNull(T node) {

		if ( node == null ) {
			return true;
		}

		return false;
	}

	public static <T extends StructureNode> boolean isNullOrEmpty(T[] nodes) {

		if ( nodes == null ) {
			return true;
		}

		if ( nodes.length == 0 ) {
			return true;
		}

		return false;
	}
}
