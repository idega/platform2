package is.idega.idegaweb.golf.entity;


public class GolferPageDataHomeImpl extends com.idega.data.IDOFactory implements GolferPageDataHome
{
 protected Class getEntityInterfaceClass(){
  return GolferPageData.class;
 }

 public GolferPageData create() throws javax.ejb.CreateException{
  return (GolferPageData) super.idoCreate();
 }

 public GolferPageData createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public GolferPageData findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (GolferPageData) super.idoFindByPrimaryKey(id);
 }

 public GolferPageData findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GolferPageData) super.idoFindByPrimaryKey(pk);
 }

 public GolferPageData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}