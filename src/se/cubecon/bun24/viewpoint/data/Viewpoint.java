package se.cubecon.bun24.viewpoint.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Last modified: $Date: 2002/11/29 12:17:48 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface Viewpoint extends IDOEntity, Case {
    String CASE_CODE_KEY = "SYMESYN";
    String STATUSKEY_ANSWERED = "ASWD";

    Group getHandlerGroup () throws RemoteException;
    String getCategory () throws RemoteException;
    String getSubject () throws RemoteException;
    String getMessage () throws RemoteException;
    String getAnswer () throws RemoteException;
    int getUserId () throws RemoteException;
    boolean isAnswered () throws RemoteException;

    void setHandlerGroupId (int handlerGroupId) throws RemoteException;
    void setUser (User user) throws RemoteException;
    void setCategory (String category) throws RemoteException;
    void setSubject (String subject) throws RemoteException;
    void setMessage (String message) throws RemoteException;
    void setAnswer (String answer) throws RemoteException;
    void setHandler (User user) throws RemoteException;

    Collection ejbFindUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException, RemoteException;
}
