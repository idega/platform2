package se.idega.idegaweb.commune.account.citizen.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

/**
 * Last modified: $Date: 2003/10/06 12:29:53 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
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
        (final int applicationId) throws FinderException {
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
