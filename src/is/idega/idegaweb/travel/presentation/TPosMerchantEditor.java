package is.idega.idegaweb.travel.presentation;
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
  private String _parameterContinue = "prm_con";
  private String tempPass = "svana";

  private IWResourceBundle _iwrb;

  public TPosMerchantEditor(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);  }

  private void init(IWContext iwc) {
    _iwrb = super.getResourceBundle();
  }

  public Form getTPosMerchantEditorForm(IWContext iwc) throws RemoteException{
    Form form = new Form();
    if (super.isTravelAdministrator(iwc)) {
      String action = iwc.getParameter(_parameterAction);
      System.err.println("tposmerchantEditor : action = "+action);
      if (action == null || action.equals("")) {
        form = getMainMenu();
      }else {
        String passw = iwc.getParameter(_parameterPassword);
        if (passw.equals(tempPass)) {
          form.add("pass OK");
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
      SubmitButton button = new SubmitButton(_iwrb.getLocalizedImageButton("travel.continue","Continue"), this._parameterAction, this._parameterContinue );

      table.add(getHeaderText(_iwrb.getLocalizedString("travel.suppliers", "Suppliers")), 1, 1);
      table.add(getHeaderText(_iwrb.getLocalizedString("travel.resellers", "Resellers")), 2, 1);
      table.add(getHeaderText(_iwrb.getLocalizedString("travel.password", "Password")), 3, 1);
      table.setRowColor(1, super.backgroundColor);

      table.add(suppMenu, 1, 2);
      table.add(reppMenu, 2, 2);
      table.add(pass, 3, 2);
      table.setRowColor(2, super.GRAY);

      table.mergeCells(1, 3, 3, 3);
      table.setAlignment(1, 3, "right");
      table.setRowColor(3, super.GRAY);
      table.add(button, 1, 3);


    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return form;
  }
}