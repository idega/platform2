package is.idega.idegaweb.golf.startingtime.business;


public class TeeTimeBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements TeeTimeBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return TeeTimeBusiness.class;
 }


 public TeeTimeBusiness create() throws javax.ejb.CreateException{
  return (TeeTimeBusiness) super.createIBO();
 }



}