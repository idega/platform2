package is.idega.idegaweb.member.business;


public class MemberUserBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements MemberUserBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return MemberUserBusiness.class;
 }


 public MemberUserBusiness create() throws javax.ejb.CreateException{
  return (MemberUserBusiness) super.createIBO();
 }



}