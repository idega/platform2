package se.cubecon.bun24.viewpoint.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * Last modified: $Date: 2003/11/10 18:59:32 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface TopCategoryHome extends IDOHome {
    TopCategory create() throws CreateException;
    TopCategory [] findAll () throws FinderException;
}
