package se.cubecon.bun24.viewpoint.data;

import com.idega.block.process.data.*;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.FinderException;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public class ViewpointBMPBean extends AbstractCaseBMPBean
    implements Viewpoint, Case {
    private static final String CASE_CODE_KEY = "SYMESYN";
    private static final String ENTITY_NAME = "vp_viewpoint";
    private static final String CASE_CODE_DESCRIPTION = "User viewpoint";
	private static final String [] CASE_STATUS_DESCRIPTIONS = { "Answered" };
	private static final String [] CASE_STATUS_KEYS =
    { Viewpoint.STATUSKEY_ANSWERED };

	private static final String COLUMN_CATEGORY = "CATEGORY";
	private static final String COLUMN_USER_ID = "USER_ID";
	private static final String COLUMN_SUBJECT = "SUBJECT";
	private static final String COLUMN_MESSAGE = "BODY";
    private static final String COLUMN_ANSWER = "ANSWER";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

    public String getCaseCodeDescription () {
        return CASE_CODE_DESCRIPTION;
    }

    public String [] getCaseStatusKeys () {
        System.out.println ("Invoking ViewpointBMPBean.getCaseStatusKeys ()");
        return CASE_STATUS_KEYS;
    }

    public String [] getCaseStatusDescriptions () {
        System.out.println
                ("Invoking ViewpointBMPBean.getCaseStatusDescriptions ()");
        return CASE_STATUS_DESCRIPTIONS;
    }

    public void insertStartData () {
        super.insertStartData ();
        System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");
    }

    public void initializeAttributes () {
		addGeneralCaseRelation();
		addAttribute (COLUMN_CATEGORY, "Category", String.class);
       	addAttribute (COLUMN_USER_ID, "User", true, true, Integer.class,
                      "many-to-one", User.class);
		addAttribute (COLUMN_SUBJECT, "Subject", String.class);
		addAttribute (COLUMN_MESSAGE, "Message", String.class, 10000);
		addAttribute (COLUMN_ANSWER, "Answer", String.class, 10000);
    }

    public Group getHandlerGroup () {
        return getHandler ();
    }

    public String getCategory () {
		return getStringColumnValue (COLUMN_CATEGORY);
    }

    public String getSubject () {
		return getStringColumnValue (COLUMN_SUBJECT);
    }

    public String getMessage () {
        return getStringColumnValue (COLUMN_MESSAGE);
    }

    public String getAnswer () {
        return getStringColumnValue (COLUMN_ANSWER);
    }

    public int getUserId () {
        return getIntegerColumnValue (COLUMN_USER_ID). intValue ();
    }

    public void setHandlerGroupId (final int handlerGroupId) {
        setHandler (handlerGroupId);
    }

    public void setUser (final User user) throws RemoteException {
        setColumn (COLUMN_USER_ID, ((Integer) user.getPrimaryKey()).intValue());
    }

    public void setCategory (final String category) {
		setColumn (COLUMN_CATEGORY, category);
    }

    public void setSubject (final String subject) {
		setColumn (COLUMN_SUBJECT, subject);
    }

    public void setMessage (final String message) {
		setColumn (COLUMN_MESSAGE, message);
    }

    public void setAnswer (final String answer) {
        setColumn (COLUMN_ANSWER, answer);
    }

    public void setHandler (final User user) throws RemoteException {
        setOwner (user);
    }

    public Collection ejbFindUnhandledViewpointsInGroups
        (final Group [] groups) throws FinderException, RemoteException {
        final StringBuffer sql = new StringBuffer ();
        if (groups.length == 0) return new ArrayList (); // return empty list
        sql.append ("select * from " + ENTITY_NAME + ", proc_case");
        sql.append (" where " + ENTITY_NAME + "." + ENTITY_NAME + "_id");
        sql.append (" = proc_case.proc_case_id");
        sql.append (" and proc_case.user_id is null");
        for (int i = 0; i < groups.length; i++) {
            sql.append (i == 0 ? " and (" : " or ");
            final int groupId
                    = ((Integer) groups [i].getPrimaryKey ()).intValue ();
            sql.append ("proc_case.handler_group_id = '" + groupId + "'");
        }
        sql.append (")");
        final Collection viewpoints = idoFindIDsBySQL (sql.toString ());
        return viewpoints;
    }
}
