package se.cubecon.bun24.viewpoint.business;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.cubecon.bun24.viewpoint.data.RoadResponsible;
import se.cubecon.bun24.viewpoint.data.SubCategory;
import se.cubecon.bun24.viewpoint.data.TopCategory;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import com.idega.block.process.business.CaseBusiness;
import com.idega.business.IBOService;
import com.idega.presentation.text.Link;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * ViewpointBusiness is a session ejb interface for creating, managing and
 * broking general viewpoints from a system user in IdegaWeb. The user is
 * selecting from a category tree - first a top category, then a sub category -
 * in order to help the system to chose what handler group should be responsible
 * for answering the entered viewpoint.
 * <p>
 * Last modified: $Date: 2005/07/07 15:23:35 $ by $Author: thomas $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.13 $
 * @see com.idega.block.process.business
 * @see com.idega.business
 * @see com.idega.user.data
 * @see javax.ejb
 * @see se.cubecon.bun24.viewpoint.data
 */
public interface ViewpointBusiness extends IBOService, CaseBusiness {
	String CONFIRMENTERVIEWPOINT_KEY = "viewpoint.confirmEnterViewpoint";
	String CONFIRMENTERVIEWPOINT_DEFAULT
        = "Tack för din synpunkt. Den är nu registrerad som ett ärende."
        + "En handläggare kommer att hantera och besvara ärendet.";

    /**
     * Creates a new viewpoint and stores it in the database.
     *
     * @param user the user that enters the viewpoint
     * @param subject a free text subject for the viewpoint
     * @param message the actual viewpoint text
     * @param category the name of the user chosen viewpoint category
     * @param handlerGroupId group responsible for answering the viewpoint
     */
    void createViewpoint (User user, String subject, String message,
                          String category, int handlerGroupId,
                          int roadResponsibleId)
        throws CreateException, RemoteException;
    void createViewpoint (String userName, String userEmail, String subject,
                          String message, String category, int handlerGroupId,
                          int roadResponsibleId)
        throws CreateException, RemoteException;
	Viewpoint findViewpoint (int viewpointID)
        throws RemoteException, FinderException;
    Viewpoint [] findUnhandledViewpointsInGroups (Group [] groups)
        throws RemoteException, FinderException;
    void registerHandler (int viewpointId, User handler)
        throws RemoteException, FinderException;
    void registerHandler (int viewpointId, Group handler)
        throws RemoteException, FinderException;
    void answerAndDeregisterViewpoint (int viewpointId, String answer)
        throws RemoteException, FinderException, CreateException;

    TopCategory [] findAllTopCategories ()
        throws RemoteException, FinderException;
    SubCategory [] findSubCategories (int topCategoryId)
        throws RemoteException, FinderException;
    SubCategory findSubCategory (int subCategoryId)
        throws RemoteException, FinderException;
	SubCategory findSubCategory(String name) throws RemoteException;
    Group [] findAllHandlingGroups () throws RemoteException, FinderException;
    RoadResponsible findRoadResponsible (int RoadResponsibleId)
        throws FinderException;
    RoadResponsible [] findAllRoadResponsible () throws FinderException;
    Link getLinkToPageForViewpoint(int pageID, Viewpoint viewpoint);
    String getCaseCodeKeyForViewpoint() throws RemoteException;

}
