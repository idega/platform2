package com.idega.block.quote.business;



public class QuoteHolder {


private int quoteID_ = -1;
private String origin_ = null;
private String text_ = null;
private String author_ = null;
private int localeID_ = -1;


  public QuoteHolder() {
  }

  /* Setters */

  protected void setQuoteID(int id) {
    this.quoteID_ = id;
  }

  protected void setOrigin(String origin) {
    this.origin_ = origin;
  }

  protected void setText(String text) {
    this.text_ = text;
  }

  protected void setAuthor(String author) {
    this.author_ = author;
  }

  protected void setLocaleID(int localeID) {
    this.localeID_ = localeID;
  }

  /* Getters */
  public int getQuoteID() {
    return this.quoteID_;
  }

  public String getOrigin() {
    return this.origin_;
  }

  public String getText() {
    return this.text_;
  }

  public String getAuthor() {
    return this.author_;
  }

}