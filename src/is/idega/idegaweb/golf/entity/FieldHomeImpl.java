package is.idega.idegaweb.golf.entity;


public class FieldHomeImpl extends com.idega.data.IDOFactory implements FieldHome
{
 protected Class getEntityInterfaceClass(){
  return Field.class;
 }

 public Field create() throws javax.ejb.CreateException{
  return (Field) super.idoCreate();
 }

 public Field createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Field findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Field) super.idoFindByPrimaryKey(id);
 }

 public Field findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Field) super.idoFindByPrimaryKey(pk);
 }

 public Field findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}