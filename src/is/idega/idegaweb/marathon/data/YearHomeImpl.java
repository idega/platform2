/*
 * $Id: YearHomeImpl.java,v 1.1 2005/08/01 17:38:19 laddi Exp $
 * Created on Jul 31, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.user.data.GroupHomeImpl;


/**
 * Last modified: $Date: 2005/08/01 17:38:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class YearHomeImpl extends GroupHomeImpl implements YearHome {

	protected Class getEntityInterfaceClass() {
		return Year.class;
	}
}
