/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import is.idega.idegaweb.member.isi.block.members.data.MemberGroupData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
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
		add(getMemberInfo(user));
		addBreak();
		addBreak();
		Text regText = new Text(_iwrb.getLocalizedString("member_overview_registration", "Membership status"));
		add(regText);
		add(getMemberRegistrationStatus());
		addBreak();
		Text histText = new Text(_iwrb.getLocalizedString("member_overview_history", "Membership history"));
		add(histText);
		add(getMemberHistory(user));
	}
	
	private PresentationObject getMemberInfo(User user) {
		String name = emptyIfNull(user.getName());
		String nameLabel = _iwrb.getLocalizedString("member_overview_name", "Name: ");
		String pNum = emptyIfNull(user.getPersonalID());
		String pNumLabel = _iwrb.getLocalizedString("member_overview_pn", "Person number: ");
		String address = getInfoFromCollection(user.getAddresses());
		String addressLabel = _iwrb.getLocalizedString("member_overview_address", "Address: ");
		String phone = getInfoFromCollection(user.getPhones());
		String phoneLabel = _iwrb.getLocalizedString("member_overview_phone", "Phone: ");
		
		user.getSystemImageID();
		
		int imageId = user.getSystemImageID();
		Image image = null;
		if(imageId != -1) {
			try {
				image = new Image(imageId, _iwrb.getLocalizedString("member_overview_imag_text", "User picture"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No image found for user " + user.getName());
		}
		
		Table table = new Table();
		table.setCellpadding(1);
		table.setCellspacing(2);
		int row = 1;
		table.add(nameLabel + name, 1, row++);
		table.add(pNumLabel + pNum, 1, row++);
		table.add(addressLabel + address, 1, row++);
		table.add(phoneLabel + phone, 1, row++);
		if(image!=null) {
			table.mergeCells(2, 1, 2, row-1);
			table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_MIDDLE);
			table.add(image, 2, 1);
		}
		return table;
	}
	
	private String getInfoFromCollection(Collection col) {
		StringBuffer buf = new StringBuffer();
		if(col!=null && !col.isEmpty()) {
			Iterator iter = col.iterator();
			boolean first = true;
			while(iter.hasNext()) {
				if(first) {
					buf.append(", ");
					first = false;
				}
				buf.append(iter.next().toString());
				first = false;
			}
		}
		return buf.length()==0?_iwrb.getLocalizedString("member_overview_no_info", "N/A"):buf.toString();
	}
	
	private PresentationObject getMemberHistory(User user) {
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
			table.add(_iwrb.getLocalizedString("member_overview_group", "Group"), 1, row);
			table.add(_iwrb.getLocalizedString("member_overview_begin_date", "Started"), 2, row);
			table.add(_iwrb.getLocalizedString("member_overview_end_date", "Quit"), 3, row++);
			
			Iterator historyIter = history.iterator();
			while(historyIter.hasNext()) {
				GroupRelation rel = (GroupRelation) historyIter.next();
				
				Group group = rel.getGroup();
				
				IWTimestamp from = new IWTimestamp(rel.getInitiationDate());
				IWTimestamp to = null;
				if (rel.getTerminationDate() != null)
					to = new IWTimestamp(rel.getTerminationDate());

				String info = _data.getStringByGroup(group);
				if(info == null) {
					info = group.getName();
				}
				table.add(info, 1, row);
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
	
	private PresentationObject getMemberRegistrationStatus() {
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
	
	public String emptyIfNull(String str) {
		return str==null?"":str;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
		
	//private Integer _userId = new Integer(338609);
	//private User _user = null;
	private MemberGroupData _data = null;
}
