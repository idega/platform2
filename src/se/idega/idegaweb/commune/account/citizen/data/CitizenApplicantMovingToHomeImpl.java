package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;

/**
 * Last modified: $Date: 2003/01/14 14:19:37 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public class CitizenApplicantMovingToHomeImpl extends IDOFactory
    implements CitizenApplicantMovingToHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantMovingTo.class;
    }

    public CitizenApplicantMovingTo create() throws CreateException {
        return (CitizenApplicantMovingTo) super.createIDO();
    }

    public CitizenApplicantMovingTo findByApplicationId
        (final int applicationId) throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((CitizenApplicantMovingToBMPBean) entity)
                .ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity (entity);
        if (ids == null || ids.isEmpty ()) {
            throw new FinderException ("Couldn't find CitizenApplicantMovingTo object with application id " + applicationId);
        }
        final Collection collection = getEntityCollectionForPrimaryKeys (ids);
        return (CitizenApplicantMovingTo) collection.iterator ().next ();
    }
}
