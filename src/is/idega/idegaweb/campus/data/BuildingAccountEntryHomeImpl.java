package is.idega.idegaweb.campus.data;


public class BuildingAccountEntryHomeImpl extends com.idega.data.IDOFactory implements BuildingAccountEntryHome
{
 protected Class getEntityInterfaceClass(){
  return BuildingAccountEntry.class;
 }


 public BuildingAccountEntry create() throws javax.ejb.CreateException{
  return (BuildingAccountEntry) super.createIDO();
 }


 public BuildingAccountEntry createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public BuildingAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BuildingAccountEntry) super.findByPrimaryKeyIDO(pk);
 }


 public BuildingAccountEntry findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BuildingAccountEntry) super.findByPrimaryKeyIDO(id);
 }


 public BuildingAccountEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}