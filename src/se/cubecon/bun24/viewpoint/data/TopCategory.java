package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/11/10 18:59:32 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface TopCategory extends IDOEntity {
    String getName ();
    void setName (String name);
}
