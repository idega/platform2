package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/10/06 12:29:53 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface CitizenApplicantMovingTo extends IDOEntity {
    int getApplicationId ();
    String getAddress ();
    String getMovingInDate ();
    String getHousingType ();
    String getPropertyType ();
    String getLandlord ();

    void setApplicationId (int applicationId);
    void setAddress (String address);
    void setMovingInDate (String movingInDate);
    void setHousingType (String housingType);
    void setPropertyType (String propertyType);
    void setLandlord (String name, String phone, String address);
}
