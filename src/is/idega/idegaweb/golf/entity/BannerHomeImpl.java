package is.idega.idegaweb.golf.entity;


public class BannerHomeImpl extends com.idega.data.IDOFactory implements BannerHome
{
 protected Class getEntityInterfaceClass(){
  return Banner.class;
 }

 public Banner create() throws javax.ejb.CreateException{
  return (Banner) super.idoCreate();
 }

 public Banner createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Banner findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Banner) super.idoFindByPrimaryKey(id);
 }

 public Banner findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Banner) super.idoFindByPrimaryKey(pk);
 }

 public Banner findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}