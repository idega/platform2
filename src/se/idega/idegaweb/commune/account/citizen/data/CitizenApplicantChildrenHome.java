package se.idega.idegaweb.commune.account.citizen.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * Last modified: $Date: 2003/10/06 13:03:10 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 */
public interface CitizenApplicantChildrenHome extends IDOHome {
    public CitizenApplicantChildren create() throws CreateException;
    public CitizenApplicantChildren [] findByApplicationId (int applicantId) throws FinderException;
}
