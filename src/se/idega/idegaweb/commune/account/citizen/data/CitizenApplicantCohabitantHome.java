package se.idega.idegaweb.commune.account.citizen.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * Last modified: $Date: 2003/10/06 12:29:53 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 */
public interface CitizenApplicantCohabitantHome extends IDOHome {
    public CitizenApplicantCohabitant create() throws CreateException;
    public CitizenApplicantCohabitant findByApplicationId (int applicantId)
        throws FinderException;
}
