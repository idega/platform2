package se.idega.idegaweb.commune.account.citizen.data;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * Last modified: $Date: 2003/10/05 20:03:03 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface CitizenApplicantChildrenHome extends IDOHome {
    public CitizenApplicantChildren create() throws CreateException;
    public CitizenApplicantChildren [] findByApplicationId (int applicantId) throws FinderException;
}
