package is.idega.idegaweb.golf.entity;


public class AdCatagoryHomeImpl extends com.idega.data.IDOFactory implements AdCatagoryHome
{
 protected Class getEntityInterfaceClass(){
  return AdCatagory.class;
 }

 public AdCatagory create() throws javax.ejb.CreateException{
  return (AdCatagory) super.idoCreate();
 }

 public AdCatagory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AdCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AdCatagory) super.idoFindByPrimaryKey(id);
 }

 public AdCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AdCatagory) super.idoFindByPrimaryKey(pk);
 }

 public AdCatagory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}