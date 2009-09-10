package com.idega.block.blog.business;


public class BlogBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements BlogBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return BlogBusiness.class;
 }


 public BlogBusiness create() throws javax.ejb.CreateException{
  return (BlogBusiness) super.createIBO();
 }



}