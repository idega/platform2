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
import com.idega.util.IWTimestamp;

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
		String name = group==null?"":group.getName();
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
		resetColor();
		while(groupIter.hasNext()) {
			User user = (User) groupIter.next();
			table.add(user.getName(), 1, row);
			table.add((new IWTimestamp(user.getDateOfBirth())).getDateString("dd-MM-yyyy"), 2, row);
			String color = getColor();
			table.setColor(1, row, color);
			table.setColor(2, row, color);
			row++;
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
	
	private String getColor() {
		if(_currentColor == _color1) {
			_currentColor = _color2;
		} else {
			_currentColor = _color1;
		}
		return _currentColor;
	}
	
	private void resetColor() {
		_currentColor = null;
	}
	
	private String _currentColor = null;
	private String _color1 = "lightgray";
	private String _color2 = "white";
	
	private GroupBusiness _groupBiz = null;
}
