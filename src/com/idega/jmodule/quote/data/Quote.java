//idega 2000 - Laddi

package com.idega.jmodule.quote.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.jmodule.boxoffice.data.*;

public class Quote extends GenericEntity{

	public Quote(){
		super();
	}

	public Quote(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("quote_origin", "Heimild", true, true, "java.lang.String");
		addAttribute("quote_text", "Texti", true, true, "java.lang.String");
		addAttribute("quote_author", "Höfundur", true, true, "java.lang.String");
		addAttribute("quote_date", "Dagsetning", true, true, "java.sql.Timestamp");
	}

	public String getIDColumnName(){
		return "quote_id";
	}

	public String getEntityName(){
		return "Quote";
	}

	public String getQuoteOrigin(){
		return getStringColumnValue("quote_origin");
	}

	public void setQuoteOrigin(String quote_origin){
			setColumn("quote_origin", quote_origin);
	}

	public String getQuoteText(){
		return getStringColumnValue("quote_text");
	}

	public void setQuoteText(String quote_text){
			setColumn("quote_text", quote_text);
	}

	public String getQuoteAuthor(){
		return getStringColumnValue("quote_author");
	}

	public void setQuoteAuthor(String quote_author){
			setColumn("quote_author", quote_author);
	}

	public java.sql.Timestamp getQuoteDate(){
		return (java.sql.Timestamp) getColumnValue("quote_date");
	}

	public void setQuoteDate(java.sql.Timestamp quote_date){
		setColumn("quote_date", quote_date);
	}

}
