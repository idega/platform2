package is.idega.idegaweb.campus.block.phone.data;


public class CampusPhoneHomeImpl extends com.idega.data.IDOFactory implements CampusPhoneHome
{
 protected Class getEntityInterfaceClass(){
  return CampusPhone.class;
 }

 public CampusPhone create() throws javax.ejb.CreateException{
  return (CampusPhone) super.createIDO();
 }

 public CampusPhone createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CampusPhone findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CampusPhone) super.idoFindByPrimaryKey(id);
 }

 public CampusPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CampusPhone) super.findByPrimaryKeyIDO(pk);
 }

 public CampusPhone findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}