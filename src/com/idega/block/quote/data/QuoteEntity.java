package com.idega.block.quote.data;

import javax.ejb.*;

public interface QuoteEntity extends com.idega.data.IDOEntity
{
 public void setQuoteOrigin(java.lang.String p0) throws java.rmi.RemoteException;
 public int getICLocaleID() throws java.rmi.RemoteException;
 public void setQuoteText(java.lang.String p0) throws java.rmi.RemoteException;
 public void setQuoteAuthor(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getQuoteOrigin() throws java.rmi.RemoteException;
 public java.lang.String getQuoteText() throws java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
 public void setICLocaleID(int p0) throws java.rmi.RemoteException;
 public java.lang.String getQuoteAuthor() throws java.rmi.RemoteException;
}
