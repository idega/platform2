package com.idega.block.quote.data;


public interface QuoteEntity extends com.idega.data.IDOEntity
{
 public int getICLocaleID();
 public java.lang.String getIDColumnName();
 public java.lang.String getQuoteAuthor();
 public java.lang.String getQuoteOrigin();
 public java.lang.String getQuoteText();
 public void initializeAttributes();
 public void setICLocaleID(int p0);
 public void setQuoteAuthor(java.lang.String p0);
 public void setQuoteOrigin(java.lang.String p0);
 public void setQuoteText(java.lang.String p0);
}
