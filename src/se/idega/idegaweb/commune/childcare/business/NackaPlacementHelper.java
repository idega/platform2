/*
 * $Id: NackaPlacementHelper.java,v 1.2 2004/11/26 09:17:48 aron Exp $
 * Created on 5.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Date;

import se.idega.idegaweb.commune.care.business.DefaultPlacementHelper;

import com.idega.idegaweb.IWResourceMessage;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2004/11/26 09:17:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public class NackaPlacementHelper extends DefaultPlacementHelper {

    
    public Date getEarliestPlacementDate() {
        IWTimestamp earliestDate = IWTimestamp.RightNow();
	    earliestDate.addWeeks(-2);
		return earliestDate.getDate();
            
    }
    public boolean hasEarliestPlacementDate() {
        return true;
    }
    
    /* (non-Javadoc)
     * @see se.idega.idegaweb.commune.childcare.business.DefaultPlacementHelper#getEarliestPlacementMessage()
     */
    public IWResourceMessage getEarliestPlacementMessage() {
        return new IWResourceMessage("child_care.not_a_valid_date_more_than_two_weeks", "You can not choose a date more than 2 weeks back in time.");
    }
}
