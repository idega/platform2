package com.idega.block.text.data;


public class ContentHomeImpl extends com.idega.data.IDOFactory implements ContentHome
{
 protected Class getEntityInterfaceClass(){
  return Content.class;
 }


 public Content create() throws javax.ejb.CreateException{
  return (Content) super.createIDO();
 }


 public Content createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Content findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Content) super.findByPrimaryKeyIDO(pk);
 }


 public Content findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Content) super.findByPrimaryKeyIDO(id);
 }


 public Content findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}