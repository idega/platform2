package is.idega.idegaweb.travel.business;

import java.rmi.RemoteException;
import com.idega.business.IBOLookup;
import javax.ejb.FinderException;
import java.util.List;
import com.idega.data.EntityFinder;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import is.idega.idegaweb.travel.data.Contract;
import com.idega.data.SimpleQuerier;
import com.idega.business.IBOServiceBean;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ContractBusinessBean extends IBOServiceBean implements ContractBusiness{

  public ContractBusinessBean() {
  }

  public Supplier[] getSuppliersWithContracts(int resellerId) {
    return getSuppliersWithContracts(resellerId, null);
  }

  public Supplier[] getSuppliersWithContracts(int resellerId, String orderBy) {
    Supplier[] suppliers =  {};
    try {
      Supplier supplier = (Supplier) com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class);
      Contract contract = (Contract) is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class);

       StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(s.*) FROM "+com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()+" s, "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c, "+ProductBMPBean.getProductEntityName()+" p");
//       StringBuffer buffer = new StringBuffer();
//        buffer.append("SELECT s.* FROM "+com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()+" s, "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c, "+ProductBMPBean.getProductEntityName()+" p");
        buffer.append(" WHERE ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = p."+ProductBMPBean.getIdColumnName());
        buffer.append(" AND ");
        buffer.append("p."+com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameSupplierId()+" = s."+supplier.getIDColumnName());
        if (orderBy != null && !orderBy.equals("")) {
        buffer.append(" ORDER BY s."+orderBy);
        }        

      suppliers = (Supplier[]) supplier.findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return suppliers;
  }

  public Product[] getProductsWithContracts(int resellerId, int supplierId, String orderBy) {
    return getProductsWithContracts(-1, resellerId ,supplierId, orderBy);
  }

  private Product[] getProductsWithContracts(int ownerResellerId, int contractedResellerId, int supplierId, String orderBy) {
    Product[] products =  {};
    try {
      Contract contract = (Contract) is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class);
      Reseller reseller = (Reseller) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(p.*) FROM  "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c");
//        buffer.append("SELECT p.* FROM  "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c");
        buffer.append(", "+ProductBMPBean.getProductEntityName()+" p");
        if (ownerResellerId != -1) {
          buffer.append(", "+reseller.getTreeRelationshipTableName(reseller)+" r");
        }
        buffer.append(" WHERE ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = "+contractedResellerId);
        buffer.append(" AND ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = p."+ProductBMPBean.getIdColumnName());
        if (supplierId != -1) {
          buffer.append(" AND ");
          buffer.append("p."+com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameSupplierId()+" = "+supplierId);
        }else if (ownerResellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+reseller.getTreeRelationshipChildColumnName(reseller)+"="+contractedResellerId);
          buffer.append(" AND ");
          buffer.append("r."+reseller.getIDColumnName()+"="+ownerResellerId);
        }
        if (orderBy != null && !orderBy.equals("")) {
//          buffer.append(" ORDER BY p."+orderBy);
        }

      List prods = EntityFinder.getInstance().findAll(Product.class, buffer.toString());
      products = (Product[]) prods.toArray(new Product[] {});
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }
    return products;
  }

  public boolean isActiveContract(int supplierId, int resellerId, int productId) {
    boolean returner = false;

    try {
      Contract contract = (Contract) is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(c.*) FROM  "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c");
        if (supplierId != -1) {
          buffer.append(", "+ProductBMPBean.getProductEntityName()+" p");
        }
        buffer.append(" WHERE ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = "+productId);
        if (supplierId != -1) {
          buffer.append(" AND ");
          buffer.append("p."+ProductBMPBean.getIdColumnName()+" = c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId());
          buffer.append(" AND ");
          buffer.append("p."+com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameSupplierId()+" = "+supplierId);
        }

      String[] resuls = SimpleQuerier.executeStringQuery(buffer.toString());
      if (resuls != null && resuls.length > 0) {
        returner = true;
      }

      //products = (Product[]) product.findAll(buffer.toString());
    }catch (Exception sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }


  public Product[] getProductsWithContracts(int resellerId, int supplierId) {
    return getProductsWithContracts(resellerId, supplierId, null);
  }


  public boolean isActiveContract(int resellerId, int productId) {
    return isActiveContract(-1, resellerId, productId);
  }

  public Product[] getProductsWithContracts(Reseller reseller, String orderBy) {
    Reseller parent = (Reseller) reseller.getParent();
    if (parent == null) {
      return getProductsWithContracts(-1, reseller.getID(),-1, orderBy);
    }else {
      return getProductsWithContracts(parent, orderBy);
    }
  }

  public Product[] getProductsWithContracts(Reseller reseller) {
    return getProductsWithContracts(reseller, null);
  }

  public Reseller[] getResellers(int serviceId, IWTimestamp stamp) {
    Reseller[] returner = {};
    try {
        Reseller reseller = (Reseller) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select r.* from "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName()+" c, "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" r");
            sql.append(" where ");
            sql.append(" c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+"="+serviceId);
            sql.append(" and ");
            sql.append(" c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameFrom()+" <= '"+stamp.toSQLDateString()+"'");
            sql.append(" and ");
            sql.append(" c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameTo()+" >= '"+stamp.toSQLDateString()+"'");
            sql.append(" and ");
            sql.append(" c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = r."+reseller.getIDColumnName());

        returner = (Reseller[]) reseller.findAll(sql.toString());

    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;

  }

  public Product[] getProductsForReseller(IWContext iwc, int resellerId) throws SQLException {
    Product[] products = {};
    /**
     * @todo Cache...
     */

    try {
        Reseller reseller = (Reseller) (com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class));

        StringBuffer sql = new StringBuffer();
          sql.append("SELECT p.* FROM "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" r, "+com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName()+" p, "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName()+" c");
          sql.append(" WHERE ");
          sql.append(" c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = r."+reseller.getIDColumnName());
          sql.append(" AND ");
          sql.append(" c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = p."+ProductBMPBean.getIdColumnName());
          sql.append(" AND ");
          sql.append(" r."+reseller.getIDColumnName() +" = "+resellerId);
          sql.append(" AND ");
          sql.append(" p."+com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameIsValid()+" = 'Y'");
//          sql.append(" ORDER BY p."+com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameProductName());

      List prods = EntityFinder.getInstance().findAll(Product.class, sql.toString());
      products = (Product[]) prods.toArray(new Product[] {});
//        products = (Product[]) product.findAll(sql.toString());

    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }

    return products;
    //(Product[]) com.idega.block.trade.stockroom.data.ProductBMPBean.getStaticInstance(Product.class).findAllByColumnOrdered(Service.getIsValidColumnName(),"Y",com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class).getIDColumnName() , Integer.toString(supplierId), com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameProductName());
  }

  public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int resellerId) throws RemoteException{
    try {
      Product[] list = getProductsForReseller(iwc, resellerId);
      DropdownMenu menu = new DropdownMenu(ProductBMPBean.getProductEntityName());
      Product product;
      if (list != null && list.length > 0) {
        for (int i = 0; i < list.length; i++) {
          menu.addMenuElement(list[i].getID(), getProductBusiness().getProductNameWithNumber(list[i]));
        }
      }
      return menu;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return new DropdownMenu(com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName());
    }
  }

  private ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }
}