package se.idega.idegaweb.commune.accounting.update.business;


public class SchoolClassSchoolTypeBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolClassSchoolTypeBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolClassSchoolTypeBusiness.class;
 }


 public SchoolClassSchoolTypeBusiness create() throws javax.ejb.CreateException{
  return (SchoolClassSchoolTypeBusiness) super.createIBO();
 }



}