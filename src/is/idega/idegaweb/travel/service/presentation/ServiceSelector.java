package is.idega.idegaweb.travel.service.presentation;

import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.service.presentation.InitialDataObject;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactory;
import com.idega.block.trade.stockroom.data.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.idegaweb.*;
import com.idega.data.IDORelationshipException;
import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.sql.SQLException;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceSelector extends TravelManager implements InitialDataObject {

  private IWResourceBundle _iwrb;
  private Supplier _supplier;

  private String ACTION = "ss_act";
  private String ACTION_HANDLE_INSERT = "han_ins";
  private String PARAMETER_PRODUCT_CATEGORY_ID = "pcat_id";
  private String PARAMETER_CHECK_BOX = "pcat_box_";
  private boolean updated = false;

  public ServiceSelector(IWContext iwc) throws Exception {
    main(iwc);
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    init(iwc);
  }

  private void init(IWContext iwc) throws RemoteException{
    _supplier = super.getSupplier();
    _iwrb = super.getResourceBundle();
  }

  public void handleInsert(IWContext iwc) throws RemoteException {
    String action = iwc.getParameter(ACTION);
    if (action == null) action = "";

    if (action.equals(ACTION_HANDLE_INSERT)) {
      updated = true;
      String[] ids = iwc.getParameterValues(PARAMETER_PRODUCT_CATEGORY_ID);
      String box;
      try {
        _supplier.removeFrom(ProductCategory.class);
        for (int i = 0 ; i < ids.length ; i++) {
          box = iwc.getParameter(PARAMETER_CHECK_BOX+ids[i]);
            if (box != null) {
              _supplier.addTo(ProductCategory.class, Integer.parseInt(ids[i]));
            }
        }
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }
  }

  public Form getForm(IWContext iwc) throws RemoteException {
    Form form = new Form();
    Table table = super.getTable();
    if (updated) {
      form.add(getHeaderText(_iwrb.getLocalizedString("travel.information_updated","Information updated")));
    }
    form.add(table);

    if (_supplier != null) {
      try {
        ProductCategoryFactory pcf = super.getProductCategoryFactory(iwc);
        Collection allCatIds = pcf.getAllProductCategories();
        Collection prodCatIds = _supplier.getProductCategories();
        ProductCategory pCat;
        CheckBox cBox;
        String id;


        int row = 1;
        table.add(getHeaderText(_iwrb.getLocalizedString("travel.product_category","Product category")), 1, row);
        table.add(getHeaderText(_iwrb.getLocalizedString("travel.use","Use")), 2, row);
        table.setRowColor(row, super.backgroundColor);

        Iterator allIter = allCatIds.iterator();
        while (allIter.hasNext()) {
          ++row;
          pCat = pcf.getProductCategoryHome().findByPrimaryKey(allIter.next());
          id = pCat.getPrimaryKey().toString();
          cBox = new CheckBox(PARAMETER_CHECK_BOX+id);
          if ( prodCatIds.contains(pCat) ) {
            cBox.setChecked(true);
          }

          table.add(getText(_iwrb.getLocalizedString(pcf.getProductCategoryType(pCat), pcf.getProductCategoryTypeDefaultName(pcf.getProductCategoryType(pCat)))), 1, row);
          table.add(new HiddenInput(PARAMETER_PRODUCT_CATEGORY_ID,id), 1 ,row);
          table.add(cBox, 2 ,row);
          table.setRowColor(row, super.GRAY);
        }

        ++row;
        table.add(super.getBackLink(), 1,row);
        table.add(new SubmitButton(_iwrb.getImage("buttons/save.gif"), this.ACTION, this.ACTION_HANDLE_INSERT), 2, row);
        table.setColumnAlignment(2, Table.VERTICAL_ALIGN_MIDDLE);
        table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.setRowColor(row, super.GRAY);




      }catch (FinderException fe) {
        fe.printStackTrace(System.err);
      }catch (IDORelationshipException re) {
        re.printStackTrace(System.err);
      }

    }else {
      table.add(getText(_iwrb.getLocalizedString("travel.no_permission","No permission")));
    }


    return form;
  }

}
