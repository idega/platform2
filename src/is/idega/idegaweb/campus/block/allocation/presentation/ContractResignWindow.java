package is.idega.idegaweb.campus.block.allocation.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.CampusProperties;
import is.idega.idegaweb.campus.presentation.CampusWindow;
import is.idega.idegaweb.campus.presentation.Edit;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.core.data.GenericGroup;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
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
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;

/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractResignWindow extends CampusWindow {


    private boolean isAdmin;

    private boolean isLoggedOn;

    private String login = null;

    private String passwd = null;

    private boolean print = false;

    private SystemProperties SysProps = null;

    private GenericGroup eGroup = null;

    private User eUser = null;

    private String prmDateSync = "date_sync";

    public static final String prmAdmin = "is_camp_isit";

    /*
     * Blue top colour # 27324B Hvítur litur fyrir neðan það # FFFFFF Ljósblár
     * litur í töflu # ECEEF0 Auka litur örlítið dekkri (í lagi að nota líka) #
     * CBCFD3
     */

    public ContractResignWindow() {
        setWidth(530);
        setHeight(370);
        setResizable(true);
    }

    protected void control(IWContext iwc) {
        //debugParameters(iwc);

        if (isAdmin || isLoggedOn) {
            if (iwc
                    .getApplicationAttribute(is.idega.idegaweb.campus.data.SystemPropertiesBMPBean
                            .getEntityTableName()) != null) {
                SysProps = (SystemProperties) iwc
                        .getApplicationAttribute(is.idega.idegaweb.campus.data.SystemPropertiesBMPBean
                                .getEntityTableName());
            }

            if (iwc.isParameterSet("save") || iwc.isParameterSet("save.x")) {
                doReSignContract(iwc);
            }
            add(getSignatureTable(iwc));
        } else
            add(getErrorText(localize("access_denied",
                    "Access denied")));

        //add(String.valueOf(iSubjectId));
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }

    public PresentationObject makeLinkTable(int menuNr) {
        Table LinkTable = new Table(6, 1);

        return LinkTable;
    }

    private PresentationObject getSignatureTable(IWContext iwc) {
        int iContractId = Integer.parseInt(iwc.getParameter("contract_id"));
        //Table T = new Table(2,8);
        DataTable T = new DataTable();
        T.setWidth("100%");
        T.addTitle(localize("contract_resign", "Contract resign"));
        T.addButton(new CloseButton(getResourceBundle().getImage("close.gif")));
        T.addButton(new SubmitButton(getResourceBundle().getImage("save.gif"), "save"));

        int row = 1;
        int col = 1;

        try {
            if (iContractId > 0) {
                Contract eContract = ((ContractHome) com.idega.data.IDOLookup
                        .getHome(Contract.class))
                        .findByPrimaryKey(new Integer(iContractId));
                Applicant eApplicant = eContract.getApplicant();
                User user = eContract.getUser();

                if (user != null) {
                    boolean isContractUser = user.getID() == eUser.getID();
                    T.add(new HiddenInput("contract_id",(eContract.getPrimaryKey().toString())), 1, row);
                    T.add(new HiddenInput("applicant_id", eContract
                            .getApplicantId().toString()), 1, row);
                    T.add(new HiddenInput("us_id", String.valueOf(eContract
                            .getUserId().intValue())), 1, row);
                    if (iwc.isParameterSet(prmAdmin)) {
                        T.add(new HiddenInput(prmAdmin, "true"));
                    }
                    T.add(Edit.formatText(localize("name",
                            "Name")), 1, row);
                    T.add(Edit.formatText(user.getName()), 2, row);
                    row++;
                    T.add(Edit.formatText(localize("ssn",
                            "SocialNumber")), 1, row);
                    T.add(Edit.formatText(eApplicant.getSSN()), 2, row);
                    row++;
                    T.add(Edit.formatText(localize("apartment",
                            "Apartment")), 1, row);
                    T.add(Edit
                            .formatText(eContract.getApartment().getName()), 2, row);
                    row++;
                    T.add(Edit.formatText(localize("valid_from",
                            "Valid from")), 1, row);
                    T.add(Edit.formatText(new IWTimestamp(eContract
                            .getValidFrom()).getLocaleDate(iwc)), 2, row);
                    row++;
                    T.add(Edit.formatText(localize("valid_to",
                            "Valid to")), 1, row);
                    T.add(Edit.formatText(new IWTimestamp(eContract
                            .getValidTo()).getLocaleDate(iwc)), 2, row);
                    row++;
                    T.add(Edit.formatText(localize(
                            "moving_date", "Moving date")), 1, row);

                    //          IWTimestamp movdate = eContract.getMovingDate()!=null?new
                    // IWTimestamp(eContract.getMovingDate()):null;
                    DateInput movDate = new DateInput("mov_date", true);
                    IWTimestamp moving = IWTimestamp.RightNow();
                    //          int termofnotice = 1;
                    int termofnoticeMonths = 1;
                    if (SysProps != null)
                            termofnoticeMonths = (int) SysProps
                                    .getTermOfNoticeMonths();

                    moving = this.addMonthsPlusCurrentMonth(moving,
                            termofnoticeMonths);
                    //          moving.addDays(termofnoticeMonths);

                    if (moving.isLaterThan(new IWTimestamp(eContract
                            .getValidTo())))
                        movDate.setDate(eContract.getValidTo());
                    else
                        movDate.setDate(moving.getSQLDate());

                    //Edit.setStyle(movDate);
                    movDate.setStyleAttribute("style", Edit.styleAttribute);

                    //          if(movdate !=null )
                    //            movDate.setDate(movdate.getSQLDate());
                    if (isAdmin || isContractUser)
                        T.add(movDate, 2, row);
                    else if (moving != null)
                            T.add(Edit.formatText(moving.getLocaleDate(iwc)),
                                    2, row);

                    row++;
                    boolean DATESYNC = getBundle().getProperty(
                            CampusProperties.PROP_CONTRACT_DATE_SYNC, "false")
                            .equals("true");
                    if (isAdmin) {
                        CheckBox dateSync = new CheckBox(prmDateSync, "true");
                        dateSync.setChecked(DATESYNC);
                        T.add(Edit.formatText(localize(
                                "update_valid_to", "Update valid to")), 1, row);
                        T.add(dateSync, 2, row);
                        row++;
                    } else {
                        if (DATESYNC)
                                T.add(new HiddenInput(prmDateSync, "true"));
                    }

                    TextInput newAddress = new TextInput("new_address");
                    newAddress.setAsNotEmpty(localize(
                            "err_new_address", "You must enter a new address"));
                    TextInput newZip = new TextInput("new_zip");
                    newZip.setAsNotEmpty(localize("err_new_zip",
                            "You must enter a new zip code"));
                    TextInput newPhone = new TextInput("new_phone");
                    newPhone.setAsNotEmpty(localize(
                            "err_new_phone", "You must enter a new phone"));

                    T.add(Edit.formatText(localize(
                            "new_address", "New address")), 1, row);
                    T.add(newAddress, 2, row);
                    row++;
                    T.add(Edit.formatText(localize("new_zip",
                            "New zip")), 1, row);
                    T.add(newZip, 2, row);
                    row++;
                    T.add(Edit.formatText(localize("new_phone",
                            "New phone")), 1, row);
                    T.add(newPhone, 2, row);
                    row++;
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

    private void doReSignContract(IWContext iwc) {
        Integer id = Integer.valueOf(iwc.getParameter("contract_id"));
        int usid = Integer.parseInt(iwc.getParameter("us_id"));
        String sInfo = iwc.getParameter("resign_info");
        if (sInfo == null) sInfo = "";
        String sMovDate = iwc.getParameter("mov_date");
        IWTimestamp movDate = null;
        if (sMovDate != null && sMovDate.length() == 10)
                movDate = new IWTimestamp(sMovDate);
        boolean datesync = iwc.getParameter(prmDateSync) != null;
        try {
			//System.out.println("saving shit");
			if (isAdmin) {
			    System.out.println("is admin");
			    getContractService(iwc).endContract(id,movDate,sInfo,datesync);
			    //ContractBusiness.endContract(id, movDate, sInfo, datesync);

			} else if (eUser != null && usid == eUser.getID()) {
			    System.out.println("is other user");
			    getContractService(iwc).resignContract(id,movDate,sInfo,datesync);
			    //ContractBusiness.resignContract(id, movDate, sInfo, datesync);

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
            if (iwc.isParameterSet("applicant_id")) {
                Integer applicantID = Integer.valueOf(iwc
                        .getParameter("applicant_id"));
                Applicant applicant = ((ApplicantHome) IDOLookup
                        .getHome(Applicant.class))
                        .findByPrimaryKey(applicantID);
                if (iwc.isParameterSet("new_address")) {
                    applicant.setResidence(iwc.getParameter("new_address"));
                }
                if (iwc.isParameterSet("new_zip")) {
                    applicant.setPO(iwc.getParameter("new_zip"));
                }
                if (iwc.isParameterSet("new_phone")) {

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
        eUser = iwc.getUser();
        isAdmin = iwc.isParameterSet(prmAdmin);
        isLoggedOn = com.idega.core.accesscontrol.business.LoginBusinessBean
                .isLoggedOn(iwc);
        control(iwc);
    }

}