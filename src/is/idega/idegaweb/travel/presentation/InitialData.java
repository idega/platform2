package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.presentation.ServiceSelector;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.Timer;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class InitialData extends TravelManager {
	
	private IWBundle bundle;
	private IWResourceBundle iwrb;
	
	private TravelStockroomBusiness tsb;
	
	private Supplier supplier;
	private Reseller reseller;
	
	private static String sAction = "admin_action";
	private static String parameterEdit = "edit";
	private static String parameterNew = "new";
	private static String parameterInvalidate = "invalidate";
	private static String parameterChoose = "InitialDataChooseSupplier";
	
	
	private static String parameterSupplierId = "supplier_id";
	
	public static String dropdownView = "dropdownView";
	
	private static String parameterViewSupplierInfo = "supplierViewInfo";
	private static String parameterViewResellerInfo = "resellerViewInfo";
	private static String parameterViewHotelPickup = "parameteViewHotelPickup";
	private static String parameterViewPriceCategories = "parameteViewPriceCategories";
	private static String parameterViewProductCategories = "parameterViewProductCategories";
	private static String parameterViewMiscellaneousServices = "parameteMicsServ";
	private static String parameterCreditCardRefund = "parameterCreditcardRefund";
	private static String parameterTPosProperties = "parTPosProp";
	private static String parameterUsers = "parameterUsers";
	public static String PARAMETER_SUPPLY_POOL= "pSupPool";
	private static String PARAMETER_USER_ID = "uid";;
	private static String  parameterVoucher = "paraneterVoucher";
	private String parameterResellerId = "contractResellerId";
	private String parameterSettings = "parameterSettings";
	private String parameterUpdateReseller = "contractUpdateReseller";
	private String parameterSaveNewReseller = "contractSaveNewReseller";
	
	public InitialData() {
	}
	
	public void add(PresentationObject mo) {
		super.add(mo);
	}
	
	public String getBundleIdentifier(){
		return super.IW_BUNDLE_IDENTIFIER;
	}
	
	public void _main(IWContext iwc) throws Exception {
		if (parameterChoose.equals(iwc.getParameter(this.sAction))) {
			try {
				UserHome uhome = (UserHome) IDOLookup.getHome(User.class);
				User user = uhome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_USER_ID)));
		    TravelSessionManager tsm = (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
		    tsm.clearAll();
				LoginBusiness lBiz = new LoginBusiness();
				lBiz.logInAsAnotherUser(iwc, user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super._main(iwc);
	}
	
	public void main(IWContext iwc) throws Exception{
		super.main(iwc);
		initialize(iwc);
		
		if (super.isLoggedOn(iwc)) {
			String action = iwc.getParameter("supplier_action");
			if (action == null) {action = "";}
			
			if (action.equals("")) {
				displayForm(iwc);
			}else if (action.equals("create")) {
				createSupplier(iwc);
			}else if (action.equals("update")) {
				updateSupplier(iwc);
			}
			
			super.addBreak();
		}else {
			add(super.getLoggedOffTable(iwc));
		}
	}
	
	public void initialize(IWContext iwc) throws RemoteException{
		bundle = super.getBundle();
		iwrb = super.getResourceBundle();
		
		supplier = super.getSupplier();
		reseller = super.getReseller();
		
		tsb = getTravelStockroomBusiness(iwc);
	}
	
	private Form getDropdownForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table table = new Table(1,1);
		table.setWidth("90%");
		form.add(table);
		
		DropdownMenu menu = new DropdownMenu(this.dropdownView);
		
		if (supplier != null) {
			menu.addMenuElement(this.parameterViewSupplierInfo, iwrb.getLocalizedString("travel.supplier_information","Supplier information"));
			menu.addMenuElement(this.parameterSettings, iwrb.getLocalizedString("travel.settings","Settings"));
			menu.addMenuElement(this.parameterViewHotelPickup, iwrb.getLocalizedString("travel.hotel_pickup_places","Hotel pick-up places"));
			menu.addMenuElement(this.parameterViewProductCategories, iwrb.getLocalizedString("travel.product_categories","Product categories"));
			menu.addMenuElement(this.parameterViewPriceCategories, iwrb.getLocalizedString("travel.price_categories","Price categories"));
			menu.addMenuElement(this.parameterViewMiscellaneousServices, iwrb.getLocalizedString("travel.misc_services","Miscellaneous services"));
			menu.addMenuElement(this.PARAMETER_SUPPLY_POOL, iwrb.getLocalizedString("travel.supply_pool", "Supply pool"));
			menu.addMenuElement(this.parameterCreditCardRefund, iwrb.getLocalizedString("travel.credidcard","Creditcard"));
			menu.addMenuElement(this.parameterUsers, iwrb.getLocalizedString("travel.users","Users"));
			menu.addMenuElement(this.parameterVoucher, iwrb.getLocalizedString("travel.vouchers","Vouchers"));
		}else if (reseller != null) {
			menu.addMenuElement(this.parameterViewResellerInfo, iwrb.getLocalizedString("travel.reseller_information","Reseller information"));
			menu.addMenuElement(this.parameterSettings, iwrb.getLocalizedString("travel.settings","Settings"));
			menu.addMenuElement(this.parameterUsers, iwrb.getLocalizedString("travel.users","Users"));
			menu.addMenuElement(this.parameterVoucher, iwrb.getLocalizedString("travel.vouchers","Vouchers"));
		}else if (super.isSupplierManager()) {
			menu.addMenuElement("", iwrb.getLocalizedString("travel.supplier_information","Supplier information"));
			menu.addMenuElement(this.parameterCreditCardRefund, iwrb.getLocalizedString("travel.credidcard","Creditcard"));
			menu.addMenuElement(this.parameterTPosProperties, iwrb.getLocalizedString("travel.credidcard_properties","Creditcard Properties"));
		}
		menu.setToSubmit();
		
		String selected = iwc.getParameter(this.dropdownView);
		if (selected != null) {
			menu.setSelectedElement(selected);
		}
		
		table.add(menu);
		return form;
	}
	
	public void displayForm(IWContext iwc) throws SQLException , RemoteException, FinderException{
		add(Text.getBreak());
		
		String action = iwc.getParameter(this.sAction);
		if (action == null) action = "";
		
		add(getDropdownForm(iwc));
		String selected = iwc.getParameter(this.dropdownView);
		if (supplier != null) {
			
			if (selected == null)  selected = this.parameterViewSupplierInfo;
			Form form = null;
			if (selected.equals(this.parameterViewSupplierInfo)) {
				form = getSupplierCreation(iwc, supplier.getID());
			}else if (selected.equals(this.parameterViewHotelPickup)) {
				try {
					AddressDesigner hppd = new AddressDesigner(iwc);
					//hppd.handleInsert(iwc,supplier);
					form = hppd.getAddressDesignerForm(iwc, supplier.getID());
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterViewProductCategories)) {
				try {
					ServiceSelector ss = new ServiceSelector(iwc);
					ss.handleInsert(iwc);
					form = ss.getForm(iwc);
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterViewPriceCategories)) {
				try {
					PriceCategoryDesigner pcd = new PriceCategoryDesigner(iwc);
					pcd.handleInsert(iwc);
					form = pcd.getPriceCategoriesForm(supplier.getID());
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterViewMiscellaneousServices)) {
				try {
					PriceCategoryDesigner pcd = new PriceCategoryDesigner(iwc);
					pcd.setMiscellaneousServices(true);
					pcd.handleInsert(iwc);
					form = pcd.getPriceCategoriesForm(supplier.getID());
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterCreditCardRefund)) {
				try {
					Link link = LinkGenerator.getLinkToRefunderForm(iwc);
					Text text = getText("Refunds");
					text.setFontColor(super.WHITE);
					link.setText(text);
					add(link);
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterUsers)) {
				try {
					Users users = new Users(iwc);
					users.maintainParameter(this.dropdownView, selected);
					form = users.handleInsert(iwc);
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterVoucher)) {
				form = VoucherWindow.getReferenceNumberForm(iwrb);
			}else if (selected.equals(this.parameterSettings)) {
				try {
					SettingsEditor se = new SettingsEditor(iwc);
					se.handleInsert(iwc);
					form = se.getSettingsFrom(iwc);
				}catch (Exception e) {
					e.printStackTrace(System.err);
				}
			} else if (selected.equals(PARAMETER_SUPPLY_POOL)) {
				SupplyPoolEditor spe = new SupplyPoolEditor();
				spe.addParameter(this.dropdownView, selected);
				try {
					form = spe.getForm(iwc);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				form = new Form();
			}
			
			if (form != null) {
				form.maintainParameter(this.dropdownView);
				add(form);
			}
		}else if (reseller != null) {
			if (selected == null)  selected = this.parameterViewResellerInfo;
			Form form = new Form();
			if (selected.equals(this.parameterViewResellerInfo)) {
				
				if (action.equals(this.parameterUpdateReseller)) {
					String sResellerId = iwc.getParameter(this.parameterResellerId);
					System.out.println("NOT SAVING RESELLER !!!! FIX ME");
	//				saveReseller(iwc,Integer.parseInt(sResellerId));
				}
				form = getResellerCreation(reseller.getID());
			}else if (selected.equals(this.parameterUsers)) {
				try {
					Users users = new Users(iwc);
					form = users.handleInsert(iwc);
				}catch (Exception e) {
					e.printStackTrace(System.err);
					form = new Form();
				}
			}else if (selected.equals(this.parameterVoucher)) {
				form = VoucherWindow.getReferenceNumberForm(iwrb);
			}else {
				form = new Form();
			}
			if (form != null) {
				form.maintainParameter(this.dropdownView);
				add(form);
			}
		}else {
			if (action.equals("")) {
				if (selected == null) {
					Table extra = new Table();
					extra.setWidth("90%");
					extra.setAlignment(1,1,"left");
					extra.setAlignment("center");
					Link newSupplier = new Link(iwrb.getImage("buttons/new.gif"));
					newSupplier.addParameter("admin_action","new");
					extra.add(newSupplier,1,1);
					add(extra);
					add(selectSupplier(iwc));
				}else if (selected.equals(this.parameterCreditCardRefund)){
					try {
						Link link = LinkGenerator.getLinkToRefunderForm(iwc);
						Text text = getText("Refunds");
						text.setFontColor(super.WHITE);
						link.setText(text);
						//	              		add(link);
						Form form = TravelCreditcardRefunderWindow.creditcardRefunderForm(iwc, iwrb);
						form.add(Text.NON_BREAKING_SPACE);
						form.add(link);
						add(form);
						
					}catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}else if (selected.equals(this.parameterTPosProperties)) {
					try {
						CreditCardMerchantEditor tme = new CreditCardMerchantEditor(iwc);
						Form form = tme.getTPosMerchantEditorForm(iwc);
						form.maintainParameter(this.dropdownView);
						add(form);
					}catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			else if (action.equals(this.parameterNew)) {
				add(getSupplierCreation(iwc, -1));
			}
			else if (action.equals(this.parameterEdit)) {
				add(getSupplierCreation(iwc, Integer.parseInt(iwc.getParameter(com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()))));
			}
			else if (action.equals(this.parameterInvalidate)) {
				String supplier_id = iwc.getParameter(com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName());
				if (supplier_id != null)
					try {
						getSupplierManagerBusiness(iwc).invalidateSupplier(((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(Integer.parseInt(supplier_id)));
					}catch (Exception e) {
						e.printStackTrace(System.err);
						add(iwrb.getLocalizedString("travel.supplier_was_not_deleted","Supplier was not deleted"));
					}
					
					add(selectSupplier(iwc));
			}
			
		}
		
		
		int row = 0;
	}
	
	
	public Table selectSupplier(IWContext iwc) throws SQLException, RemoteException {
		Table table = new Table();
		table.setBorder(0);
		table.setCellspacing(1);
		table.setColor(super.WHITE);
		table.setWidth("90%");
		
		int row=1;
		
		Link editLink = new Link(iwrb.getImage("buttons/change.gif"));
		editLink.addParameter(this.sAction, this.parameterEdit);
		
		Link deleteLink = new Link(iwrb.getImage("buttons/delete.gif"));
		deleteLink.addParameter(this.sAction, this.parameterInvalidate);
		
		Link chooseLink = new Link(iwrb.getImage("buttons/use.gif"));
		chooseLink.addParameter(this.sAction, this.parameterChoose);
		
		
		Text suppText = (Text) theBoldText.clone();
		suppText.setText(iwrb.getLocalizedString("travel.suppliers","Suppliers"));
		Text suppLogin = (Text) theBoldText.clone();
		suppLogin.setText(iwrb.getLocalizedString("travel.user_name","User name"));
		Text suppPass = (Text) theBoldText.clone();
		suppPass.setText(iwrb.getLocalizedString("travel.password","Password"));
				
		Text suppNameText;
		Text suppLoginText;
		Text suppPassText;
		Link link;
		
		PermissionGroup pGroup;
		User user;
		LoginTable logTable;
		
		table.add(suppText,1,row);
		table.add(suppLogin,2,row);
		//      table.add(Text.NON_BREAKING_SPACE, 3, row);
		table.mergeCells(2,row,3,row);
		table.add(Text.NON_BREAKING_SPACE, 4, row);
		table.setRowColor(row, super.backgroundColor);
		
		Timer t = new Timer();
		t.start();
		SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
		Collection coll = null;
		try {
			coll = suppHome.findAll(super.getSupplierManager());
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		} 
		t.stop();
		System.out.println("Time to getSupplier = "+t.getTimeString());
		
//		Supplier[] supps = com.idega.block.trade.stockroom.data.SupplierBMPBean.getValidSuppliers();
		
		String theColor = super.GRAY;
		Link useLink;
		
		if (coll != null ) {
			Iterator iter = coll.iterator();
			Supplier supp;
			while (iter.hasNext()) {
				supp = (Supplier) iter.next();
			//for (int i = 0; i < supps.length; i++) {
				++row;
				//        theColor = super.getNextZebraColor(super.GRAY, super.WHITE, theColor);
				
				link = (Link) editLink.clone();
				link.addParameter(com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName(),supp.getID());
				table.add(link,4,row);
				table.add(Text.NON_BREAKING_SPACE,4,row);
				useLink = (Link) chooseLink.clone();
				useLink.addParameter(com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName(),supp.getID());
				table.add(useLink,4,row);
				table.add(Text.NON_BREAKING_SPACE,4,row);
				link = (Link) deleteLink.clone();
				link.addParameter(com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName(),supp.getID());
				table.add(link,4,row);
				table.setAlignment(4,row,"right");
				
				table.setRowColor(row, theColor);
				
				suppNameText = (Text) theText.clone();
				suppNameText.setText(supp.getName());
				suppNameText.setFontColor(super.BLACK);
				
				table.add(suppNameText,1,row);
				
				
				//pGroup = SupplierManager.getPermissionGroup(supps[i]);
				try { /** @todo Sko�a betur.......*/
					//users = UserGroupBusiness.getUsersContained(pGroup);
					user = getSupplierManagerBusiness(iwc).getMainUser(supp);
					if (user != null) {
						useLink.addParameter(PARAMETER_USER_ID, user.getPrimaryKey().toString());
						//for (int j = 0; j < users.size(); j++) {
						//if (j > 0) ++row;
						
						//table.setRowColor(row,super.backgroundColor);
						
						//user = (User) users.get(j);
						logTable = LoginDBHandler.getUserLogin(user.getID());
						suppLoginText = (Text) theText.clone();
						suppLoginText.setText(logTable.getUserLogin());
						suppLoginText.setFontColor(super.BLACK);
						suppPassText = (Text) theText.clone();
						suppPassText.setText(logTable.getUserPassword());
						suppPassText.setFontColor(super.BLACK);
						
						table.add(suppLoginText,2,row);
						table.mergeCells(2,row,3,row);
						//}
						
					}
				}catch (Exception e) {
					e.printStackTrace(System.err);
				}
				
				
			}
		}
		
		return table;
	}
	
	
	public Form getSupplierCreation(IWContext iwc, int supplier_id) throws SQLException, RemoteException, FinderException{
		Form form = new Form();
		
		Table table = new Table();
		form.add(table);
		table.setColor(super.WHITE);
		table.setCellspacing(1);
		table.setColumnAlignment(1,"right");
		table.setColumnAlignment(2,"left");
		table.setBorder(0);
		
		int row = 0;
		Supplier lSupplier = null;
		
		Text newSupplierText = (Text) theBigBoldText.clone();
		if (supplier_id == -1) newSupplierText.setText(iwrb.getLocalizedString("travel.new_supplier","New supplier"));
		else newSupplierText.setText(iwrb.getLocalizedString("travel.update_supplier_information","Update supplier information"));
		
		
		Text nameText = (Text) theBoldText.clone();
		nameText.setFontColor(super.BLACK);
		nameText.setText(iwrb.getLocalizedString("travel.name","Name"));
		nameText.addToText(":");
		
		Text descText = (Text) theBoldText.clone();
		descText.setFontColor(super.BLACK);
		descText.setText(iwrb.getLocalizedString("travel.Description","Description"));
		descText.addToText(":");
		
		Text addressText = (Text) theBoldText.clone();
		addressText.setFontColor(super.BLACK);
		addressText.setText(iwrb.getLocalizedString("travel.address_long","Address"));
		addressText.addToText(":");
		
		Text postalText = (Text) theBoldText.clone();
		postalText.setFontColor(super.BLACK);
		postalText.setText(iwrb.getLocalizedString("travel.postal_code_long","Postal code"));
		postalText.addToText(":");
		
		Text phoneText = (Text) theBoldText.clone();
		phoneText.setFontColor(super.BLACK);
		phoneText.setText(iwrb.getLocalizedString("travel.telephone_number_lg","Telephone number"));
		phoneText.addToText(":");
		
		Text faxText = (Text) theBoldText.clone();
		faxText.setFontColor(super.BLACK);
		faxText.setText(iwrb.getLocalizedString("travel.fax","Fax number"));
		faxText.addToText(":");
		
		Text emailText = (Text) theBoldText.clone();
		emailText.setFontColor(super.BLACK);
		emailText.setText(iwrb.getLocalizedString("travel.email_lg","E-mail"));
		emailText.addToText(":");

		Text orgIDText = (Text) theBoldText.clone();
		orgIDText.setFontColor(super.BLACK);
		orgIDText.setText(iwrb.getLocalizedString("travel.organization_id","Organization ID"));
		orgIDText.addToText(":");

		
		Text loginText = (Text) theBoldText.clone();
		loginText.setFontColor(super.BLACK);
		loginText.setText(iwrb.getLocalizedString("travel.user_name","User name"));
		loginText.addToText(":");
		
		Text passwordText = (Text) theBoldText.clone();
		passwordText.setFontColor(super.BLACK);
		passwordText.setText(iwrb.getLocalizedString("travel.password","Password"));
		passwordText.addToText(":");
		
		int inputSize = 40;
		String inputSizeStr = "260";
		
		TextInput name = new TextInput("supplier_name");
		name.setSize(inputSize);
		TextArea description = new TextArea("supplier_description");
		description.setWidth(inputSizeStr);
		description.setHeight("80");
		TextInput address = new TextInput("supplier_address");
		address.setSize(inputSize);
		
		DropdownMenu postalCode = new DropdownMenu("supplier_postal_code");
		PostalCodeHome pch = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
		Collection allPostalCodes = pch.findAllOrdererByCode();
		Iterator iter = allPostalCodes.iterator();
		PostalCode pc;
		while (iter.hasNext()) {
			pc = (PostalCode) iter.next();
			postalCode.addMenuElement(pc.getPrimaryKey().toString(), pc.getPostalCode()+" "+pc.getName());
		}
		TextInput phone = new TextInput("supplier_phone");
		phone.setSize(inputSize);
		TextInput fax = new TextInput("supplier_fax");
		fax.setSize(inputSize);
		TextInput email = new TextInput("supplier_email");
		email.setSize(inputSize);
		TextInput orgID = new TextInput("organization_id");
		orgID.setSize(inputSize);
		TextInput userName = new TextInput("supplier_user_name");
		userName.setAsNotEmpty(iwrb.getLocalizedString("travel.a_username_must_be_selected","Ver�ur a� velja notendanafn"));
		PasswordInput passOne = new PasswordInput("supplier_password_one");
		passOne.setAsNotEmpty("Gimmi flippar");
		PasswordInput passTwo = new PasswordInput("supplier_password_two");
		
		if (supplier_id != -1) {
			table.add(new HiddenInput(this.parameterSupplierId,Integer.toString(supplier_id)));
			
			lSupplier = ((com.idega.block.trade.stockroom.data.SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(supplier_id);
			name.setContent(lSupplier.getName());
			description.setContent(lSupplier.getDescription());
			
			Address addr = lSupplier.getAddress();
			if (addr != null) {
				String namer = addr.getStreetName();
				String number = addr.getStreetNumber();
				if (number == null) {
					address.setContent(namer);
				}else {
					address.setContent(namer+" "+number);
				}
				int iPostalCodeId = addr.getPostalCodeID();
				if (iPostalCodeId != -1){
					postalCode.setSelectedElement(iPostalCodeId);
				}
			}
			
			List phones = lSupplier.getHomePhone();
			if (phones != null) {
				if (phones.size() > 0) {
					Phone phone1 = (Phone) phones.get(0);
					phone.setContent(phone1.getNumber());
				}
			}
			
			phones = lSupplier.getFaxPhone();
			if (phones != null) {
				if (phones.size() > 0) {
					Phone phone2 = (Phone) phones.get(0);
					fax.setContent(phone2.getNumber());
				}
			}
			
			Email eEmail = lSupplier.getEmail();
			if (eEmail != null) {
				email.setContent(eEmail.getEmailAddress());
			}
			if (lSupplier.getOrganizationID() != null) {
				orgID.setContent(lSupplier.getOrganizationID());
			}
		}
		
		SubmitButton submit = null;
		if (supplier_id == -1) {
			submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),"supplier_action","create");
		} else {
			submit = new SubmitButton(iwrb.getImage("buttons/update.gif"),"supplier_action","update");
		}
		BackButton back = new BackButton(iwrb.getImage("buttons/back.gif"));
		Link lBack = new Link(super.getBackLink());
		
		
		++row;
		table.mergeCells(1,row,2,row);
		table.add(newSupplierText,1,row);
		table.setAlignment(1,row,"center");
		table.setRowColor(row,super.backgroundColor);
		
		++row;
		table.add(nameText,1,row);
		table.add(name,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(descText,1,row);
		table.setVerticalAlignment(1,row,"top");
		table.add(description,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(addressText,1,row);
		table.add(address,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(postalText,1,row);
		table.add(postalCode,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(phoneText,1,row);
		table.add(phone,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(faxText,1,row);
		table.add(fax,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(emailText,1,row);
		table.add(email,2,row);
		table.setAlignment(1,row,"left");
		table.setAlignment(2,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(orgIDText, 1,row);
		table.add(orgID, 2, row);
		table.setAlignment(1, row, "left");
		table.setAlignment(2, row, "left");
		table.setRowColor(row, GRAY);
		
		if (supplier_id == -1) {
			++row;
			table.add(loginText,1,row);
			table.add(userName,2,row);
			table.setAlignment(1,row,"left");
			table.setAlignment(2,row,"left");
			table.setRowColor(row,super.GRAY);
			
			++row;
			table.add(passwordText,1,row);
			table.setVerticalAlignment(1,row,"top");
			table.add(passOne,2,row);
			table.addBreak(2,row);
			table.add(passTwo,2,row);
			table.setAlignment(1,row,"left");
			table.setAlignment(2,row,"left");
			table.setRowColor(row,super.GRAY);
		}
		
		++row;
		table.add(Text.NON_BREAKING_SPACE,1,row);
		table.setRowColor(row,super.GRAY);
		table.mergeCells(1,row,2,row);
		++row;
		table.setAlignment(1,row,"left");
		table.add(lBack,1,row);
		if (super.isInPermissionGroup || isSupplierManager()) {
			table.setAlignment(2,row,"right");
			table.add(submit,2,row);
		}
		table.setRowColor(row,super.GRAY);
		
		
		return form;
	}
	
	public void updateSupplier(IWContext iwc) {
		String supplierId = iwc.getParameter(this.parameterSupplierId);
		try {
			createSupplier(iwc, Integer.parseInt(supplierId));
		}catch (NumberFormatException n) {}
		
	}
	
	public void createSupplier(IWContext iwc)  {
		createSupplier(iwc, -1);
	}
	
	public void createSupplier(IWContext iwc, int supplierId)  {
		add(Text.getBreak());
		//      javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
		
		try {
			String name = iwc.getParameter("supplier_name");
			String description = iwc.getParameter("supplier_description");
			String address = iwc.getParameter("supplier_address");
			String postalCode = iwc.getParameter("supplier_postal_code");
			String phone = iwc.getParameter("supplier_phone");
			String fax = iwc.getParameter("supplier_fax");
			String email = iwc.getParameter("supplier_email");
			String orgID = iwc.getParameter("organization_id");
			String supplier_id = iwc.getParameter("supplier_id");
			
			String userName = iwc.getParameter("supplier_user_name");
			String passOne = iwc.getParameter("supplier_password_one");
			String passTwo = iwc.getParameter("supplier_password_two");
			
			int iPostalCode = -1;
			try {
				if (postalCode != null){
					iPostalCode = Integer.parseInt(postalCode);
				}
			}catch (NumberFormatException e) {
			}
			
			
			boolean isUpdate = false;
			if (supplierId != -1) isUpdate = true;
			
			
			if (isUpdate) {
				Vector phoneIDS = new Vector();
				Supplier supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(supplierId);
				
				Phone ph;
				List phones = supplier.getPhones(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
				if (phones != null) {
					if (phones.size() > 0) {
						for (int i = 0; i < phones.size(); i++) {
							ph = (Phone) phones.get(i);
							ph.setNumber(phone);
							ph.update();
							phoneIDS.add(new Integer(ph.getID()));
						}
					}else {
						ph = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						ph.setNumber(phone);
						ph.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
						ph.insert();
						phoneIDS.add(new Integer(ph.getID()));
					}
				}
				phones = supplier.getPhones(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
				if (phones != null) {
					if (phones.size() > 0) {
						for (int i = 0; i < phones.size(); i++) {
							ph = (Phone) phones.get(i);
							ph.setNumber(fax);
							ph.update();
							phoneIDS.add(new Integer(ph.getID()));
						}
					}else {
						ph = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						ph.setNumber(fax);
						ph.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
						ph.insert();
						phoneIDS.add(new Integer(ph.getID()));
					}
				}
				
				int[] phoneIds = new int[phoneIDS.size()];
				for (int i = 0; i < phoneIDS.size(); i++) {
					phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
				}
				
				
				Address addr = supplier.getAddress();
				addr.setStreetName(address);
				if (iPostalCode != -1) {
					addr.setPostalCodeID(iPostalCode);
				}
				addr.update();
				
				int[] addressIds = new int[1];
				addressIds[0] = addr.getID();
				
				
				Email eml = supplier.getEmail();
				eml.setEmailAddress(email);
				eml.update();
				
				int[] emailIds = new int[1];
				emailIds[0] = eml.getID();
				
				supplier = getSupplierManagerBusiness(iwc).updateSupplier(supplierId,name, description, addressIds, phoneIds, emailIds, orgID);
				
				
				add(iwrb.getLocalizedString("travel.information_updated","Information updated"));
				this.displayForm(iwc);
			}
			else {
				//                  tm.begin();
				if (passOne.equals(passTwo) && !LoginDBHandler.isLoginInUse(userName)) {
					
					Vector phoneIDS = new Vector();
					if (phone.length() > 0) {
						Phone phonePhone = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						phonePhone.setNumber(phone);
						phonePhone.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
						phonePhone.insert();
						phoneIDS.add(new Integer(phonePhone.getID()));
					}
					if (fax.length() > 0) {
						Phone faxPhone = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						faxPhone.setNumber(fax);
						faxPhone.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
						faxPhone.insert();
						phoneIDS.add(new Integer(faxPhone.getID()));
					}
					
					int[] phoneIds = new int[phoneIDS.size()];
					for (int i = 0; i < phoneIDS.size(); i++) {
						phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
					}
					
					int[] addressIds = new int[1];
					Address addressAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
					addressAddress.setStreetName(address);
					if (iPostalCode != -1) {
						addressAddress.setPostalCodeID(iPostalCode);
					}
					addressAddress.insert();
					addressIds[0] = addressAddress.getID();
					
					int[] emailIds = new int[1];
					Email eEmail = ((com.idega.core.contact.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy();
					eEmail.setEmailAddress(email);
					eEmail.insert();
					emailIds[0] = eEmail.getID();
					
					Supplier supplier = getSupplierManagerBusiness(iwc).createSupplier(name, userName, passOne, description, addressIds, phoneIds, emailIds, orgID);
					supplier.setSupplierManager(getSupplierManager());
					supplier.store();
					
					this.displayForm(iwc);
				}else {
					if (LoginDBHandler.isLoginInUse(userName)) {
						add(iwrb.getLocalizedString("username_in_use","Username in use"));
						add(Text.BREAK);
					}
					if (!passOne.equals(passTwo)) {
						add(iwrb.getLocalizedString("passwords_not_the_same","Passwords not the same"));
						add(Text.BREAK);
					}
					add(Text.BREAK);
					add(new BackButton(iwrb.getImage("buttons/back.gif")));
					
				}
			}
			
		}
		catch (Exception sql) {
			//        try {
			//          tm.rollback();
			add(iwrb.getLocalizedString("travel.supplier_not_created","Supplier was not created"));
			//        }
			//        catch (javax.transaction.SystemException se) {
			//          se.printStackTrace(System.err);
			//        }
			sql.printStackTrace(System.err);
		}
	}
	
	private Form getResellerCreation(int resellerId) throws SQLException{
		Form form = new Form();
		Table table = new Table();
		form.add(table);
		table.setColor(super.WHITE);
		table.setCellspacing(1);
		table.setAlignment("center");
		table.setColumnAlignment(1,"right");
		table.setBorder(0);
		
		boolean isUpdate = false;
		if (resellerId != -1) {
			isUpdate = true;
		}
		
		int row = 0;
		
		Text newSupplierText = (Text) theBoldText.clone();
		if (isUpdate) newSupplierText.setText(iwrb.getLocalizedString("travel.update_reseller_information","Update reseller information"));
		else newSupplierText.setText(iwrb.getLocalizedString("travel.new_reseller","New Reseller"));
		
		Text nameText = (Text) theBoldText.clone();
		nameText.setText(iwrb.getLocalizedString("travel.name","Name"));
		nameText.addToText(":");
		nameText.setFontColor(super.BLACK);
		
		Text descText = (Text) theBoldText.clone();
		descText.setText(iwrb.getLocalizedString("travel.Description","Description"));
		descText.addToText(":");
		descText.setFontColor(super.BLACK);
		
		Text addressText = (Text) theBoldText.clone();
		addressText.setText(iwrb.getLocalizedString("travel.address_long","Address"));
		addressText.addToText(":");
		addressText.setFontColor(super.BLACK);
		
		Text phoneText = (Text) theBoldText.clone();
		phoneText.setText(iwrb.getLocalizedString("travel.telephone_number_lg","Telephone number"));
		phoneText.addToText(":");
		phoneText.setFontColor(super.BLACK);
		
		Text faxText = (Text) theBoldText.clone();
		faxText.setText(iwrb.getLocalizedString("travel.fax","Fax number"));
		faxText.addToText(":");
		faxText.setFontColor(super.BLACK);
		
		Text emailText = (Text) theBoldText.clone();
		emailText.setText(iwrb.getLocalizedString("travel.email_lg","E-mail"));
		emailText.addToText(":");
		emailText.setFontColor(super.BLACK);
		
		Text refNumberText = (Text) theBoldText.clone();
		refNumberText.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
		refNumberText.addToText(":");
		refNumberText.setFontColor(super.BLACK);
		
		Text refNumberText2 = (Text) theText.clone();
		refNumberText2.setFontColor(super.BLACK);
		
		int inputSize = 40;
		String inputSizeStr = "260";
		
		TextInput name = new TextInput("reseller_name");
		name.setSize(inputSize);
		TextArea description = new TextArea("reseller_description");
		description.setWidth(inputSizeStr);
		description.setHeight("80");
		TextInput address = new TextInput("reseller_address");
		address.setSize(inputSize);
		TextInput phone = new TextInput("reseller_phone");
		phone.setSize(inputSize);
		TextInput fax = new TextInput("reseller_fax");
		fax.setSize(inputSize);
		TextInput email = new TextInput("reseller_email");
		email.setSize(inputSize);
		TextInput userName = new TextInput("reseller_user_name");
		userName.setAsNotEmpty(iwrb.getLocalizedString("travel.a_username_must_be_selected","Ver�ur a� velja notendanafn"));
		PasswordInput passOne = new PasswordInput("reseller_password_one");
		PasswordInput passTwo = new PasswordInput("reseller_password_two");
		
		
		SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction,this.parameterSaveNewReseller);
		BackButton back = new BackButton(iwrb.getImage("buttons/back.gif"));
		
		
		
		if (resellerId != -1) {
			table.add(new HiddenInput(this.parameterResellerId,Integer.toString(resellerId)));
			
			Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
			name.setContent(reseller.getName());
			description.setContent(reseller.getDescription());
			
			Address addr = reseller.getAddress();
			if (addr != null) {
				String namer = addr.getStreetName();
				String number = addr.getStreetNumber();
				if (number == null) {
					address.setContent(namer);
				}else {
					address.setContent(namer+" "+number);
				}
			}
			
			List phones = reseller.getHomePhone();
			if (phones != null) {
				if (phones.size() > 0) {
					Phone phone1 = (Phone) phones.get(0);
					phone.setContent(phone1.getNumber());
				}
			}
			
			phones = reseller.getFaxPhone();
			if (phones != null) {
				if (phones.size() > 0) {
					Phone phone2 = (Phone) phones.get(0);
					fax.setContent(phone2.getNumber());
				}
			}
			
			Email eEmail = reseller.getEmail();
			if (eEmail != null) {
				email.setContent(eEmail.getEmailAddress());
			}
			refNumberText2.setText(reseller.getReferenceNumber());
			submit = new SubmitButton(iwrb.getImage("buttons/update.gif"),this.sAction,this.parameterUpdateReseller);
			
		}
		
		
		++row;
		table.mergeCells(1,row,2,row);
		table.setAlignment(1,row,"center");
		table.add(newSupplierText,1,row);
		table.setRowColor(row,super.backgroundColor);
		
		++row;
		table.add(nameText,1,row);
		table.add(name,2,row);
		table.setRowColor(row,super.GRAY);
		table.setAlignment(1,row,"left");
		
		++row;
		table.add(descText,1,row);
		table.setVerticalAlignment(1,row,"top");
		table.add(description,2,row);
		table.setRowColor(row,super.GRAY);
		table.setAlignment(1,row,"left");
		
		++row;
		table.add(addressText,1,row);
		table.add(address,2,row);
		table.setAlignment(1,row,"left");
		table.setRowColor(row,super.GRAY);
		
		++row;
		table.add(phoneText,1,row);
		table.add(phone,2,row);
		table.setRowColor(row,super.GRAY);
		table.setAlignment(1,row,"left");
		
		++row;
		table.add(faxText,1,row);
		table.add(fax,2,row);
		table.setRowColor(row,super.GRAY);
		table.setAlignment(1,row,"left");
		
		++row;
		table.add(emailText,1,row);
		table.add(email,2,row);
		table.setRowColor(row,super.GRAY);
		table.setAlignment(1,row,"left");
		
		++row;
		table.add(refNumberText,1,row);
		table.add(refNumberText2,2,row);
		table.setRowColor(row,super.GRAY);
		table.setAlignment(1,row,"left");
		
		table.setColumnAlignment(2,"left");
		++row;
		table.setAlignment(1,row,"left");
		table.add(back,1,row);
		table.setAlignment(2,row,"right");
		if (super.isInPermissionGroup) {
			table.add(submit,2,row);
		}
		table.setRowColor(row,super.GRAY);
		
		
		//      add(Text.getBreak());
		return form;
		
		
	}
	/*
	public void saveReseller(IWContext iwc, int resellerId)  {
		add(Text.getBreak());
		try {
			String name = iwc.getParameter("reseller_name");
			String description = iwc.getParameter("reseller_description");
			String address = iwc.getParameter("reseller_address");
			String phone = iwc.getParameter("reseller_phone");
			String fax = iwc.getParameter("reseller_fax");
			String email = iwc.getParameter("reseller_email");
			
			String userName = iwc.getParameter("reseller_user_name");
			String passOne = iwc.getParameter("reseller_password_one");
			String passTwo = iwc.getParameter("reseller_password_one");
			//                  tm.begin();
			boolean isUpdate = false;
			if (resellerId != -1) isUpdate = true;
			
			//          System.err.println(name+" : "+description+" : "+address+" : "+phone+" : "+fax+" : "+email);
			
			
			if (isUpdate) {
				Vector phoneIDS = new Vector();
				Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
				
				Phone ph;
				List phones = reseller.getPhones(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
				if (phones != null) {
					if (phones.size() > 0) {
						for (int i = 0; i < phones.size(); i++) {
							ph = (Phone) phones.get(i);
							ph.setNumber(phone);
							ph.update();
							phoneIDS.add(new Integer(ph.getID()));
						}
					}else {
						ph = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						ph.setNumber(phone);
						ph.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
						ph.insert();
						phoneIDS.add(new Integer(ph.getID()));
					}
				}
				
				phones = reseller.getPhones(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
				if (phones != null) {
					if (phones.size() > 0 ) {
						for (int i = 0; i < phones.size(); i++) {
							ph = (Phone) phones.get(i);
							ph.setNumber(fax);
							ph.update();
							phoneIDS.add(new Integer(ph.getID()));
						}
					}else {
						ph = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						ph.setNumber(fax);
						ph.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
						ph.insert();
						phoneIDS.add(new Integer(ph.getID()));
					}
				}
				
				int[] phoneIds = new int[phoneIDS.size()];
				for (int i = 0; i < phoneIDS.size(); i++) {
					phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
				}
				
				
				Address addr = reseller.getAddress();
				addr.setStreetName(address);
				addr.update();
				
				int[] addressIds = new int[1];
				addressIds[0] = addr.getID();
				
				
				Email eml = reseller.getEmail();
				eml.setEmailAddress(email);
				eml.update();
				
				int[] emailIds = new int[1];
				emailIds[0] = eml.getID();
				
				reseller = getResellerManager(iwc).updateReseller(resellerId,name, description, addressIds, phoneIds, emailIds);
				
				
				add(iwrb.getLocalizedString("travel.information_updated","Information updated"));
				
			}else {
				if (passOne.equals(passTwo)) {
					
					Vector phoneIDS = new Vector();
					if (phone.length() > 0) {
						Phone phonePhone = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						phonePhone.setNumber(phone);
						phonePhone.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
						phonePhone.insert();
						phoneIDS.add(new Integer(phonePhone.getID()));
					}
					if (fax.length() > 0) {
						Phone faxPhone = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
						faxPhone.setNumber(fax);
						faxPhone.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
						faxPhone.insert();
						phoneIDS.add(new Integer(faxPhone.getID()));
					}
					
					
					int[] phoneIds = new int[phoneIDS.size()];
					for (int i = 0; i < phoneIDS.size(); i++) {
						phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
					}
					
					int[] addressIds = new int[1];
					Address addressAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
					addressAddress.setStreetName(address);
					addressAddress.insert();
					addressIds[0] = addressAddress.getID();
					
					int[] emailIds = new int[1];
					Email eEmail = ((com.idega.core.contact.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy();
					eEmail.setEmailAddress(email);
					eEmail.insert();
					emailIds[0] = eEmail.getID();
					
					Reseller reseller = getResellerManager(iwc).createReseller(this.reseller, name, userName, passOne, description, addressIds, phoneIds, emailIds);
					reseller.addTo(supplier);
					
					//add(iwrb.getLocalizedString("travel.reseller_created","Reseller was created"));
				}else {
					add("TEMP - PASSWORDS not the same");
				}
			}
			
		}
		catch (Exception sql) {
			add(iwrb.getLocalizedString("travel.reseller_not_created","Reseller was not created"));
			sql.printStackTrace(System.err);
		}
	}
*/	
}
