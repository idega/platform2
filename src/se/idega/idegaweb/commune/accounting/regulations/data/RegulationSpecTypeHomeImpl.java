package se.idega.idegaweb.commune.accounting.regulations.data;


public class RegulationSpecTypeHomeImpl extends com.idega.data.IDOFactory implements RegulationSpecTypeHome
{
 protected Class getEntityInterfaceClass(){
  return RegulationSpecType.class;
 }


 public RegulationSpecType create() throws javax.ejb.CreateException{
  return (RegulationSpecType) super.createIDO();
 }


public java.util.Collection findAllRegulationSpecTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegulationSpecTypeBMPBean)entity).ejbFindAllRegulationSpecTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public RegulationSpecType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RegulationSpecType) super.findByPrimaryKeyIDO(pk);
 }



}