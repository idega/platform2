package se.idega.idegaweb.commune.message.data;
public class PrintedLetterMessageHomeImpl extends com.idega.data.IDOFactory implements PrintedLetterMessageHome
{
	protected Class getEntityInterfaceClass()
	{
		return PrintedLetterMessage.class;
	}
	public Message create() throws javax.ejb.CreateException
	{
		return (PrintedLetterMessage) super.createIDO();
	}
	public java.util.Collection findMessages(com.idega.user.data.User p0) throws javax.ejb.FinderException, java.rmi.RemoteException
	{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity).ejbFindMessages(p0);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	public java.util.Collection findAllPrintedLetters() throws javax.ejb.FinderException, java.rmi.RemoteException
	{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity).ejbFindAllPrintedLetters();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	public java.util.Collection findAllUnPrintedLetters() throws javax.ejb.FinderException, java.rmi.RemoteException
	{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity).ejbFindAllUnPrintedLetters();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException
	{
		return (PrintedLetterMessage) super.findByPrimaryKeyIDO(pk);
	}
	public int getNumberOfPrintedLetters() throws java.rmi.RemoteException
	{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PrintedLetterMessageBMPBean) entity).ejbHomeGetNumberOfPrintedLetters();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
	public int getNumberOfUnPrintedLetters() throws java.rmi.RemoteException
	{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PrintedLetterMessageBMPBean) entity).ejbHomeGetNumberOfUnPrintedLetters();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}