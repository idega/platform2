package is.idega.idegaweb.campus.block.application.data;


public class CurrentResidencyHomeImpl extends com.idega.data.IDOFactory implements CurrentResidencyHome
{
 protected Class getEntityInterfaceClass(){
  return CurrentResidency.class;
 }

 public CurrentResidency create() throws javax.ejb.CreateException{
  return (CurrentResidency) super.idoCreate();
 }

 public CurrentResidency createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CurrentResidency findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CurrentResidency) super.idoFindByPrimaryKey(id);
 }

 public CurrentResidency findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CurrentResidency) super.idoFindByPrimaryKey(pk);
 }

 public CurrentResidency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}