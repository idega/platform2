package is.idega.idegaweb.travel.business;


public class InquirerHomeImpl extends com.idega.business.IBOHomeImpl implements InquirerHome
{
 protected Class getBeanInterfaceClass(){
  return Inquirer.class;
 }


 public Inquirer create() throws javax.ejb.CreateException{
  return (Inquirer) super.createIBO();
 }



}