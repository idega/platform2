/*
 * Created on Mar 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroupMemberList extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		setGroupBusiness(iwc);
		setUserBusiness(iwc);
		_iwrb = getResourceBundle(iwc);
		_group = getGroup(iwc, _groupId);
		_club = getGroup(iwc, _clubId);
		add(_group.getName());
		addBreak();
		add(getPlayerList());
	}

	private PresentationObject getPlayerList() {
		/*try {
			users = _userBiz.getUsersInGroup(_group);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}*/

		Table table = new Table();
		Iterator groupIter = _group.getChildren();
		int row = 1;
		while(groupIter.hasNext()) {
			Group group = (Group) groupIter.next();
			System.out.print("Checking child " + group.getName() + " of group");
			if(group.isUser()) {
				System.out.print(" (is user)");
				table.add(group.getName(), 1, row++);
			}
			System.out.println();
		}
		
		return table;
	}
	
	private Group getGroup(IWContext iwc, int groupId) {
		Group group = null;
		try {
			group = _groupBiz.getGroupByGroupID(_groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return group;
	}
	
	private void setGroupBusiness(IWContext iwc) {
		if(_groupBiz == null) {
			try {
				_groupBiz = (GroupBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), GroupBusiness.class);
			} catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setUserBusiness(IWContext iwc) {
		if(_userBiz == null) {
			try {
				_userBiz = (UserBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), UserBusiness.class);
			} catch (IBOLookupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private GroupBusiness _groupBiz = null;
	private UserBusiness _userBiz = null;
	private int _groupId = 338342;
	private Group _group = null;
	private int _clubId = 330185; // the club being viewed
	private Group _club = null;
}
