package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentStatus;
import com.idega.block.finance.data.FinanceAccount;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffGroup;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class AccountTariffer extends Finance {
	private static String prmGroup = "at_grp";
	private FinanceAccount account;
	private Integer accountId = null;
	private Integer externalID = null;
	private int viewPage = -1;
	boolean showAllTariffs = false;
	private String prmNewTariff = "fin_ati_nwta";
	private String prmQuantity = "fin_trf_qty";
	private String prmTariffIds = "fin_trf_ids";
	private String prmTariffCheck = "fin_trf_chk";
	private String prmTariffName = "fin_trf_nme";
	private String prmTariffInfo = "fin_trf_ifo";
	private String prmAccountKey = "fin_acc_kid";
	private String prmTariffGroupId = "fin_tgr_id";
	private String prmPayDate = "fin_pay_dte";
	private String prmAmount = "fin_trf_amt";
	private String prmDiscount = "fin_trf_dsc";
	private String prmConfirm = "fin_confirm";
	private String prmSaveTariff = "fin_sve_trf";
	Integer groupId = null;
	public AccountTariffer() {
	}
	protected void control(IWContext iwc) throws java.rmi.RemoteException {
		if (isAdmin) {
			
			if(getExternalIDParameter()!=null && iwc.isParameterSet(getExternalIDParameter()))
				externalID = Integer.valueOf(iwc.getParameter(getExternalIDParameter()));
			
			showAllTariffs = iwc.isParameterSet("fin_show_all");
			
			//      iCategoryId = Finance.parseCategoryId(iwc);
			Collection groups = null;
			try {
				groups = getFinanceService().getTariffGroupHome().findByCategory(getFinanceCategoryId());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
			//List groups =
			// FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
			TariffGroup group = null;
			if (iwc.isParameterSet(prmGroup))
				groupId = Integer.valueOf(iwc.getParameter(prmGroup));
			if (groupId != null && groupId.intValue() > 0) {
				try {
					group = getFinanceService().getTariffGroupHome().findByPrimaryKey(groupId);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
				//group =
				// FinanceFinder.getInstance().getTariffGroup(iGroupId);
			} else if (groups != null) {
				//group = (TariffGroup) groups.get(0);
				//iGroupId = group.getID();
			}
			if (iwc.isParameterSet(prmAccountId)) {
				accountId = Integer.valueOf(iwc.getParameter(prmAccountId));
				if (accountId != null && accountId.intValue() > 0) {
					parse(iwc);
					try {
						account = (FinanceAccount)getFinanceService().getAccountHome().findByPrimaryKey(accountId);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (FinderException e) {
						e.printStackTrace();
					}
					//account =
					// FinanceFinder.getInstance().getFinanceAccount(iAccountId);
				}
			}
			
			setLocalizedTitle("account_tariffer", "Account tariffer");
			setSearchPanel(getAccountInfo(iwc));
			PresentationObject external = getExternalInfo(iwc);
			if(external!=null)
				setSearchPanel(external);
		
			setTabPanel(getGroupLinks(groups));
			if (group != null)
				setMainPanel(getTariffTable(group));
			else
				setMainPanel(getNewTariffTable(iwc));
			setMainPanel(getAssessmentRoundChoiceTable(iwc));
			setMainPanel(getTariffPropertiesTable());
			//T.setWidth("450");
			addHiddenInput(prmCategoryId, getFinanceCategoryId().toString());
			if(groupId!=null)
				addHiddenInput(prmGroup, groupId.toString());
			
			
		}
	}
	private void parse(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet(prmConfirm) && iwc.getParameter(prmConfirm).equals("true")) {
			//System.err.println("confirmation");
			String paydate = iwc.getParameter(prmPayDate);
			IWTimestamp Pd = new IWTimestamp(paydate);
			String SDiscount = iwc.getParameter(prmDiscount);
			Integer assessmentRound = iwc.isParameterSet("latest_assmts")?Integer.valueOf(iwc.getParameter("latest_assmts")):null;
			int discount = SDiscount != null && !SDiscount.equals("") ? Integer.parseInt(SDiscount) : -1;
			System.out.println(discount);
			//AssessmentBusiness assBuiz = getAssessmentService(iwc);
			String[] qtys = iwc.getParameterValues(prmQuantity);
			String[] ids = iwc.getParameterValues(prmTariffIds);
			if (qtys != null && qtys.length > 0 && ids != null && qtys.length == ids.length) {
				Vector tariffIDs = new Vector();
				Vector factors = new Vector();
				for (int i = 0; i < qtys.length; i++) {
					if (!"".equals(qtys[i])) {
						try {
							factors.add(Double.valueOf(qtys[i]));
							tariffIDs.add(Integer.valueOf(ids[i]));
						} catch (NumberFormatException e) {
						}
					}
				}
				if (tariffIDs.size() > 0) {
					Integer[] tariffIds = (Integer[]) tariffIDs.toArray(new Integer[0]);
					Double[] mfactors = (Double[]) factors.toArray(new Double[0]);
					createTariffs(iwc,Pd, discount, tariffIds, mfactors,assessmentRound);
				}
			} else if (iwc.isParameterSet(prmTariffCheck)) {
				//System.err.println("using tariffs checks");
				String[] tariff_ids = iwc.getParameterValues(prmTariffCheck);
				Integer[] tar_ids = new Integer[tariff_ids.length];
				for (int i = 0; i < tariff_ids.length; i++) {
					tar_ids[i] = new Integer(tariff_ids[i]);
				}
				createTariffs(iwc,Pd, discount, tar_ids,assessmentRound);
			} else {
				Integer keyId = iwc.isParameterSet(prmAccountKey) ? Integer.valueOf(iwc.getParameter(prmAccountKey)) : null;
				;
				int price = iwc.isParameterSet(prmAmount) ? Integer.parseInt(iwc.getParameter(prmAmount)) : 0;
				if (keyId.intValue() > 0 && price != 0) {
					Integer TariffGroupId = iwc.isParameterSet(prmTariffGroupId) ? Integer.valueOf(iwc
							.getParameter(prmTariffGroupId)) : null;
					//System.err.println("using new tariff");
					String name = iwc.getParameter(prmTariffName);
					String info = iwc.getParameter(prmTariffInfo);
					boolean saveTariff = iwc.isParameterSet(prmSaveTariff);
					createTariff(iwc,Pd, keyId, price, TariffGroupId, name, info, saveTariff,assessmentRound);
				}
			}
		}
	}
	
	protected PresentationObject getExternalInfo(IWContext iwc){
		return null;
	}
	
	protected void createTariffs(IWContext iwc,IWTimestamp paymentDate, int discountPercentage,  Integer[] tariffIDs, Double[] multiplyFactors,Integer assessmentRound) throws RemoteException {
		//System.err.println("trying
		// assessTariffsToAcount("+tariffIds+","+iAccountId+","+Pd.toString()+","+discount+","+iGroupId+","+iCategoryId);
		getAssessmentService(iwc).assessTariffsToAccount(tariffIDs, multiplyFactors, accountId, paymentDate.getSQLDate(),
				discountPercentage, groupId, getFinanceCategoryId(),externalID,assessmentRound);
	}
	protected void createTariff(IWContext iwc,IWTimestamp paymentDate, Integer tariffkeyID, int amount, Integer tariffGroupID, String name, String info, boolean saveTariff,Integer assessmentRound) throws RemoteException {
		getAssessmentService(iwc).assessTariffsToAccount(amount, name, info, accountId, tariffkeyID, paymentDate.getSQLDate(),
				tariffGroupID, getFinanceCategoryId(), externalID,saveTariff,assessmentRound);
	}
	protected AssessmentBusiness getAssessmentService(IWApplicationContext iwac) throws RemoteException {
		return getFinanceService().getFinanceBusiness();
	}
	protected void createTariffs(IWContext iwc,IWTimestamp paymentDate, int discountPercentage,  Integer[] tariffIDs,Integer assessmentRound) throws RemoteException {
		getAssessmentService(iwc).assessTariffsToAccount(tariffIDs, null, accountId, paymentDate.getSQLDate(), discountPercentage, groupId
				, getFinanceCategoryId(),externalID,assessmentRound);
	}
	protected void createInvoice(Integer accountID, Integer tariffGroupID, Integer categoryID, Date dueDate, List tariffs,List multipliers , int discount){
		
	}
	
	private PresentationObject getAccountInfo(IWContext iwc) throws java.rmi.RemoteException {
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setTitlesVertical(true);
		if (account != null) {
			T.add(getHeader(localize("account_number", "Account number")), 1,1);
			if (viewPage > 0) {
				Link viewLink = getLink((account.getAccountName()));
				viewLink.addParameter(prmAccountId, account.getAccountId().toString());
				viewLink.addParameter(getCategoryParameter(iCategoryId));
				viewLink.setPage(viewPage);
				T.add(viewLink, 2, 1);
			} else {
				T.add(getText(account.getAccountName()), 2, 1);
			}
			UserBusiness uBuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			User user = uBuiz.getUser(account.getUserId());
			T	.add(getHeader(localize("account_owner", "Account owner")), 1, 2);
			T.add(getText(user.getName()+Text.getNonBrakingSpace()+" ( "+user.getPersonalID() +" )"), 2, 2);
			
			T.add(getHeader(localize("published_account_balance", "Published Account balance")),					1, 3);
			T.add(getAmountText(getFinanceService().getAccountBalance(account.getAccountId(),AssessmentStatus.PUBLISHED)), 2, 3);

			T.add(getHeader(localize("account_balance", "Account balance")),					1, 4);
			T.add(getAmountText(getFinanceService().getAccountBalance(account.getAccountId())), 2, 4);

			
			T.add(getHeader(localize("last_updated", "Last updated")), 1, 5);
			java.util.Date lastUpdate = getFinanceService().getAccountLastUpdate(account.getAccountId());
			if(lastUpdate!=null){
				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
				T.add(getText(df.format(lastUpdate)), 2, 5);
			}
		}
		return T;
	}
	private PresentationObject getGroupLinks(Collection groups) throws java.rmi.RemoteException {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		int col = 1;
		if (groups != null) {
			java.util.Iterator I = groups.iterator();
			TariffGroup group;
			Link tab;
			while (I.hasNext()) {
				group = (TariffGroup) I.next();
				tab = new Link(iwb.getImageTab(group.getName(), false));
				tab.addParameter(Finance.getCategoryParameter(iCategoryId));
				tab.addParameter(prmGroup, group.getPrimaryKey().toString());
				if (account != null)
					tab.addParameter(prmAccountId, account.getAccountId().toString());
				T.add(tab, col++, 1);
			}
		}
		Link newTariff = new Link(iwrb.getLocalizedImageTab("new", "New", false));
		newTariff.addParameter(getCategoryParameter(iCategoryId));
		if (account != null)
			newTariff.addParameter(prmAccountId, account.getAccountId().toString());
		newTariff.addParameter(prmNewTariff, "true");
		//Link edit = new
		// Link(iwrb.getLocalizedImageTab("edit","textFormat",false));
		//edit.setWindowToOpen(TariffGroupWindow.class);
		//edit.addParameter(Finance.getCategoryParameter(iCategoryId));
		T.add(newTariff, col, 1);
		return T;
	}
	private PresentationObject getTariffTable(TariffGroup group) throws java.rmi.RemoteException {
		Collection tariffs = null;
		Map map = null;
		boolean hasMap = false;
	
		if (group != null) {
			if (group.getHandlerId() > 0) {
				FinanceHandler handler = getFinanceService().getFinanceHandler(new Integer(group.getHandlerId()));
				//FinanceHandler handler =
				// FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
				map = handler.getAttributeMap();
				if(!showAllTariffs)
					tariffs = handler.getTariffsForAccountInGroup(this.accountId,(Integer)group.getPrimaryKey());
				if (map != null)
					hasMap = true;
			}
		}
		if(tariffs==null || tariffs.isEmpty()){
			showAllTariffs = true;
			try {
				tariffs = getFinanceService().getTariffHome().findByTariffGroup((Integer) group.getPrimaryKey());
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		//listOfTariffs =
		// FinanceFinder.getInstance().listOfTariffs(group.getID());
		//Table T = new Table();
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.addTitle(localize("tariffs", "Tariffs") + "  " + group.getName());
		T.setTitlesVertical(false);
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("use", "Use")), col++, row);
		if (hasMap)
			T.add(getHeader(localize("attribute", "Attribute")), col++, row);
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("price", "Price")), col++, row);
		T.add(getHeader(localize("quantity", "Qty.")), col++, row);
		row++;
		if (tariffs != null) {
			java.util.Iterator I = tariffs.iterator();
			Tariff tariff;
			while (I.hasNext()) {
				col = 1;
				tariff = (Tariff) I.next();
				CheckBox chk = getCheckBox(prmTariffCheck, tariff.getPrimaryKey().toString());
				T.add(chk, col++, row);
				if (hasMap)
					T.add(getText((String) map.get(tariff.getTariffAttribute())), col++, row);
				T.add(getText(tariff.getName()), col++, row);
				T.add(getAmountText(tariff.getPrice()), col++, row);
				TextInput QtyInput = getTextInput(prmQuantity);
				QtyInput.setLength(5);
				T.add(QtyInput, col, row);
				T.add(new HiddenInput(prmTariffIds, tariff.getPrimaryKey().toString()));
				row++;
			}
			T.getContentTable().setColumnAlignment(col - 1, "right");
			T.getContentTable().setAlignment(1, col, "left");
			if(!showAllTariffs)
				T.addButton(new SubmitButton(iwrb.getLocalizedImageButton("fin_show_all", "Show All"), "fin_show_all","true"));
			
		}
		return T;
	}
	private PresentationObject getTariffPropertiesTable() throws java.rmi.RemoteException {
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setTitlesHorizontal(true);
		//T.addTitle(localize("properties","Properties"));
		int row = 1;
		int col = 1;
		T.add(getHeader(localize("paydate", "Paydate")), col, row++);
		DateInput payDate = new DateInput(prmPayDate, true);
		payDate.setDate(IWTimestamp.RightNow().getSQLDate());
		T.add(payDate, col, row);
		col++;
		row = 1;
		T.add(getHeader(localize("discount", "Discount") + " (%)"), col, row++);
		//DropdownMenu dr = getIntDrop("discount",0,100,"");
		TextInput discount = getTextInput(prmDiscount);
		discount.setContent("0");
		T.add(discount, col, row);
		col++;
		row = 2;
		SubmitButton confirm = new SubmitButton(iwrb.getLocalizedImageButton("fin_confirm", "Confirm"), prmConfirm,
				"true");
		T.add(confirm, col, row);
		if (account != null)
			T.add(new HiddenInput(prmAccountId, String.valueOf(account.getAccountId())));
		return T;
	}
	
	private PresentationObject getAssessmentRoundChoiceTable(IWContext iwc) throws java.rmi.RemoteException {
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setTitlesHorizontal(false);
		//T.addTitle(localize("properties","Properties"));
		int row = 1;
		int col = 1;
		T.add(getHeader(localize("latest_assessmendround", "Latest assessments")), col++, row);
		DropdownMenu latestAssessments = new DropdownMenu("latest_assmts");
		latestAssessments.addMenuElement("",localize("create_new_assessment","Create new assessment"));
		try {
			Collection rounds = getFinanceService().getAssessmentRoundHome().findByCategoryAndTariffGroup(getFinanceCategoryId(),getGroupId(),null,null,AssessmentStatus.ASSESSED,20,-1);
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
			for (Iterator iter = rounds.iterator(); iter.hasNext();) {
				AssessmentRound element = (AssessmentRound) iter.next();
				String display = element.getName()+" ("+df.format(element.getRoundStamp())+")";
				latestAssessments.addMenuElement(element.getPrimaryKey().toString(),display);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		T.add(latestAssessments,col,row);
		return T;
	}
	private PresentationObject getNewTariffTable(IWContext iwc) throws java.rmi.RemoteException {
		DataTable T = getDataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
		T.setTitlesVertical(true);
		TextInput tariffName = getTextInput(prmTariffName);
		DropdownMenu accountKeys = getAccountKeysDrop(prmAccountKey);
		DropdownMenu tariffGroups = getTariffGroupsDrop(prmTariffGroupId);
		CheckBox saveTariff = new CheckBox(prmSaveTariff);
		TextInput amount = getTextInput(prmAmount);
		T.add(getHeader(localize("tariff.name", "Tariff name")), 1, 1);
		T.add(getHeader(localize("tariff.account_key", "Account key")), 1, 2);
		T.add(getHeader(localize("tariff.save_under", "Save under")), 1, 3);
		T.add(getHeader(localize("tariff.amount", "Amount")), 1, 4);
		T.add(getHeader(localize("tariff.save_tariff", "Save tariff")), 1, 5);
		T.add(tariffName, 2, 1);
		T.add(accountKeys, 2, 2);
		T.add(tariffGroups, 2, 3);
		T.add(amount, 2, 4);
		T.add(saveTariff, 2, 5);
		return T;
	}
	private DropdownMenu getAccountKeysDrop(String name) throws java.rmi.RemoteException {
		Collection keys = null;
		try {
			keys = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		DropdownMenu drp = new DropdownMenu(name);
		drp = (DropdownMenu )setStyle(drp,STYLENAME_INTERFACE);
		if (keys != null) {
			Iterator iter = keys.iterator();
			while (iter.hasNext()) {
				AccountKey key = (AccountKey) iter.next();
				drp.addMenuElement(key.getPrimaryKey().toString(), key.getInfo());
			}
		}
		return drp;
	}
	private DropdownMenu getTariffGroupsDrop(String name) throws java.rmi.RemoteException {
		//Collection groups =
		// FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
		//Collection groups =
		// FinanceFinder.getInstance().listOfTariffGroupsWithOutHandlers(iCategoryId);
		Collection groups = null;
		try {
			groups = getFinanceService().getTariffGroupHome().findByCategoryWithouthHandlers(getFinanceCategoryId());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		DropdownMenu drp = new DropdownMenu(name);
		drp = (DropdownMenu )setStyle(drp,STYLENAME_INTERFACE);
		if (groups != null) {
			Iterator iter = groups.iterator();
			while (iter.hasNext()) {
				TariffGroup grp = (TariffGroup) iter.next();
				drp.addMenuElement(grp.getPrimaryKey().toString(), grp.getName());
			}
		}
		return drp;
	}
	
	protected String getExternalIDParameter(){
		return null;
	}
	
	public void setAccountViewPage(int pageId) {
		this.viewPage = pageId;
	}
	
	public void main(IWContext iwc) throws java.rmi.RemoteException {
		control(iwc);
	}
	/**
	 * @return Returns the account.
	 */
	public FinanceAccount getAccount() {
		return account;
	}
	/**
	 * @return Returns the accountId.
	 */
	public Integer getAccountId() {
		return accountId;
	}
	/**
	 * @return Returns the externalID.
	 */
	public Integer getExternalID() {
		return externalID;
	}
	/**
	 * @return Returns the groupId.
	 */
	public Integer getGroupId() {
		return groupId;
	}
}
