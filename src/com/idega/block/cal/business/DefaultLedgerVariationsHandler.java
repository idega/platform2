/*
 * Created on Apr 6, 2004
 */
package com.idega.block.cal.business;

import java.util.Collection;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class DefaultLedgerVariationsHandler implements LedgerVariationsHandler{
	
	public String getParentGroupName(Collection parentGroups) {
		return null;
	}
	
	public void saveLedger(IWContext iwc, int groupID, String coachName, int coachGroupID, String date) {
		CalBusiness calBiz = getCalBusiness(iwc);
		
		GroupBusiness grBiz =getGroupBusiness(iwc);
		Group g = null;
		String name = null;
		try {
			g = grBiz.getGroupByGroupID(groupID);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if(g != null) {
			name = g.getName();
		}
		
		calBiz.createNewLedger(name + "_ledger",groupID,coachName,date,coachGroupID);	
	}
	public Collection getParentGroupRelation(IWContext iwc, User user) {
		Collection parents =null;
		return parents;
	}
	public String getParentKey() {
		return "parent";
	}
	
	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
		CalBusiness calBiz = null;
		if (calBiz == null) {
			try {
				calBiz = (CalBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CalBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return calBiz;
	}
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		GroupBusiness groupBiz =null;
		if (groupBiz == null) {
			try {
				groupBiz = (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return groupBiz;
	}

}
