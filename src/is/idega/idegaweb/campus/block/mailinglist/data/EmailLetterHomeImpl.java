package is.idega.idegaweb.campus.block.mailinglist.data;


public class EmailLetterHomeImpl extends com.idega.data.IDOFactory implements EmailLetterHome
{
 protected Class getEntityInterfaceClass(){
  return EmailLetter.class;
 }

 public EmailLetter create() throws javax.ejb.CreateException{
  return (EmailLetter) super.idoCreate();
 }

 public EmailLetter createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public EmailLetter findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (EmailLetter) super.idoFindByPrimaryKey(id);
 }

 public EmailLetter findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EmailLetter) super.idoFindByPrimaryKey(pk);
 }

 public EmailLetter findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}