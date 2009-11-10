package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.CampusProperties;
import is.idega.idegaweb.campus.presentation.CampusWindow;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class ContractResignWindow extends CampusWindow {

	private static final String PARAM_MOVING_DATE = "mov_date";

	private static final String PARAM_RESIGN_INFO = "resign_info";

	private static final String PARAM_USER = "us_id";

	private static final String PARAM_CONTRACT = "contract_id";

	private static final String PARAM_NEW_PHONE = "new_phone";

	private static final String PARAM_NEW_ZIP = "new_zip";

	private static final String PARAM_NEW_ADDRESS = "new_address";

	private static final String PARAM_APPLICANT = "applicant_id";

	private static final String PARAM_DATE_SYNC = "date_sync";

	public static final String PARAM_IS_ADMIN = "is_camp_isit";
	
	private static final String PARAM_NEW_EMAIL = "new_email";

	private boolean isAdmin;

	private boolean isLoggedOn;

	private String login = null;

	private String passwd = null;

	private boolean print = false;

	private SystemProperties SysProps = null;

	private Group eGroup = null;

	private User eUser = null;

	/*
	 * Blue top colour # 27324B Hv�tur litur fyrir ne�an �a� # FFFFFF Lj�sbl�r
	 * litur � t�flu # ECEEF0 Auka litur �rl�ti� dekkri (� lagi a� nota l�ka) #
	 * CBCFD3
	 */
	public ContractResignWindow() {
		setWidth(530);
		setHeight(370);
		setResizable(true);
	}

	protected void control(IWContext iwc) {
		// debugParameters(iwc);
		if (isAdmin || isLoggedOn) {
			if (iwc
					.getApplicationAttribute(is.idega.idegaweb.campus.data.SystemPropertiesBMPBean
							.getEntityTableName()) != null) {
				SysProps = (SystemProperties) iwc
						.getApplicationAttribute(is.idega.idegaweb.campus.data.SystemPropertiesBMPBean
								.getEntityTableName());
			}
			if (iwc.isParameterSet("save") || iwc.isParameterSet("save.x")) {
				doResignContract(iwc);
			}
			add(getSignatureTable(iwc));
		} else
			add(getErrorText(localize("access_denied", "Access denied")));
		// add(String.valueOf(iSubjectId));
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);
		return LinkTable;
	}

	private PresentationObject getSignatureTable(IWContext iwc) {
		int iContractId = Integer.parseInt(iwc.getParameter(PARAM_CONTRACT));
		// Table T = new Table(2,8);
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(localize("contract_resign", "Contract resign"));
		T.addButton(new CloseButton(getResourceBundle().getImage("close.gif")));
		T.addButton(new SubmitButton(getResourceBundle().getImage("save.gif"),
				"save"));
		int row = 1;
		int col = 1;
		try {
			if (iContractId > 0) {
				Contract eContract = ((ContractHome) com.idega.data.IDOLookup
						.getHome(Contract.class)).findByPrimaryKey(new Integer(
						iContractId));
				Applicant eApplicant = eContract.getApplicant();
				User user = eContract.getUser();
				if (user != null) {
					boolean isContractUser = user.getPrimaryKey().toString()
							.equals(eUser.getPrimaryKey().toString());
					T.add(new HiddenInput(PARAM_CONTRACT, (eContract
							.getPrimaryKey().toString())), 1, row);
					T.add(new HiddenInput(PARAM_APPLICANT, eContract
							.getApplicantId().toString()), 1, row);
					T.add(new HiddenInput(PARAM_USER, String.valueOf(eContract
							.getUserId().intValue())), 1, row);
					if (iwc.isParameterSet(PARAM_IS_ADMIN)) {
						T.add(new HiddenInput(PARAM_IS_ADMIN, "true"));
					}
					T.add(Edit.formatText(localize("name", "Name")), 1, row);
					T.add(Edit.formatText(user.getName()), 2, row);
					row++;
					T.add(Edit.formatText(localize("ssn", "SocialNumber")), 1,
							row);
					T.add(Edit.formatText(eApplicant.getSSN()), 2, row);
					row++;
					T.add(Edit.formatText(localize("apartment", "Apartment")),
							1, row);
					T.add(Edit.formatText(eContract.getApartment().getName()),
							2, row);
					row++;
					T.add(
							Edit
									.formatText(localize("valid_from",
											"Valid from")), 1, row);
					T.add(Edit.formatText(new IWTimestamp(eContract
							.getValidFrom()).getLocaleDate(iwc
							.getCurrentLocale())), 2, row);
					row++;
					T.add(Edit.formatText(localize("valid_to", "Valid to")), 1,
							row);
					T.add(Edit.formatText(new IWTimestamp(eContract
							.getValidTo())
							.getLocaleDate(iwc.getCurrentLocale())), 2, row);
					row++;
					T.add(
							Edit.formatText(localize("moving_date",
									"Moving date")), 1, row);
					// IWTimestamp movdate = eContract.getMovingDate()!=null?new
					// IWTimestamp(eContract.getMovingDate()):null;
					DateInput movDate = new DateInput(PARAM_MOVING_DATE, true);
					IWTimestamp moving = IWTimestamp.RightNow();
					// int termofnotice = 1;
					int termofnoticeMonths = 1;
					if (SysProps != null) {
						termofnoticeMonths = (int) SysProps
								.getTermOfNoticeMonths();
					}
					moving = this.addMonthsPlusCurrentMonth(moving,
							termofnoticeMonths);

					if (moving.isLaterThan(new IWTimestamp(eContract
							.getValidTo()))) {
						movDate.setDate(eContract.getValidTo());
					} else {
						movDate.setDate(moving.getDate());
					}
					IWTimestamp now = IWTimestamp.RightNow();
					now.addMonths(1);
					if (now
							.isLaterThan(new IWTimestamp(eContract.getValidTo()))) {
						now = new IWTimestamp(eContract.getValidTo());
					}
					if (!isAdmin) {
						movDate
								.setEarliestPossibleDate(
										now.getDate(),
										localize("must_select_one_month_ahead",
												"You must select a date at least one month later then today"));
					}
					movDate.setStyleAttribute("style", Edit.styleAttribute);

					if (isAdmin || isContractUser) {
						T.add(movDate, 2, row);
					} else if (moving != null) {
						T.add(Edit.formatText(moving.getLocaleDate(iwc
								.getCurrentLocale())), 2, row);
					}

					row++;

					boolean DATESYNC = getBundle().getProperty(
							CampusProperties.PROP_CONTRACT_DATE_SYNC, "false")
							.equals("true");
					if (isAdmin) {
						CheckBox dateSync = new CheckBox(PARAM_DATE_SYNC,
								"true");
						dateSync.setChecked(DATESYNC);
						T.add(Edit.formatText(localize("update_valid_to",
								"Update valid to")), 1, row);
						T.add(dateSync, 2, row);
						row++;
					} 

					TextInput newAddress = new TextInput(PARAM_NEW_ADDRESS);
					newAddress.setAsNotEmpty(localize("err_new_address",
							"You must enter a new address"));
					TextInput newZip = new TextInput(PARAM_NEW_ZIP);
					newZip.setAsNotEmpty(localize("err_new_zip",
							"You must enter a new zip code"));
					TextInput newPhone = new TextInput(PARAM_NEW_PHONE);
					newPhone.setAsNotEmpty(localize("err_new_phone",
							"You must enter a new phone"));
					TextInput newEmail = new TextInput(PARAM_NEW_EMAIL);
					newEmail.setAsNotEmpty(localize("err_new_email",
							"You must enter your current email"));

					T.add(Edit.formatText(localize(PARAM_NEW_ADDRESS,
							"New address")), 1, row);
					T.add(newAddress, 2, row++);
					T.add(Edit.formatText(localize(PARAM_NEW_ZIP, "New zip")),
							1, row);
					T.add(newZip, 2, row++);
					T.add(
							Edit.formatText(localize(PARAM_NEW_PHONE,
									"New phone")), 1, row);
					T.add(newPhone, 2, row++);
					T.add(
							Edit.formatText(localize(PARAM_NEW_EMAIL,
									"Current email")), 1, row);
					T.add(newEmail, 2, row++);
				}
			}
		} catch (Exception ex) {
		}
		Form F = new Form();
		F.add(T);
		return F;
	}

	private IWTimestamp addMonthsPlusCurrentMonth(IWTimestamp timestamp,
			int monthsToAdd) {
		int month = timestamp.getMonth();
		timestamp.setDay(1);
		if (month == 12) {
			timestamp.setMonth(1);
			int year = timestamp.getYear();
			timestamp.setYear(year++);
		} else {
			timestamp.setMonth(month++);
		}
		timestamp.addMonths(monthsToAdd);
		return timestamp;
	}

	private void doResignContract(IWContext iwc) {
		Integer id = Integer.valueOf(iwc.getParameter(PARAM_CONTRACT));
		int usid = Integer.parseInt(iwc.getParameter(PARAM_USER));
		String sInfo = iwc.getParameter(PARAM_RESIGN_INFO);
		if (sInfo == null)
			sInfo = "";
		String sMovDate = iwc.getParameter(PARAM_MOVING_DATE);
		IWTimestamp movDate = null;
		if (sMovDate != null && sMovDate.length() == 10)
			movDate = new IWTimestamp(sMovDate);
		boolean datesync = iwc.getParameter(PARAM_DATE_SYNC) != null;
		try {
			if (isAdmin) {
				System.out.println("is admin");
				getContractService(iwc).endContract(id, movDate, sInfo,
						datesync);
			} else if (eUser != null
					&& String.valueOf(usid).equals(
							eUser.getPrimaryKey().toString())) {
				System.out.println("is other user");
				getContractService(iwc).resignContract(id, movDate, sInfo,
						datesync);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		doChangeApplicantAddress(iwc);
		setParentToReload();
		close();
	}

	private void doChangeApplicantAddress(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAM_APPLICANT)) {
				Integer applicantID = Integer.valueOf(iwc
						.getParameter(PARAM_APPLICANT));
				Applicant applicant = ((ApplicantHome) IDOLookup
						.getHome(Applicant.class))
						.findByPrimaryKey(applicantID);
				if (iwc.isParameterSet(PARAM_NEW_ADDRESS)) {
					applicant.setResignationAddress(iwc.getParameter(PARAM_NEW_ADDRESS));
				}
				
				if (iwc.isParameterSet(PARAM_NEW_ZIP)) {
					applicant.setResignationPO(iwc.getParameter(PARAM_NEW_ZIP));
				}
				
				if (iwc.isParameterSet(PARAM_NEW_PHONE)) {
					applicant.setResignationPhone(iwc.getParameter(PARAM_NEW_PHONE));
				}
				
				if (iwc.isParameterSet(PARAM_NEW_EMAIL)) {
					applicant.setResignationEmail(iwc.getParameter(PARAM_NEW_EMAIL));
				}
				applicant.store();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	private void doAddEmail(int iUserId, IWContext iwc) {
		String sEmail = iwc.getParameter("new_email");
		UserBusiness.addNewUserEmail(iUserId, sEmail);
	}

	private PresentationObject getApartmentTable(Apartment A) {
		Table T = new Table();
		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
		T.add(Edit.formatText(A.getName()), 1, 1);
		T.add(Edit.formatText(F.getName()), 2, 1);
		T.add(Edit.formatText(B.getName()), 3, 1);
		T.add(Edit.formatText(C.getName()), 4, 1);
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

	public void main(IWContext iwc) throws Exception {
		eUser = iwc.getCurrentUser();
		isAdmin = iwc.isParameterSet(PARAM_IS_ADMIN);
		isLoggedOn = com.idega.core.accesscontrol.business.LoginBusinessBean
				.isLoggedOn(iwc);
		control(iwc);
	}
}