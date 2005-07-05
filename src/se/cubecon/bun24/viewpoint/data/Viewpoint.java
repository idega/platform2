package se.cubecon.bun24.viewpoint.data;

import java.util.Date;
import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Last modified: $Date: 2005/07/05 16:41:50 $ by $Author: thomas $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.13 $
 */
public interface Viewpoint extends IDOEntity, Case {
 

    Group getHandlerGroup ();
    String getCategory ();
    String getSubject ();
    String getMessage ();
    String getAnswer ();
    Integer getUserId ();
    boolean isAnswered ();
    Integer getRoadResponsibleId ();
    String getUserEmail ();
    String getUserName ();
    Date getAnswerDate ();
    Date getQuestionDate ();

    void setHandlerGroupId (int handlerGroupId);
    void setUser (User user);
    void setCategory (String category);
    void setSubject (String subject);
    void setMessage (String message);
    void setAnswer (String answer);
    void setHandler (User user);
    void unsetHandler ();
    void setRoadResponsibleId (int id);
    void setUserEmail (String email);
    void setUserName (String name);
}
