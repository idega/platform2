package com.idega.block.news.data;


public interface NwNews extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getAuthor();
 public int getContentId();
 public int getNewsCategoryId();
 public java.lang.String getSource();
 public void setAuthor(java.lang.String p0);
 public void setContentId(java.lang.Integer p0);
 public void setContentId(int p0);
 public void setDefaultValues();
 public void setNewsCategoryId(java.lang.Integer p0);
 public void setNewsCategoryId(int p0);
 public void setSource(java.lang.String p0);
}
