/**
 * 
 */
package se.idega.idegaweb.commune.care.resource.data;

import java.sql.Date;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.data.IDOEntity;


/**
 * <p>
 * TODO Maris_O Describe Type ResourceClassMember
 * </p>
 *  Last modified: $Date: 2006/03/14 11:31:24 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1.2.1 $
 */
public interface ResourceClassMember extends IDOEntity
{

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getResource
     */
    public Resource getResource();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getSchoolClassMember
     */
    public SchoolClassMember getSchoolClassMember();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setResourceFK
     */
    public void setResourceFK(int rscId);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getResourceFK
     */
    public int getResourceFK();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setMemberFK
     */
    public void setMemberFK(int memId);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getMemberFK
     */
    public int getMemberFK();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setStartDate
     */
    public void setStartDate(java.util.Date start);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getStartDate
     */
    public Date getStartDate();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setEndDate
     */
    public void setEndDate(java.util.Date end);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getEndDate
     */
    public Date getEndDate();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setRegistratorId
     */
    public void setRegistratorId(int id);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getRegistratorId
     */
    public int getRegistratorId();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setCreatedDate
     */
    public void setCreatedDate(java.util.Date end);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getCreatedDate
     */
    public Date getCreatedDate();

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#setTeacherId
     */
    public void setTeacherId(String tchid);

    /**
     * @see se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberBMPBean#getTeacherId
     */
    public String getTeacherId();
}
