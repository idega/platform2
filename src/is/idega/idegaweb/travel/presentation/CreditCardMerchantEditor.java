package is.idega.idegaweb.travel.presentation;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.trade.data.CreditCardInformation;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CreditCardMerchantEditor extends TravelManager {
  /**
   *  @todo Dropa tpos_merchant-id úr grunni a edison og einsein...áður en þetta fer inn
   */

  private String _parameterAction = "prm_act";
  private String _parameterResellerId = "prm_res";
  private String _parameterSupplierId = "prm_sup";
  private String _parameterPassword = "prm_psw";
  private String _parameterMerchantID = "prm_mchid";
  private String _parameterMerchant = "prm_mch";
  private String _parameterLocationID = "prm_locid";
  private String _parameterUserID = "prm_usrid";
  private String _parameterPassw = "prm_pssw";
  private String _parameterPosID = "prm_posid";
  private String _parameterKeyReceivedPassword = "prm_keyrcvpass";
  private String _parameterType = "prm_type";
  private String _actionContinue = "act_con";
  private String _actionInfoSelected = "act_inSel";
  private String _actionSave = "act_sav";
  private String _actionDelete = "act_del";
  private String _actionDeleteVerified = "act_del_ver";
  private String tempPass = "svana";

  private IWResourceBundle _iwrb;
  private Supplier _supplier;
  //private Reseller _reseller;
  private String _name = "";
  private CreditCardMerchant _merchant;

  public CreditCardMerchantEditor(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);
  }

  private void init(IWContext iwc) throws RemoteException{
    _iwrb = super.getResourceBundle();

    try {
      String supplierId = iwc.getParameter(_parameterSupplierId);
      if (supplierId != null && !supplierId.equals("-1")) {
        SupplierHome suppHome = (SupplierHome) IDOLookup.getHomeLegacy(Supplier.class);
        _supplier = suppHome.findByPrimaryKeyLegacy(Integer.parseInt(supplierId));
        _name = _supplier.getName();
        String merchantID = iwc.getParameter(_parameterMerchantID);
        if (merchantID != null) {
          _merchant = getCreditCardBusiness(iwc).getCreditCardMerchant(_supplier, new Integer(merchantID));
        }
      }
      /*else if (resellerId != null && !resellerId.equals("-1")) {
        ResellerHome resHome = (ResellerHome) IDOLookup.getHomeLegacy(Reseller.class);
        _reseller = resHome.findByPrimaryKeyLegacy(Integer.parseInt(resellerId));
        _name = _reseller.getName();
        try {
          _merchant = _reseller.getTPosMerchant();
        }catch (FinderException fe) {
          //fe.printStackTrace(System.err);
          debug("Cannot find TPosMerchant for reseller : "+_name);
        }
      }*/
      
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  public Form getTPosMerchantEditorForm(IWContext iwc) throws RemoteException{
    Form form = new Form();
    if (super.isSupplierManager()) {
      String action = iwc.getParameter(_parameterAction);
      if (action == null || action.equals("")) {
        form = getMainMenu();
      }else if (action.equals(_actionSave)) {
        if (handleInsert(iwc)) {
          form = getMerchantForm();
          form.add(getHeaderText(_iwrb.getLocalizedString("travel.information_updated","Information updated")));
        }else {
          form = getMainMenu();
          form.add(getHeaderText(_iwrb.getLocalizedString("travel.update_failed","Update failed")));
        }
      }else if (action.equals(_actionContinue)){
        String passw = iwc.getParameter(_parameterPassword);
//        if (passw.equals(tempPass)) {
          //form = getMerchantForm();
          form = getCreditCardInformation(iwc);
//        }else {
//          form = getMainMenu();
//          form.add(getHeaderText(_iwrb.getLocalizedString("travel.password invalid","Password invalid")));
//        }
      } else if (action.equals(_actionInfoSelected)) {
        form = getMerchantForm();
      }else if (action.equals(_actionDelete)) {
      	form = verifyDelete(iwc);
      } else if (action.equals(_actionDeleteVerified)) {
      	delete(iwc);
      	form = getMerchantForm();
      }

      return form;
    }else {
      form.add(getHeaderText(_iwrb.getLocalizedString("travel.no_permission","No permission")));
      return form;
    }
  }

  private Form selectCreditCardHandler() {
  	Form form = new Form();
  	/*
  	 * How can i dynamically do this....
  	 * Where do I save the data ?
  	 * 
  	 * 
  	 */
  	return form;
  }

  private Form getMainMenu() throws RemoteException{
    Form form = new Form();
    Table table = getTable();
    form.add(Text.BREAK);
    form.add(table);

    try {
    	SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
    	Collection coll = suppHome.findAll(getSupplierManager());
      //Supplier[] supps = SupplierBMPBean.getValidSuppliers();
      //Reseller[] repps = ResellerBMPBean.getValidResellers();

      DropdownMenu suppMenu = new DropdownMenu(coll, this._parameterSupplierId);
      //DropdownMenu reppMenu = new DropdownMenu(repps, this._parameterResellerId);
        suppMenu.addMenuElementFirst("-1", _iwrb.getLocalizedString("travel.please_select","Please select"));
        suppMenu.keepStatusOnAction();
        //reppMenu.addMenuElementFirst("-1", _iwrb.getLocalizedString("travel.please_select","Please select"));
        //reppMenu.keepStatusOnAction();
      PasswordInput pass = new PasswordInput(_parameterPassword);
      SubmitButton button = new SubmitButton(_iwrb.getLocalizedImageButton("travel.continue","Continue"), this._parameterAction, this._actionContinue );

      table.add(getHeaderText(_iwrb.getLocalizedString("travel.suppliers", "Suppliers")), 1, 1);
      //table.add(getHeaderText(_iwrb.getLocalizedString("travel.resellers", "Resellers")), 2, 1);
      //table.add(getHeaderText(_iwrb.getLocalizedString("travel.password", "Password")), 2, 1);
      table.setRowColor(1, super.backgroundColor);

      table.add(suppMenu, 1, 2);
      //table.add(reppMenu, 2, 2);
      //table.add(pass, 2, 2);
      table.setRowColor(2, super.GRAY);

      //table.mergeCells(1, 2, 2, 2);
      table.setAlignment(1, 3, "right");
      table.setRowColor(3, super.GRAY);
      table.add(button, 1, 3);
      //table.add(getText(_iwrb.getLocalizedString("travel.please_select_a_supplier","Please select a supplier")), 2, 2);


    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }

    return form;
  }

  private Form verifyDelete(IWContext iwc) throws RemoteException {
  	Form form = new Form();
  	Table table = getTable();
  	form.add(table);
  	table.setWidth("400");
  	Text verifyText = null;
  	if (_supplier != null) {
  		verifyText =getText(_iwrb.getLocalizedString("travel.confirm_delete_tpos_merchant", "Are you sure you want to delete the TPOS Merchant for supplier : "));
  		verifyText.addToText(_supplier.getName());
  		form.maintainParameter(this._parameterSupplierId);
  	} /*else if (_reseller != null) {
  		verifyText =getText(_iwrb.getLocalizedString("travel.confirm_delete_tpos_merchant", "Are you sure you want to delete the TPOS Merchant for reseller : "));  		
  		verifyText.addToText(_reseller.getName());
  		form.maintainParameter(this._parameterResellerId);
  	}*/
  	form.maintainParameter(this._parameterSupplierId);
  	int row = 1;
  	table.add(getHeaderText(_iwrb.getLocalizedString("travel.delete_tpos_merchant", "Delete TPOS Merchant")), 1, row);
  	table.mergeCells(1, row, 2, row);
  	table.setRowColor(row, backgroundColor);
  	
  	++row;
  	table.mergeCells(1, row, 2, row);
  	table.add(verifyText, 1, row);
  	table.setRowColor(row, GRAY);
  	
  	++row;
  	table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
  	table.add(getBackLink(1), 1, row);
  	table.add(new SubmitButton(_iwrb.getImage("/buttons/delete.gif"), _parameterAction, _actionDeleteVerified), 2, row);
  	table.setRowColor(row, GRAY);
  	
  	return form;
  }
  
  private void delete(IWContext iwc) {
  	/*
		if (_supplier != null) {
		  _supplier.setTPosMerchantId(null);
		  _supplier.store();
		}*/
		
		/*else if (_reseller != null) {
		  _reseller.setTPosMerchantId(null);
		  _reseller.store();
		}*/
  }
  
  private Form getCreditCardInformation(IWContext iwc) throws RemoteException{
  	Form form = new Form();
  	Table table = getTable();
  	form.add(table);
  	int row = 1;
  	

  	
  	Text name = getHeaderText(_iwrb.getLocalizedString("travel.cc.name", "Name"));
  	Text type = getHeaderText(_iwrb.getLocalizedString("travel.cc.type", "Type"));
  	Text startDate = getHeaderText(_iwrb.getLocalizedString("travel.cc.start_date", "Start date"));
  	Text modDate = getHeaderText(_iwrb.getLocalizedString("travel.cc.modification_date", "Modification date"));
  	Text endDate = getHeaderText(_iwrb.getLocalizedString("travel.cc.end_date", "End date"));

  	table.add(name, 1, row);
  	table.add(type, 2, row);
  	table.add(startDate, 3, row);
  	table.add(modDate, 4, row);
  	table.add(endDate, 5, row);
  	table.setRowColor(row, backgroundColor);
  	
  	Collection coll;
  	
  	try {
			
  		coll = getCreditCardBusiness(iwc).getCreditCardInformations(_supplier);
			if (coll != null) {
				CreditCardInformation info;
				CreditCardMerchant merchant;
				Link lName;
				Text mType;
				Text mFrom;
				Text mMod;
				Text mTo;
				IWTimestamp from;
				IWTimestamp mod;
				IWTimestamp to;
				Locale currentLocale = iwc.getCurrentLocale();
				
				
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					info = (CreditCardInformation) iter.next();
					merchant = getCreditCardBusiness(iwc).getCreditCardMerchant(_supplier, info.getMerchantPKString());
					//merchant = info.getMerchant();
					if (merchant != null) {
						++row;
						lName = new Link(getText(merchant.getName()));
						lName.addParameter(_parameterAction, _actionInfoSelected);
						lName.addParameter(_parameterMerchantID, merchant.getPrimaryKey().toString());
						lName.maintainParameter(InitialData.dropdownView, iwc);
						lName.maintainParameter(_parameterSupplierId, iwc);
						
						mType = getText(merchant.getType());
						if ( merchant.getStartDate() != null ) {
							from = new IWTimestamp(merchant.getStartDate());
							mFrom = getText(from.getLocaleDateAndTime(currentLocale));
						} else {
							mFrom = getText("-");
						}
						if ( merchant.getModificationDate() != null ) {
							mod = new IWTimestamp(merchant.getModificationDate());
							mMod = getText(mod.getLocaleDateAndTime(currentLocale));
						} else {
							mMod = getText("-");
						}
						if ( merchant.getEndDate() != null ) {
							to = new IWTimestamp(merchant.getEndDate());
							mTo = getText(to.getLocaleDateAndTime(currentLocale));
						} else {
							mTo = getText("-");
						}
						
						table.add(lName, 1, row);
						table.add(mType, 2, row);
						table.add(mFrom, 3, row);
						table.add(mMod, 4, row);
						table.add(mTo, 5, row);
						table.setRowColor(row, GRAY);
					}
				}
				
				
				++row;
				lName = new Link(_iwrb.getLocalizedImageButton("new", "New"));
				lName.addParameter(_parameterAction, _actionInfoSelected);
				lName.maintainParameter(InitialData.dropdownView, iwc);
				lName.maintainParameter(_parameterSupplierId, iwc);
				table.add(lName, 1, row);
				table.mergeCells(1, row, 5, row);
				table.setRowColor(row, GRAY);

			}
						
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
  	
  	
  	
  	return form;
  }
  
  private Form getMerchantForm() throws RemoteException{
    Form form = new Form();
    Table table = getTable();
    form.add(table);

    int row = 1;
    String name = "";

    PresentationObject type = null;    
    TextInput merchId = new TextInput(_parameterMerchant);
    TextInput locId = new TextInput(_parameterLocationID);
    TextInput userId = new TextInput(_parameterUserID);
    TextInput passw = new TextInput(_parameterPassw);
    TextInput posId = new TextInput(_parameterPosID);
    TextInput keyRcvPassw = new TextInput(_parameterKeyReceivedPassword);

    if (_supplier != null) {
      form.maintainParameter(_parameterSupplierId);
      name = _supplier.getName();
    }/*else if (_reseller != null) {
      form.maintainParameter(_parameterResellerId);
      name = _reseller.getName();
    }*/

    if (_merchant != null) {
      merchId.setContent(_merchant.getMerchantID());
      locId.setContent(_merchant.getLocation());
      userId.setContent(_merchant.getUser());
      passw.setContent(_merchant.getPassword());
      posId.setContent(_merchant.getTerminalID());
      keyRcvPassw.setContent(_merchant.getExtraInfo());
    	type = getText(_merchant.getType());
    	table.add(new HiddenInput(_parameterType, _merchant.getType()));
    	table.add(new HiddenInput(_parameterMerchantID, _merchant.getPrimaryKey().toString()));
    } else {
      type = new DropdownMenu(_parameterType);
      ((DropdownMenu) type).addMenuElement(CreditCardMerchant.MERCHANT_TYPE_TPOS);
      ((DropdownMenu) type).addMenuElement(CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN);
    }

    table.add(getHeaderText(name), 1, row);
    table.mergeCells(1, row, 2, row);
    table.setRowColor(row, super.backgroundColor);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_type","Type")), 1, row);
    table.add(type, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);
    
    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_merchant_id","Merchant ID")), 1, row);
    table.add(merchId, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_location_id","Location ID")), 1, row);
    table.add(locId, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_user_id","User ID")), 1, row);
    table.add(userId, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_password","Password")), 1, row);
    table.add(passw, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_terminal_id","Terminal ID")), 1, row);
    table.add(posId, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("tpos_key_received_password","Key Received Password")), 1, row);
    table.add(keyRcvPassw, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getBackLink(1), 1, row);
    if (_merchant != null) {
    	table.add(new SubmitButton(_iwrb.getImage("buttons/delete.gif"), _parameterAction, _actionDelete), 2, row);
    	table.add(Text.NON_BREAKING_SPACE, 2, row);
    }
    table.add(new SubmitButton(_iwrb.getImage("buttons/save.gif"), _parameterAction, _actionSave), 2 ,row);
    table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);


    return form;
  }

  private boolean handleInsert(IWContext iwc) throws RemoteException{
    String merchantId = iwc.getParameter(_parameterMerchant);
    String locationId = iwc.getParameter(_parameterLocationID);
    String userId = iwc.getParameter(_parameterUserID);
    String passw = iwc.getParameter(_parameterPassw);
    String posId = iwc.getParameter(_parameterPosID);
    String keyRcvPass = iwc.getParameter(_parameterKeyReceivedPassword);
    String type = iwc.getParameter(_parameterType);

    try {
    	boolean createNewInfo = false;
      if (_merchant == null) {
      	_merchant = getCreditCardBusiness(iwc).createCreditCardMerchant(type);
      	createNewInfo = true;
        //TPosMerchantHome mHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
        //_merchant = mHome.create();
      }

      _merchant.setName(_name);
      _merchant.setMerchantID(merchantId);
      _merchant.setLocation(locationId);
      _merchant.setUser(userId);
      _merchant.setPassword(passw);
      _merchant.setTerminalID(posId);
      _merchant.setExtraInfo(keyRcvPass);
      _merchant.store();

      
      
      if (_supplier != null && createNewInfo) {
      	getCreditCardBusiness(iwc).addCreditCardMerchant(_supplier, _merchant);
        //_supplier.setTPosMerchantId( ((Integer) _merchant.getPrimaryKey()).intValue());
        //_supplier.store();
      }/*else if (_reseller != null) {
        _reseller.setTPosMerchantId( ((Integer) _merchant.getPrimaryKey()).intValue());
        _reseller.store();
      }*/

    }catch (CreateException ce) {
      ce.printStackTrace(System.err);
      return false;
    }

    return true;
  }
  
  protected CreditCardBusiness getCreditCardBusiness(IWContext iwc) {
	  	try {
	  		return (CreditCardBusiness) IBOLookup.getServiceInstance(iwc, CreditCardBusiness.class);
	  	} catch (IBOLookupException rt) {
	  		throw new IBORuntimeException();
	  	}
  }
}