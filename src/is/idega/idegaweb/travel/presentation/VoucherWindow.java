package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.ui.Window;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.travel.presentation.Voucher;

import is.idega.idegaweb.travel.data.GeneralBooking;


/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class VoucherWindow extends Window {

  public static String parameterBookingId = "voucherWindowBookingId";
  public static String parameterReferenceNumber = "voucherWindowReferenceNumber";

  public VoucherWindow() {
    super.setWidth(Voucher.width+40);
    super.setTitle("Voucher");
    super.setResizable(true);
    super.setMenubar(true);
  }

  public void main(IWContext iwc) throws Exception {
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    String sReferenceNumber = iwc.getParameter(this.parameterReferenceNumber);
    if (sBookingId != null) {
      Voucher voucher = new Voucher(iwc, Integer.parseInt(sBookingId));
      add(voucher.getVoucher());
    }else if (sReferenceNumber != null){
      GeneralBooking[] gBooking = (GeneralBooking[]) (GeneralBooking.getStaticInstance(GeneralBooking.class)).findAllByColumn(GeneralBooking.getReferenceNumberColumnName(), sReferenceNumber);
      if (gBooking.length > 0) {
        Voucher voucher = new Voucher(iwc, gBooking[0].getID());
        add(voucher.getVoucher());
      }else {
        // handleError;
      }
    }
  }

}