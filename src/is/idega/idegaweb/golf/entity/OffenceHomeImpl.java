package is.idega.idegaweb.golf.entity;


public class OffenceHomeImpl extends com.idega.data.IDOFactory implements OffenceHome
{
 protected Class getEntityInterfaceClass(){
  return Offence.class;
 }

 public Offence create() throws javax.ejb.CreateException{
  return (Offence) super.idoCreate();
 }

 public Offence createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Offence findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Offence) super.idoFindByPrimaryKey(id);
 }

 public Offence findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Offence) super.idoFindByPrimaryKey(pk);
 }

 public Offence findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}