package com.idega.block.banner.data;


public class BannerEntityHomeImpl extends com.idega.data.IDOFactory implements BannerEntityHome
{
 protected Class getEntityInterfaceClass(){
  return BannerEntity.class;
 }

 public BannerEntity create() throws javax.ejb.CreateException{
  return (BannerEntity) super.idoCreate();
 }

 public BannerEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public BannerEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BannerEntity) super.idoFindByPrimaryKey(id);
 }

 public BannerEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BannerEntity) super.idoFindByPrimaryKey(pk);
 }

 public BannerEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}