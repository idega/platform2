package is.idega.idegaweb.golf.block.login.data;


public class LoginTableHomeImpl extends com.idega.data.IDOFactory implements LoginTableHome
{
 protected Class getEntityInterfaceClass(){
  return LoginTable.class;
 }


 public LoginTable create() throws javax.ejb.CreateException{
  return (LoginTable) super.createIDO();
 }


 public LoginTable createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public LoginTable findByMember(is.idega.idegaweb.golf.entity.Member p0)throws javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((LoginTableBMPBean)entity).ejbFindByMember(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public LoginTable findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (LoginTable) super.findByPrimaryKeyIDO(pk);
 }


 public LoginTable findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (LoginTable) super.findByPrimaryKeyIDO(id);
 }


 public LoginTable findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}