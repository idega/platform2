package is.idega.idegaweb.golf.entity;


public class StrokeHomeImpl extends com.idega.data.IDOFactory implements StrokeHome
{
 protected Class getEntityInterfaceClass(){
  return Stroke.class;
 }

 public Stroke create() throws javax.ejb.CreateException{
  return (Stroke) super.idoCreate();
 }

 public Stroke createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Stroke findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Stroke) super.idoFindByPrimaryKey(id);
 }

 public Stroke findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Stroke) super.idoFindByPrimaryKey(pk);
 }

 public Stroke findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}