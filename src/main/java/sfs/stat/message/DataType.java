package sfs.stat.message;

import java.util.Map;

import sfs.util.reflection.ReflectionUtil;

public class DataType {

	private final String dataType;
	public static DataType FORM_DATA = new DataType( "form-data;" );
	public static DataType ATTACHMENT = new DataType( "attachment;" );
	public static Map DATATYPES;

	static{
		DATATYPES = ReflectionUtil.getStaticMembers( DataType.class, false );
	}
	
	public DataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof DataType ) {
			DataType dType = (DataType) obj;

			return dataType.equals( dType.toString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return dataType;
	}
}
