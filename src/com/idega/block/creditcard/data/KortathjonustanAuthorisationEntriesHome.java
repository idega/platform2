package com.idega.block.creditcard.data;


public interface KortathjonustanAuthorisationEntriesHome extends com.idega.data.IDOHome
{
 public KortathjonustanAuthorisationEntries create() throws javax.ejb.CreateException;
 public KortathjonustanAuthorisationEntries findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public KortathjonustanAuthorisationEntries findByAuthorizationCode(java.lang.String p0)throws javax.ejb.FinderException;

}