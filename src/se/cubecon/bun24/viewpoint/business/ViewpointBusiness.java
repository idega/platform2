package se.cubecon.bun24.viewpoint.business;

import se.cubecon.bun24.viewpoint.data.*;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.CaseCode;
import com.idega.business.IBOService;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public interface ViewpointBusiness extends IBOService, CaseBusiness {
    void createViewpoint (User user, String subject, String message,
                          String category, int handlerGroupId)
        throws CreateException, RemoteException;
	Viewpoint findViewpoint (int viewpointID)
        throws RemoteException, FinderException;
    Viewpoint [] findUnhandledViewpointsInGroups (Group [] groups)
        throws RemoteException, FinderException;
    void registerHandler (int viewpointId, User handler)
        throws RemoteException, FinderException;
    void answerAndDeregisterViewpoint (int viewpointId, String answer)
        throws RemoteException, FinderException, CreateException;

    TopCategory [] findAllTopCategories ()
        throws RemoteException, FinderException;
    SubCategory [] findSubCategories (int topCategoryId)
        throws RemoteException, FinderException;
    SubCategory findSubCategory (int subCategoryId)
        throws RemoteException, FinderException;
}
