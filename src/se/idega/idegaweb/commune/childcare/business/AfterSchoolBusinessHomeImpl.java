package se.idega.idegaweb.commune.childcare.business;


public class AfterSchoolBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements AfterSchoolBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return AfterSchoolBusiness.class;
 }


 public AfterSchoolBusiness create() throws javax.ejb.CreateException{
  return (AfterSchoolBusiness) super.createIBO();
 }



}