package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOEntity;
import com.idega.user.data.Group;

/**
 * Last modified: $Date: 2003/11/10 19:02:07 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface SubCategory extends IDOEntity {
    String getName ();
    Group getHandlerGroup ();
    TopCategory getTopCategory ();

    void setName (String name);
    void setTopCategoryId (int id);
    void setHandlerGroupId (int id);
}
