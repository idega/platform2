package is.idega.idegaweb.travel.presentation;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.Text;
import com.idega.block.trade.stockroom.data.*;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.data.IDOLookup;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWResourceBundle;

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
  private String PARAMETER_PERFORM_ACTION = "stEd_par_prf_act";
  private String ACTION = "stEd_par_act";

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
      } else if ( reseller != null ) {
        _settings = reseller.getSettings();
      }

    } catch ( RemoteException re ) {
      re.printStackTrace( System.err );
    } catch ( CreateException ce ) {
      ce.printStackTrace( System.err );
    }
  }

  public Form getSettingsFrom() throws RemoteException {
    Form form = new Form();
    Table table = new Table();
    form.add( table );
    table.setCellpadding(1);
    table.setCellspacing(1);
    table.setColor(super.WHITE);

    BooleanInput doubleConfirm = new BooleanInput(PARAMETER_DOUBLE_CONFIRMATION);
      doubleConfirm.setSelected(_settings.getIfDoubleConfirmation());
    BooleanInput receiveEmail = new BooleanInput(PARAMETER_RECEIVE_EMAIL_AFTER_ONLINE_BOOKING);
      receiveEmail.setSelected(_settings.getIfEmailAfterOnlineBooking());
    SubmitButton save = new SubmitButton(_iwrb.getImage("buttons/save.gif"), ACTION, PARAMETER_PERFORM_ACTION);


    table.add(getHeaderText(_iwrb.getLocalizedString("travel.settings","Settings")), 1, 1);
    table.mergeCells(1, 1, 2, 1);
    table.setRowColor(1, super.backgroundColor);

    table.add(getText(_iwrb.getLocalizedString("travel.double_confirmation","Double confirmation")), 1, 2);
    table.add(doubleConfirm, 2, 2);
    table.setRowColor(2, super.GRAY);

    table.add(getText(_iwrb.getLocalizedString("travel.receive_email_after_online_booking","Receive email after online booking")), 1, 3);
    table.add(receiveEmail, 2, 3);
    table.setRowColor(3, super.GRAY);

    table.add(save, 1, 4);
    table.mergeCells(1, 4, 2, 4);
    table.setAlignment(1, 4, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(4, super.GRAY);


    return form;
  }

  public void handleInsert(IWContext iwc) {
    String doubleConfirmation = iwc.getParameter(PARAMETER_DOUBLE_CONFIRMATION);
    String reveiveEmail = iwc.getParameter(PARAMETER_RECEIVE_EMAIL_AFTER_ONLINE_BOOKING);
    String action = iwc.getParameter(ACTION);

    if (action != null && action.equals(PARAMETER_PERFORM_ACTION)) {
      try {
        if (doubleConfirmation != null && doubleConfirmation.equals("Y")) {
          _settings.setIfDoubleConfirmation(true);
        }else if (doubleConfirmation != null && doubleConfirmation.equals("N")) {
          _settings.setIfDoubleConfirmation(false);
        }

        if (reveiveEmail != null && reveiveEmail.equals("Y")) {
          _settings.setIfEmailAfterOnlineBooking(true);
        }else if (reveiveEmail != null && reveiveEmail.equals("N")) {
          _settings.setIfEmailAfterOnlineBooking(false);
        }

        _settings.store();
      }catch (RemoteException re) {
        re.printStackTrace(System.err);
      }
    }

  }
/*
  private Text getText(String content) {
    Text text = (Text) super.theText.clone();
      text.setText(content);
      text.setFontColor(super.BLACK);
    return text;
  }

  private Text getHeaderText(String content) {
    Text text = getText(content);
      text.setBold(true);
      text.setFontColor(super.WHITE);
    return text;
  }
*/
}
