package se.cubecon.bun24.viewpoint.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * Last modified: $Date: 2003/11/10 19:02:07 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 */
public interface SubCategoryHome extends IDOHome {
    SubCategory create() throws CreateException;
    SubCategory [] findSubCategories (int topCategoryId)
        throws FinderException;
    SubCategory findByPrimaryKey (Integer primaryKey) throws FinderException;
    SubCategory findSubCategoryByName(String name) throws FinderException;
    SubCategory [] findAllSubCategories ()
        throws FinderException;
}
