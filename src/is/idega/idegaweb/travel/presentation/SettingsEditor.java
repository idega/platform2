package is.idega.idegaweb.travel.presentation;
import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Settings;
import com.idega.block.trade.stockroom.data.SettingsHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    16. apríl 2002
 *@version    1.0
 */

public class SettingsEditor extends TravelManager {

  private IWResourceBundle _iwrb;
  private Settings _settings;

  private String PARAMETER_DOUBLE_CONFIRMATION = "stEd_par_dbl_con";
  private String PARAMETER_RECEIVE_EMAIL_AFTER_ONLINE_BOOKING = "stEd_par_rcv_eml_onl_b";
  private String PARAMETER_DEFAULT_CURRENCY = "stEd_par_def_cur";
  private String PARAMETER_PERFORM_ACTION = "stEd_par_prf_act";
  private String BOOLEAN_TRUE = "Y";
  private String BOOLEAN_FALSE = "N";
  private String ACTION = "stEd_par_act";

  private boolean supplierSettings = false;

  public SettingsEditor(IWContext iwc) throws Exception{
    init(iwc);
  }

  private void init(IWContext iwc) throws Exception {
    super.main(iwc);
    _iwrb = super.getResourceBundle();
    Supplier supplier = super.getSupplier();
    Reseller reseller = super.getReseller();

    try {
      SettingsHome shome = ( SettingsHome ) IDOLookup.getHome( Settings.class );

      if ( supplier != null ) {
        _settings = supplier.getSettings();
        supplierSettings = true;
      } else if ( reseller != null ) {
        _settings = reseller.getSettings();
      }

    } catch ( RemoteException re ) {
      re.printStackTrace( System.err );
    } catch ( CreateException ce ) {
      ce.printStackTrace( System.err );
    }
  }

  public Form getSettingsFrom(IWContext iwc) throws RemoteException {
    Form form = new Form();
    Table table = new Table();
    form.add( table );
    table.setCellpadding(1);
    table.setCellspacing(1);
    table.setColor(super.WHITE);
    int row = 0;

    BooleanInput doubleConfirm = new BooleanInput(PARAMETER_DOUBLE_CONFIRMATION);
    doubleConfirm.setSelected(_settings.getIfDoubleConfirmation());

    BooleanInput receiveEmail = new BooleanInput(PARAMETER_RECEIVE_EMAIL_AFTER_ONLINE_BOOKING);
    receiveEmail.setSelected(_settings.getIfEmailAfterOnlineBooking());

    SubmitButton save = new SubmitButton(_iwrb.getImage("buttons/save.gif"), ACTION, PARAMETER_PERFORM_ACTION);


    ++row;
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.settings","Settings")), 1, row);
    table.mergeCells(1, row, 2, row);
    table.setRowColor(row, super.backgroundColor);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("travel.double_confirmation","Double confirmation")), 1, row);
    table.add(doubleConfirm, 2, row);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.add(getText(_iwrb.getLocalizedString("travel.receive_email_after_online_booking","Receive email after online booking")), 1, row);
    table.add(receiveEmail, 2, row);
    table.setRowColor(row, super.GRAY);

    if (this.supplierSettings) {
      DropdownMenu defaultCurrency = super.getTravelStockroomBusiness(iwc).getCurrencyDropdownMenu(PARAMETER_DEFAULT_CURRENCY);//new DropdownMenu(PARAMETER_DEFAULT_CURRENCY);
      int settingsCurrencyId = _settings.getCurrencyId();
      if (settingsCurrencyId > 0) {
        defaultCurrency.setSelectedElement(Integer.toString(settingsCurrencyId));
      } else {
        defaultCurrency.setSelectedElement(Integer.toString(CurrencyBusiness.getCurrencyHolder(CurrencyBusiness.defaultCurrency).getCurrencyID()));
      }
      ++row;
      table.add(getText(_iwrb.getLocalizedString("travel.default_currency","Default currency")), 1, row);
      table.add(defaultCurrency, 2, row);
      table.setRowColor(row, super.GRAY);
    }

    ++row;
    table.add(save, 1, row);
    table.mergeCells(1, row, 2, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.GRAY);


    return form;
  }

  public void handleInsert(IWContext iwc) {
    String doubleConfirmation = iwc.getParameter(PARAMETER_DOUBLE_CONFIRMATION);
    String reveiveEmail = iwc.getParameter(PARAMETER_RECEIVE_EMAIL_AFTER_ONLINE_BOOKING);
    String defaultCurrency = iwc.getParameter(PARAMETER_DEFAULT_CURRENCY);

    String action = iwc.getParameter(ACTION);

    if (action != null && action.equals(PARAMETER_PERFORM_ACTION)) {
      try {
        if (doubleConfirmation != null && doubleConfirmation.equals(BOOLEAN_TRUE)) {
          _settings.setIfDoubleConfirmation(true);
        }else if (doubleConfirmation != null && doubleConfirmation.equals(BOOLEAN_FALSE)) {
          _settings.setIfDoubleConfirmation(false);
        }

        if (reveiveEmail != null && reveiveEmail.equals(BOOLEAN_TRUE)) {
          _settings.setIfEmailAfterOnlineBooking(true);
        }else if (reveiveEmail != null && reveiveEmail.equals(BOOLEAN_FALSE)) {
          _settings.setIfEmailAfterOnlineBooking(false);
        }

        try {
          _settings.setCurrencyId(Integer.parseInt(defaultCurrency));
        }catch (NumberFormatException n) {}

        _settings.store();
      }catch (RemoteException re) {
        re.printStackTrace(System.err);
      }
    }

  }


}
