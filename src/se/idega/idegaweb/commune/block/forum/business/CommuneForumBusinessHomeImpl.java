package se.idega.idegaweb.commune.block.forum.business;


public class CommuneForumBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CommuneForumBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CommuneForumBusiness.class;
 }


 public CommuneForumBusiness create() throws javax.ejb.CreateException{
  return (CommuneForumBusiness) super.createIBO();
 }



}