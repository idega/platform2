/*
 * Created on Mar 18, 2004
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.DefaultLedgerVariationsHandler;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserPropertyWindow;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CashierLedgerWindow extends CashierSubWindowTemplate{
	public static String NEW_USER_IN_LEDGER = "user_new_in_ledger_";
	public static final String BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS = "ledger_variations_class";
	private String bold = "bold";
	
	public CashierLedgerWindow() {
		super();
	}
	public void main(IWContext iwc) {
		EntityToPresentationObjectConverter converterLink = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				User user = (User) entity;		
				CalendarLedger ledger = null;
				Collection groups = null;
				boolean isInGroup = false;

				String metaData = user.getMetaData(NEW_USER_IN_LEDGER);
				if(metaData != null && !metaData.equals("") && !metaData.equals("-1")) {
					int ledGroupID = -1;
					if(metaData != null && !metaData.equals("")) {
						try {
							ledGroupID = Integer.parseInt(metaData);
						}catch(NumberFormatException nfe) {
							ledGroupID = -1;
						}					
					}				
					PresentationObject text = browser.getDefaultConverter().getPresentationObject(entity, path, browser, iwc);
					Integer userID = (Integer) user.getPrimaryKey();
					try {
						groups = getUserBusiness(iwc).getUserGroupsDirectlyRelated(user);
					}catch(Exception e) {
						e.printStackTrace();
					}
					Iterator groupIter = groups.iterator();
					//go through the groupIDs to see if the user is in the ledgerGroup
					while(groupIter.hasNext()) {
						Group g = (Group) groupIter.next();
						if(g != null) {
							Integer groupID = (Integer) g.getPrimaryKey();
							if(ledGroupID == groupID.intValue()) {
								isInGroup = true;
								user.setMetaData(NEW_USER_IN_LEDGER,"-1");
								user.store();
							}
						}				
					}
					UserBusiness userBiz = getUserBusiness(iwc);
					Collection parentGroups = null;
					String divisionNameField = null;
					String clubNameField =  null;
					LedgerVariationsHandler ledgerVariationsHandler = getLedgerVariationsHandler(iwc);
					Group ledgerGroup = null;
					try{
						ledgerGroup = userBiz.getGroupBusiness().getGroupByGroupID(ledGroupID);
						parentGroups = userBiz.getGroupBusiness().getParentGroupsRecursive(userBiz.getGroupBusiness().getGroupByGroupID(ledGroupID));
					}catch(Exception e) {
						ledgerGroup = null;
					}		
					if(parentGroups != null) {						
						divisionNameField = ledgerVariationsHandler.getParentGroupName(parentGroups);
						clubNameField = ledgerVariationsHandler.getParentOfParentGroupName(parentGroups);
					}
					Link aLink = null;
					IWResourceBundle iwrb = getResourceBundle(iwc);
					String displayString = "";
					if(ledgerGroup != null) {
						Text t = new Text(iwrb.getLocalizedString("cashierLedgerWindow.in_group_text","in group") + ":");
						displayString += " - " + t + " " + ledgerGroup.getName();
					}
					if(clubNameField != null) {
						Text t = new Text(iwrb.getLocalizedString("cashierLedgerWindow.club_name_text", "club name") + ":");
						displayString += " - " + t + " " + clubNameField;
					}
					if(divisionNameField != null) {
						Text t = new Text(iwrb.getLocalizedString("cashierLedgerWindow.division_name_text","division name") + ":");
						displayString += " - " + t + " " + divisionNameField;
					}
					aLink = new Link(text);
					if(!isInGroup) {
						aLink.setStyleClass("errorMessage");
					}
					else {
						aLink.setStyleClass("styledLink");
					}
					aLink.setWindowToOpen(UserPropertyWindow.class);
					aLink.addParameter(UserPropertyWindow.PARAMETERSTRING_USER_ID, user.getPrimaryKey().toString());
					Text displayText = new Text(displayString);
					displayText.setStyleClass(bold);
					Table te = new Table();
					te.add(aLink,1,1);
					te.add(displayText,1,1);
					return te;
					
				}
				else {
					return null;
				}				
			}
		};
		
		Collection users = null;
		User user = null;
		
		try {
			users = getUserBusiness(getIWApplicationContext()).getUserHome().findUsersByMetaData(NEW_USER_IN_LEDGER,null);
		} catch (Exception e){
			e.printStackTrace();
		}
		Collection u = new ArrayList();
		Iterator usersIter = (Iterator) users.iterator();
		while(usersIter.hasNext()) {
			User us = (User) usersIter.next();
			if(us.getMetaData(NEW_USER_IN_LEDGER) != null && !us.getMetaData(NEW_USER_IN_LEDGER).equals("-1")) {
				u.add(us);
			}
		}

		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		EntityBrowser entityBrowser = new EntityBrowser();

		entityBrowser.setEntities("havanna",u);
		entityBrowser.setDefaultNumberOfRows(Math.min(users.size(), 30));
		entityBrowser.setWidth(Table.HUNDRED_PERCENT);
		
		String nameKey = User.class.getName() + ".FIRST_NAME:" + User.class.getName() + ".MIDDLE_NAME:" + User.class.getName() + ".LAST_NAME";
		String ssnKey = User.class.getName() + ".PERSONAL_ID";
//		entityBrowser.setMandatoryColumn(1, "Delete");
		
		entityBrowser.setDefaultColumn(1, nameKey);
		
		entityBrowser.setOptionColumn(0,ssnKey);
		
		entityBrowser.setEntityToPresentationConverter(nameKey, converterLink);
		
		add(entityBrowser);
		
	}
	public static LedgerVariationsHandler getLedgerVariationsHandler(IWContext iwc) {
		// the class used to handle ledgerVariations is an applicationProperty... 
		String bClass = null;
		try {
			bClass = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS);
		} catch(Exception e) {
			// just user default LedgerVariationHandler class
		}
		LedgerVariationsHandler ledgerVariationsHandler;
		if(bClass!=null && bClass.trim().length()>0) {
			Class classDef;
			try {
				classDef = Class.forName(bClass);
				ledgerVariationsHandler = (LedgerVariationsHandler) classDef.newInstance();
			} catch (Exception e) {
				System.out.println("Couldn't instantiate class for ledgerVariationsHandler, using default: " + bClass);
				e.printStackTrace();
				ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
			}
		} else {
			ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		}
		return ledgerVariationsHandler;
		
	}

	
	public UserBusiness getUserBusiness(IWApplicationContext iwc) {
		UserBusiness userBiz = null;
		if (userBiz == null) {
			try {
				userBiz = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return userBiz;
	}
	public CalBusiness getCalendarBusiness(IWApplicationContext iwc) {
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

}
