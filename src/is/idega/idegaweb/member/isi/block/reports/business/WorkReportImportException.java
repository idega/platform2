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
	
	public WorkReportImportException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public WorkReportImportException(String message) {
		super(message);
	}
	
	public String getColumnForError() {
		return _col;
	}
	
	public void setColumnForError(String col) {
		_col = col;
	}
	
	public String getRowForError() {
		return _row;
	}
	
	public void setRowForError(String row) {
		_row = row;
	}
}