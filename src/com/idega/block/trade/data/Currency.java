package com.idega.block.trade.data;


public interface Currency extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getCurrencyAbbreviation();
 public java.lang.String getCurrencyName();
 public java.lang.String getName();
 public void initializeAttributes();
 public void setCurrencyAbbreviation(java.lang.String p0);
 public void setCurrencyName(java.lang.String p0);
}
