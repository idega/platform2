package is.idega.idegaweb.golf.business;


public class ScorecardBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ScorecardBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ScorecardBusiness.class;
 }


 public ScorecardBusiness create() throws javax.ejb.CreateException{
  return (ScorecardBusiness) super.createIBO();
 }



}