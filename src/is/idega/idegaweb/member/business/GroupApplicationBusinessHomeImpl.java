package is.idega.idegaweb.member.business;


public class GroupApplicationBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GroupApplicationBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GroupApplicationBusiness.class;
 }

 public GroupApplicationBusiness create() throws javax.ejb.CreateException{
  return (GroupApplicationBusiness) super.createIBO();
 }



}