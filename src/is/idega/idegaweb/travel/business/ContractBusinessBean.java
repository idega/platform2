package is.idega.idegaweb.travel.business;

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
      Product product = (Product) com.idega.block.trade.stockroom.data.ProductBMPBean.getStaticInstance(Product.class);
      Contract contract = (Contract) is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(s.*) FROM "+com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()+" s, "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c, "+product.getEntityName()+" p");
        buffer.append(" WHERE ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = p."+product.getIDColumnName());
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
      Product product = (Product) com.idega.block.trade.stockroom.data.ProductBMPBean.getStaticInstance(Product.class);
      Contract contract = (Contract) is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class);
      Reseller reseller = (Reseller) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(p.*) FROM  "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c");
        buffer.append(", "+product.getEntityName()+" p");
        if (ownerResellerId != -1) {
          buffer.append(", "+reseller.getTreeRelationshipTableName(reseller)+" r");
        }
        buffer.append(" WHERE ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = "+contractedResellerId);
        buffer.append(" AND ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = p."+product.getIDColumnName());
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

      products = (Product[]) product.findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return products;
  }

  public boolean isActiveContract(int supplierId, int resellerId, int productId) {
    boolean returner = false;

    try {
      Product product = (Product) com.idega.block.trade.stockroom.data.ProductBMPBean.getStaticInstance(Product.class);
      Contract contract = (Contract) is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT distinct(c.*) FROM  "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName() +" c");
        if (supplierId != -1) {
          buffer.append(", "+product.getEntityName()+" p");
        }
        buffer.append(" WHERE ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+" = "+resellerId);
        buffer.append(" AND ");
        buffer.append("c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+" = "+productId);
        if (supplierId != -1) {
          buffer.append(" AND ");
          buffer.append("p."+product.getIDColumnName()+" = c."+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId());
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
}