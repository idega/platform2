/*
 * $Id: PointOfView.java,v 1.1 2004/09/29 11:31:40 thomas Exp $
 * Created on Sep 27, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.block.pointOfView.data;

import java.sql.Timestamp;
import com.idega.repository.data.ImplementorPlaceholder;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2004/09/29 11:31:40 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface PointOfView extends ImplementorPlaceholder {
	
	Object getPrimaryKey();
	
    String getCategory ();
    
    String getSubject ();
    
    String getMessage ();
    
    Timestamp getCreated();
    
    Group getHandlerGroup();
    
    
}
