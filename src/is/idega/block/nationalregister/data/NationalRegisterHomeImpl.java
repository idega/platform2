package is.idega.block.nationalregister.data;


public class NationalRegisterHomeImpl extends com.idega.data.IDOFactory implements NationalRegisterHome
{
 protected Class getEntityInterfaceClass(){
  return NationalRegister.class;
 }


 public NationalRegister create() throws javax.ejb.CreateException{
  return (NationalRegister) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((NationalRegisterBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySSN(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((NationalRegisterBMPBean)entity).ejbFindAllBySSN(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public NationalRegister findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (NationalRegister) super.findByPrimaryKeyIDO(pk);
 }



}