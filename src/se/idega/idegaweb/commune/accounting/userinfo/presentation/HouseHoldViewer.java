/*
 * Created on Aug 13, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.presentation;

import is.idega.idegaweb.member.business.NoCustodianFound;
import is.idega.idegaweb.member.business.NoSpouseFound;
import is.idega.idegaweb.member.presentation.UserSearcher;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.user.presentation.CitizenEditorWindow;

import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * HouseHoldViewer
 * @author aron 
 * @version 1.0
 */

public class HouseHoldViewer extends CommuneBlock {

	private User firstUser = null;
	private User secondUser = null;
	private boolean hasUser = false;
	private List children = null;
	private Map childrenMap = null;
	private NumberFormat nf = null;
	private Integer userEditorPageID = null;
	private Integer userBruttoIncomePageID = null;
	private Integer userLowIncomePageID = null;
	private Class userEditorWindowClass = CitizenEditorWindow.class;
	private Class userBruttoIncomeWindowClass = BruttoIncomeWindow.class;
	private Class userLowIncomeWindowClass = null;
	private String userEditorUserParameterName = CitizenEditorWindow.getUserIDParameterName();
	private String userBruttoIncomeUserParameterName = BruttoIncomeWindow.getUserIDParameterName();
	private String userLowIncomeUserParameterName = null;

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		nf = NumberFormat.getNumberInstance(iwc.getCurrentLocale());
		process(iwc);
		presentate(iwc);
	}

	public void process(IWContext iwc) {
		String prm = UserSearcher.getUniqueUserParameterName("one");
		if (iwc.isParameterSet(prm)) {
			Integer firstUserID = Integer.valueOf(iwc.getParameter(prm));
			try {
				firstUser = getUserService(iwc).getUser(firstUserID);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			add(firstUserID.toString());
		}
		prm = UserSearcher.getUniqueUserParameterName("two");
		if (iwc.isParameterSet(prm)) {
			Integer secondUserID = Integer.valueOf(iwc.getParameter(prm));
			try {
				secondUser = getUserService(iwc).getUser(secondUserID);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			add(secondUserID.toString());
		}
		lookupChildren(iwc);
	}

	private void lookupChildren(IWContext iwc) {
		try {
			CommuneUserBusiness userService = getUserService(iwc);
			childrenMap = new HashMap();
			children = new Vector();
			List parents = new Vector();
			if (firstUser != null) {
				parents.add(firstUser);
			}
			if (secondUser != null) {
				parents.add(secondUser);
			}

			for (Iterator iter = parents.iterator(); iter.hasNext();) {
				User parent = (User) iter.next();
				Collection childs = userService.getChildrenForUser(parent);
				if (childs != null && !childs.isEmpty()) {
					for (Iterator iter2 = childs.iterator(); iter2.hasNext();) {
						User child = (User) iter2.next();
						if (!childrenMap.containsKey((Integer) child.getPrimaryKey())) {
							children.add(child);
							childrenMap.put((Integer) child.getPrimaryKey(), child);
						}
					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
	}

	public void presentate(IWContext iwc) {
		presentateSearch(iwc);
		presentateUsersFound(iwc);
		presentateChildren(iwc);
		presentateButtons(iwc);
	}

	public void presentateSearch(IWContext iwc) {
		Table table = new Table();
		UserSearcher searcherOne = new UserSearcher();
		searcherOne.setShowMiddleNameInSearch(false);
		searcherOne.setOwnFormContainer(false);
		searcherOne.setUniqueIdentifier("one");
		searcherOne.setSkipResultsForOneFound(false);
		searcherOne.setHeaderFontStyleName(getStyleName(STYLENAME_HEADER));
		searcherOne.setButtonStyleName(getStyleName(STYLENAME_INTERFACE_BUTTON));
		UserSearcher searcherTwo = new UserSearcher();
		searcherTwo.setShowMiddleNameInSearch(false);
		searcherTwo.setOwnFormContainer(false);
		searcherTwo.setUniqueIdentifier("two");
		searcherTwo.setSkipResultsForOneFound(false);
		searcherTwo.setHeaderFontStyleName(getStyleName(STYLENAME_HEADER));
		searcherTwo.setButtonStyleName(getStyleName(STYLENAME_INTERFACE_BUTTON));

		String prmTwo = UserSearcher.getUniqueUserParameterName("two");
		String prmOne = UserSearcher.getUniqueUserParameterName("one");
		if (iwc.isParameterSet(prmTwo)) {
			searcherOne.maintainParameter(new Parameter(prmTwo, iwc.getParameter(prmTwo)));
		}
		if (iwc.isParameterSet(prmOne)) {
			searcherTwo.maintainParameter(new Parameter(prmOne, iwc.getParameter(prmOne)));
		}

		table.add(searcherOne, 1, 1);
		table.add(searcherTwo, 1, 2);

		//add(table);
		Form form = new Form();
		form.maintainParameter(prmOne);
		form.maintainParameter(prmTwo);
		form.add(table);
		add(form);
		add(Text.getBreak());
	}

	public void presentateUsersFound(IWContext iwc) {
		Text tAdults = getHeader(localize("household.adults","Adults"));
		add(tAdults);
		Table table = new Table();
		Text tIndividual = getHeader(localize("household.individual", "Individual"));
		Text tPersonalID = getHeader(localize("household.personal_id", "Personal ID"));
		Text tStreetAddress = getHeader(localize("household.streetaddress", "Street address"));
		Text tSpouse = getHeader(localize("household.spouse", "Spouse"));
		Text tPartner = getHeader(localize("household.partner", "Partner"));
		Text tBruttoIncome = getHeader(localize("household.brutto_income", "Brutto income"));

		int col = 1;
		int row = 1;
		table.add(tIndividual, col++, row);
		table.add(tPersonalID, col++, row);
		table.add(tStreetAddress, col++, row);
		table.add(tSpouse, col++, row);
		table.add(tPartner, col++, row);
		table.add(tBruttoIncome, col++, row);
		row++;
		Vector users = new Vector(2);
		if (firstUser != null) {
			users.add(firstUser);
		}
		if (secondUser != null) {
			users.add(secondUser);
		}

		for (Iterator iter = users.iterator(); iter.hasNext();) {
			User user = (User) iter.next();
			col = 1;
			table.add(getText(user.getNameLastFirst()), col++, row);
			table.add(getText(user.getPersonalID()), col++, row);
			Address address = getUserAddress(iwc, user);

			if (address != null) {
				table.add(getText(address.getStreetAddress()), col, row);
			}
			col++;
			User spouse = getSpouse(iwc, user);
			if (spouse != null) {
				table.add(getText(spouse.getPersonalID()), col, row);
			}
			col++;
			// partner ??
			col++;
			BruttoIncome income = getBruttoIncome(user);
			if (income != null) {
				table.add(getText(nf.format(income.getIncome().doubleValue())), col, row);
			}
			col++;
			row++;
		}
		add(table);
		add(Text.getBreak());
	}

	public void presentateChildren(IWContext iwc) {
		Text tChildren = getHeader(localize("household.children","Children"));
		add(tChildren);
		Table table = new Table();
		Text tIndividual = getHeader(localize("household.individual", "Individual"));
		Text tPersonalID = getHeader(localize("household.personal_id", "Personal ID"));
		Text tStreetAddress = getHeader(localize("household.streetaddress", "Street address"));
		Text tSiblingOrder = getHeader(localize("household.sibling_order", "Sibling order"));
		Text tCalculatedAge = getHeader(localize("household.calculated_age", "Calculated age"));
		Text tLowIncome = getHeader(localize("household.low_income", "Low income"));
		Text tFirstCustodian = getHeader(localize("household.first_custodian", "First custodian"));
		Text tSecondCustodian = getHeader(localize("household.second_custodian", "Second custodian"));
		int row = 1;
		int col = 1;
		table.add(tIndividual, col++, row);
		table.add(tPersonalID, col++, row);
		table.add(tStreetAddress, col++, row);
		table.add(tSiblingOrder, col++, row);
		table.add(tCalculatedAge, col++, row);
		table.add(tLowIncome, col++, row);
		table.add(tFirstCustodian, col++, row);
		table.add(tSecondCustodian, col++, row);
		row++;
		if (children != null) {
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				User child = (User) iter.next();
				col = 1;
				table.add(getText(child.getFirstName()), col++, row);
				table.add(getText(child.getPersonalID()), col++, row);
				Address address = getUserAddress(iwc, child);
				if (address != null) {
					table.add(getText(address.getStreetAddress()), col, row);
				}
				col++;
				Integer siblingOrder = getSiblingOrder(child);
				if (siblingOrder != null) {
					table.add(getText(siblingOrder.toString()), col, row);
				}
				col++;
				Age age = getCalculatedAge(child);
				if (age != null) {
					table.add(getText(String.valueOf(age.getYears())), col, row);
				}
				col++;
				// TODO get lowIncome properly
				Object lowIncome = getLowIncome(child);
				if (lowIncome != null) {
					table.add(getText(nf.format(lowIncome.toString())), col, row);
				}
				col++;
				Collection custodians = getCustodians(iwc, child);
				if (custodians != null && !custodians.isEmpty()) {
					for (Iterator iterator = custodians.iterator(); iterator.hasNext();) {
						User custodian = (User) iterator.next();
						table.add(getText(custodian.getPersonalID()), col, row);
						col++;
					}
				}
				row++;
				
			}
		}
		add(table);
		add(Text.getBreak());
	}

	public void presentateButtons(IWContext iwc) {
		DropdownMenu drp = new DropdownMenu("usr_drp");
		
		if (firstUser != null) {
			drp.addMenuElement(firstUser.getPrimaryKey().toString(), firstUser.getName());
			hasUser = true;
		}
		if (secondUser != null) {
			drp.addMenuElement(secondUser.getPrimaryKey().toString(), secondUser.getName());
			hasUser = true;
		}
		Table table = new Table();
		table.add(drp, 1, 1);
		table.add(getUserEditorButton(iwc),2,1);
		table.add(getBruttoIncomeEditorButton(iwc),3,1);
		table.add(getLowIncomeEditorButton(iwc),4,1);

		Form form = new Form();
		form.add(table);
		add(form);
	}

	private PresentationObject getUserEditorButton(IWContext iwc) {
		GenericButton button = new SubmitButton(localize("household.edit_user", "Edit user"));
		button = getButton(button);
		
		if (hasUser && userEditorPageID != null) {
			button.setPageToOpen(userEditorPageID.intValue());
		}
		else if (hasUser && userEditorWindowClass != null){
			button.setOnClick(getButtonOnClickForWindow(iwc,userEditorWindowClass,userEditorUserParameterName));
		}
		else{
			button.setDisabled(true);
		}
		return button;
	}

	private PresentationObject getBruttoIncomeEditorButton(IWContext iwc) {
		GenericButton button = new SubmitButton(localize("household.edit_brutto_income", "Edit brutto income"));
		button = getButton(button);
		if (hasUser && userBruttoIncomePageID != null ) {
			button.setPageToOpen(userBruttoIncomePageID.intValue());
		}
		else if (hasUser && userBruttoIncomeWindowClass != null) {
			button.setOnClick(getButtonOnClickForWindow(iwc,userBruttoIncomeWindowClass,userBruttoIncomeUserParameterName));
		}
		else{
			button.setDisabled(true);
		}
		return button;
	}

	private PresentationObject getLowIncomeEditorButton(IWContext iwc) {
		GenericButton button = new SubmitButton(localize("household.edit_low_income", "Edit low income"));
		button = getButton(button);
		if (hasUser && userLowIncomePageID != null) {
			button.setPageToOpen(userLowIncomePageID.intValue());
		}
		else if (hasUser && userLowIncomeWindowClass != null) {
			button.setOnClick(getButtonOnClickForWindow(iwc,userLowIncomeWindowClass,userLowIncomeUserParameterName));
		}
		else{
			button.setDisabled(true);
		}
		return button;
	}
	
	private String getButtonOnClickForWindow(IWContext iwc,Class windowClass, String userParameterName){
		String prm = "";
		if(userParameterName!=null)
			prm = "&"+userParameterName+"="+"'+this.form.usr_drp.value+' ";
		String URL = Window.getWindowURL(windowClass, iwc) + prm;
		return "javascript:" + Window.getCallingScriptString(windowClass, URL, true, iwc)+";return false;";
	}

	private Collection getCustodians(IWContext iwc, User user) {
		try {
			return getUserService(iwc).getMemberFamilyLogic().getCustodiansFor(user);
		}
		catch (NoCustodianFound e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	private User getSpouse(IWContext iwc, User user) {
		try {
			return getUserService(iwc).getMemberFamilyLogic().getSpouseFor(user);
		}
		catch (NoSpouseFound e) {
			
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	private BruttoIncome getBruttoIncome(User user) {
		try {
			return getBruttoIncomeHome().findLatestByUser((Integer) user.getPrimaryKey());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			
		}
		return null;
	}

	private Address getUserAddress(IWContext iwc, User user) {
		try {
			return getUserService(iwc).getUserAddress1(((Integer) firstUser.getPrimaryKey()).intValue());
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO do some clever calculation
	private Age getCalculatedAge(User user) {
		if (user.getDateOfBirth() != null)
			return new Age(user.getDateOfBirth());
		return null;
	}

	// TODO get sibling order from database somehow
	private Integer getSiblingOrder(User child) {
		return new Integer(1);
	}

	// TODO fetch low income invoice record
	private Object getLowIncome(User user) {
		return null;
	}

	private CommuneUserBusiness getUserService(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}

	private BruttoIncomeHome getBruttoIncomeHome() throws RemoteException {
		return (BruttoIncomeHome) IDOLookup.getHome(BruttoIncome.class);
	}

}
