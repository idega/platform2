package com.idega.block.trade.stockroom.business;

import com.idega.util.IsCollator;
import com.idega.util.idegaTimestamp;
import java.util.*;
import java.util.Comparator;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.business.ProductBusiness;


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
//  public static final int YEARFROMDATE = 3;
//  public static final int TOFROMDATE = 3;


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
