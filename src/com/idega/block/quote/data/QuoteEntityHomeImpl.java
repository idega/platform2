package com.idega.block.quote.data;


public class QuoteEntityHomeImpl extends com.idega.data.IDOFactory implements QuoteEntityHome
{
 protected Class getEntityInterfaceClass(){
  return QuoteEntity.class;
 }

 public QuoteEntity create() throws javax.ejb.CreateException{
  return (QuoteEntity) super.idoCreate();
 }

 public QuoteEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public QuoteEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (QuoteEntity) super.idoFindByPrimaryKey(id);
 }

 public QuoteEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (QuoteEntity) super.idoFindByPrimaryKey(pk);
 }

 public QuoteEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}