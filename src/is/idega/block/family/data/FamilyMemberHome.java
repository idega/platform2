package is.idega.block.family.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public interface FamilyMemberHome extends IDOHome {

	public FamilyMember create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public FamilyMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindAllByFamilyNR
	 */
	public Collection findAllByFamilyNR(String familyNr) throws FinderException, RemoteException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindForUser
	 */
	public FamilyMember findForUser(User user) throws FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindBySSN
	 */
	public FamilyMember findBySSN(String ssn) throws IDORelationshipException, FinderException;
}
