package is.idega.idegaweb.campus.block.building.data;


public class ApartmentTypePeriodsHomeImpl extends com.idega.data.IDOFactory implements ApartmentTypePeriodsHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentTypePeriods.class;
 }

 public ApartmentTypePeriods create() throws javax.ejb.CreateException{
  return (ApartmentTypePeriods) super.idoCreate();
 }

 public ApartmentTypePeriods createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ApartmentTypePeriods findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ApartmentTypePeriods) super.idoFindByPrimaryKey(id);
 }

 public ApartmentTypePeriods findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentTypePeriods) super.idoFindByPrimaryKey(pk);
 }

 public ApartmentTypePeriods findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}