package com.idega.jmodule.quote.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.quote.data.*;
import com.idega.data.*;
import com.idega.util.text.*;


public class QuoteReader extends JModuleObject{

private String quote_id = null;
private boolean isAdmin = false;

private int quoteBorder = 0;
private String quoteBorderColor = "#FFFFFF";
private String quoteColor = "";
private Image quoteBackgroundImage;
private String quoteWidth = "150";
private String quoteHeight = "0";
private int quotePadding = 0;

private String textColor = "#000000";
private String originColor = "#000000";
private String quoteTextColor = "#000000";
private String authorColor = "#000000";
private boolean showOrigin = true;
private boolean leftHeader = true;
private boolean rightHeader = true;
private String headlineColor = "#000000";
private int headlineSize = 2;
private boolean headerLeft = false;
private int quoteTextSize = 2;
private int authorTextSize = 1;
private int originTextSize = 1;
private int headerSize = 21;
private String headerFontFace = "Verdana, Arial, Helvetica, sans-serif";
private boolean isClean = false;

private Table outerTable;
private Table myTable;
private Table contentTable;

private String adminURL = "/quote/quoteadmin.jsp";

public QuoteReader(){
}

public QuoteReader(String quote_id){
	this.quote_id=quote_id;
}

	public void main(ModuleInfo modinfo) throws Exception {

        this.isAdmin=isAdministrator(modinfo);

		drawTable();
		drawContentTable();

		add(contentTable);

	}

	private void drawTable() throws SQLException {

		myTable = new Table(1,3);
			myTable.setBorder(0);
			myTable.setColor(quoteColor);
			myTable.setWidth("100%");
			myTable.setHeight("100%");
			myTable.setAlignment("center");

			if ( quoteBackgroundImage != null ) {
				myTable.setBackgroundImage(quoteBackgroundImage);
			}

		Quote[] quote = (Quote[]) (new Quote()).findAllOrdered("quote_date desc");

		if ( quote.length > 0 ) {

			Random number = new Random();
			int quoteNumber = number.nextInt(quote.length);

			myTable.setAlignment(1,3,"right");
			myTable.setAlignment(1,1,"left");
			myTable.setAlignment(1,2,"center");

			quote_id = String.valueOf(quote[quoteNumber].getID());

			Text quoteOrigin = new Text(quote[quoteNumber].getQuoteOrigin()+":");
				quoteOrigin.setBold();
				quoteOrigin.setFontSize(originTextSize);
				quoteOrigin.setFontColor(originColor);

			Text quoteText = new Text("\""+quote[quoteNumber].getQuoteText()+"\"");
				quoteText.setItalic();
				quoteText.setFontColor(quoteTextColor);
				quoteText.setFontSize(quoteTextSize);

			Text quoteAuthor = new Text("- "+quote[quoteNumber].getQuoteAuthor()+"&nbsp;&nbsp;");
				quoteAuthor.setBold();
				quoteAuthor.setFontSize(authorTextSize);
				quoteAuthor.setFontColor(authorColor);

			if ( showOrigin ) {
				myTable.add(quoteOrigin,1,1);
				myTable.add(quoteText,1,2);
				myTable.add(quoteAuthor,1,3);
			}

			else {
				myTable.mergeCells(1,1,1,2);
				myTable.setAlignment(1,1,"center");
				myTable.setVerticalAlignment(1,1,"middle");

				myTable.add(quoteText,1,1);
				myTable.add(quoteAuthor,1,3);
			}

		}

		else {

			myTable.resize(1,1);
			myTable.setAlignment(1,1,"center");

			myTable.addText("No quotes in database...");

		}

		if ( isAdmin ) {

			int tableHeight = myTable.getRows()+1;
			int tableWidth = myTable.getColumns();

			myTable.resize(tableWidth,tableHeight);

			Table formTable = new Table(3,1);

			Link newLink = new Link(new Image("/pics/jmodules/quote/create.gif","Nýtt orð dagsins",10,10),adminURL);
				newLink.addParameter("new","true");
			Link updateLink = new Link(new Image("/pics/jmodules/quote/update.gif","Breyta orði dagsins",10,10),adminURL);
				updateLink.addParameter("change","true");
				updateLink.addParameter("quote_id",quote_id);
			Link deleteLink = new Link(new Image("/pics/jmodules/quote/delete.gif","Eyða orði dagsins",10,10),adminURL);
				deleteLink.addParameter("delete","true");
				deleteLink.addParameter("quote_id",quote_id);

			formTable.add(newLink,1,1);
			formTable.add(updateLink,2,1);
			formTable.add(deleteLink,3,1);

			myTable.add(formTable,1,tableHeight);

		}

		outerTable = new Table(1,1);
			outerTable.setBorder(0);
			outerTable.setCellpadding(quotePadding);
			outerTable.setWidth("100%");
			outerTable.setHeight(quoteHeight);
			outerTable.setCellspacing(quoteBorder);
			outerTable.setColor(quoteBorderColor);
			outerTable.setAlignment(1,1,"center");
			outerTable.setVerticalAlignment(1,1,"middle");

		outerTable.add(myTable,1,1);

	}

	private void drawContentTable() {

		contentTable = new Table(1,2);
                    if ( isClean ) {
                      contentTable.resize(1,1);
                    }
			contentTable.setWidth(quoteWidth);
			contentTable.setCellpadding(0);
			contentTable.setCellspacing(0);

		Table headerTable = new Table(3,1);
			headerTable.setCellpadding(0);
			headerTable.setCellspacing(0);
			headerTable.setColor(quoteBorderColor);
			headerTable.setWidth(1,"17");
			headerTable.setWidth(3,"17");
			headerTable.setWidth("100%");
			headerTable.setHeight(String.valueOf(headerSize));
			headerTable.setVerticalAlignment(1,1,"top");
			headerTable.setVerticalAlignment(2,1,"middle");
			headerTable.setVerticalAlignment(3,1,"top");
			headerTable.setAlignment(1,1,"left");
			headerTable.setAlignment(2,1,"center");
			headerTable.setAlignment(3,1,"right");

			if ( leftHeader ) {
				headerTable.add(new Image("/pics/jmodules/boxoffice/leftcorner.gif","",17,17),1,1);
			}

			Text header = new Text("Orð dagsins");

			if ( headerLeft ) {
				header = new Text("&nbsp;Orð dagsins");
				headerTable.empty();
				headerTable.mergeCells(1,1,2,1);
				headerTable.setWidth(1,"100%");
				headerTable.setVerticalAlignment(1,1,"middle");
				headerTable.add(header,1,1);
			}

			else {
				headerTable.add(header,2,1);
			}

				header.setBold();
				header.setFontColor(headlineColor);
				header.setFontSize(headlineSize);
				header.setFontFace(headerFontFace);


			if ( rightHeader ) {
				headerTable.add(new Image("/pics/jmodules/boxoffice/rightcorner.gif","",17,17),3,1);
			}

		if ( isClean ) {
                  contentTable.add(myTable,1,1);
		}
                else {
                  contentTable.add(headerTable,1,1);
                  contentTable.add(outerTable,1,2);
                }

	}

	public void setAdminURL(String adminURL){
		this.adminURL=adminURL;
	}

	public void setAdmin(boolean isAdmin){
		this.isAdmin=isAdmin;
	}

	public void setQuoteBorder(int quoteBorder){
		this.quoteBorder=quoteBorder;
	}

	public void setQuoteBorderColor(String quoteBorderColor){
		this.quoteBorderColor=quoteBorderColor;
	}

	public void setQuoteColor(String quoteColor){
		this.quoteColor=quoteColor;
	}

	public void setQuoteBackgroundImage(Image quoteBackgroundImage){
		this.quoteBackgroundImage=quoteBackgroundImage;
	}

	public void setQuoteWidth(String quoteWidth){
		this.quoteWidth=quoteWidth;
	}

	public void setQuoteHeight(String quoteHeight){
		this.quoteHeight=quoteHeight;
	}

	public void setQuotePadding(int quotePadding){
		this.quotePadding=quotePadding;
	}

	public void setTextColor(String textColor){
		this.originColor=textColor;
		this.quoteTextColor=textColor;
		this.authorColor=textColor;
	}

	public void setOriginColor(String originColor){
		this.originColor=originColor;
	}

	public void setQuoteTextColor(String quoteTextColor){
		this.quoteTextColor=quoteTextColor;
	}

	public void setQuoteTextSize(int quoteTextSize){
		this.quoteTextSize=quoteTextSize;
	}

	public void setAuthorTextSize(int authorTextSize){
		this.authorTextSize=authorTextSize;
	}
	public void setOriginTextSize(int originTextSize){
		this.originTextSize=originTextSize;
	}

	public void setHeadlineColor(String headlineColor){
		this.headlineColor=headlineColor;
	}

	public void setHeadlineSize(int headlineSize){
		this.headlineSize=headlineSize;
	}

	public void setAuthorColor(String authorColor){
		this.authorColor=authorColor;
	}

	public void setShowOrigin(boolean showOrigin){
		this.showOrigin=showOrigin;
	}

	/**
         * Sets a rounded corner on the left side of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setLeftHeader(boolean leftHeader){
		this.leftHeader=leftHeader;
	}

	/**
         * Sets a rounded corner on the right side of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setRightHeader(boolean rightHeader){
		this.rightHeader=rightHeader;
	}

        public void setHeadlineLeft(){
		this.headerLeft=true;

	}

        public void setHeaderSize(int headerSize){
		this.headerSize=headerSize;

	}

        public void setHeaderFontFace(String headerFontFace){
		this.headerFontFace=headerFontFace;

	}

        public void setClean() {
          this.isClean = true;
        }
}
