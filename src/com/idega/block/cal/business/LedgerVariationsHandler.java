/*
 * Created on Apr 6, 2004
 */
package com.idega.block.cal.business;

import java.util.Collection;

import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public interface LedgerVariationsHandler{
	
	public String getParentGroupName(Collection parentGroups);
	public void saveLedger(IWContext iwc, int groupID, String coachName, int coachGroupID, String date);
	public Collection getParentGroupRelation(IWContext iwc, User user);
	public String getParentKey();

}
