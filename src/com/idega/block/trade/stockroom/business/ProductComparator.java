package com.idega.block.trade.stockroom.business;

import com.idega.util.IsCollator;
import com.idega.util.idegaTimestamp;
import java.util.*;
import java.util.Comparator;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import java.sql.SQLException;

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
  public static final int DEPARTURETIME = 3;
  public static final int DEPARTURETIME_NAME = 4;
  public static final int PRICE = 5;


  private int sortBy;

  public ProductComparator() {
      sortBy = NAME;
  }

  public ProductComparator(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      Product p1 = (Product) o1;
      Product p2 = (Product) o2;
      int result = 0;

      switch (this.sortBy) {
        case NAME     : result = nameSort(o1, o2);
        break;
        case NUMBER   : result = numberSort(o1, o2);
        break;
        case DEPARTURETIME   : result = departureTimeSort(o1, o2);
        break;
        case DEPARTURETIME_NAME   : result = departureTimeNameSort(o1, o2);
        break;
        case PRICE : result = priceSort(o1, o2);
        break;
      }

      return result;
  }

  private int nameSort(Object o1, Object o2) {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    String one = ProductBusiness.getProductName(p1)!=null?ProductBusiness.getProductName(p1):"";
    String two = ProductBusiness.getProductName(p2)!=null?ProductBusiness.getProductName(p2):"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int numberSort(Object o1, Object o2) {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    String one = p1.getNumber()!=null?p1.getNumber():"";
    String two = p2.getNumber()!=null?p2.getNumber():"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int departureTimeNameSort(Object o1, Object o2) {
    int result = departureTimeSort(o1, o2);
    if (result == 0) {
      return nameSort(o1, o2);
    }else {
      return result;
    }
  }

  private int departureTimeSort(Object o1, Object o2) {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    try {
      idegaTimestamp s1 = ProductBusiness.getDepartureTime(p1);
      idegaTimestamp s2 = ProductBusiness.getDepartureTime(p2);

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
    }

  }

  private int priceSort(Object o1, Object o2) {
    Product p1 = (Product) o1;
    Product p2 = (Product) o2;

    ProductPrice price1 = StockroomBusiness.getPrice(p1);
    ProductPrice price2 = StockroomBusiness.getPrice(p2);

    float pr1 = 0;
    if (price1 != null) pr1 = price1.getPrice();
    float pr2 = 0;
    if (price2 != null) pr2 = price2.getPrice();

    if (price1 == null)
    System.err.println("Price 1 == null");
    if (price2 == null)
    System.err.println("Price 2 == null");
    System.err.println(ProductBusiness.getProductName(p1)+" - pr1 : "+pr1+" , "+ProductBusiness.getProductName(p2)+" - pr2 : "+pr2);

    if (pr1 < pr2) return 1;
    else if (pr2 < pr1) return -1;
    else return 0;
    /**
     * @todo implementa fyrir mörg verð...
     */
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

}
