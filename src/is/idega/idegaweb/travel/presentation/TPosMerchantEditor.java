package is.idega.idegaweb.travel.presentation;
import com.idega.presentation.text.Link;
import javax.ejb.CreateException;
import com.idega.block.tpos.data.TPosMerchantHome;
import javax.ejb.FinderException;
import com.idega.block.tpos.data.TPosMerchant;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import java.sql.SQLException;
import com.idega.block.trade.stockroom.data.*;
import java.rmi.RemoteException;
import com.idega.data.IDOLookup;
import com.idega.presentation.Table;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TPosMerchantEditor extends TravelManager {
  /**
   *  @todo Dropa tpos_merchant-id úr grunni a edison og einsein...áður en þetta fer inn
   */

  private String _parameterAction = "prm_act";
  private String _parameterResellerId = "prm_res";
  private String _parameterSupplierId = "prm_sup";
  private String _parameterPassword = "prm_psw";
  private String _parameterMerchantID = "prm_mchid";
  private String _parameterLocationID = "prm_locid";
  private String _parameterUserID = "prm_usrid";
  private String _parameterPassw = "prm_pssw";
  private String _parameterPosID = "prm_posid";
  private String _parameterKeyReceivedPassword = "prm_keyrcvpass";
  private String _actionContinue = "act_con";
  private String _actionSave = "act_sav";
  private String tempPass = "svana";

  private IWResourceBundle _iwrb;
  private Supplier _supplier;
  private Reseller _reseller;
  private String _name = "";
  private TPosMerchant _merchant;

  public TPosMerchantEditor(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);
  }

  private void init(IWContext iwc) throws RemoteException{
    _iwrb = super.getResourceBundle();

    try {
      String supplierId = iwc.getParameter(_parameterSupplierId);
      String resellerId = iwc.getParameter(_parameterResellerId);
      if (supplierId != null && !supplierId.equals("-1")) {
        SupplierHome suppHome = (SupplierHome) IDOLookup.getHomeLegacy(Supplier.class);
        _supplier = suppHome.findByPrimaryKeyLegacy(Integer.parseInt(supplierId));
        _name = _supplier.getName();
        try {
          _merchant = _supplier.getTPosMerchant();
        }catch (FinderException fe) {
          //fe.printStackTrace(System.err);
          debug("Cannot find TPosMerchant for supplier : "+_name);
        }
      }else if (resellerId != null && !resellerId.equals("-1")) {
        ResellerHome resHome = (ResellerHome) IDOLookup.getHomeLegacy(Reseller.class);
        _reseller = resHome.findByPrimaryKeyLegacy(Integer.parseInt(resellerId));
        _name = _reseller.getName();
        try {
          _merchant = _reseller.getTPosMerchant();
        }catch (FinderException fe) {
          //fe.printStackTrace(System.err);
          debug("Cannot find TPosMerchant for reseller : "+_name);
        }
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  public Form getTPosMerchantEditorForm(IWContext iwc) throws RemoteException{
    Form form = new Form();
    if (super.isTravelAdministrator(iwc)) {
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
        if (passw.equals(tempPass)) {
          form = getMerchantForm();
        }else {
          form = getMainMenu();
          form.add(getHeaderText(_iwrb.getLocalizedString("travel.password invalid","Password invalid")));
        }

      }

      return form;
    }else {
      form.add(getHeaderText(_iwrb.getLocalizedString("travel.no_permission","No permission")));
      return form;
    }
  }


  private Form getMainMenu() throws RemoteException{
    Form form = new Form();
    Table table = getTable();
    form.add(Text.BREAK);
    form.add(table);

    try {
      Supplier[] supps = SupplierBMPBean.getValidSuppliers();
      Reseller[] repps = ResellerBMPBean.getValidResellers();

      DropdownMenu suppMenu = new DropdownMenu(supps, this._parameterSupplierId);
      DropdownMenu reppMenu = new DropdownMenu(repps, this._parameterResellerId);
        suppMenu.addMenuElementFirst("-1", _iwrb.getLocalizedString("travel.please_select","Please select"));
        suppMenu.keepStatusOnAction();
        reppMenu.addMenuElementFirst("-1", _iwrb.getLocalizedString("travel.please_select","Please select"));
        reppMenu.keepStatusOnAction();
      PasswordInput pass = new PasswordInput(_parameterPassword);
      SubmitButton button = new SubmitButton(_iwrb.getLocalizedImageButton("travel.continue","Continue"), this._parameterAction, this._actionContinue );

      table.add(getHeaderText(_iwrb.getLocalizedString("travel.suppliers", "Suppliers")), 1, 1);
      table.add(getHeaderText(_iwrb.getLocalizedString("travel.resellers", "Resellers")), 2, 1);
      table.add(getHeaderText(_iwrb.getLocalizedString("travel.password", "Password")), 3, 1);
      table.setRowColor(1, super.backgroundColor);

      table.add(suppMenu, 1, 2);
      table.add(reppMenu, 2, 2);
      table.add(pass, 3, 2);
      table.setRowColor(2, super.GRAY);

      table.mergeCells(1, 3, 2, 3);
      table.setAlignment(3, 3, "right");
      table.setRowColor(3, super.GRAY);
      table.add(button, 3, 3);
      table.add(getText(_iwrb.getLocalizedString("travel.please_select_reseller_of_supplier","Please select either a supplier OR a reseller")), 1, 3);


    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return form;
  }


  private Form getMerchantForm() throws RemoteException{
    Form form = new Form();
    Table table = getTable();
    form.add(table);

    int row = 1;
    String name = "";

    TextInput merchId = new TextInput(_parameterMerchantID);
    TextInput locId = new TextInput(_parameterLocationID);
    TextInput userId = new TextInput(_parameterUserID);
    TextInput passw = new TextInput(_parameterPassw);
    TextInput posId = new TextInput(_parameterPosID);
    TextInput keyRcvPassw = new TextInput(_parameterKeyReceivedPassword);

    if (_supplier != null) {
      form.maintainParameter(_parameterSupplierId);
      name = _supplier.getName();
    }else if (_reseller != null) {
      form.maintainParameter(_parameterResellerId);
      name = _reseller.getName();
    }

    if (_merchant != null) {
      merchId.setContent(_merchant.getMerchantID());
      locId.setContent(_merchant.getLocationID());
      userId.setContent(_merchant.getUserID());
      passw.setContent(_merchant.getPassword());
      posId.setContent(_merchant.getPosID());
      keyRcvPassw.setContent(_merchant.getKeyReceivedPassword());
    }

    table.add(getHeaderText(name), 1, row);
    table.mergeCells(1, row, 2, row);
    table.setRowColor(row, super.backgroundColor);

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
    table.add(getText(_iwrb.getLocalizedString("tpos_pos_id","Pos ID")), 1, row);
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
    table.add(new SubmitButton(_iwrb.getImage("buttons/save.gif"), _parameterAction, _actionSave), 2 ,row);
    table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);


    return form;
  }

  private boolean handleInsert(IWContext iwc) throws RemoteException{
    String merchantId = iwc.getParameter(_parameterMerchantID);
    String locationId = iwc.getParameter(_parameterLocationID);
    String userId = iwc.getParameter(_parameterUserID);
    String passw = iwc.getParameter(_parameterPassw);
    String posId = iwc.getParameter(_parameterPosID);
    String keyRcvPass = iwc.getParameter(_parameterKeyReceivedPassword);

    try {
      if (_merchant == null) {
        TPosMerchantHome mHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
        _merchant = mHome.create();
      }

      _merchant.setName(_name);
      _merchant.setMerchantID(merchantId);
      _merchant.setLoactionID(locationId);
      _merchant.setUserID(userId);
      _merchant.setPassword(passw);
      _merchant.setPosID(posId);
      _merchant.setKeyReceivedPassword(keyRcvPass);
      _merchant.store();

      if (_supplier != null) {
        _supplier.setTPosMerchantId( ((Integer) _merchant.getPrimaryKey()).intValue());
        _supplier.store();
      }else if (_reseller != null) {
        _reseller.setTPosMerchantId( ((Integer) _merchant.getPrimaryKey()).intValue());
        _reseller.store();
      }

    }catch (CreateException ce) {
      ce.printStackTrace(System.err);
      return false;
    }

    return true;
  }
}