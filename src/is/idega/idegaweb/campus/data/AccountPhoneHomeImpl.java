package is.idega.idegaweb.campus.data;


public class AccountPhoneHomeImpl extends com.idega.data.IDOFactory implements AccountPhoneHome
{
 protected Class getEntityInterfaceClass(){
  return AccountPhone.class;
 }

 public AccountPhone create() throws javax.ejb.CreateException{
  return (AccountPhone) super.idoCreate();
 }

 public AccountPhone createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountPhone findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountPhone) super.idoFindByPrimaryKey(id);
 }

 public AccountPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountPhone) super.idoFindByPrimaryKey(pk);
 }

 public AccountPhone findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}