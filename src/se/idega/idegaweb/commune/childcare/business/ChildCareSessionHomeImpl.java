package se.idega.idegaweb.commune.childcare.business;


public class ChildCareSessionHomeImpl extends com.idega.business.IBOHomeImpl implements ChildCareSessionHome
{
 protected Class getBeanInterfaceClass(){
  return ChildCareSession.class;
 }


 public ChildCareSession create() throws javax.ejb.CreateException{
  return (ChildCareSession) super.createIBO();
 }



}