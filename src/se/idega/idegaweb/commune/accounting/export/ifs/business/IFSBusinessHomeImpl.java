package se.idega.idegaweb.commune.accounting.export.ifs.business;


public class IFSBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements IFSBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return IFSBusiness.class;
 }


 public IFSBusiness create() throws javax.ejb.CreateException{
  return (IFSBusiness) super.createIBO();
 }



}