package se.cubecon.bun24.viewpoint.business;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.cubecon.bun24.viewpoint.data.RoadResponsible;
import se.cubecon.bun24.viewpoint.data.RoadResponsibleHome;
import se.cubecon.bun24.viewpoint.data.SubCategory;
import se.cubecon.bun24.viewpoint.data.SubCategoryHome;
import se.cubecon.bun24.viewpoint.data.TopCategory;
import se.cubecon.bun24.viewpoint.data.TopCategoryHome;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.cubecon.bun24.viewpoint.data.ViewpointBMPBean;
import se.cubecon.bun24.viewpoint.data.ViewpointHome;
import se.cubecon.bun24.viewpoint.presentation.ViewpointForm;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.process.data.CaseStatusHome;
import com.idega.block.process.message.data.Message;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.text.Link;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Last modified: $Date: 2005/10/13 18:36:12 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.17 $
 */
public class ViewpointBusinessBean extends CaseBusinessBean  implements ViewpointBusiness {

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
                                 final int handlerGroupId,
                                 final int roadResponsibleId)
        throws CreateException, RemoteException {
		final Viewpoint viewpoint = getViewpointHome ().create ();
        
		viewpoint.setUser (user);
		viewpoint.setSubject (subject);
		viewpoint.setMessage (body);
		viewpoint.setCategory (category);
  		viewpoint.setHandlerGroupId (handlerGroupId);
        if (roadResponsibleId >= 0) {
            viewpoint.setRoadResponsibleId (roadResponsibleId);
        }
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
		final CommuneMessageBusiness messageBusiness = getMessageBusiness ();
		final Message message = messageBusiness.createUserMessage
                (viewpoint.getUserId ().intValue (), messageSubject,
                 messageBody);
		message.setParentCase(viewpoint);
		message.store();
    }

    public void createViewpoint (final String userName, final String userEmail,
                                 final String subject, final String body,
                                 final String category,
                                 final int handlerGroupId,
                                 final int roadResponsibleId)
        throws CreateException, RemoteException {
		final Viewpoint viewpoint = getViewpointHome ().create ();
        
		viewpoint.setUserName (userName);
		viewpoint.setUserEmail (userEmail);
		viewpoint.setSubject (subject);
		viewpoint.setMessage (body);
		viewpoint.setCategory (category);
  		viewpoint.setHandlerGroupId (handlerGroupId);
        if (roadResponsibleId >= 0) {
            viewpoint.setRoadResponsibleId (roadResponsibleId);
        }
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
		final CommuneMessageBusiness messageBusiness = getMessageBusiness ();
        messageBusiness.sendMessage (userEmail, messageSubject, messageBody);
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

    public void registerHandler (final int viewpointId, final Group handler)
        throws RemoteException, FinderException {
        final Viewpoint viewpoint = findViewpoint (viewpointId);
        final Integer id = (Integer) handler.getPrimaryKey ();
  		viewpoint.setHandlerGroupId (id.intValue ());
        viewpoint.unsetHandler ();
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
                + (questionLine.length () > 400
                   ? (questionLine.substring (0, 397) + "...")
                   : questionLine) + "\n";
        final CommuneMessageBusiness messageBusiness = getMessageBusiness ();
        final Integer userId = viewpoint.getUserId ();
        final String email = viewpoint.getUserEmail ();
        final String subject = "Re: " + viewpoint.getSubject ();
        if (null != userId) {
            final Message message = messageBusiness.createUserMessage
                    (userId.intValue (), subject, messageBody);
            message.setParentCase(viewpoint);
            message.store();
        } else if (null != email) {
            messageBusiness.sendMessage (email, subject, messageBody);
        } else {
            throw new RemoteException ("No user id and no email from user"
                                       + " in viewpoint " + viewpointId);
        }

        // 3. save answer and set 'closed' status on case
        viewpoint.setAnswer (answer);
		final CaseStatusHome caseStatusHome
                = (CaseStatusHome) IDOLookup.getHome (CaseStatus.class);
        final CaseStatus statusAnswered = caseStatusHome.findByPrimaryKey
                (ViewpointBMPBean.STATUSKEY_ANSWERED);
        viewpoint.setCaseStatus (statusAnswered);
        viewpoint.store ();
    }

    private ViewpointHome getViewpointHome () throws RemoteException {
        return (ViewpointHome) IDOLookup.getHome (Viewpoint.class);
    }

    private CommuneMessageBusiness getMessageBusiness () throws RemoteException {
        return (CommuneMessageBusiness) IBOLookup.getServiceInstance
                (getIWApplicationContext(), CommuneMessageBusiness.class);
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

    public Group [] findAllHandlingGroups () throws RemoteException,
                                                    FinderException {
       final SubCategoryHome subCategoryHome
               = (SubCategoryHome) IDOLookup.getHome (SubCategory.class);
       final SubCategory [] categories
               = subCategoryHome.findAllSubCategories ();
       final Map groups = new HashMap ();
       for (int i = 0; i < categories.length; i++) {
           final Group group = categories [i].getHandlerGroup ();
           groups.put (group.getPrimaryKey (), group);
       }
       return (Group []) groups.values ().toArray (new Group [groups.size ()]);
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
		} catch (FinderException fe) {
			return null;
		}
	}

    public RoadResponsible findRoadResponsible (final int id)
        throws FinderException {
		try {
			final RoadResponsibleHome roadResponsibleHome
                    = (RoadResponsibleHome) IDOLookup.getHome
                    (RoadResponsible.class);
			return roadResponsibleHome.findByPrimaryKey (new Integer (id));
		} catch (final IDOLookupException e) {
			throw new FinderException (e.getMessage ());
		}
    }

    public RoadResponsible [] findAllRoadResponsible () throws FinderException {
		try {
            final RoadResponsibleHome roadResponsibleHome
                    = (RoadResponsibleHome) IDOLookup.getHome
                    (RoadResponsible.class);
            return roadResponsibleHome.findAll ();
		} catch (final IDOLookupException e) {
			throw new FinderException (e.getMessage ());
		}
    }
    
    public Link getLinkToPageForViewpoint(int pageID, Viewpoint viewpoint) {
    	String primaryKey = viewpoint.getPrimaryKey().toString();
    	Link viewpointLink = new Link(primaryKey);
		viewpointLink.setPage(pageID);
		viewpointLink.addParameter(ViewpointForm.PARAM_ACTION, ViewpointForm.SHOWVIEWPOINT_ACTION + "");
		viewpointLink.addParameter(ViewpointForm.PARAM_VIEWPOINT_ID, primaryKey);
		return viewpointLink;    	
    }
    
    public String getCaseCodeKeyForViewpoint() throws RemoteException {
    	return ViewpointBMPBean.CASE_CODE_KEY;
    }
}
