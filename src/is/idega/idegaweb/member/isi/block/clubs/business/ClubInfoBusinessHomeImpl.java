package is.idega.idegaweb.member.isi.block.clubs.business;


public class ClubInfoBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ClubInfoBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ClubInfoBusiness.class;
 }


 public ClubInfoBusiness create() throws javax.ejb.CreateException{
  return (ClubInfoBusiness) super.createIBO();
 }



}