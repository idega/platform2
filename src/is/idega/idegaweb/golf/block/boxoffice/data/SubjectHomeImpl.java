package is.idega.idegaweb.golf.block.boxoffice.data;


public class SubjectHomeImpl extends com.idega.data.IDOFactory implements SubjectHome
{
 protected Class getEntityInterfaceClass(){
  return Subject.class;
 }


 public Subject create() throws javax.ejb.CreateException{
  return (Subject) super.createIDO();
 }


 public Subject createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Subject findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Subject) super.findByPrimaryKeyIDO(pk);
 }


 public Subject findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Subject) super.findByPrimaryKeyIDO(id);
 }


 public Subject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}