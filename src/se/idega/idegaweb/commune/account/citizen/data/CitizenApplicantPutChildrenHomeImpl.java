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
public class CitizenApplicantPutChildrenHomeImpl extends IDOFactory
    implements CitizenApplicantPutChildrenHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantPutChildren.class;
    }

    public CitizenApplicantPutChildren create() throws CreateException {
        return (CitizenApplicantPutChildren) super.createIDO();
    }

    public CitizenApplicantPutChildren findByApplicationId (int applicationId) throws FinderException {
        IDOEntity entity = idoCheckOutPooledEntity();
        Collection ids = ((CitizenApplicantPutChildrenBMPBean)entity).ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity(entity);
        if (ids == null || ids.isEmpty ()) {
            throw new FinderException ("Couldn't find CitizenApplicantPutChildren object with application id " + applicationId);
        }
        final Collection collection = getEntityCollectionForPrimaryKeys(ids);
        return (CitizenApplicantPutChildren) collection.iterator ().next ();
    }
}
