package com.idega.block.creditcard.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;


public interface KortathjonustanAuthorisationEntriesHome extends com.idega.data.IDOHome
{
 public KortathjonustanAuthorisationEntries create() throws javax.ejb.CreateException;
 public KortathjonustanAuthorisationEntries findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public KortathjonustanAuthorisationEntries findByAuthorizationCode(java.lang.String p0, IWTimestamp stamp)throws javax.ejb.FinderException;
 public Collection findRefunds(IWTimestamp from, IWTimestamp to) throws FinderException;
}