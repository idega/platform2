package is.idega.idegaweb.member.data;


public class UserTitlesHomeImpl extends com.idega.data.IDOFactory implements UserTitlesHome
{
 protected Class getEntityInterfaceClass(){
  return UserTitles.class;
 }


 public UserTitles create() throws javax.ejb.CreateException{
  return (UserTitles) super.createIDO();
 }


public java.util.Collection findAllBoardTitle()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserTitlesBMPBean)entity).ejbFindAllBoardTitle();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllTitles()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserTitlesBMPBean)entity).ejbFindAllTitles();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public UserTitles findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserTitles) super.findByPrimaryKeyIDO(pk);
 }



}