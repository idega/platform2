package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.data.Service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductCategoryHome;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceOverview extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private String actionParameter = "service_overview_action";
  private String deleteParameter = "service_to_delete_id";
  private String parameterStartNumber = "parameterStartNumber";
  private Supplier supplier;
  private Reseller reseller;

  private List products = new Vector();

  public ServiceOverview() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception{
      super.main(iwc);
      init(iwc);

      if (super.isLoggedOn(iwc) ) {

          String action = iwc.getParameter(actionParameter);
          if (action == null) {action = "";}

          if (action.equals("")) {
              displayForm(iwc);
          }else if (action.equals("delete")) {
              deleteServices(iwc);
              displayForm(iwc);
          }


          super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }

  }

  private void init(IWContext iwc) throws RemoteException {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();
      reseller = super.getReseller();
      
      if ( supplier != null) {
      	products = getProductBusiness(iwc).getProducts(supplier.getID());
      } else if ( super.getReseller() != null ) {
      	try {
					Product[] list = super.getContractBusiness(iwc).getProductsForReseller(iwc, ((Integer) reseller.getPrimaryKey()).intValue());
					if (list != null) {
						for (int i = 0; i < list.length; i++) {
							products.add((Product) list[i]);
						}
					}
				} catch (SQLException e) {
					logWarning("ServiceOverview : no products found for reseller");
				}
      }
      
  }

  public void deleteServices(IWContext iwc) throws RemoteException, FinderException, SQLException{
    String[] serviceIds = (String[]) iwc.getParameterValues(deleteParameter);
    Service serviceToDelete;
    for (int i = 0; i < serviceIds.length; i++) {
        serviceToDelete = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(serviceIds[i]));
        serviceToDelete.delete();
    }

  }

  public void displayForm(IWContext iwc) throws RemoteException, IDOFinderException, SQLException{
      add(Text.getBreak());
      Form form = new Form();
//      Table topTable = this.getTopTable(iwc);
        form.add(Text.BREAK);
      Table table = new Table();
        table.setBorder(0);
        form.add(table);


      table.setWidth("90%");
      String sYear = iwc.getParameter("year");
      if (sYear == null) {
          sYear = Text.emptyString().toString();
      }

      int iStartNumber = 0;
      int manyPerPage = 5;
      int iStopNumber = manyPerPage;
      int pages = 1;
      int currentPage = 1;

      String startNumber = iwc.getParameter(parameterStartNumber);
      if (startNumber != null) {
        iStartNumber = Integer.parseInt(startNumber);
      }

      int row = 0;
      IWTimestamp stamp = IWTimestamp.RightNow();



      Link delete;
      Link getLink;
      Link bookClone = new Link(iwrb.getImage("/buttons/book.gif"),Booking.class);
        bookClone.addParameter(super.sAction, super.parameterBooking);
      Link editClone = new Link(iwrb.getImage("/buttons/change.gif"),ServiceDesigner.class);
        editClone.addParameter(super.sAction, super.parameterServiceDesigner);

      Link book;
      Link edit;


        int productsSize = products.size();

        if (productsSize > iStartNumber + manyPerPage) {
          iStopNumber = iStartNumber + manyPerPage;
        }else {
          iStopNumber = productsSize;
        }

        pages = (int) productsSize / manyPerPage;
        if ( (productsSize % manyPerPage) > 0) ++pages;

        if (pages == 0) pages = 1;

        if ((productsSize - iStartNumber) < manyPerPage) {
          currentPage = pages;
        }else {
          for (int i = manyPerPage, j=1; i <= productsSize; i += manyPerPage,j++) {
            if (iStartNumber < i) {
              currentPage = j;
              break;
            }
          }
        }

        Table contentTable;
        Table pagesTable = getPagesTable(pages, currentPage, iStartNumber, manyPerPage);
        Product product;

        if (productsSize > 0) {
          ++row;
          table.mergeCells(1,row,5,row);
          table.add(pagesTable,1,row);
          table.setAlignment(1, row, "center");
        }

        Collection coll;
        ProductCategory pCat;
        ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
        Iterator pCatIds;
        try {
        	if(supplier != null) {
	          coll = this.supplier.getProductCategories();
	          pCatIds = coll.iterator();
	          while (pCatIds.hasNext()) {
	            pCat = (ProductCategory) pCatIds.next();
	          }
        	}
        }catch (Exception e) {
          e.printStackTrace(System.err);
        }

        is.idega.idegaweb.travel.service.presentation.ServiceOverview so;

        for (int i = iStartNumber; i < iStopNumber; i++) {
          try {
            product = (Product) products.get(i);
            so = super.getServiceHandler(iwc).getServiceOverview(iwc, product);
            contentTable = so.getServiceInfoTable(iwc, product);
//            contentTable = getServiceInfoTable(iwc,iwrb,product);

            ++row;
            table.mergeCells(1,row,5,row);
            table.add(contentTable,1,row);

            ++row;
            table.mergeCells(1,row,5,row);
            table.setAlignment(1,row,"right");

            getLink = new Link(iwrb.getImage("buttons/link.gif"));
              getLink.setWindowToOpen(LinkGenerator.class);
              getLink.addParameter(LinkGenerator.parameterProductId ,product.getID());

            delete = new Link(iwrb.getImage("buttons/delete.gif"));
              delete.addParameter(actionParameter,"delete");
              delete.addParameter(deleteParameter,product.getID());

            book = LinkGenerator.getLink(iwc, product.getID(), Booking.class);
              book.setImage(iwrb.getImage("buttons/book.gif"));

            edit = (Link) editClone.clone();
              edit.addParameter(ServiceDesigner.parameterUpdateServiceId, product.getID());

            if (super.isInPermissionGroup && supplier != null) {
              table.add(edit,1,row);
              table.add("&nbsp;&nbsp;",1,row);
            }
            table.add(book,1,row);
            table.add("&nbsp;&nbsp;",1,row);
            if (this.supplier != null) {
              table.add(getLink,1,row);
              table.add("&nbsp;&nbsp;",1,row);
            }
            if (super.isInPermissionGroup && supplier != null) {
              table.add(delete,1,row);
            }


            table.setColor(1,row,super.backgroundColor);
            ++row;
            table.mergeCells(1,row,5,row);
            table.setColor(1,row,super.backgroundColor);
            HorizontalRule hr = new HorizontalRule("100%",1);
              hr.setAlignment(hr.ALIGN_CENTER);
              hr.setNoShade(true);
              hr.setColor(super.textColor);

            ++row;
            table.mergeCells(1,row,5,row);
          }catch (ServiceNotFoundException snf) {
            snf.printStackTrace(System.err);
          }catch (TimeframeNotFoundException tnf) {
            tnf.printStackTrace(System.err);
          }catch (FinderException fe) {
            fe.printStackTrace(System.err);
          }

      }
        if (productsSize < 1) ++row;
        table.add(pagesTable,1, row);
        table.setAlignment(1,row,"center");
        
      add(form);
  }

  private Table getPagesTable(int pages, int currentPage, int iStartNumber, int manyPerPage) {
    Table pagesTable = new Table(pages+2, 1);
      pagesTable.setCellpadding(2);
      pagesTable.setCellspacing(2);

    Text pageText;


    if (currentPage > 1) {
      pageText = getWhiteText(iwrb.getLocalizedString("travel.previous","Previous"));
      Link prevLink = new Link(pageText);
        prevLink.addParameter(this.parameterStartNumber, iStartNumber - manyPerPage);
      pagesTable.add(prevLink, 1, 1);
    }

    Link pageLink;
    for (int i = 1; i <= pages; i++) {
      if (i == currentPage) {
        pageText = getWhiteTextBold(Integer.toString(i));
      }else {
        pageText = getWhiteText(Integer.toString(i));
      }
      pageLink = new Link(pageText);
        pageLink.addParameter(this.parameterStartNumber, (i-1) * manyPerPage);
      pagesTable.add(pageLink, i+1, 1);
    }

    if (currentPage < pages) {
      pageText = getWhiteText(iwrb.getLocalizedString("travel.next","Next"));
      Link nextLink = new Link(pageText);
        nextLink.addParameter(this.parameterStartNumber, iStartNumber + manyPerPage);
      pagesTable.add(nextLink, pages + 2, 1);
    }

    return pagesTable;
  }

  public Text getWhiteTextBold(String content) {
    Text text = (Text)  super.theBoldText.clone();
      text.setText(content);
    return text;
  }

  public Text getWhiteText(String content) {
    Text text = (Text)  super.theText.clone();
      text.setText(content);
    return text;
  }

}
