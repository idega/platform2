package se.cubecon.bun24.viewpoint.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.cubecon.bun24.viewpoint.data.SubCategory;
import se.cubecon.bun24.viewpoint.data.SubCategoryHome;
import se.cubecon.bun24.viewpoint.data.TopCategory;
import se.cubecon.bun24.viewpoint.data.TopCategoryHome;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.cubecon.bun24.viewpoint.data.ViewpointHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.process.data.CaseStatusHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Last modified: $Date: 2003/04/02 16:12:22 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.7 $
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
	private final static String CONFIRMSUBJECT_KEY
        = "viewpoint.confirmEnterViewpointSubject";
	private final static String CONFIRMSUBJECT_DEFAULT
        = "Tack för din synpunkt";

    public void createViewpoint (final User user, final String subject,
                                 final String body, final String category,
                                 final int handlerGroupId)
        throws CreateException, RemoteException {
		final Viewpoint viewpoint = getViewpointHome ().create ();
        
		viewpoint.setUser (user);
		viewpoint.setSubject (subject);
		viewpoint.setMessage (body);
		viewpoint.setCategory (category);
  		viewpoint.setHandlerGroupId (handlerGroupId);
		viewpoint.store();

        final String messageBody
                = getLocalizedString (CONFIRMENTERVIEWPOINT_KEY,
                                      CONFIRMENTERVIEWPOINT_DEFAULT)
                + "\n\n-------------------------------------------------------"
                + "-----\n\n" + category + "\n\n" + subject + "\n\n" + body
                + "\n";
        final String messageSubject
                = getLocalizedString (CONFIRMSUBJECT_KEY,
                                      CONFIRMSUBJECT_DEFAULT);
		final MessageBusiness messageBusiness
                = (MessageBusiness) IBOLookup.getServiceInstance
                (getIWApplicationContext(), MessageBusiness.class);
		final Message message = messageBusiness.createUserMessage
                (viewpoint.getUserId (), messageSubject, messageBody);
		message.setParentCase(viewpoint);
		message.store();
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
	
	public SubCategory findSubCategory(String name) throws RemoteException {
		try {
			final SubCategoryHome subCategoryHome = (SubCategoryHome) IDOLookup.getHome (SubCategory.class);
			return subCategoryHome.findSubCategoryByName(name);
		}
		catch (FinderException fe) {
			return null;
		}
	}
}
