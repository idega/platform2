package is.idega.idegaweb.golf.entity;


public class PriceCatalogueHomeImpl extends com.idega.data.IDOFactory implements PriceCatalogueHome
{
 protected Class getEntityInterfaceClass(){
  return PriceCatalogue.class;
 }

 public PriceCatalogue create() throws javax.ejb.CreateException{
  return (PriceCatalogue) super.idoCreate();
 }

 public PriceCatalogue createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PriceCatalogue findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PriceCatalogue) super.idoFindByPrimaryKey(id);
 }

 public PriceCatalogue findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PriceCatalogue) super.idoFindByPrimaryKey(pk);
 }

 public PriceCatalogue findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}