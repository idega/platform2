package com.idega.block.news.data;

import com.idega.block.text.data.Content;


public interface NwNews extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getAuthor();
 public int getContentId();
 public Content getContent();
 public int getNewsCategoryId();
 public com.idega.block.category.data.ICCategory getNewsCategory();
 public java.util.Collection getRelatedFiles()throws com.idega.data.IDORelationshipException;
 public java.lang.String getSource();
 public void initializeAttributes();
 public void setAuthor(java.lang.String p0);
 public void setContentId(int p0);
 public void setContentId(java.lang.Integer p0);
 public void setNewsCategoryId(int p0);
 public void setNewsCategoryId(java.lang.Integer p0);
 public void setSource(java.lang.String p0);
}
