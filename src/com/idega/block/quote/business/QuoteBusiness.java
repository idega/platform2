package com.idega.block.quote.business;



import java.sql.SQLException;

import com.idega.util.idegaTimestamp;

import com.idega.block.quote.data.QuoteEntity;

import com.idega.presentation.IWContext;



public class QuoteBusiness{



public static final String PARAMETER_QUOTE = "quote";

public static final String PARAMETER_QUOTE_ID = "quote_id";

public static final String PARAMETER_QUOTE_DATE = "quote_date";

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



  public static QuoteHolder getRandomQuote(IWContext iwc, int localeID) {

    QuoteHolder holder = null;

    QuoteHolder newHolder = null;

    String date = null;

    String dateNow = new idegaTimestamp().toSQLDateString();



    try {

      holder = (QuoteHolder) iwc.getApplicationAttribute(PARAMETER_QUOTE+"_"+Integer.toString(localeID));

    }

    catch (Exception e) {

      holder = null;

    }



    date = (String) iwc.getApplicationAttribute(PARAMETER_QUOTE_DATE+"_"+Integer.toString(localeID));



    if ( date != null && holder != null && date.equalsIgnoreCase(dateNow) )

      return holder;

    else {

      newHolder = QuoteFinder.getQuoteHolder(QuoteFinder.getRandomQuote(localeID));

      if ( holder != null ) {

	while ( holder.getQuoteID() == newHolder.getQuoteID() )

	  newHolder = QuoteFinder.getQuoteHolder(QuoteFinder.getRandomQuote(localeID));

      }



      if ( newHolder != null ) {

	iwc.setApplicationAttribute(PARAMETER_QUOTE+"_"+Integer.toString(localeID),newHolder);

	iwc.setApplicationAttribute(PARAMETER_QUOTE_DATE+"_"+Integer.toString(localeID),dateNow);

	return newHolder;

      }

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

      quote = QuoteFinder.getQuote(quoteID);

      if ( quote == null ) {

	quote = ((com.idega.block.quote.data.QuoteEntityHome)com.idega.data.IDOLookup.getHomeLegacy(QuoteEntity.class)).createLegacy();

	update = false;

      }

    }

    else {

      quote = ((com.idega.block.quote.data.QuoteEntityHome)com.idega.data.IDOLookup.getHomeLegacy(QuoteEntity.class)).createLegacy();

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

	((com.idega.block.quote.data.QuoteEntityHome)com.idega.data.IDOLookup.getHomeLegacy(QuoteEntity.class)).findByPrimaryKeyLegacy(quoteID).delete();

      }

    }

    catch (SQLException e) {

      e.printStackTrace(System.err);

    }

  }



}
