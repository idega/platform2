package com.idega.block.text.data;


public class TxTextHomeImpl extends com.idega.data.IDOFactory implements TxTextHome
{
 protected Class getEntityInterfaceClass(){
  return TxText.class;
 }

 public TxText create() throws javax.ejb.CreateException{
  return (TxText) super.idoCreate();
 }

 public TxText createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TxText findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TxText) super.idoFindByPrimaryKey(id);
 }

 public TxText findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TxText) super.idoFindByPrimaryKey(pk);
 }

 public TxText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}