package is.idega.idegaweb.member.business;

import com.idega.business.IBOHome;


/**
 * @author Joakim
 *
 */
public interface MemberFamilyLogicHome extends IBOHome {

	public MemberFamilyLogic create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
