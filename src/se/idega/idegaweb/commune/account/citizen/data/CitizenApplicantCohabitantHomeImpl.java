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
public class CitizenApplicantCohabitantHomeImpl extends IDOFactory
    implements CitizenApplicantCohabitantHome {

    protected Class getEntityInterfaceClass(){
        return CitizenApplicantCohabitant.class;
    }

    public CitizenApplicantCohabitant create() throws CreateException {
        return (CitizenApplicantCohabitant) super.createIDO();
    }

    public Collection findByApplicationId (int applicationId) {
        throw new UnsupportedOperationException ();
    }
}
