package se.idega.idegaweb.commune.printing.business;


public class DocumentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements DocumentBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return DocumentBusiness.class;
 }


 public DocumentBusiness create() throws javax.ejb.CreateException{
  return (DocumentBusiness) super.createIBO();
 }



}