package com.idega.block.trade.stockroom.data;


public class SettingsHomeImpl extends com.idega.data.IDOFactory implements SettingsHome
{
 protected Class getEntityInterfaceClass(){
  return Settings.class;
 }


public Settings create(com.idega.data.IDOLegacyEntity p0)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SettingsBMPBean)entity).ejbCreate(p0);
	((SettingsBMPBean)entity).ejbPostCreate(p0);
	this.idoCheckInPooledEntity(entity);
	try{
		return this.findByPrimaryKey(pk);
	}
	catch(javax.ejb.FinderException fe){
		throw new com.idega.data.IDOCreateException(fe);
	}
	catch(Exception e){
		throw new com.idega.data.IDOCreateException(e);
	}
}

 public Settings create() throws javax.ejb.CreateException{
  return (Settings) super.createIDO();
 }


 public Settings findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Settings) super.findByPrimaryKeyIDO(pk);
 }



}