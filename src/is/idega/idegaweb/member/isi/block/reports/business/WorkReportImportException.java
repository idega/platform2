/*
 * Created on Jul 2, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.business;

/**
 * Description: WorkReportImportException is thrown when an import exception is encountered such as if the import file is corrupt,missing etc.<br>
 * It is also a superclass for similar WorkReport related errors.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportImportException extends Exception {
	private String _col = null;
	private String _row = null;
	private String _detail = null;
	private static String EXCEL_COLUMN_NAMES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public WorkReportImportException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public WorkReportImportException(String message) {
		super(message);
	}

	public WorkReportImportException(String message, String row, String col, String detail) {
		super(message);
		this._col = col;
		this._row = row;
		this._detail = detail;
	}

	public WorkReportImportException(String message, int row, int col, String detail) {
	    this(message,Integer.toString(row),col<26?EXCEL_COLUMN_NAMES.substring(col-1,col):Integer.toString(col),detail);	
	}
	
	public String getColumnForError() {
		return this._col;
	}
	
	public void setColumnForError(String col) {
		this._col = col;
	}
	
	public String getRowForError() {
		return this._row;
	}
	
	public void setRowForError(String row) {
		this._row = row;
	}
	
	public String getDetail() {
		return this._detail;
	}
	
	public void setDetail(String detail) {
		this._detail = detail;
	}
}