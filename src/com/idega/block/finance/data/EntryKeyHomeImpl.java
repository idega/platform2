package com.idega.block.finance.data;


public class EntryKeyHomeImpl extends com.idega.data.IDOFactory implements EntryKeyHome
{
 protected Class getEntityInterfaceClass(){
  return EntryKey.class;
 }

 public EntryKey create() throws javax.ejb.CreateException{
  return (EntryKey) super.idoCreate();
 }

 public EntryKey createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public EntryKey findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (EntryKey) super.idoFindByPrimaryKey(id);
 }

 public EntryKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EntryKey) super.idoFindByPrimaryKey(pk);
 }

 public EntryKey findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}