/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import is.idega.idegaweb.member.isi.block.members.data.MemberGroupData;

import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
		_collator = Collator.getInstance(iwc.getLocale());
		
		User user = iwc.getCurrentUser();
		_iwrb = getResourceBundle(iwc);
		_data = new MemberGroupData(user);
		Table table = new Table();
		add(getMemberInfo(user));
		addBreak();
		addBreak();
		
		int row = 1;
		Text regText = new Text(_iwrb.getLocalizedString("member_overview_registration", "Membership status"));
		regText.setBold();
		table.mergeCells(1, row, 3, row);
		table.add(regText, 1, row++);
		row = getMemberRegistrationStatus(table, row);
		row += 2;
		table.mergeCells(1, row, 3, row);
		Text histText = new Text(_iwrb.getLocalizedString("member_overview_history", "Membership history"));
		histText.setBold();
		table.add(histText, 1, row++);
		row = getMemberHistory(user, table, row);
		
		add(table);
	}
	
	private PresentationObject getMemberInfo(User user) {
		String name = emptyIfNull(user.getName());
		Text nameLabel = new Text(_iwrb.getLocalizedString("member_overview_name", "Name: "));
		nameLabel.setBold();
		String pNum = emptyIfNull(user.getPersonalID());
		Text pNumLabel = new Text(_iwrb.getLocalizedString("member_overview_pn", "Person number: "));
		pNumLabel.setBold();
		String address = getInfoFromCollection(user.getAddresses());
		Text addressLabel = new Text(_iwrb.getLocalizedString("member_overview_address", "Address: "));
		addressLabel.setBold();
		String phone = getInfoFromCollection(user.getPhones());
		Text phoneLabel = new Text(_iwrb.getLocalizedString("member_overview_phone", "Phone: "));
		phoneLabel.setBold();
		
		String clubs = null;
		Text clubsLabel = new Text(_iwrb.getLocalizedString("member_overview_clubs", "Clubs: "));
		clubsLabel.setBold();
		List clubList = _data.getClubList();
		int clCount = clubList.size();
		if(clubList != null && clCount>0) {
			StringBuffer clubListBuf = new StringBuffer();
			boolean first = true;
			for(int i = 0; i<clCount; i++) {
				if(!first) {
					clubListBuf.append(", ");
				} else {
					first = false;
				}
				clubListBuf.append(clubList.get(i));
			}
			
			clubs = clubListBuf.toString();
		}
		
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
		table.add(nameLabel, 1, row);
		table.add(name, 2, row++);
		table.add(pNumLabel, 1, row);
		table.add(pNum, 2, row++);
		table.add(addressLabel, 1, row);
		table.add(address, 2, row++);
		table.add(phoneLabel, 1, row);
		table.add(phone, 2, row++);
		table.add(clubsLabel, 1, row);
		table.add(clubs, 2, row++);
		if(image!=null) {
			table.mergeCells(3, 1, 3, row-1);
			table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_MIDDLE);
			table.add(image, 3, 1);
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
	
	private int getMemberRegistrationStatus(Table table, int row) {
		try {
			List memberInfo = _data.getMemberInfo();
			Collections.sort(memberInfo, new Comparator() {

				public int compare(Object arg0, Object arg1) {
					String[] sa0 = (String[]) arg0;
					String[] sa1 = (String[]) arg1;
					int result = _collator.compare(sa0[0], sa1[0]);
					if(result==0) {
						result = _collator.compare(sa0[1], sa1[1]);
						if(result==0) {
							result = _collator.compare(sa0[2], sa1[2]);
						}
					}
					return result;
				}
				
			});
			Iterator miIter = memberInfo.iterator();
			resetColor();
			while(miIter.hasNext()) {
				try {
					String color = getColor();
					table.setColor(1, row, color);
					table.setColor(2, row, color);
					table.setColor(3, row, color);
					String[] mi = (String[]) miIter.next();
					table.add(mi[0], 1, row);
					table.add(mi[1], 2, row);
					table.add(mi[2], 3, row++);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	
	private int getMemberHistory(User user, Table table, int row) {
		Collection history = null;
		try {
			history = (Collection) ((GroupRelationHome) com.idega.data.IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsByRelatedGroup(user.getID(),"GROUP_PARENT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List historyList = new ArrayList(history);
		Collections.sort(historyList, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				Group gr0 = ((GroupRelation) arg0).getGroup();
				Group gr1 = ((GroupRelation) arg1).getGroup();
				String st0 = _data.getStringByGroup(gr0);
				String st1 =  _data.getStringByGroup(gr1);
				if(st0==null) {
					return 1;
				} else if(st1==null) {
					return -1;
				}
				return _collator.compare(st0, st1);
			}
		
		});
		if(historyList.size()>0) {
			table.add(_iwrb.getLocalizedString("member_overview_group", "Group"), 1, row);
			table.add(_iwrb.getLocalizedString("member_overview_begin_date", "Started"), 2, row);
			table.add(_iwrb.getLocalizedString("member_overview_end_date", "Quit"), 3, row++);
			
			Iterator historyIter = historyList.iterator();
			resetColor();
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
				String color = getColor();
				table.setColor(1, row, color);
				table.setColor(2, row, color);
				table.setColor(3, row, color);
				table.add(info, 1, row);
				table.add(from.getDateString("dd-MM-yyyy"), 2, row);
				if (to != null) {
					table.add(to.getDateString("dd-MM-yyyy"), 3, row);
				}
				row++;
			}
		}
		return row;
	}
	
	public String emptyIfNull(String str) {
		return str==null?"":str;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
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
		
	//private Integer _userId = new Integer(338609);
	//private User _user = null;
	private MemberGroupData _data = null;
	
	private Collator _collator = null;
}
