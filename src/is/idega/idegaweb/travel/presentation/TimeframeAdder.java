package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TimeframeAdder extends TravelWindow {
  public static final String _parameterProductId = "tf_adder_service_id";

  private String _parameterTimeframeId = "tf_adder_tf_id";
  private String _parameterTimeframeFrom = "tf_adder_tf_from";
  private String _parameterTimeframeTo = "tf_adder_tf_to";
  private String _parameterTimeframeYearly = "tf_adder_tf_yearly";
  private String _parameterTimeframeDelete = "tf_adder_tf_delete_";
  private String _actionSaveTimeframe = "tf_adder_save_timeframe";
  private String _actionCloseWindow = "tf_adder_close_window";

  private static String sAction = "tf_adder_action";
  private IWResourceBundle iwrb;
  private int _productId = -1;
  private Product _product = null;
  private int _emptySlots = 3;

  public TimeframeAdder() {
    super.setWidth(600);
    super.setHeight(500);
    super.setTitle("idegaWeb Travel");
    super.setStatus(true);
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);
    mainMenu(iwc);
  }

  private void init(IWContext iwc) throws RemoteException{
    iwrb = super.iwrb;
    try {
      String sProductId = iwc.getParameter(_parameterProductId);
      if (sProductId != null) {
        _productId = Integer.parseInt(sProductId);
        _product = getProductBusiness(iwc).getProduct(_productId);
      }
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }catch (NumberFormatException n) {
      n.printStackTrace(System.err);
    }


  }

  private void mainMenu(IWContext iwc) throws RemoteException{
    String action = iwc.getParameter(sAction);

    add(Text.BREAK);

    if (_product == null) {

    }else {
      if (action == null) {
        add(getMainForm(iwc));
      }else if (action.equals(this._actionSaveTimeframe)) {

        add(handleInsert(iwc));
      }else if (action.equals(this._actionCloseWindow )) {
        closeWindow();
      }else {

      }
    }

  }

  private Form getMainForm(IWContext iwc) throws RemoteException{
    Form form = getEmptyForm();

    Timeframe[] timeframes = {};
    try {
      timeframes = _product.getTimeframes();

      Table table = new Table();
        table.setCellspacing(1);
        table.setCellpadding(2);
        table.setColor(TravelManager.WHITE);
        table.setAlignment("center");
      form.add(table);
      int row = 1;
      int counter = 0;

      IWTimestamp fromStamp;
      IWTimestamp toStamp;
      DateInput from;
      DateInput to;
      BooleanInput yearly;
      CheckBox delete;

/*      Text timeframeText = (Text) text.clone();
        timeframeText.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));*/
      Text tfFromText = (Text) text.clone();
        tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
        tfFromText.setFontColor(TravelManager.WHITE);
        tfFromText.setBold(true);
      Text tfToText = (Text) text.clone();
        tfToText.setText(iwrb.getLocalizedString("travel.to","to"));
        tfToText.setFontColor(TravelManager.WHITE);
        tfToText.setBold(true);
      Text tfYearlyText = (Text) text.clone();
        tfYearlyText.setText(iwrb.getLocalizedString("travel.yearly","yearly"));
        tfYearlyText.setFontColor(TravelManager.WHITE);
        tfYearlyText.setBold(true);
      Text tfDeleteText = (Text) text.clone();
        tfDeleteText.setText(iwrb.getLocalizedString("travel.delete","delete"));
        tfDeleteText.setFontColor(TravelManager.WHITE);
        tfDeleteText.setBold(true);

      table.add(tfFromText, 1, row);
      table.add(tfToText, 2, row);
      table.add(tfYearlyText, 3, row);
      table.add(tfDeleteText, 4, row);
      table.setRowColor(row, TravelManager.backgroundColor);

      int currentYear = IWTimestamp.RightNow().getYear();

      for (int i = 0; i < timeframes.length; i++) {
        ++row;
        ++counter;
        fromStamp = new IWTimestamp(timeframes[i].getFrom());
        toStamp = new IWTimestamp(timeframes[i].getTo());

        from = new DateInput(this._parameterTimeframeFrom+counter);
          from.setYearRange(2001, currentYear+5);
        to = new DateInput(this._parameterTimeframeTo+counter);
          to.setYearRange(2001, currentYear+5);
        yearly = new BooleanInput(this._parameterTimeframeYearly+counter);
        delete = new CheckBox(this._parameterTimeframeDelete+timeframes[i].getID());

        from.setDate(fromStamp.getSQLDate());
        to.setDate(toStamp.getSQLDate());
        yearly.setSelected(timeframes[i].getYearly());
        delete.setChecked(false);

        table.add(from, 1,row);
        table.add(to, 2,row);
        table.add(yearly, 3,row);
        table.add(delete, 4,row);
        table.add(new HiddenInput(this._parameterTimeframeId+counter, Integer.toString(timeframes[i].getID())),1,row);
        table.setRowColor(row, TravelManager.GRAY);
      }

      ++row;
      table.add(Text.NON_BREAKING_SPACE,1,row);
      table.setRowColor(row, TravelManager.GRAY);

      for (int i = 0; i < this._emptySlots; i++) {
        ++row;
        ++counter;
        from = new DateInput(this._parameterTimeframeFrom+counter);
          from.setYearRange(2001, currentYear+5);
        to = new DateInput(this._parameterTimeframeTo+counter);
          to.setYearRange(2001, currentYear+5);
        yearly = new BooleanInput(this._parameterTimeframeYearly+counter);

        table.add(from, 1,row);
        table.add(to, 2,row);
        table.add(yearly, 3,row);
        table.add(Text.NON_BREAKING_SPACE, 4,row);
        table.add(new HiddenInput(this._parameterTimeframeId+counter, "-1"),1,row);
        table.setRowColor(row, TravelManager.GRAY);
      }
      ++row;
      table.mergeCells(1,row,2,row);
      table.mergeCells(3,row,4,row);
      table.setAlignment(1, row, "left");
      table.setAlignment(3, row, "right");
      table.setRowColor(row, TravelManager.GRAY);
      SubmitButton close = new SubmitButton(iwrb.getImage("/buttons/close.gif"), this.sAction, this._actionCloseWindow);
      table.add(close, 1, row);
      SubmitButton save = new SubmitButton(iwrb.getImage("/buttons/save.gif"), this.sAction, this._actionSaveTimeframe);
      table.add(save, 3, row);



    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return form;
  }

  private Form handleInsert(IWContext iwc) throws RemoteException{
    boolean success = saveTimeframe(iwc);
    if (success) {
      return getMainForm(iwc);
    }else {
      return getMainForm(iwc);
//      return getSaveFailed(iwc);
    }
  }

  private Form getEmptyForm() {
    Form form = new Form();
      form.maintainParameter(this._parameterProductId);
    return form;
  }

  private Form getSaveFailed(IWContext iwc) {
    Form form = getEmptyForm();
    Table table = new Table();
    form.add(table);

    table.add("Save / Update klikkaði þokkalega");

    return form;
  }

  private boolean saveTimeframe(IWContext iwc) throws RemoteException{
    boolean returner = true;

    Timeframe tFrame;
    IWTimestamp from;
    IWTimestamp to;
    int counter = 0;


    try {
      while (iwc.getParameter(this._parameterTimeframeFrom+(++counter)) != null) {
        String tfId = iwc.getParameter(this._parameterTimeframeId+counter);
        String tfFrom = iwc.getParameter(this._parameterTimeframeFrom+counter);
        String tfTo = iwc.getParameter(this._parameterTimeframeTo+counter);
        String tfYearly = iwc.getParameter(this._parameterTimeframeYearly+counter);
        String tfDelete = iwc.getParameter(this._parameterTimeframeDelete+counter);

        if (tfId != null && tfId.equals("-1")) {
          if (!tfFrom.equals("")) {
            from = new IWTimestamp(tfFrom);
            to = new IWTimestamp(tfTo);

            tFrame = ((com.idega.block.trade.stockroom.data.TimeframeHome)com.idega.data.IDOLookup.getHomeLegacy(Timeframe.class)).createLegacy();
            tFrame.setFrom(from.getTimestamp());
            tFrame.setTo(to.getTimestamp());
            if (tfYearly.equals("Y")) {
              tFrame.setYearly(true);
            }else if (tfYearly.equals("N")) {
              tFrame.setYearly(false);
            }
            tFrame.insert();
            _product.addTimeframe(tFrame);
//            _product.addTo(tFrame);
          }
        }else {
          if (tfId != null && !tfId.equals("-1")) {
            String del = iwc.getParameter(this._parameterTimeframeDelete+tfId);

            tFrame = ((com.idega.block.trade.stockroom.data.TimeframeHome)com.idega.data.IDOLookup.getHomeLegacy(Timeframe.class)).findByPrimaryKeyLegacy(Integer.parseInt(tfId));

            if (del == null) {
              from = new IWTimestamp(tfFrom);
              to = new IWTimestamp(tfTo);

              tFrame.setFrom(from.getTimestamp());
              tFrame.setTo(to.getTimestamp());
              if (tfYearly.equals("Y")) {
                tFrame.setYearly(true);
              }else if (tfYearly.equals("N")) {
                tFrame.setYearly(false);
              }
              tFrame.update();
            }else {
              _product.removeTimeframe(tFrame);
//              tFrame.removeFrom(_product);
              tFrame.delete();
            }
          }else {
          }

        }
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      returner = false;
    }catch (IDOException ide) {
      ide.printStackTrace(System.err);
      returner = false;
    }

    return returner;
  }

  private void closeWindow() {
    super.close(true);
  }

}
