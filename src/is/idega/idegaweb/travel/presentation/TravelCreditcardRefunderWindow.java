/*
 * Created on 29.3.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.travel.presentation;

import com.idega.block.creditcard.presentation.CreditcardRefunder;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TravelCreditcardRefunderWindow extends TravelWindow {

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		CreditcardRefunder crf = new CreditcardRefunder();
		add(crf);
	}

  public static Form creditcardRefunderForm(IWContext iwc, IWResourceBundle iwrb) {
    Form form = new Form();
    Table table = new Table();
    form.add(table);

    Text tSecureForm = new Text(iwrb.getLocalizedString("travel.secure_form","Secure form"));
      tSecureForm.setFontStyle(TravelManager.theBoldTextStyle);
      tSecureForm.setFontColor(TravelManager.WHITE);

    Link lSecureForm = new Link(tSecureForm);
    	
      lSecureForm.setHttps(LinkGenerator.getIsHttps());
      lSecureForm.setWindowToOpen(TravelCreditcardRefunderWindow.class);

    form.add(lSecureForm);
    System.out.println(lSecureForm.getURL(iwc));


    return form;
  }
}
