/*
 * Created on May 4, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.user.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.user.business.UserInfoBusiness;
import com.idega.block.user.business.UserInfoBusinessBean;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;

/**
 * Displays info on a group, such as name, address, home page, etc.
 */
public class GroupInfo extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.user";
	public static final String PARAM_NAME_GROUP_ID = "group_id";
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		_iwrb = getResourceBundle(iwc);
		_biz = UserInfoBusinessBean.getUserInfoBusiness(iwc);
		
		String groupId = iwc.getParameter(PARAM_NAME_GROUP_ID);
		Group group = null;
		if(groupId!=null && groupId.length()>0) {
			try {
				group = _biz.getGroup(iwc, groupId);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if(group == null) {
			System.out.println("No group found to display info on");
		} else {
			add(getGroupInfo(iwc, group));
		}
	}
	
	private PresentationObject getGroupInfo(IWContext iwc, Group group) {
		String phone = "";
		String fax = "";
		if(_showPhone || _showFax) {
			Collection phones = group.getPhones();
			if(phones!=null) {
				Iterator phoneIter = phones.iterator();
				while(phoneIter.hasNext()) {
					Phone phoneObj = (Phone) phoneIter.next();
					if(phoneObj.getPhoneTypeId() == PhoneType.WORK_PHONE_ID) {
						phone = emptyIfNull(phoneObj.getNumber());
						break;
					} else if(phoneObj.getPhoneTypeId() == PhoneType.FAX_NUMBER_ID) {
						fax = emptyIfNull(phoneObj.getNumber());
					}
				}
			}
		}
		
		Table table = new Table();
		int row = 1;
		
		if(_showName) {
			String name = group.getName();
			String nameLabel = _iwrb.getLocalizedString("name", "Name: ");
			table.add(nameLabel, 2, row);
			table.add(name, 3, row++);
		}
		if(_showShortName) {
			String shortName = emptyIfNull(group.getShortName());
			if(_showEmptyFields || (shortName!=null && shortName.length()>0)) {
				String shortNameLabel = _iwrb.getLocalizedString("shor_name", "Short name: ");
				addTextToTable(table, row++, shortNameLabel, shortName);
			}
		}
		if(_showAddress) {
			String address = "";
			try {
				Address addr = _biz.getGroupAddress(iwc, group);
				if(addr!=null) {
					address = addr.getName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(_showEmptyFields || (address!=null && address.length()>0)) {
				String addressLabel = _iwrb.getLocalizedString("address", "Address: ");
				addTextToTable(table, row++, addressLabel, address);
			}
		}
		if(_showPhone) {
			if(_showEmptyFields || (phone!=null && phone.length()>0)) {
				String phoneLabel = _iwrb.getLocalizedString("phone", "Phone: ");
				addTextToTable(table, row++, phoneLabel, phone);
			}
		}
		if(_showFax) {
			if(_showEmptyFields || (fax!=null && fax.length()>0)) {
				String faxLabel = _iwrb.getLocalizedString("fax", "Fax: ");
				addTextToTable(table, row++, faxLabel, fax);
			}
		}
		if(_showHomePage) {
			String homePageURL = emptyIfNull(group.getHomePageURL());
			if(_showEmptyFields || (homePageURL!=null && homePageURL.length()>0)) {
				String homePageLabel = _iwrb.getLocalizedString("homepage", "Homepage: ");
				Link link = new Link();
				link.setText(homePageURL);
				link.setURL(homePageURL);
				addPOToTable(table, row++, homePageLabel, link);
			}
		}
		if(_showEmails) {
			Table emails = getEmailTable(group);
			if(_showEmptyFields || (emails!=null && emails.getRows()>0)) {
				Text emailsLabel = new Text(_iwrb.getLocalizedString("email", "Email: "));
				emailsLabel.setStyle(_textLabelStyle);
				table.add(emailsLabel, 2, row);
				if(emails.getRows()>0) {
					table.add(emails, 3, row++);
				}
			}
		}
		if(_showDescription) {
			String description = emptyIfNull(group.getDescription());
			if(_showEmptyFields || (description!=null && description.length()>0)) {
				String descriptionLabel = _iwrb.getLocalizedString("description", "Description: ");
				addTextToTable(table, row++, descriptionLabel, description);
			}
		}
		if(_showExtraInfo) {
			String extraInfo = emptyIfNull(group.getExtraInfo());
			if(_showEmptyFields || (extraInfo!=null && extraInfo.length()>0)) {
				String extraInfoLabel = _iwrb.getLocalizedString("extra_info", "Info: ");
				addTextToTable(table, row++, extraInfoLabel, extraInfo);
			}
		}
		
		return table;
	}
	
	/**
	 * Utility method for adding text to a table
	 * @param table
	 * @param row
	 * @param strLabel
	 * @param strText
	 */
	private void addTextToTable(Table table, int row, String strLabel, String strText) {
		Text text = new Text(strText);
		text.setStyle(_textInfoStyle);
		Text label = new Text(strLabel);
		label.setStyle(_textLabelStyle);
		table.add(label, 2, row);
		table.add(text, 3, row);
	}
	
	/**
	 * Utility method for adding a PresentationObject to a table
	 * @param table
	 * @param row
	 * @param strLabel
	 * @param po
	 */
	private void addPOToTable(Table table, int row, String strLabel, PresentationObject po) {
		Text label = new Text(strLabel);
		label.setStyle(_textLabelStyle);
		table.add(label, 2, row);
		table.add(po, 3, row);
	}
	
	private String emptyIfNull(String str) {
		return str==null?"":str;
	}
	
	/**
	 * Gets a table containing user's emails
	 * @param user The user
	 * @return The user's emails in a Table
	 */
	private Table getEmailTable(Group group) {
		Table table = new Table();
		int row = 1;
		try {
			Iterator addressIter = group.getEmails().iterator();
			while(addressIter.hasNext()) {
				String address = (String) addressIter.next();
				if(address==null || address.length()==0) {
					continue;
				}
				Link link = new Link(address);
				link.setURL("mailto:" + address);
				link.setSessionId(false);
				table.add(link, 1, row++);
			}
		} catch(Exception e) {
			// don't give a pair of donkeys kiddneys, most likely means there are no emails for user
		}
		return table;
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void setShowName(boolean value) {
		_showName = value;
	}
	
	public void setShowHomePage(boolean value) {
		_showHomePage = value;
	}
	
	public void setShowDescription(boolean value) {
		_showDescription = value;
	}
	
	public void setShowExtraInfo(boolean value) {
		_showExtraInfo = value;
	}
	
	public void setShowShortName(boolean value) {
		_showShortName = value;
	}
	
	public void setShowPhone(boolean value) {
		_showPhone = value;
	}
	
	public void setShowFax(boolean value) {
		_showFax = value;
	}
	
	public void setShowEmails(boolean value) {
		_showEmails = value;
	}
	
	public void setShowAddress(boolean value) {
		_showAddress = value;
	}
	
	public void setTextInfoStyle(String style) {
		_textInfoStyle = style;
	}

	public void setTextLabelStyle(String style) {
		_textLabelStyle = style;
	}
	
	public void setShowEmptyFields(boolean value) {
		_showEmptyFields = value;
	}
	
	private boolean _showName = true;
	private boolean _showHomePage = false;
	private boolean _showDescription = false;
	private boolean _showExtraInfo = false;
	private boolean _showShortName = false;
	private boolean _showPhone = false;
	private boolean _showFax = false;
	private boolean _showEmails = false;
	private boolean _showAddress = false;
	private boolean _showEmptyFields = true;
	private String _textInfoStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;";
	private String _textLabelStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 8pt;color: #000000;";
	
	private UserInfoBusiness _biz = null;
}
