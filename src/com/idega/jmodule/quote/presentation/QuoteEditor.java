package com.idega.jmodule.quote.presentation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.quote.data.*;
import com.idega.util.text.*;

public class QuoteEditor extends JModuleObject{

private String adminURL = "/quote/quoteadmin.jsp";
private boolean isAdmin = false;
private boolean update = false;
private boolean save = false;
private Table outerTable;
private String color = "DDDDDD";
private String textColor = "000000";
private String headlineColor = "000000";
private String headlineBgColor = "FFFFFF";

public QuoteEditor(){
}

	public void main(ModuleInfo modinfo) throws Exception {

        isAdmin=isAdministrator(modinfo);

		if ( isAdmin ) {

			String newQuote = modinfo.getRequest().getParameter("new");
			String change = modinfo.getRequest().getParameter("change");
			String delete = modinfo.getRequest().getParameter("delete");
			String action = modinfo.getRequest().getParameter("action");
				if ( action == null ) { action = "none"; }
			String mode = modinfo.getRequest().getParameter("mode");
				if ( mode == null ) { mode = "none"; }

			if ( newQuote != null ) {
				newQuote(modinfo);
			}

			if ( change != null ) {
				update = true;
				newQuote(modinfo);
			}

			if ( delete != null ) {
				deleteQuote(modinfo);
			}

			if ( mode.equals("Vista") ) {

				if ( action.equals("update") ) {
					update = true;
				}

				saveQuote(modinfo);
			}

			add(outerTable);

		}

		else {

			noAccess();
			add(outerTable);

		}

	}

	public void noAccess() throws IOException,SQLException {

		outerTable = new Table(1,2);

		Text noDice = new Text("Þú hefur ekki réttindi til að skoða þessa síðu!");

		Form backForm = new Form("/index.jsp");
			backForm.add(new SubmitButton("Til baka"));

		outerTable.add(noDice,1,1);
		outerTable.add(backForm,1,2);

	}

	public void newQuote(ModuleInfo modinfo) throws IOException,SQLException {

		int quote_id = -1;

		if ( update ) {
			String quote_id2 = modinfo.getRequest().getParameter("quote_id");
				if ( quote_id2 == null ) { quote_id2 = "-1"; }

			quote_id = Integer.parseInt(quote_id2);
		}

		Quote quote = new Quote();

		if ( quote_id != -1 ) {
			quote = new Quote(quote_id);
		}

		HttpSession Session = modinfo.getSession();

		outerTable = new Table(1,3);
			outerTable.setCellpadding(2);
			outerTable.setCellspacing(2);
			outerTable.setColor(color);

		Form myForm = new Form(adminURL,"get");

		Table myTable = new Table(2,4);
			myTable.setBorder(0);
			myTable.setWidth("100%");
			myTable.setCellpadding(6);
			myTable.setCellspacing(6);
			myTable.setAlignment(2,4,"right");
			myTable.setVerticalAlignment(2,4,"bottom");

			TextInput quoteOrigin = new TextInput("quote_origin");
				quoteOrigin.setLength(30);
				quoteOrigin.setMaxlength(255);

			TextArea quoteText = new TextArea("quote_text",30,3);
				quoteText.setWrap(true);

			TextInput quoteAuthor = new TextInput("quote_author");
				quoteAuthor.setLength(30);
				quoteAuthor.setMaxlength(255);

				if ( update ) {

					if ( quote.getQuoteOrigin() != null ) {
						quoteOrigin.setContent(quote.getQuoteOrigin());
					}
					if ( quote.getQuoteText() != null ) {
						quoteText.setContent(quote.getQuoteText());
					}
					if ( quote.getQuoteAuthor() != null ) {
						quoteAuthor.setContent(quote.getQuoteAuthor());
					}

					myForm.add(new HiddenInput("action","update"));
					myForm.add(new HiddenInput("quote_id",String.valueOf(quote_id)));
				}

			SubmitButton vista = new SubmitButton("mode","Vista");

			Text origin_text = new Text("Uppruni:");
				origin_text.setFontColor(textColor);
			Text quote_text = new Text("Málsháttur:");
				quote_text.setFontColor(textColor);
			Text author_text = new Text("Höfundur:");
				author_text.setFontColor(textColor);

			myTable.add(origin_text,1,1);
			myTable.add(quoteOrigin,2,1);

			myTable.add(quote_text,1,2);
			myTable.add(quoteText,2,2);

			myTable.add(author_text,1,3);
			myTable.add(quoteAuthor,2,3);

			myTable.add(vista,2,4);

		myForm.add(myTable);

		outerTable.setRowColor(1,headlineBgColor);

		Text headline_text = new Text("Málsháttastjórinn");
			headline_text.setBold();
			headline_text.setFontSize(3);
			headline_text.setFontColor(headlineColor);

		outerTable.add(headline_text,1,1);
		outerTable.add(myForm,1,3);

	}

	public void saveQuote(ModuleInfo modinfo) throws IOException,SQLException {

		boolean check = true;

		HttpServletRequest request = modinfo.getRequest();

		String quote_id = request.getParameter("quote_id");

		String quote_origin = request.getParameter("quote_origin");
			if ( quote_origin == null || quote_origin.equals("") ) { quote_origin = "Uppruni óþekktur"; }

		String quote_text = request.getParameter("quote_text");
			if ( quote_text == null || quote_text.equals("") ) { quote_text = ""; }

		String quote_author = request.getParameter("quote_author");
			if ( quote_author == null || quote_author.equals("") ) { quote_author = "Óþekktur"; }

		idegaTimestamp date = new idegaTimestamp();

		Quote quote = new Quote();

		if ( update ) { quote = new Quote(Integer.parseInt(quote_id)); }

		quote.setQuoteOrigin( quote_origin );
		quote.setQuoteText( quote_text );
		quote.setQuoteAuthor(quote_author);
        quote.setQuoteDate( date.getTimestampRightNow() );

		if ( update ) { quote.update(); }
		else { quote.insert(); }

		outerTable = new Table(1,2);

		outerTable.addText("Orð dagsins hefur verið vistað!",1,1);

		Form myForm = new Form("/index.jsp");
			myForm.add(new SubmitButton("Til baka"));

		outerTable.add(myForm,1,2);

	}

	public void deleteQuote(ModuleInfo modinfo) throws IOException,SQLException {

		String quote_id = modinfo.getRequest().getParameter("quote_id");

		Quote quote = new Quote(Integer.parseInt(quote_id));

		quote.delete();

		outerTable = new Table(1,2);
			outerTable.setCellpadding(3);
			outerTable.setCellspacing(3);

		outerTable.addText("Orði dagsins hefur verið eytt!",1,1);

		Form myForm = new Form("/index.jsp");
			myForm.add(new SubmitButton("Til baka"));

		outerTable.add(myForm,1,2);

	}

	public void setColor(String color){
		this.color=color;
	}

	public void setTextColor(String color){
		this.textColor=color;
	}

	public void setHeadlineColor(String headlineColor){
		this.headlineColor=headlineColor;
	}

	public void setHeadlineBgColor(String headlineBgColor){
		this.headlineBgColor=headlineBgColor;
	}

	public void setAdmin(boolean isAdmin){
		this.isAdmin=isAdmin;
	}
}

