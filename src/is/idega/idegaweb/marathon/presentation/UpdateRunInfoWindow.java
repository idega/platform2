/*
 * Created on Aug 19, 2004
 *
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author birna
 *
 */
public class UpdateRunInfoWindow extends Window {
	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_DISPLAY = 1;
	private static final int ACTION_SAVE = 2;
	
	//texts
	private Text yearText;
	private Text runText;
	private Text distanceText;
	private Text groupText;
	private Text participantNumberText;
	private Text chipNumberText;
	private Text runTimeText;
	private Text chipTimeText;
	private Text bestTimeText;
	private Text goalTimeText;
	private Text tshirtText;
	private Text payMethodText;
	private Text payedAmountText;
	private Text nationalityText;

	//fields
	private Text runField;
	private Text yearField;
	private Text distanceField;
	private Text groupField;
	private SubmitButton submitButton;
	private TextInput participantNumberField;
	private TextInput chipNumberField;
	private String runTimeField;
	private String chipTimeField;
	private TextInput bestTimeField;
	private TextInput goalTimeField;
	private DropdownMenu nationalityField;

	private DropdownMenu tShirtField;
	private SelectOption selectAdult;
	private SelectOption small;
	private SelectOption medium;
	private SelectOption large;
	private SelectOption xlarge;
	private SelectOption xxlarge;
	private SelectOption selectKids;
	private SelectOption smallKids;
	private SelectOption mediumKids;
	private SelectOption largeKids;
	private SelectOption xlargeKids;
	
	private DropdownMenu payMethodField;
	private SelectOption notPayed;
	private SelectOption credit;
	private SelectOption debet;
	private SelectOption cash;
	
	private TextInput payedAmountField;
	
	private Form f;
	
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		yearText = new Text(iwrb.getLocalizedString("run_tab.year", "Year"));
		runText = new Text(iwrb.getLocalizedString("run_tab.run", "Run"));
		distanceText = new Text(iwrb.getLocalizedString("run_tab.distance", "Distance"));
		groupText = new Text(iwrb.getLocalizedString("run_tab.group", "Group"));
		participantNumberText = new Text(iwrb.getLocalizedString("run_tab.participant_nr", "Participant number"));
		chipNumberText = new Text(iwrb.getLocalizedString("run_tab.chip_nr", "Chip number"));
		runTimeText = new Text(iwrb.getLocalizedString("run_tab.run_time", "Run time"));
		chipTimeText = new Text(iwrb.getLocalizedString("run_tab.chip_time", "Chip time"));
		bestTimeText = new Text(iwrb.getLocalizedString("run_tab.best_time", "Best time"));
		goalTimeText = new Text(iwrb.getLocalizedString("run_tab.goal_time", "Goal time"));
		tshirtText = new Text(iwrb.getLocalizedString("run_tab.tshirt", "Tshirt size"));
		payMethodText = new Text(iwrb.getLocalizedString("run_tab.pay_method", "Pay method"));
		payedAmountText = new Text(iwrb.getLocalizedString("run_tab.payed_amount", "Payed amount"));
		nationalityText = new Text(iwrb.getLocalizedString("run_tab.nationality", "Nationality"));
	}
	
	public void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		RunBusiness runBiz = getRunBiz(iwc);
		String userID = iwc.getParameter("ic_user_id");
		String runGroupID = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
		User user = null;
		if(userID != null) {
			try {
				user = getUserBiz().getUser(Integer.parseInt(userID));
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
		if(runGroupID != null && !runGroupID.equals("")) {
			Group runGroup = null;
			try {
				runGroup = getGroupBiz().getGroupByGroupID(Integer.parseInt(runGroupID));
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			runField = new Text(iwrb.getLocalizedString(runGroup.getName(),runGroup.getName()));
		}
		Group dis = getRunBiz(iwc).getDistanceByUserID(Integer.parseInt(userID));
		f.addParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE,dis.getPrimaryKey().toString());
		Run run = null;
		if(userID != null && runGroupID != null) {
			run = getRunBiz(iwc).getRunObjByUserIDandDistanceID(Integer.parseInt(userID),Integer.parseInt(dis.getPrimaryKey().toString()));
		}
		participantNumberField = new TextInput(IWMarathonConstants.PARAMETER_PARTICIPANT_NUMBER);
		chipNumberField = new TextInput(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
		tShirtField = new DropdownMenu(IWMarathonConstants.PARAMETER_TSHIRT);
		SelectOption empty = new SelectOption(iwrb.getLocalizedString("run_reg.select_tee_shirt_size", "Select tee-shirt size"), "-1");
		SelectOption selectAdult = new SelectOption(iwrb.getLocalizedString("run_reg.adult_sized", "Adult sizes"), "-1");
		small = new SelectOption("- " + iwrb.getLocalizedString("run_reg.small", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S);
		medium = new SelectOption("- " + iwrb.getLocalizedString("run_reg.medium", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M);
		large = new SelectOption("- " + iwrb.getLocalizedString("run_reg.large", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L);
		xlarge = new SelectOption("- " + iwrb.getLocalizedString("run_reg.xlarge", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL);
		xxlarge = new SelectOption("- " + iwrb.getLocalizedString("run_reg.xxlarge", "Largest"), IWMarathonConstants.PARAMETER_TSHIRT_XXL);

		tShirtField.addOption(empty);
		tShirtField.addOption(selectAdult);
		tShirtField.addOption(small);
		tShirtField.addOption(medium);
		tShirtField.addOption(large);
		tShirtField.addOption(xlarge);
		tShirtField.addOption(xxlarge);

		selectKids = new SelectOption(iwrb.getLocalizedString("run_reg.kids_sized", "Kids sizes"), "-1");
		smallKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.small_kids", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S + "_kids");
		mediumKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.medium_kids", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M + "_kids");
		largeKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.large_kids", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L + "_kids");
		xlargeKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.xlarge_kids", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL + "_kids");

		tShirtField.addOption(selectKids);
		tShirtField.addOption(smallKids);
		tShirtField.addOption(mediumKids);
		tShirtField.addOption(largeKids);
		tShirtField.addOption(xlargeKids);

		if(run != null) {
			participantNumberField.setContent(String.valueOf(run.getParticipantNumber()));
			chipNumberField.setContent(String.valueOf(run.getChipNumber()));
			tShirtField.setSelectedElement(run.getTShirtSize());
		}
		submitButton = new SubmitButton(iwrb.getLocalizedString("run_tab.save", "Save"),PARAMETER_ACTION,Integer.toString(ACTION_SAVE));
		submitButton.setAsImageButton(true);
		
		payMethodField = new DropdownMenu(IWMarathonConstants.PARAMETER_PAY_METHOD);
		notPayed = new SelectOption(iwrb.getLocalizedString("run_tab.not_payed", "Not payed"), "not_payed");
		credit = new SelectOption(iwrb.getLocalizedString("run_tab.credit", "Credit"), "credit");
		debet = new SelectOption(iwrb.getLocalizedString("run_tab.debet", "Debet"), "debet");
		cash = new SelectOption(iwrb.getLocalizedString("run_tab.cash", "Cash"), "cash");
		
		payMethodField.addOption(notPayed);
		payMethodField.addOption(credit);
		payMethodField.addOption(debet);
		payMethodField.addOption(cash);
		
		payedAmountField = new TextInput(IWMarathonConstants.PARAMETER_AMOUNT);
	}

	public boolean store(IWContext iwc) {
		String payMethod = iwc.getParameter(IWMarathonConstants.PARAMETER_PAY_METHOD);
		String payedAmount = iwc.getParameter(IWMarathonConstants.PARAMETER_AMOUNT);
		String participantNr = iwc.getParameter(IWMarathonConstants.PARAMETER_PARTICIPANT_NUMBER);
		String chipNr = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
		String distance = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		String userIDString = iwc.getParameter("ic_user_id");
		User user = null;
		if(userIDString != null && !userIDString.equals("")) {
			try {
				user = getUserBiz().getUser(Integer.parseInt(userIDString));
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
		int userID = Integer.parseInt(user.getPrimaryKey().toString());
		int disID = Integer.parseInt(distance);
		try {
//			getRunBiz(iwc).savePayment(userID,disID,payMethod,payedAmount);
			getRunBiz(iwc).savePaymentByUserID(userID,payMethod,payedAmount);
			getRunBiz(iwc).updateParticipantAndChip(userID,participantNr,chipNr);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Table t = new Table();
		t.add(getResourceBundle(iwc).getLocalizedString("update.successful", "Update successful"));
		f.add(t);
		
		return true;
	}
	public void main(IWContext iwc)throws Exception{
		f = new Form();
		f.maintainAllParameters();
		initializeTexts();
		initializeFields();

		switch (parseAction(iwc)) {
			case ACTION_DISPLAY:
				lineUp(iwc);
				break;
			case ACTION_SAVE:
				store(iwc);
				break;
		}
		add(f);
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_DISPLAY;
		}
	}
	public void lineUp(IWContext iwc) {
		Table t = new Table();
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.add(runText + ": ",1,1);
		t.add(runField,2,1);
		t.add(distanceText + ": ",1,2);
		t.add(distanceField,2,2);
		t.add(groupText + ": ",1,3);
		t.add(groupField,2,3);
		t.add(payMethodText + ": ",1,4);
		t.add(payMethodField, 2,4);
		t.add(payedAmountText + ": ", 1,5);
		t.add(payedAmountField,2,5);
		t.add(participantNumberText + ": ",1,6);
		t.add(participantNumberField,2,6);
		t.add(chipNumberText + ": ",1,7);
		t.add(chipNumberField,2,7);
		t.add(tshirtText + ": ",1,8);
		t.add(tShirtField,2,8);
		t.add(submitButton,1,9);
		f.add(t);
	}
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}
	private UserBusiness getUserBiz() throws IBOLookupException {
		UserBusiness business = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		return business;
	}
	private GroupBusiness getGroupBiz() throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		return business;
	}






}
