package is.idega.idegaweb.campus.block.mailinglist.data;


public class MailingListHomeImpl extends com.idega.data.IDOFactory implements MailingListHome
{
 protected Class getEntityInterfaceClass(){
  return MailingList.class;
 }


 public MailingList create() throws javax.ejb.CreateException{
  return (MailingList) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MailingListBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public MailingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailingList) super.findByPrimaryKeyIDO(pk);
 }



}