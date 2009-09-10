package com.idega.block.news.data;


public class NewsCategoryAttributeHomeImpl extends com.idega.data.IDOFactory implements NewsCategoryAttributeHome
{
 protected Class getEntityInterfaceClass(){
  return NewsCategoryAttribute.class;
 }

 public NewsCategoryAttribute create() throws javax.ejb.CreateException{
  return (NewsCategoryAttribute) super.idoCreate();
 }

 public NewsCategoryAttribute createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public NewsCategoryAttribute findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (NewsCategoryAttribute) super.idoFindByPrimaryKey(id);
 }

 public NewsCategoryAttribute findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (NewsCategoryAttribute) super.idoFindByPrimaryKey(pk);
 }

 public NewsCategoryAttribute findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}