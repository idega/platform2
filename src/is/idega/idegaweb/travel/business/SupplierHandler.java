package is.idega.idegaweb.travel.business;

import java.util.List;

import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.builder.data.ICPropertyHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */
public class SupplierHandler implements ICPropertyHandler {

  public static final String IW_BUNDLE_IDENTIFIER = "is.idegaweb.travel";

  private IWResourceBundle iwrb;

  /**
   *
   */
  public SupplierHandler() {
  }

  /**
   *
   */
  public List getDefaultHandlerTypes() {
    return(null);
  }

  /**
   *
   */
  public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
    DropdownMenu menu = null;

    iwrb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);

    Supplier[] supp = null;
    try{
      supp = com.idega.block.trade.stockroom.data.SupplierBMPBean.getValidSuppliers();
    }
    catch(Exception e){
     e.printStackTrace(System.err);//don't really need to do this
    }

    if( supp!=null ){
      menu = new DropdownMenu(supp);
      menu.addMenuElementFirst("",iwrb.getLocalizedString("travel.select","Select:"));
      menu.setSelectedElement(value);
    }
    else{
      menu = new DropdownMenu();
      menu.addMenuElementFirst("",iwrb.getLocalizedString("travel.no.suppliers","No suppliers"));
    }

    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
