package se.idega.idegaweb.commune.complaint.business;


public class ComplaintBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ComplaintBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ComplaintBusiness.class;
 }


 public ComplaintBusiness create() throws javax.ejb.CreateException{
  return (ComplaintBusiness) super.createIBO();
 }



}