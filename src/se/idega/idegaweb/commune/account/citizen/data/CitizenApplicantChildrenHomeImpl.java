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
public class CitizenApplicantChildrenHomeImpl extends IDOFactory
    implements CitizenApplicantChildrenHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantChildren.class;
    }

    public CitizenApplicantChildren create() throws CreateException {
        return (CitizenApplicantChildren) super.createIDO();
    }

    public CitizenApplicantChildren [] findByApplicationId
        (final int applicationId) throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((CitizenApplicantChildrenBMPBean) entity)
                .ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity (entity);
        final Collection childrens = getEntityCollectionForPrimaryKeys (ids);
        return (CitizenApplicantChildren []) childrens.toArray
                (new CitizenApplicantChildren [0]);
    }
}
