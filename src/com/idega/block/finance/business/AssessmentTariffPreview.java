package com.idega.block.finance.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class AssessmentTariffPreview {

  private String tariffName;
  private float totals;
  private int accounts;

  public AssessmentTariffPreview(String name, int accounts ,float totals) {
    this.totals = totals;
    this.accounts = accounts;
    this.tariffName = name;
  }

  public AssessmentTariffPreview(String name) {
    this.totals = 0;
    this.accounts = 0;
    this.tariffName = name;
  }

  public float getTotals(){
    return this.totals;
  }

  public String getName(){
    return this.tariffName;
  }
  public int getAccounts(){
    return this.accounts;
  }
  public void addAmount(float amount){
    this.totals += amount;
    this.accounts ++;
  }
}
