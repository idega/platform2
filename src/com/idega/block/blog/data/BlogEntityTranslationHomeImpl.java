package com.idega.block.blog.data;


public class BlogEntityTranslationHomeImpl extends com.idega.data.IDOFactory implements BlogEntityTranslationHome
{
 protected Class getEntityInterfaceClass(){
  return BlogEntityTranslation.class;
 }


 public BlogEntityTranslation create() throws javax.ejb.CreateException{
  return (BlogEntityTranslation) super.createIDO();
 }


 public BlogEntityTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BlogEntityTranslation) super.findByPrimaryKeyIDO(pk);
 }



}