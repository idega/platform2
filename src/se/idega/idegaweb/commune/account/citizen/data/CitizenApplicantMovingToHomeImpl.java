package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOFactory;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/11/15 08:34:29 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class CitizenApplicantMovingToHomeImpl extends IDOFactory
    implements CitizenApplicantMovingToHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantMovingTo.class;
    }

    public CitizenApplicantMovingTo create() throws CreateException {
        return (CitizenApplicantMovingTo) super.createIDO();
    }

    public Collection findByApplicationId (int applicationId) {
        throw new UnsupportedOperationException ();
    }
}
