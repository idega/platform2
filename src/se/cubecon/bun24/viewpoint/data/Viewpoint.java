package se.cubecon.bun24.viewpoint.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public interface Viewpoint extends IDOEntity, Case {
    String CASE_CODE_KEY = "SYMESYN";
    String STATUSKEY_ANSWERED = "ASWD";

    Group getHandlerGroup ();
    String getCategory ();
    String getSubject ();
    String getMessage ();
    String getAnswer ();
    int getUserId ();

    void setHandlerGroupId (int handlerGroupId);
    void setUser (User user) throws RemoteException;
    void setCategory (String category);
    void setSubject (String subject);
    void setMessage (String message);
    void setAnswer (String answer);
    void setHandler (User user) throws RemoteException;

    Collection ejbFindUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException, RemoteException;
}
