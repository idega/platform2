package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.IsCollator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ProductComparator implements Comparator {

  public static final int NAME = 1;
  public static final int NUMBER = 2;
//  public static final int DEPARTURETIME = 3;
//  public static final int DEPARTURETIME_NAME = 4;
  public static final int PRICE = 5;
  public static final int CREATION_DATE = 6;
  public static final int SUPPLIER = 7; 

  private int localeId = -1;
  private int sortBy;
  private StockroomBusiness stockroomBusiness;
	private ProductBusiness productBusiness;

  private PriceCategory priceCategoryToSortBy;
  private int currencyId;
  private IWTimestamp time;
  private IWContext iwc;

  public ProductComparator() {
    this(1);
  }

  public ProductComparator(int toSortBy) {
    this(toSortBy, IWContext.getInstance().getCurrentLocaleId());
  }

  public ProductComparator(int toSortBy, int localeId) {
      sortBy = toSortBy;
      this.localeId = localeId;
      iwc = IWContext.getInstance();
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      int result = 0;

      try {
        switch (this.sortBy) {
          case NAME     : result = nameSort(o1, o2);
          break;
          case NUMBER   : result = numberSort(o1, o2);
          break;
          /*case DEPARTURETIME   : result = departureTimeSort(o1, o2);
          break;
          case DEPARTURETIME_NAME   : result = departureTimeNameSort(o1, o2);
          break;*/
          case PRICE : result = priceSort(o1, o2);
          break;
          case CREATION_DATE : result = dateSort(o1, o2);
          break;
          case SUPPLIER : result = supplierSort(o1, o2);
        }
      }catch (RemoteException rme) {
        rme.printStackTrace(System.err);
      }

      return result;
  }

  private int nameSort(Object o1, Object o2) throws RemoteException{
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    String one = p1.getProductName(localeId)!=null?p1.getProductName(localeId):"";
    String two = p2.getProductName(localeId)!=null?p2.getProductName(localeId):"";

    return IsCollator.getIsCollator().compare(two,one);
  }
  
  private int supplierSort(Object o1, Object o2) throws RemoteException {
  		Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    String one = p1.getSupplier().getName() != null ? p1.getSupplier().getName(): "";
    String two = p2.getSupplier().getName() != null ? p2.getSupplier().getName(): "";
    return IsCollator.getIsCollator().compare(two, one);
  	
  }

  private int numberSort(Object o1, Object o2) throws RemoteException {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    String one = p1.getNumber()!=null?p1.getNumber():"";
    String two = p2.getNumber()!=null?p2.getNumber():"";

    return IsCollator.getIsCollator().compare(one,two);
  }
/*
  private int departureTimeNameSort(Object o1, Object o2) {
    int result = departureTimeSort(o1, o2);
    if (result == 0) {
      return nameSort(o1, o2);
    }else {
      return result;
    }
  }
*/
/*
  private int departureTimeSort(Object o1, Object o2) {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    try {
      IWTimestamp s1 = p1.getDepartureTime(p1);
      IWTimestamp s2 = getServiceHandler().getDepartureTime(p2);

      if (s1.isLaterThan(s2)) {
	return 1;
      }else if (s2.isLaterThan(s1)){
	return -1;
      }else {
	return 0;
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return 0;
    }catch (RemoteException r) {
      throw new RuntimeException(r.getMessage());
    }

  }
*/
  private int priceSort(Object o1, Object o2) {
    try {
      Product p1 = (Product) o1;
      Product p2 = (Product) o2;
	
		  float pr1 = 0;
		  float pr2 = 0;

			if (priceCategoryToSortBy == null) {
      	ProductPrice price1 = getStockroomBusiness().getPrice(p1);
      	ProductPrice price2 = getStockroomBusiness().getPrice(p2);

	      if (price1 != null) pr1 = price1.getPrice();
      	if (price2 != null) pr2 = price2.getPrice();
			} else {
				Timeframe timeframe = getProductBusiness().getTimeframe(p1, time, -1);
				int timeframeId1 = -1;
				if (timeframe != null) {
					timeframeId1 = timeframe.getID();
				}
				timeframe = getProductBusiness().getTimeframe(p2, time, -1);
				int timeframeId2 = -1;
				if (timeframe != null) {
					timeframeId2 = timeframe.getID();
				}				
				try {
					pr1 = getStockroomBusiness().getPrice(-1, p1.getID(), Integer.parseInt(priceCategoryToSortBy.getPrimaryKey().toString()), currencyId, IWTimestamp.getTimestampRightNow(), timeframeId1, -1);
					//System.out.println("[ProductComparator] : price for p1 = "+pr1+" ("+p1.getProductName(IWContext.getInstance().getCurrentLocaleId())+"="+p1.getID()+")");
				}
				catch (ProductPriceException e) {
					//System.out.println("[ProductComparator] : cannot get priceCategory specific price ("+p1.getProductName(IWContext.getInstance().getCurrentLocaleId())+"="+p1.getID()+")");
				}
				catch (Exception e) {
					//System.out.println("[ProductComparator] : cannot get priceCategory specific price ("+p1.getProductName(IWContext.getInstance().getCurrentLocaleId())+"="+p1.getID()+")");
					e.printStackTrace();
				}
				try {
					pr2 = getStockroomBusiness().getPrice(-1, p2.getID(), Integer.parseInt(priceCategoryToSortBy.getPrimaryKey().toString()), currencyId, IWTimestamp.getTimestampRightNow(), timeframeId2, -1);
					//System.out.println("[ProductComparator] : price for p2 = "+pr2+" ("+p2.getProductName(IWContext.getInstance().getCurrentLocaleId())+"="+p2.getID()+")");
				}
				catch (ProductPriceException e) {
					//System.out.println("[ProductComparator] : cannot get priceCategory specific price ("+p2.getProductName(IWContext.getInstance().getCurrentLocaleId())+"="+p2.getID()+")");
				}
				catch (Exception e) {
					//System.out.println("[ProductComparator] : cannot get priceCategory specific price ("+p2.getProductName(IWContext.getInstance().getCurrentLocaleId())+"="+p2.getID()+")");
					e.printStackTrace();
				}
			}

      if (pr1 < pr2) return 1;
      else if (pr2 < pr1) return -1;
      else return 0;

      }catch (RemoteException re) {
        throw new RuntimeException(re.getMessage());
      }
  }

  private int dateSort(Object o1, Object o2) throws RemoteException {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    IWTimestamp s1 = new IWTimestamp(p1.getCreationDate());
    IWTimestamp s2 = new IWTimestamp(p2.getCreationDate());

    if (s1.isLaterThan(s2)) {
      return -1;
    }
    else if (s2.isLaterThan(s1)){
      return 1;
    }
    else {
      return 0;
    }
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(Product[] products, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < products.length; i++) {
	  list.add(products[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(Product[] products) {
      List list = new LinkedList();
      for(int i = 0; i < products.length; i++) {
	  list.add(products[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Product[] sortedArray(Product[] products, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < products.length; i++) {
	  list.add(products[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
	  products[i] = (Product) objArr[i];
      }
      return (products);
  }

   public Vector sortedArray(Vector list) {
      Collections.sort(list, this);
      return list;
  }


  public Product[] sortedArray(Product[] products) {
      List list = new LinkedList();
      for(int i = 0; i < products.length; i++) {
	  list.add(products[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
	  products[i] = (Product) objArr[i];
      }
      return (products);
  }

  public Product[] reverseSortedArray(Product[] products, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < products.length; i++) {
	  list.add(products[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
	  products[i] = (Product) objArr[i];
      }
      return (products);
  }

  private StockroomBusiness getStockroomBusiness() {
    try {
      if (stockroomBusiness == null) {
        stockroomBusiness = (StockroomBusiness) IBOLookup.getServiceInstance(IWContext.getInstance(), StockroomBusiness.class);
      }
      return stockroomBusiness;
    }catch (RemoteException re) {
      throw new RuntimeException(re.getMessage());
    }
  }
  
  private ProductBusiness getProductBusiness() {
		try {
		  if (productBusiness == null) {
			productBusiness = (ProductBusiness) IBOLookup.getServiceInstance(IWContext.getInstance(), ProductBusiness.class);
		  }
		  return productBusiness;
		}catch (RemoteException re) {
		  throw new RuntimeException(re.getMessage());
		}
  }

	public void setPriceCategoryValues(PriceCategory priceCategory, int currencyId, IWTimestamp time) {
		this.priceCategoryToSortBy = priceCategory;
		this.currencyId = currencyId;
		this.time = time;
	}

}
