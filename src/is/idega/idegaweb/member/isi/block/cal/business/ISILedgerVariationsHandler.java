/*
 * Created on Apr 6, 2004
 */
package is.idega.idegaweb.member.isi.block.cal.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class ISILedgerVariationsHandler implements LedgerVariationsHandler{
	
	public String getParentGroupName(Collection parentGroups) {
			
		Iterator pgIter = parentGroups.iterator();
		while(pgIter.hasNext()) {
			Group g = (Group) pgIter.next();
			String type = g.getGroupType();
			if(IWMemberConstants.GROUP_TYPE_CLUB.equals(type)) {
				return g.getName();
			}
		}
		return null;
	}
	
	public void saveLedger(IWContext iwc, int groupID, String coachName, int coachGroupID, String date) {
		CalBusiness calBiz = getCalBusiness(iwc);		
		GroupBusiness grBiz =getGroupBusiness(iwc);
		Collection playerGroups = null;
		Group g = null;
		String name = null;

		try {
			g = grBiz.getGroupByGroupID(groupID);			
		}catch (Exception e){
			e.printStackTrace();
		}		

		Collection groupTypes = new ArrayList();
		groupTypes.add(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);

		if( g!= null) {
			if(g.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
				try {
					playerGroups = grBiz.getChildGroupsRecursiveResultFiltered(g,groupTypes,true);
				}catch (Exception e) {
					e.printStackTrace();
				}			
				if(playerGroups != null) {
					Iterator playersGroupIter = playerGroups.iterator();
					while(playersGroupIter.hasNext()) {
						Group group = (Group) playersGroupIter.next();
						name = group.getName();
						Integer grID = (Integer) group.getPrimaryKey();
						calBiz.createNewLedger(name + "_ledger",grID.intValue(),coachName,date,coachGroupID);
					}			
				}
			}			
			else if(g.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)){			 
				name = g.getName();
				calBiz.createNewLedger(name + "_ledger",groupID,coachName,date,coachGroupID);
			}	
		}		
	}
	
	public Collection getParentGroupRelation(IWContext iwc, User user) {
		GroupRelation groupRelation = null;
		Collection parents =null;
		try {
			parents = getMemberFamilyLogic(iwc).getParentsFor(user);
			System.out.println("parents: " + parents.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}		
		return parents;
	}
	public String getParentKey() {
		String parentKey = null;
		parentKey = GroupRelation.class.getName() + ".IC_GROUP_RELATION_ID|RELATED_IC_GROUP_ID";
		return parentKey;
	}
	public MemberFamilyLogic getMemberFamilyLogic(IWApplicationContext iwc) {
		MemberFamilyLogic famLog = null;
		if(famLog ==null) {
			try {
				famLog = (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
			} catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return famLog;
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
