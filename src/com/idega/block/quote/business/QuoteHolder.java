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
    quoteID_ = id;
  }

  protected void setOrigin(String origin) {
    origin_ = origin;
  }

  protected void setText(String text) {
    text_ = text;
  }

  protected void setAuthor(String author) {
    author_ = author;
  }

  protected void setLocaleID(int localeID) {
    localeID_ = localeID;
  }

  /* Getters */
  public int getQuoteID() {
    return quoteID_;
  }

  public String getOrigin() {
    return origin_;
  }

  public String getText() {
    return text_;
  }

  public String getAuthor() {
    return author_;
  }

}