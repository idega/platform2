package com.idega.block.banner.data;


public class AdEntityHomeImpl extends com.idega.data.IDOFactory implements AdEntityHome
{
 protected Class getEntityInterfaceClass(){
  return AdEntity.class;
 }


 public AdEntity create() throws javax.ejb.CreateException{
  return (AdEntity) super.createIDO();
 }


 public AdEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public AdEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AdEntity) super.findByPrimaryKeyIDO(pk);
 }


 public AdEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AdEntity) super.findByPrimaryKeyIDO(id);
 }


 public AdEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}