package se.idega.idegaweb.commune.accounting.regulations.data;


public class KeyMappingHomeImpl extends com.idega.data.IDOFactory implements KeyMappingHome
{
 protected Class getEntityInterfaceClass(){
  return KeyMapping.class;
 }


 public KeyMapping create() throws javax.ejb.CreateException{
  return (KeyMapping) super.createIDO();
 }


public KeyMapping findValueByCategoryAndKey(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((KeyMappingBMPBean)entity).ejbFindValueByCategoryAndKey(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public KeyMapping findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (KeyMapping) super.findByPrimaryKeyIDO(pk);
 }



}