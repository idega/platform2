package is.idega.idegaweb.member.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author Joakim
 *
 */
public class MemberFamilyLogicHomeImpl extends IBOHomeImpl implements MemberFamilyLogicHome {

	protected Class getBeanInterfaceClass() {
		return MemberFamilyLogicHomeImpl.class;
	}

	public MemberFamilyLogic create() throws javax.ejb.CreateException {
		return (MemberFamilyLogic) super.createIBO();
	}
}
