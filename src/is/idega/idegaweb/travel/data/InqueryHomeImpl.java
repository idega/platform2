package is.idega.idegaweb.travel.data;


public class InqueryHomeImpl extends com.idega.data.IDOFactory implements InqueryHome
{
 protected Class getEntityInterfaceClass(){
  return Inquery.class;
 }

 public Inquery create() throws javax.ejb.CreateException{
  return (Inquery) super.idoCreate();
 }

 public Inquery createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Inquery findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Inquery) super.idoFindByPrimaryKey(id);
 }

 public Inquery findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Inquery) super.idoFindByPrimaryKey(pk);
 }

 public Inquery findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}