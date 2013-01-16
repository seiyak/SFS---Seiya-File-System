package sfs.stat.message;

import sfs.mime.Mime;

public class ContentDisposition {

	private DataType dataType;
	private String fieldName;
	private String fileName;
	private Mime contentType;
	private String fieldValue;

	public ContentDisposition() {

		setDataType( null );
		setFieldName( "" );
		setFileName( "" );
		setContentType( Mime.NULL );
		setFieldValue( "" );
	}

	public ContentDisposition(DataType dataType, String fieldName, String fileName, Mime contentType, String fieldValue) {

		setDataType( dataType );
		setFieldName( fieldName );
		setFileName( fileName );
		setContentType( contentType );
		setFieldValue( fieldValue );
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Mime getContentType() {
		return contentType;
	}

	public void setContentType(Mime contentType) {
		this.contentType = contentType;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof ContentDisposition ) {

			ContentDisposition contentDisposition = (ContentDisposition) obj;

			return dataType.equals( contentDisposition.getDataType() )
					&& fieldName.equals( contentDisposition.getFieldName() )
					&& fileName.equals( contentDisposition.getFileName() )
					&& contentType.equals( contentDisposition.getContentType() )
					&& fieldValue.equals( contentDisposition.getFieldValue() );
		}

		return false;
	}

	@Override
	public String toString() {

		return "{dataType=" + dataType + " fieldName=" + fieldName + " fileName=" + fileName + " contentType="
				+ contentType + " fieldValue=" + fieldValue + "}";
	}
}
