package is.idega.idegaweb.campus.block.application.data;


public class SpouseOccupationHomeImpl extends com.idega.data.IDOFactory implements SpouseOccupationHome
{
 protected Class getEntityInterfaceClass(){
  return SpouseOccupation.class;
 }

 public SpouseOccupation create() throws javax.ejb.CreateException{
  return (SpouseOccupation) super.idoCreate();
 }

 public SpouseOccupation createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public SpouseOccupation findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (SpouseOccupation) super.idoFindByPrimaryKey(id);
 }

 public SpouseOccupation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SpouseOccupation) super.idoFindByPrimaryKey(pk);
 }

 public SpouseOccupation findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}