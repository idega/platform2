package is.idega.idegaweb.campus.block.building.data;


public class BuildingApartmentHomeImpl extends com.idega.data.IDOFactory implements BuildingApartmentHome
{
 protected Class getEntityInterfaceClass(){
  return BuildingApartment.class;
 }

 public BuildingApartment create() throws javax.ejb.CreateException{
  return (BuildingApartment) super.createIDO();
 }

 public BuildingApartment createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public BuildingApartment findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BuildingApartment) super.idoFindByPrimaryKey(id);
 }

 public BuildingApartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BuildingApartment) super.findByPrimaryKeyIDO(pk);
 }

 public BuildingApartment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}