package is.idega.idegaweb.travel.service.business;

import java.rmi.RemoteException;

import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.DropdownMenu;

public interface ProductCategoryFactory extends com.idega.business.IBOService
{
 public com.idega.block.trade.stockroom.data.ProductCategoryHome getProductCategoryHome() throws java.rmi.RemoteException;
 public java.util.Collection getAllProductCategories()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getProductCategory(com.idega.block.trade.stockroom.data.Product p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getProductCategoryType(com.idega.block.trade.stockroom.data.ProductCategory p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public String getProductCategoryTypeDefaultName(String type);
 public DropdownMenu getProductCategoryDropdown(IWResourceBundle iwrb, Supplier supplier, String name) throws IDORelationshipException, RemoteException;
}
