package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;

/**
 * Last modified: $Date: 2003/10/05 20:03:03 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public class CitizenApplicantCohabitantHomeImpl extends IDOFactory
    implements CitizenApplicantCohabitantHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantCohabitant.class;
    }

    public CitizenApplicantCohabitant create() throws CreateException {
        return (CitizenApplicantCohabitant) super.createIDO();
    }

    public CitizenApplicantCohabitant findByApplicationId
        (final int applicationId) throws FinderException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((CitizenApplicantCohabitantBMPBean) entity)
                .ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity (entity);
        if (ids == null || ids.isEmpty ()) {
            throw new FinderException ("Couldn't find cohabitant with "
                                       + "application id " + applicationId);
        }
        final Collection cohabitants = getEntityCollectionForPrimaryKeys (ids);
        return (CitizenApplicantCohabitant) cohabitants.iterator ().next ();
    }
}
