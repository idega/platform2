package se.cubecon.bun24.viewpoint.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.*;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.cubecon.bun24.viewpoint.data.*;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public class ViewpointBusinessBean extends CaseBusinessBean
    implements ViewpointBusiness {

    public final static String ANSWER_KEY = "viewpoint.answer";
    public final static String ANSWER_DEFAULT = "Svar till medborgare";
    public final static String APPLIES_KEY = "viewpoint.applies";
    public final static String APPLIES_DEFAULT = "Avser";
    public final static String ORIGINALVIEWPOINT_KEY
        = "viewpoint.originalViewpoint";
    public final static String ORIGINALVIEWPOINT_DEFAULT
        = "Ursprunglig synpunkt";

    public void createViewpoint (final User user, final String subject,
                          final String message, final String category,
                          final int handlerGroupId)
        throws CreateException, RemoteException {
		final Viewpoint viewpoint = getViewpointHome ().create ();

		viewpoint.setUser (user);
		viewpoint.setSubject (subject);
		viewpoint.setMessage (message);
		viewpoint.setCategory (category);
  		viewpoint.setHandlerGroupId (handlerGroupId);
		viewpoint.store();
    }

    public Viewpoint [] findUnhandledViewpointsInGroups
        (final Group [] groups) throws RemoteException, FinderException {
        return getViewpointHome ().findUnhandledViewpointsInGroups (groups);
    }

	public Viewpoint findViewpoint (final int viewpointID)
        throws RemoteException, FinderException {
		return getViewpointHome ().findByPrimaryKey (new Integer (viewpointID));
	}

    public void registerHandler (final int viewpointId, final User handler)
        throws RemoteException, FinderException {
        final Viewpoint viewpoint = findViewpoint (viewpointId);
        viewpoint.setHandler (handler);
		viewpoint.store();
    }

    public void answerAndDeregisterViewpoint (final int viewpointId,
                                              final String answer)
        throws RemoteException, FinderException, CreateException {

        // 1. find viewpoint
        final Viewpoint viewpoint = findViewpoint (viewpointId);

        // 2. send answer message to citizen
        final String categoryLine = getLocalizedString
                (APPLIES_KEY, APPLIES_DEFAULT).toUpperCase () + ": "
                + viewpoint.getCategory ();
        final String answerLine = getLocalizedString
                (ANSWER_KEY, ANSWER_DEFAULT).toUpperCase () + ": " + answer;
        final String questionLine = "---- " + getLocalizedString
                (ORIGINALVIEWPOINT_KEY,
                 ORIGINALVIEWPOINT_DEFAULT).toUpperCase () + " ----\n\n"
                + viewpoint.getMessage ();
        final String messageBody = categoryLine + "\n\n" + answerLine + "\n\n"
                + questionLine + "\n";
        final int userId = viewpoint.getUserId ();
		final MessageBusiness messageBusiness
                = (MessageBusiness) IBOLookup.getServiceInstance
                (getIWApplicationContext(), MessageBusiness.class);
		final Message message = messageBusiness.createUserMessage
                (viewpoint.getUserId (), "Re: " + viewpoint.getSubject (),
                 messageBody);
		message.setParentCase(viewpoint);
		message.store();

        // 3. save answer and set 'closed' status on case
        viewpoint.setAnswer (answer);
		final CaseStatusHome caseStatusHome
                = (CaseStatusHome) IDOLookup.getHome (CaseStatus.class);
        final CaseStatus statusAnswered = caseStatusHome.findByPrimaryKey
                (Viewpoint.STATUSKEY_ANSWERED);
        viewpoint.setCaseStatus (statusAnswered);
        viewpoint.store ();
    }

    private ViewpointHome getViewpointHome () throws RemoteException {
        return (ViewpointHome) IDOLookup.getHome (Viewpoint.class);
    }

    public TopCategory [] findAllTopCategories () throws RemoteException,
                                                         FinderException {
       final TopCategoryHome topCategoryHome
               = (TopCategoryHome) IDOLookup.getHome (TopCategory.class);
       return topCategoryHome.findAll ();
    }

    public SubCategory [] findSubCategories (final int topCategoryId)
        throws RemoteException, FinderException {
       final SubCategoryHome subCategoryHome
               = (SubCategoryHome) IDOLookup.getHome (SubCategory.class);
       return subCategoryHome.findSubCategories (topCategoryId);
    }

	public SubCategory findSubCategory (final int subCategoryID)
        throws RemoteException, FinderException {
        final SubCategoryHome home
                = (SubCategoryHome) IDOLookup.getHome (SubCategory.class);
		return home.findByPrimaryKey (new Integer (subCategoryID));
	}
}
