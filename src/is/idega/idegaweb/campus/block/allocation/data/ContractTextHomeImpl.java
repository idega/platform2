package is.idega.idegaweb.campus.block.allocation.data;


public class ContractTextHomeImpl extends com.idega.data.IDOFactory implements ContractTextHome
{
 protected Class getEntityInterfaceClass(){
  return ContractText.class;
 }


 public ContractText create() throws javax.ejb.CreateException{
  return (ContractText) super.createIDO();
 }


public java.util.Collection findByLanguage(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractTextBMPBean)entity).ejbFindByLanguage(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ContractText findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractText) super.findByPrimaryKeyIDO(pk);
 }



}