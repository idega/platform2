package is.idega.idegaweb.campus.block.allocation.presentation;
import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.business.CampusSettings;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.Account;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.contact.data.Email;
import com.idega.core.data.GenericGroup;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractSignWindow extends CampusWindow {
	
	private boolean isAdmin;
	private String login = null;
	private String passwd = null;
	private boolean print = false;
	private GenericGroup eGroup = null;
	public static final String prmAdmin = "is_camp_csat";
	private FinanceService finServ = null;
	/*
	 * Blár litur í topp # 27324B Hvítur litur fyrir neðan það # FFFFFF
	 * Ljósblár litur í töflu # ECEEF0 Auka litur örlítið dekkri (í lagi að
	 * nota líka) # CBCFD3
	 */
	public ContractSignWindow() {
		//setResizable(true);
	}
	protected void control(IWContext iwc) throws java.rmi.RemoteException {
		//debugParameters(iwc);
		
		finServ = getFinanceService(iwc);
		// permissons !!
		if (true) {
			
			if (iwc.isParameterSet("save") || iwc.isParameterSet("save.x")) {
				doSignContract(iwc);
				setParentToReload();
				//        this.getParentPage().
			}
			add(getSignatureTable(iwc));
		} else
			add(getHeader(localize("access_denied", "Access denied")));
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);
		return LinkTable;
	}
	private PresentationObject getSignatureTable(IWContext iwc) throws java.rmi.RemoteException {
		Integer iContractId = Integer.valueOf(iwc.getParameter("signed_id"));
		
			try {
				ContractHome cHome = getContractService(iwc).getContractHome();
				Contract contract = cHome.findByPrimaryKey(iContractId);
				Collection contracts = cHome.findByApartmentAndRented(contract.getApartmentId(),Boolean.TRUE);
				User user = contract.getUser();
				Applicant applicant = contract.getApplicant();
				IWTimestamp from = new IWTimestamp(contract.getValidFrom());
				IWTimestamp to = new IWTimestamp(contract.getValidTo());
				Collection emails = user.getEmails();
				
				Collection financeAccounts = null;
				Collection phoneAccounts = null;
				
				try {
					financeAccounts = finServ.getAccountHome().findAllByUserIdAndType(contract.getUserId().intValue(),
							finServ.getAccountTypeFinance());
					phoneAccounts = finServ.getAccountHome().findAllByUserIdAndType(contract.getUserId().intValue(),
							finServ.getAccountTypePhone());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (FinderException e1) {
					e1.printStackTrace();
				}
				
				CampusSettings settings = getCampusSettings(iwc);
				if(settings!=null){
					try {
						getUserService(iwc).getGroupHome().findByPrimaryKey(settings.getTenantGroupID());
					} catch (RemoteException e2) {
						e2.printStackTrace();
					} catch (FinderException e2) {
						e2.printStackTrace();
					}
				}
				// TODO use userservice  to lookup login name
				LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer)user.getPrimaryKey()).intValue());
				DataTable T = new DataTable();
				T.setWidth("100%");
				T.addTitle(localize("contract_signing", "Contract signing"));
				T.addButton(new CloseButton(getResourceBundle().getImage("close.gif")));
				T.addButton(new SubmitButton(getResourceBundle().getImage("save.gif"), "save"));
				SubmitButton save = new SubmitButton("save", localize("save", "Save"));
				SubmitButton signed = new SubmitButton("sign", localize("signed", "Signed"));
				CloseButton close = new CloseButton(localize("close", "Close"));
				PrintButton PB = new PrintButton(localize("print", "Print"));
				TextInput emailInput = new TextInput("new_email");
				emailInput.setAsEmail(localize("warning_illlegal_email",
						"Please enter a legal email address"));
				CheckBox accountCheck = new CheckBox("new_fin_account", "true");
				accountCheck.setChecked(true);
				CheckBox phoneAccountCheck = new CheckBox("new_phone_account", "true");
				phoneAccountCheck.setChecked(true);
				CheckBox loginCheck = new CheckBox("new_login", "true");
				loginCheck.setChecked(true);
				int row = 1;
				HiddenInput HI = new HiddenInput("signed_id", contract.getPrimaryKey().toString());
				T.add(HI, 1, row);
				if (iwc.isParameterSet(prmAdmin)) {
					T.add(new HiddenInput(prmAdmin, "true"));
				}
				T.add(getHeader(localize("name", "Name")), 1, row);
				T.add(getText(applicant.getFullName()), 2, row);
				row++;
				T.add(getHeader(localize("ssn", "SocialNumber")), 1, row);
				T.add(getText(applicant.getSSN()), 2, row);
				row++;
				T.add(getHeader(localize("apartment", "Apartment")), 1, row);
				T.add(getText(getApartmentString(contract.getApartment())), 2, row);
				row++;
				T.add(getHeader(localize("valid_from", "Valid from")), 1, row);
				T.add(getText(from.getLocaleDate(iwc)), 2, row);
				row++;
				T.add(getHeader(localize("valid_to", "Valid to")), 1, row);
				T.add(getText(to.getLocaleDate(iwc)), 2, row);
				row++;
				boolean canSign = true;
				Integer con_id = new Integer(-1);
				if (contracts != null && !contracts.isEmpty()) {
					Contract C = (Contract) contracts.iterator().next();
					con_id = ((Integer)C.getPrimaryKey());
					if (con_id.intValue() != ((Integer)contract.getPrimaryKey()).intValue())
						canSign = false;
				}
				T.add(getHeader(localize("email", "Email")), 1, row);
				if (emails != null) {
					//T.add(getText(
					// ((Email)lEmails.get(0)).getEmailAddress()),2,row);
					int pos = emails.size() - 1;
					
					Email email = null;
					for (Iterator iter = emails.iterator(); iter.hasNext();) {
						email = (Email) iter.next();
					}
					if (email != null)
						emailInput.setContent(email.getEmailAddress());
					T.add(emailInput, 2, row);
				} else {
					T.add(emailInput, 2, row);
				}
				row++;
				if (eGroup != null) {
					HiddenInput Hgroup = new HiddenInput("user_group", String.valueOf(eGroup.getID()));
					T.add(Hgroup);
					if (financeAccounts.isEmpty()) {
						T.add(accountCheck, 2, row);
						T.add(getHeader(localize("fin_account", "New finance account")), 2, row);
					} else {
						int len = financeAccounts.size();
						for (Iterator iter = financeAccounts.iterator(); iter.hasNext();) {
							T.add(getHeader(localize("fin_account", "Finance account")), 1, row);
							T.add(getText(((Account) iter.next()).getName() + " "), 2, row);
						}
					}
					row++;
					if (phoneAccounts.isEmpty()) {
						T.add(phoneAccountCheck, 2, row);
						T.add(getHeader(localize("phone_account", "New phone account")), 2, row);
					} else {
						int len = phoneAccounts.size();
						for (Iterator iter = phoneAccounts.iterator(); iter.hasNext();) {
							T.add(getHeader(localize("phone_account", "Phone account")), 1, row);
							T.add(getText(((Account) iter.next()).getName() + " "), 2, row);
						}
					}
					row++;
					if (loginTable != null) {
						T.add(getHeader(localize("login", "Login")), 1, row);
						T.add(getText(loginTable.getUserLogin()), 2, row);
						row++;
						T.add(getHeader(localize("passwd", "Passwd")), 1, row);
						if (passwd != null)
							T.add(getText(passwd), 2, row++);
					} else {
						T.add(loginCheck, 2, row);
						T.add(getHeader(localize("new_login", "New login")), 2, row);
					}
					row++;
					/*
					 * if(eContract.getStatus().equalsIgnoreCase(eContract.statusSigned))
					 * T.add(save,2,row); else T.add(signed,2,row); if(print){
					 * T.add(PB,2,row); } T.add(close,2,row);
					 */
				} else {
					T.add(getHeader(localize("sys_props_error", "System property error")), 2, row++);
					T.add(getHeader(localize("no_default_group", "No default group")), 2, row++);
				}
				if (!canSign) {
					row++;
					Text msg = getHeader(localize("contract_conflict", "Apartment is still in rent"));
					msg.setFontColor("#FF0000");
					T.add(msg, 2, row);
					//T.add(CampusContracts.getReSignLink(iwb.getImage("/scissors.gif"),con_id),2,row);
				}
				Form F = new Form();
				F.add(T);
				return F;
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			return null;
		
	}
	private void doSignContract(IWContext iwc) {
		Integer id = Integer.valueOf(iwc.getParameter("signed_id"));
		String sEmail = iwc.getParameter("new_email");
		String sSendMail = iwc.getParameter("send_mail");
		String sFinAccount = iwc.getParameter("new_fin_account");
		String sPhoneAccount = iwc.getParameter("new_phone_account");
		String sCreateLogin = iwc.getParameter("new_login");
		String sUserGroup = iwc.getParameter("user_group");
		String sSigned = iwc.getParameter("sign");
		Integer iGroupId = sUserGroup != null ? Integer.valueOf(sUserGroup) : null;
		boolean sendMail = sSendMail != null ? true : false;
		sendMail = true;
		boolean newAccount = sFinAccount != null ? true : false;
		boolean newPhoneAccount = sPhoneAccount != null ? true : false;
		boolean createLogin = sCreateLogin != null ? true : false;
		try {
			passwd =getContractService(iwc).signContract(id, iGroupId, new Integer(1),getCampusSettings(iwc).getFinanceCategoryID(), 
					sEmail, sendMail, newAccount,
					newPhoneAccount, createLogin, false, getResourceBundle(), login, passwd);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (login != null && passwd != null)
			print = true;
		else
			print = false;
		//add(passwd);
	}
	private void doAddEmail(int iUserId, IWContext iwc) {
		String sEmail = iwc.getParameter("new_email");
		try {
			getUserService(iwc).addNewUserEmail(iUserId, sEmail);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private PresentationObject getApartmentTable(Apartment A) {
		Table T = new Table();
		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
		T.add(getText(A.getName()), 1, 1);
		T.add(getText(F.getName()), 2, 1);
		T.add(getText(B.getName()), 3, 1);
		T.add(getText(C.getName()), 4, 1);
		return T;
	}
	private String getApartmentString(Apartment A) {
		StringBuffer S = new StringBuffer();
		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
		S.append(A.getName());
		S.append(" ");
		S.append(F.getName());
		S.append(" ");
		S.append(B.getName());
		S.append(" ");
		S.append(C.getName());
		return S.toString();
	}
	public FinanceService getFinanceService(IWContext iwc) throws RemoteException {
		return (FinanceService) IDOLookup.getServiceInstance(iwc, FinanceService.class);
	}
	public void main(IWContext iwc) throws java.rmi.RemoteException {
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		isAdmin = iwc.isParameterSet(prmAdmin);
		control(iwc);
	}

}
