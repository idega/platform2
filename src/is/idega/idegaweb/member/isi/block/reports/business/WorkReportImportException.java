/*
 * Created on Jul 2, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.business;

/**
 * Description: WorkReportImportException is thrown when an import exception is encountered such as if the import file is corrupt,missingg etc.<br>
 * It is also a superclass for similar WorkReport related errors.
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportImportException extends Exception {
	
	public WorkReportImportException() {
		super();
	}
	/**
	 * @param message
	 */
	public WorkReportImportException(String message) {
		super(message);
	}
	
	
	

}
