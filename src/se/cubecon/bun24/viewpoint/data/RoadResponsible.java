package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/05/15 11:44:11 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface RoadResponsible extends IDOEntity {
    String getRoad ();
    String getArea ();
    String getResponsible ();

    void setRoad (String road);
    void setArea (String area);
    void setResponsible (String responsible);
}
