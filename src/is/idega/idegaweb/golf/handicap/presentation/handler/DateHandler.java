/*
 * $Id: DateHandler.java,v 1.1 2005/02/07 14:05:33 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.presentation.handler;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/02/07 14:05:33 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class DateHandler extends DateInput {

	public void main(IWContext iwc) throws Exception {
		setYearRange(2000, new IWTimestamp().getYear());
		super.main(iwc);
	}
}