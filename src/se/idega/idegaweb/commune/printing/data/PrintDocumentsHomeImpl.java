package se.idega.idegaweb.commune.printing.data;


public class PrintDocumentsHomeImpl extends com.idega.data.IDOFactory implements PrintDocumentsHome
{
 protected Class getEntityInterfaceClass(){
  return PrintDocuments.class;
 }


 public PrintDocuments create() throws javax.ejb.CreateException{
  return (PrintDocuments) super.createIDO();
 }


public java.util.Collection findAllDocumentByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintDocumentsBMPBean)entity).ejbFindAllDocumentByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllDocumentByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintDocumentsBMPBean)entity).ejbFindAllDocumentByType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllPrintedLetterDocuments()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintDocumentsBMPBean)entity).ejbFindAllPrintedLetterDocuments();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PrintDocuments findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PrintDocuments) super.findByPrimaryKeyIDO(pk);
 }



}