package is.idega.idegaweb.travel.business;


public class AssignerHomeImpl extends com.idega.business.IBOHomeImpl implements AssignerHome
{
 protected Class getBeanInterfaceClass(){
  return Assigner.class;
 }


 public Assigner create() throws javax.ejb.CreateException{
  return (Assigner) super.createIBO();
 }



}