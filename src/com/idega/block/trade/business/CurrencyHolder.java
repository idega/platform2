package com.idega.block.trade.business;

import com.idega.util.IWTimestamp;

public class CurrencyHolder {

public static final String USA_DOLLAR = "USD";
public static final String BRITISH_POUND = "GBP";
public static final String CANADIAN_DOLLAR = "CAD";
public static final String DANISH_KRONA = "DKK";
public static final String NORWEGIAN_KRONA = "NOK";
public static final String SWEDISH_KRONA = "SEK";
public static final String ICELANDIC_KRONA = "ISK";
public static final String FINNISH_MARK = "FIM";
public static final String FRENCH_FRANK = "FRF";
public static final String BELGIAN_FRANK = "BEF";
public static final String SWISS_FRANK = "CHF";
public static final String DUTCH_GYLDER = "NLG";
public static final String GERMAN_MARK = "DEM";
public static final String ITALIAN_LIRA = "ITL";
public static final String AUSTRIAN_SCHILLING = "ATS";
public static final String PORTUGESE_ESCUDO = "PTE";
public static final String SPANISH_PESETO = "ESP";
public static final String JAPANESE_YEN = "JPY";
public static final String IRISH_POUND = "IEP";
public static final String GREEK_DRAKMA = "GRD";
public static final String SDR = "XDR";
public static final String EURO = "EUR";

private String currencyName_ = null;
private String currencyAbbreviation_ = null;
private float buyValue_ = -1;
private float sellValue_ = -1;
private float middleValue_ = -1;
private IWTimestamp timestamp_ = null;
private int currencyID_ = -1;

  public CurrencyHolder() {
  }


  /* Setters */
  public void setCurrencyID(int id) {
    this.currencyID_ = id;
  }

  public void setCurrencyName(String name) {
    this.currencyName_ = name;
  }

  public void setBuyValue(float value) {
    this.buyValue_ = value;
  }

  public void setSellValue(float value) {
    this.sellValue_ = value;
  }

  public void setMiddleValue(float value) {
    this.middleValue_ = value;
  }

  public void setTimestamp(IWTimestamp timestamp) {
    this.timestamp_ = timestamp;
  }

  public void setCurrencyAbbreviation(String abbreviation) {
    this.currencyAbbreviation_ = abbreviation;
  }

  /* Getters */
  public int getCurrencyID() {
    return this.currencyID_;
  }

  public String getCurrencyName() {
    return this.currencyName_;
  }

  public float getBuyValue() {
    return this.buyValue_;
  }

  public float getSellValue() {
    return this.sellValue_;
  }

  public float getMiddleValue() {
    return this.middleValue_;
  }

  public IWTimestamp getTimestamp() {
    return this.timestamp_;
  }

  public String getCurrencyAbbreviation() {
    return this.currencyAbbreviation_;
  }

}
