package com.idega.block.finance.data;


public class EntryGroupHomeImpl extends com.idega.data.IDOFactory implements EntryGroupHome
{
 protected Class getEntityInterfaceClass(){
  return EntryGroup.class;
 }

 public EntryGroup create() throws javax.ejb.CreateException{
  return (EntryGroup) super.idoCreate();
 }

 public EntryGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public EntryGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (EntryGroup) super.idoFindByPrimaryKey(id);
 }

 public EntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EntryGroup) super.idoFindByPrimaryKey(pk);
 }

 public EntryGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}