package se.idega.idegaweb.commune.accounting.posting.business;


public class PostingBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements PostingBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return PostingBusiness.class;
 }


 public PostingBusiness create() throws javax.ejb.CreateException{
  return (PostingBusiness) super.createIBO();
 }



}