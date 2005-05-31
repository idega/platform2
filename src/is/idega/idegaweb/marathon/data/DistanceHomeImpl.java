/*
 * $Id: DistanceHomeImpl.java,v 1.2 2005/05/31 19:04:34 laddi Exp $
 * Created on May 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.user.data.GroupHomeImpl;


/**
 * Last modified: $Date: 2005/05/31 19:04:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class DistanceHomeImpl extends GroupHomeImpl implements DistanceHome {

	protected Class getEntityInterfaceClass() {
		return Distance.class;
	}
}