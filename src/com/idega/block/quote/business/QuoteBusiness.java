package com.idega.block.quote.business;

import java.sql.*;
import com.idega.block.quote.data.*;

public class QuoteBusiness{

public static final String PARAMETER_QUOTE_ID = "quote_id";
public static final String PARAMETER_LOCALE_ID = "locale_id";
public static final String PARAMETER_QUOTE_TEXT = "quote_text";
public static final String PARAMETER_QUOTE_AUTHOR = "quote_author";
public static final String PARAMETER_QUOTE_ORIGIN = "quote_origin";
public static final String PARAMETER_MODE = "mode";
public static final String PARAMETER_NEW = "new";
public static final String PARAMETER_DELETE = "delete";
public static final String PARAMETER_EDIT = "edit";
public static final String PARAMETER_SAVE = "save";
public static final String PARAMETER_CLOSE = "close";

  public static QuoteEntity getRandomQuote(int localeID) {
    try {
      QuoteEntity[] quotes = (QuoteEntity[]) QuoteEntity.getStaticInstance(QuoteEntity.class).findAllByColumn(QuoteEntity.getColumnNameICLocaleID(),Integer.toString(localeID),"=");
      if ( quotes != null ) {
        if ( quotes.length > 0 ) {
          int quoteNumber = (int) Math.round(Math.random() * (quotes.length - 1));
          return quotes[quoteNumber];
        }
      }
      return null;
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static QuoteEntity getQuote(int quoteID) {
    try {
      return new QuoteEntity(quoteID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static void saveQuote(int quoteID,int iLocaleID,String quoteOrigin,String quoteText,String quoteAuthor) {
    boolean update = false;
    if ( quoteID != -1 ) {
      update = true;
    }

    QuoteEntity quote = null;
    if ( update ) {
      quote = getQuote(quoteID);
      if ( quote == null ) {
        quote = new QuoteEntity();
        update = false;
      }
    }
    else {
      quote = new QuoteEntity();
    }

    if ( quoteOrigin != null || quoteOrigin.length() == 0 ) {
      quote.setQuoteOrigin(quoteOrigin);
    }
    if ( quoteText != null || quoteText.length() == 0 ) {
      quote.setQuoteText(quoteText);
    }
    if ( quoteAuthor != null || quoteAuthor.length() == 0 ) {
      quote.setQuoteAuthor(quoteAuthor);
    }

    if ( !update ) {
      quote.setICLocaleID(iLocaleID);
      try {
        quote.insert();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        quote.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
  }

  public static void deleteQuote(int quoteID) {
    try {
      if ( quoteID != -1 ) {
        new QuoteEntity(quoteID).delete();
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

}