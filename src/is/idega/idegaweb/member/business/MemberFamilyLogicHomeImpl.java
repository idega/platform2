package is.idega.idegaweb.member.business;


public class MemberFamilyLogicHomeImpl extends com.idega.business.IBOHomeImpl implements MemberFamilyLogicHome
{
 protected Class getBeanInterfaceClass(){
  return MemberFamilyLogic.class;
 }


 public MemberFamilyLogic create() throws javax.ejb.CreateException{
  return (MemberFamilyLogic) super.createIBO();
 }



}