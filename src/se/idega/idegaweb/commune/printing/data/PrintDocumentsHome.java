package se.idega.idegaweb.commune.printing.data;


public interface PrintDocumentsHome extends com.idega.data.IDOHome
{
 public PrintDocuments create() throws javax.ejb.CreateException;
 public PrintDocuments findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllDocumentByType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllDocumentByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllPrintedLetterDocuments()throws javax.ejb.FinderException;

}