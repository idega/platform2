package is.idega.idegaweb.campus.block.mailinglist.data;


public class MailingListHomeImpl extends com.idega.data.IDOFactory implements MailingListHome
{
 protected Class getEntityInterfaceClass(){
  return MailingList.class;
 }

 public MailingList create() throws javax.ejb.CreateException{
  return (MailingList) super.idoCreate();
 }

 public MailingList createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MailingList findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MailingList) super.idoFindByPrimaryKey(id);
 }

 public MailingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailingList) super.idoFindByPrimaryKey(pk);
 }

 public MailingList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}