/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import is.idega.idegaweb.member.isi.block.members.data.MemberGroupData;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MemberOverview extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		User user = iwc.getCurrentUser();
		_iwrb = getResourceBundle(iwc);
		_data = new MemberGroupData(user);
		Text regText = new Text(_iwrb.getLocalizedString("member_overview_registration", "Skraningar"));
		add(regText);
		add(getMemberRegistrationStatus());
		addBreak();
		Text histText = new Text(_iwrb.getLocalizedString("member_overview_history", "Saga"));
		add(histText);
		add(getMemberHistory(user));
	}
	
	public PresentationObject getMemberHistory(User user) {
		Collection history = null;
		int rows = 0;
		try {
			history = (Collection) ((GroupRelationHome) com.idega.data.IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsByRelatedGroup(user.getID(),"GROUP_PARENT");
			rows = history.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rows>0) {
			Table table = new Table(3, rows+1);
			table.setCellpadding(2);
			table.setBorder(2);
			int row = 1;
			table.add(_iwrb.getLocalizedString("member_overview_group", "Hopur"), 1, row);
			//table.add(_iwrb.getLocalizedString("member_overview_state", "Virkni"), 2, row);
			table.add(_iwrb.getLocalizedString("member_overview_begin_date", "Byrjadi"), 2, row);
			table.add(_iwrb.getLocalizedString("member_overview_end_date", "Haetti"), 3, row++);
			
			Iterator historyIter = history.iterator();
			while(historyIter.hasNext()) {
				GroupRelation rel = (GroupRelation) historyIter.next();
				
				IWTimestamp from = new IWTimestamp(rel.getInitiationDate());
				IWTimestamp to = null;
				if (rel.getTerminationDate() != null)
					to = new IWTimestamp(rel.getTerminationDate());

				table.add(rel.getGroup().getName(), 1, row);
				table.add(from.getDateString("dd-MM-yyyy"), 2, row);
				if (to != null) {
					table.add(to.getDateString("dd-MM-yyyy"), 3, row);
				}
				row++;
			}
			
			return table;
		} else {
			return null;
		}
	}
	
	public PresentationObject getMemberRegistrationStatus() {
		try {
			List memberInfo = _data.getMemberInfo();
			Table table = new Table(1, memberInfo.size());
			//table.setBorder(2);
			table.setCellpadding(2);
			table.setBorder(2);
			Iterator miIter = memberInfo.iterator();
			int row = 1;
			while(miIter.hasNext()) {
				String mi = (String) miIter.next();
				table.add(mi, 1, row++);
			}
			return table;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
		
	//private Integer _userId = new Integer(338609);
	//private User _user = null;
	private MemberGroupData _data = null;
}
