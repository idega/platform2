package se.cubecon.bun24.viewpoint.business;

import com.idega.block.process.business.CaseBusiness;
import com.idega.business.IBOService;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import se.cubecon.bun24.viewpoint.data.*;
import se.idega.idegaweb.commune.block.pointOfView.business.PointOfViewBusiness;

/**
 * ViewpointBusiness is a session ejb interface for creating, managing and
 * broking general viewpoints from a system user in IdegaWeb. The user is
 * selecting from a category tree - first a top category, then a sub category -
 * in order to help the system to chose what handler group should be responsible
 * for answering the entered viewpoint.
 * <p>
 * Last modified: $Date: 2004/09/29 11:34:06 $ by $Author: thomas $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.11 $
 * @see com.idega.block.process.business
 * @see com.idega.business
 * @see com.idega.user.data
 * @see javax.ejb
 * @see se.cubecon.bun24.viewpoint.data
 */
public interface ViewpointBusiness extends IBOService, CaseBusiness, PointOfViewBusiness {
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
}
