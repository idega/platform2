package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/11/22 12:58:04 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
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
        (final int applicationId) throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((CitizenApplicantCohabitantBMPBean) entity)
                .ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity (entity);
        final Collection cohabitants = getEntityCollectionForPrimaryKeys (ids);
        if (cohabitants.size () < 1) {
            throw new FinderException ("Couldn't find cohabitant with "
                                       + "application id " + applicationId);
        }
        return (CitizenApplicantCohabitant) cohabitants.iterator ().next ();
    }
}
