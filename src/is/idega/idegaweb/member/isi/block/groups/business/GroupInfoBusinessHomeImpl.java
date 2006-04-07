/*
 * Created on Apr 7, 2006
 */
package is.idega.idegaweb.member.isi.block.groups.business;


public class GroupInfoBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GroupInfoBusinessHome {
	protected Class getBeanInterfaceClass(){
		return GroupInfoBusiness.class;
	}

	public GroupInfoBusiness create() throws javax.ejb.CreateException{
		return (GroupInfoBusiness) super.createIBO();
	}
}