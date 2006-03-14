/**
 * 
 */
package se.idega.idegaweb.commune.care.resource.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookupException;


/**
 * <p>
 * TODO Maris_O Describe Type ResourceClassMemberHomeImpl
 * </p>
 *  Last modified: $Date: 2006/03/14 11:31:24 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1.2.1 $
 */
public class ResourceClassMemberHomeImpl extends IDOFactory implements ResourceClassMemberHome
{

    protected Class getEntityInterfaceClass()
    {
        return ResourceClassMember.class;
    }

    public ResourceClassMember create() throws javax.ejb.CreateException
    {
        return (ResourceClassMember) super.createIDO();
    }

    public ResourceClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException
    {
        return (ResourceClassMember) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAllByRscIdAndMemberId(Integer rscId, Integer mbrId) throws FinderException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ResourceClassMemberBMPBean) entity).ejbFindAllByRscIdAndMemberId(rscId, mbrId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int countByRscIdAndMemberId(Integer rscId, Integer mbrId) throws IDOException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ResourceClassMemberBMPBean) entity).ejbHomeCountByRscIdAndMemberId(rscId, mbrId);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int countByRscIdsAndUserId(int[] rscIds, int userId) throws IDOException, IDOLookupException,
            IDOCompositePrimaryKeyException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ResourceClassMemberBMPBean) entity).ejbHomeCountByRscIdsAndUserId(rscIds, userId);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int countByRscSchoolTypeSeasonManagementTypeAndCommune(int resourceId, int schoolTypeId, int seasonId,
            String managementTypeId, int communeId, boolean outsideCommune) throws IDOException, IDOLookupException,
            IDOCompositePrimaryKeyException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ResourceClassMemberBMPBean) entity).ejbHomeCountByRscSchoolTypeSeasonManagementTypeAndCommune(
                resourceId, schoolTypeId, seasonId, managementTypeId, communeId, outsideCommune);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findAllByClassMemberId(Integer schClassMemberId) throws FinderException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ResourceClassMemberBMPBean) entity).ejbFindAllByClassMemberId(schClassMemberId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByClsMbrIdOrderByRscName(Integer memberId) throws FinderException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ResourceClassMemberBMPBean) entity).ejbFindAllByClsMbrIdOrderByRscName(memberId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByRscIdsAndSeasonId(int[] rscIds, int seasonId) throws FinderException, IDOLookupException,
            IDOCompositePrimaryKeyException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ResourceClassMemberBMPBean) entity).ejbFindByRscIdsAndSeasonId(rscIds, seasonId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getCountOfResources(int schoolClassMemberID, String resourceIDs) throws IDOException
    {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ResourceClassMemberBMPBean) entity).ejbHomeGetCountOfResources(schoolClassMemberID,
                resourceIDs);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }
}
