package is.idega.idegaweb.golf.block.login.data;


public class LoginTypeHomeImpl extends com.idega.data.IDOFactory implements LoginTypeHome
{
 protected Class getEntityInterfaceClass(){
  return LoginType.class;
 }


 public LoginType create() throws javax.ejb.CreateException{
  return (LoginType) super.createIDO();
 }


 public LoginType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public LoginType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (LoginType) super.findByPrimaryKeyIDO(pk);
 }


 public LoginType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (LoginType) super.findByPrimaryKeyIDO(id);
 }


 public LoginType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}