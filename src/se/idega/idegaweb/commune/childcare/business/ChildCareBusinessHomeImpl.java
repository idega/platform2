package se.idega.idegaweb.commune.childcare.business;


public class ChildCareBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ChildCareBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ChildCareBusiness.class;
 }


 public ChildCareBusiness create() throws javax.ejb.CreateException{
  return (ChildCareBusiness) super.createIBO();
 }



}