package se.cubecon.bun24.viewpoint.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Last modified: $Date: 2003/05/23 08:25:23 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.7 $
 */
public interface Viewpoint extends IDOEntity, Case {
    String CASE_CODE_KEY = "SYMESYN";
    String STATUSKEY_ANSWERED = "ASWD";

    Group getHandlerGroup () throws RemoteException;
    String getCategory () throws RemoteException;
    String getSubject () throws RemoteException;
    String getMessage () throws RemoteException;
    String getAnswer () throws RemoteException;
    Integer getUserId () throws RemoteException;
    boolean isAnswered () throws RemoteException;
    Integer getRoadResponsibleId ();
    String getUserEmail ();
    String getUserName ();

    void setHandlerGroupId (int handlerGroupId) throws RemoteException;
    void setUser (User user) throws RemoteException;
    void setCategory (String category) throws RemoteException;
    void setSubject (String subject) throws RemoteException;
    void setMessage (String message) throws RemoteException;
    void setAnswer (String answer) throws RemoteException;
    void setHandler (User user) throws RemoteException;
    void unsetHandler () throws RemoteException;
    void setRoadResponsibleId (int id);
    void setUserEmail (String email);
    void setUserName (String name);

    Collection ejbFindUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException, RemoteException;
}
