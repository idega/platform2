package is.idega.idegaweb.campus.block.finance.business;


public class CampusAssessmentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CampusAssessmentBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CampusAssessmentBusiness.class;
 }


 public CampusAssessmentBusiness create() throws javax.ejb.CreateException{
  return (CampusAssessmentBusiness) super.createIBO();
 }



}