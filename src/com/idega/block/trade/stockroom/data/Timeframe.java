package com.idega.block.trade.stockroom.data;

import java.util.Locale;


public interface Timeframe extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getFrom();
 public boolean getIfYearly();
 public java.lang.String getName();
 public String getName(Locale locale);
 public java.sql.Timestamp getTo();
 public boolean getYearly();
 public void setDefaultValue();
 public void setFrom(java.sql.Timestamp p0);
 public void setIfYearly(boolean p0);
 public void setName(java.lang.String p0);
 public void setTo(java.sql.Timestamp p0);
 public void setYearly(boolean p0);
}
