/**
 * 
 */
package se.idega.idegaweb.commune.care.resource.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.care.resource.data.Resource;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMember;
import se.idega.idegaweb.commune.care.resource.data.ResourcePermission;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOService;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;


/**
 * <p>
 * TODO Maris_O Describe Type ResourceBusiness
 * </p>
 *  Last modified: $Date: 2006/03/21 08:58:42 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1.2.1 $
 */
public interface ResourceBusiness extends IBOService
{

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#findAllSchoolTypes
     */
    public Collection findAllSchoolTypes() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#findAllSchoolYears
     */
    public Collection findAllSchoolYears() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#findAllResources
     */
    public Collection findAllResources() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getResourceByPrimaryKey
     */
    public Resource getResourceByPrimaryKey(Integer pk) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getResourceByName
     */
    public Resource getResourceByName(String name) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getAssignRightResourcesForGroup
     */
    public Collection getAssignRightResourcesForGroup(Integer grpId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getViewRightResourcesForGroup
     */
    public Collection getViewRightResourcesForGroup(Integer grpId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#hasResources
     */
    public boolean hasResources(int schoolClassMemberID, String resourceIDs) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getAssignableResourcesForPlacement
     */
    public Collection getAssignableResourcesForPlacement(Integer grpID, Integer clsMemberID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getAssignableResourcesByYearAndType
     */
    public Collection getAssignableResourcesByYearAndType(String yearIdStr, String typeIdStr)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getSchoolClassMember
     */
    public SchoolClassMember getSchoolClassMember(Integer memberID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getSchoolBusiness
     */
    public SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getRscPermByRscAndGrpId
     */
    public ResourcePermission getRscPermByRscAndGrpId(Integer rscId, Integer grpId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getResourcePlacementsByMemberId
     */
    public Collection getResourcePlacementsByMemberId(Integer memberId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getResourcePlacementsByMbrIdOrderByRscName
     */
    public Collection getResourcePlacementsByMbrIdOrderByRscName(Integer memberId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#countResourcePlacementsByRscIDAndMemberID
     */
    public int countResourcePlacementsByRscIDAndMemberID(Integer rID, Integer mID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#saveResource
     */
    public void saveResource(boolean isSavingExisting, String rscName, int[] typeInts, int[] yearInts,
            boolean permitAssign, boolean permitView, int grpId, int rscId) throws ResourceException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#createResource
     */
    public void createResource(String name, int[] typeInts, int[] yearInts) throws RemoteException, CreateException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#createResourcePermission
     */
    public void createResourcePermission(int rscId, int grpId, boolean permitAssign, boolean permitView)
            throws RemoteException, CreateException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#createResourcePlacement
     */
    public ResourceClassMember createResourcePlacement(int rscId, int memberId, String startDateStr)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#createResourcePlacement
     */
    public ResourceClassMember createResourcePlacement(int rscId, int memberId, String startDateStr, int registratorID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#createResourcePlacement
     */
    public void createResourcePlacement(int rscId, int memberId, String startDateStr, String endDateStr,
            int registratorID, String teacherId) throws RemoteException, DateException, ResourceException,
            ClassMemberException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#createResourcePlacement
     */
    public void createResourcePlacement(int rscId, int schClsMbrID, String startDateStr, String endDateStr,
            int registratorID, boolean isCentralAdmin, String teacherId) throws RemoteException, DateException,
            ResourceException, ClassMemberException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#finishResourceClassMember
     */
    public void finishResourceClassMember(Integer schClsMbrID, Integer rscClsMbrId, String startDateStr,
            String endDateStr) throws FinderException, RemoteException, DateException, ClassMemberException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#finishResourceClassMember
     */
    public void finishResourceClassMember(Integer schClsMbrID, Integer rscClsMbrId, String startDateStr,
            String endDateStr, boolean isCentralAdmin) throws FinderException, RemoteException, DateException,
            ClassMemberException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#deletePermissionsForResource
     */
    public void deletePermissionsForResource(Integer rscId) throws RemoteException, FinderException, RemoveException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#deleteResourceClassMember
     */
    public void deleteResourceClassMember(Integer memberId) throws RemoteException, FinderException, RemoveException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getRelatedSchoolTypes
     */
    public Map getRelatedSchoolTypes(Resource rsc) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getRelatedSchoolYears
     */
    public Map getRelatedSchoolYears(Resource rsc) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#removeResource
     */
    public void removeResource(Integer rscId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#findAllSchoolCategories
     */
    public Collection findAllSchoolCategories() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#findAllResourcesByCategory
     */
    public Collection findAllResourcesByCategory(String schCategoryID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getResourcesString
     */
    public String getResourcesString(SchoolClassMember placement) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getResourcesStringXtraInfo
     */
    public String getResourcesStringXtraInfo(SchoolClassMember placement) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getSchoolCategoryHome
     */
    public SchoolCategoryHome getSchoolCategoryHome() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.business.ResourceBusinessBean#getUserBusiness
     */
    public UserBusiness getUserBusiness() throws RemoteException;
}
