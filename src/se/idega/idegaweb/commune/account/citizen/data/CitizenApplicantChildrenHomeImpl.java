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
public class CitizenApplicantChildrenHomeImpl extends IDOFactory
    implements CitizenApplicantChildrenHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantChildren.class;
    }

    public CitizenApplicantChildren create() throws CreateException {
        return (CitizenApplicantChildren) super.createIDO();
    }

    public CitizenApplicantChildren [] findByApplicationId
        (final int applicationId) throws FinderException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((CitizenApplicantChildrenBMPBean) entity)
                .ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity (entity);
        if (ids == null) {
            throw new FinderException ();
        }
        final Collection childrens = getEntityCollectionForPrimaryKeys (ids);
        return (CitizenApplicantChildren []) childrens.toArray
                (new CitizenApplicantChildren [0]);
    }
}
