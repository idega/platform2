package se.idega.idegaweb.commune.accounting.school.business;


public class StudyPathBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements StudyPathBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return StudyPathBusiness.class;
 }


 public StudyPathBusiness create() throws javax.ejb.CreateException{
  return (StudyPathBusiness) super.createIBO();
 }



}