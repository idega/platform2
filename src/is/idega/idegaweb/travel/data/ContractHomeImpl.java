package is.idega.idegaweb.travel.data;


public class ContractHomeImpl extends com.idega.data.IDOFactory implements ContractHome
{
 protected Class getEntityInterfaceClass(){
  return Contract.class;
 }


 public Contract create() throws javax.ejb.CreateException{
  return (Contract) super.createIDO();
 }


 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Contract) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection findByProductId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ContractBMPBean)entity).ejbHomeFindByProductId(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}