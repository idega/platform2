package is.idega.block.nationalregister.business;


public class NationalRegisterBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements NationalRegisterBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return NationalRegisterBusiness.class;
 }


 public NationalRegisterBusiness create() throws javax.ejb.CreateException{
  return (NationalRegisterBusiness) super.createIBO();
 }



}