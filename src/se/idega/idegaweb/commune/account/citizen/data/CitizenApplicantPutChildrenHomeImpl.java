package se.idega.idegaweb.commune.account.citizen.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

/**
 * Last modified: $Date: 2003/04/02 16:12:21 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
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
