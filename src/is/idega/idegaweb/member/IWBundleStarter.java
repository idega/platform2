/*
 * $Id: IWBundleStarter.java,v 1.14 2004/09/08 11:15:54 thomas Exp $
 * Created on Sep 8, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member;

import is.idega.idegaweb.member.util.IWMemberConstants;
import com.idega.block.dataquery.business.DataqueryConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;


/**
 * 
 *  Last modified: $Date: 2004/09/08 11:15:54 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.14 $
 */
public class IWBundleStarter implements IWBundleStartable {

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		// setting the group types of the highest top nodes for the query builder
		DataqueryConstants.highestTopNodeGroupTypes = new String[] {
				IWMemberConstants.GROUP_TYPE_FEDERATION,
				IWMemberConstants.GROUP_TYPE_UNION,
				IWMemberConstants.GROUP_TYPE_REGIONAL_UNION,
				IWMemberConstants.GROUP_TYPE_LEAGUE,
				IWMemberConstants.GROUP_TYPE_CLUB,
				IWMemberConstants.GROUP_TYPE_CLUB_DIVISION};
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		//nothing to do
	}
}
