package is.idega.idegaweb.travel.business;

import javax.ejb.*;

public interface ContractBusiness extends com.idega.business.IBOService
{
 public com.idega.presentation.ui.DropdownMenu getDropdownMenuWithProducts(com.idega.presentation.IWContext p0,int p1) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProductsForReseller(com.idega.presentation.IWContext p0,int p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProductsWithContracts(com.idega.block.trade.stockroom.data.Reseller p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProductsWithContracts(com.idega.block.trade.stockroom.data.Reseller p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProductsWithContracts(int p0,int p1) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProductsWithContracts(int p0,int p1,java.lang.String p2) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Reseller[] getResellers(int p0,com.idega.util.IWTimeStamp p1) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Supplier[] getSuppliersWithContracts(int p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Supplier[] getSuppliersWithContracts(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public boolean isActiveContract(int p0,int p1) throws java.rmi.RemoteException;
 public boolean isActiveContract(int p0,int p1,int p2) throws java.rmi.RemoteException;
}
