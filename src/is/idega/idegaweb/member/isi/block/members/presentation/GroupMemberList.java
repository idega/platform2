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

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
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
	private static final String PARAM_NAME_GROUP_ID = "group_id";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		String groupId = iwc.getParameter(PARAM_NAME_GROUP_ID);
		if(groupId == null || groupId.length()==0) {
			System.out.println("no group to display players for");
		}
		Group group = null;
		try {
			group = getGroup(iwc, Integer.parseInt(groupId));
		} catch(Exception e) {
			e.printStackTrace();
		}
		String name = group==null?"null":group.getName();
		Text title = new Text(name + ": ");
		title.setBold();
		add(title);
		addBreak();
		if(group!=null) {
			add(getPlayerList(iwc, group));
		}
	}

	private PresentationObject getPlayerList(IWContext iwc, Group group) {
		Table table = new Table();
		Iterator groupIter; //group.getChildren();
		try {
			groupIter = getGroupBusiness(iwc).getUsersDirectlyRelated(group).iterator();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		int row = 1;
		while(groupIter.hasNext()) {
			Group childGroup = (Group) groupIter.next();
			System.out.print("Checking child " + childGroup.getName() + " of group (" + childGroup.getPrimaryKey() + ")");
			if(childGroup.isUser()) {
				System.out.print(" (is user)");
				table.add(childGroup.getName(), 1, row++);
			}
			System.out.println();
		}
		
		return table;
	}
	
	private Group getGroup(IWContext iwc, int groupId) {
		Group group = null;
		try {
			group = getGroupBusiness(iwc).getGroupByGroupID(groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return group;
	}
	
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		if(_groupBiz == null) {
			try {
				_groupBiz = (GroupBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), GroupBusiness.class);
			} catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		
		return _groupBiz;
	}
	
	private GroupBusiness _groupBiz = null;
}
