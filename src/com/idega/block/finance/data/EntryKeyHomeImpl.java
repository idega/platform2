package com.idega.block.finance.data;


public class EntryKeyHomeImpl extends com.idega.data.IDOFactory implements EntryKeyHome
{
 protected Class getEntityInterfaceClass(){
  return EntryKey.class;
 }


 public EntryKey create() throws javax.ejb.CreateException{
  return (EntryKey) super.createIDO();
 }


 public EntryKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EntryKey) super.findByPrimaryKeyIDO(pk);
 }



}