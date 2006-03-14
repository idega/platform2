/**
 * 
 */
package se.idega.idegaweb.commune.care.resource.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookupException;


/**
 * <p>
 * TODO Maris_O Describe Type ResourceClassMemberHome
 * </p>
 *  Last modified: $Date: 2006/03/14 11:31:25 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1.2.1 $
 */
public interface ResourceClassMemberHome extends IDOHome
{

    public ResourceClassMember create() throws javax.ejb.CreateException;

    public ResourceClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbFindAllByRscIdAndMemberId
     */
    public Collection findAllByRscIdAndMemberId(Integer rscId, Integer mbrId) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbHomeCountByRscIdAndMemberId
     */
    public int countByRscIdAndMemberId(Integer rscId, Integer mbrId) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbHomeCountByRscIdsAndUserId
     */
    public int countByRscIdsAndUserId(int[] rscIds, int userId) throws IDOException, IDOLookupException,
            IDOCompositePrimaryKeyException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbHomeCountByRscSchoolTypeSeasonManagementTypeAndCommune
     */
    public int countByRscSchoolTypeSeasonManagementTypeAndCommune(int resourceId, int schoolTypeId, int seasonId,
            String managementTypeId, int communeId, boolean outsideCommune) throws IDOException, IDOLookupException,
            IDOCompositePrimaryKeyException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbFindAllByClassMemberId
     */
    public Collection findAllByClassMemberId(Integer schClassMemberId) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbFindAllByClsMbrIdOrderByRscName
     */
    public Collection findAllByClsMbrIdOrderByRscName(Integer memberId) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbFindByRscIdsAndSeasonId
     */
    public Collection findByRscIdsAndSeasonId(int[] rscIds, int seasonId) throws FinderException, IDOLookupException,
            IDOCompositePrimaryKeyException;

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#ejbHomeGetCountOfResources
     */
    public int getCountOfResources(int schoolClassMemberID, String resourceIDs) throws IDOException;
}
