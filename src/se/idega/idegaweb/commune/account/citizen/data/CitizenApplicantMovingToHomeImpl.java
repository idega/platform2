package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/12 13:06:59 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public class CitizenApplicantMovingToHomeImpl extends IDOFactory
    implements CitizenApplicantMovingToHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantMovingTo.class;
    }

    public CitizenApplicantMovingTo create() throws CreateException {
        return (CitizenApplicantMovingTo) super.createIDO();
    }

    public Collection findByApplicationId
        (final int applicationId) throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((CitizenApplicantMovingToBMPBean) entity)
                .ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity (entity);
        return getEntityCollectionForPrimaryKeys (ids);
    }
}
