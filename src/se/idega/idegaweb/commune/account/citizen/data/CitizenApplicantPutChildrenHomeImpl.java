package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/11 12:50:49 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public class CitizenApplicantPutChildrenHomeImpl extends IDOFactory
    implements CitizenApplicantPutChildrenHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantPutChildren.class;
    }

    public CitizenApplicantPutChildren create() throws CreateException {
        return (CitizenApplicantPutChildren) super.createIDO();
    }

    public Collection findByApplicationId (int applicationId) throws FinderException {
        IDOEntity entity = idoCheckOutPooledEntity();
        Collection ids = ((CitizenApplicantPutChildrenBMPBean)entity).ejbFindByApplicationId(applicationId);
        idoCheckInPooledEntity(entity);
        return getEntityCollectionForPrimaryKeys(ids);
    }
}
