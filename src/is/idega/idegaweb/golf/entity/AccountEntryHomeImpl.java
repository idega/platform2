package is.idega.idegaweb.golf.entity;


public class AccountEntryHomeImpl extends com.idega.data.IDOFactory implements AccountEntryHome
{
 protected Class getEntityInterfaceClass(){
  return AccountEntry.class;
 }

 public AccountEntry create() throws javax.ejb.CreateException{
  return (AccountEntry) super.idoCreate();
 }

 public AccountEntry createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountEntry findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountEntry) super.idoFindByPrimaryKey(id);
 }

 public AccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountEntry) super.idoFindByPrimaryKey(pk);
 }

 public AccountEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}