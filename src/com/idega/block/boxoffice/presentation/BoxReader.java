package com.idega.block.boxoffice.presentation;

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
import com.idega.block.boxoffice.data.*;
import com.idega.block.news.data.*;
import com.idega.block.news.presentation.*;
import com.idega.data.*;
import com.idega.projects.lv.templates.*;
import com.idega.util.text.*;
import com.idega.jmodule.projectmanager.data.*;

public class BoxReader extends JModuleObject{

private String issue_id = null;
private boolean isAdmin=false;
private	int numberOfColumns = 3;
private int numberOfDisplayed = 4;
private int numberOfLettersAdmin = 15;
private int numberOfLetters = 18;
private String boxTableColor = "FFFFFF";
private int boxWidth = 177;
private int boxHeight = 153;
private int boxBorder = 0;
private int leftBoxWidth = 17;
private int rightBoxWidth = 20;
private int topBoxHeight = 15;
private int innerBoxBorder = 0;
private String issueColor = "FFFFFF";
private String issueTextColor = "000000";
private int issueTextSize = 4;
private String subjectHeadlineColor = "#000000";
private int subjectHeadlineSize = 3;
private String subjectHeadlineBgColor = "#FFFFFF";
private String subjectTextColor = "#000000";
private int subjectTextSize = 2;
private String subjectTextBgColor = "#FFFFFF";
private String categoryHeadlineColor = "#000000";
private String categoryTextColor = "#000000";
private int categoryHeadlineSize = 3;
private int categoryTextSize = 1;
private boolean mergeBox = false;
private boolean includeBackgroundImage = true;
private Image headerImage = new Image("/pics/jmodules/boxoffice/spot.gif","",boxWidth,topBoxHeight);
private int boxOutline = 0;
private String outlineColor = "";
private String inBoxColor = "";
private String tableAlignment = "center";
private int boxPadding = 0;
private boolean boxOnly = false;
private int boxSpacing = 0;
private boolean showCategoryHeadline = false;
private String boxCategoryHeadlineColor = "#000000";
private int boxCategoryHeadlineSize = 3;
private boolean leftHeader = true;
private boolean rightHeader = true;
private String boxCategoryHeadlineFont = "Verdana, Arial, Helvetica, sans-serif";
private String boxURL = "/boxoffice/index.jsp";
private String adminURL = "/boxoffice/boxadmin.jsp";
private boolean headerLeft = false;
private boolean usePopUp = true;
private int headerSize = 21;

private	int tableWidth = 0;
private Issues[] issues;
private IssuesIssuesCategory[] categories;
private IssuesCategory issues_category;
private Subject[] subject;
private	Table myTable;
private int width = 3;
private	Subject subject2;

private String language = "IS";
private String[] Lang = {"Issues in this database:", "No issues in database", "Breyta skjali","Eyða skjali","Engin fyrirsögn","Bæta við","...nánar","Tegund","Nafn skjals","Höfundur","Dagsetning"};;
private String[] IS = {"Issues in this database:", "No issues in database", "Breyta skjali","Eyða skjali","Engin fyrirsögn","Bæta við","...nánar","Tegund","Nafn skjals","Höfundur","Dagsetning"};
private String[] EN = {"Issues in this database:", "No issues in database", "Change Document","Delete Document","No Title","Add Document","...more","Type","Document name","Author","Date"};

public BoxReader(){
}

public BoxReader(String issue_id){
        this();
	this.issue_id=issue_id;
}


/**
 * @deprecated replaced with the default constructor
 */
public BoxReader(boolean isAdmin){
        this();
	//this.isAdmin=isAdmin;
}

public BoxReader(String issue_id, boolean isAdmin){
	this.issue_id=issue_id;
	this.isAdmin=isAdmin;
}

public BoxReader(String issue_id, boolean isAdmin, int numberOfColumns){
	this.issue_id=issue_id;
	this.isAdmin=isAdmin;
	this.numberOfColumns=numberOfColumns;
}

public BoxReader(String issue_id, int numberOfColumns){
	this.issue_id=issue_id;
	this.numberOfColumns=numberOfColumns;
}

private void setSpokenLanguage(ModuleInfo modinfo){
	String language2 = modinfo.getRequest().getParameter("language");
    if (language2==null) language2 = ( String ) modinfo.getSession().getAttribute("language");
    if ( language2 != null) language = language2;
}


	public void main(ModuleInfo modinfo) throws Exception {
            this.empty();

          String temp_issue_id = modinfo.getParameter("issue_id");
          if (temp_issue_id != null) {
            this.issue_id = temp_issue_id;
          }
          this.isAdmin=isAdministrator(modinfo);

	  setSpokenLanguage(modinfo);

        HttpSession Session = modinfo.getSession();
			if ( !boxOnly ) {
				Session.removeAttribute("file_id");
			}

		if ( issue_id == null ) {
			issues = (Issues[]) (new Issues()).findAll();

			if ( isAdmin ) {

				Form editForm = new Form(adminURL,"get");
					editForm.add(new SubmitButton(new Image("/pics/jmodules/boxoffice/"+language+"/boxeditor.gif")));

				add(editForm);

			}

			if ( issues.length > 0 ) {
				// Ná í fjölda flokka undir þessum málaflokki;
				categories = (IssuesIssuesCategory[]) (new IssuesIssuesCategory()).findAllByColumnOrdered("issue_id",String.valueOf(issues[0].getID()),"issue_category_id");

				// Búa til töflu
				createBoxTable(numberOfColumns);
			}

			else {
				myTable = new Table(1,1);

				myTable.addText("No issues in database");
			}

			// Setja töflu í síðu
			add(myTable);

		}

		else {

			String issue_category_id = modinfo.getRequest().getParameter("issue_category_id");
			String subject_id = modinfo.getRequest().getParameter("subject_id");

			if ( isAdmin ) {

				Form editForm = new Form(adminURL);
					editForm.add(new SubmitButton(new Image("/pics/jmodules/boxoffice/"+language+"/boxeditor.gif")));
					editForm.add(new HiddenInput("issue_id",issue_id));

				add(editForm);

			}

			if ( boxOnly ) {

				categories = (IssuesIssuesCategory[]) (new IssuesIssuesCategory()).findAllByColumnOrdered("issue_id",issue_id,"issue_category_id");

				// Búa til töflu
				createBoxTable(numberOfColumns);

				// Setja töflu í síðu
				add(myTable);

			}

			else {

				if ( issue_category_id != null && subject_id == null) {

					issues_category = new IssuesCategory(Integer.parseInt(issue_category_id));

					subject = (Subject[]) (new Subject()).findAllByColumnOrdered("issue_id",issue_id,"issue_category_id",issue_category_id,"subject_date desc");

					createCategoryTable(issue_category_id);

					add(myTable);

				}

				else if ( subject_id != null && issue_category_id == null) {

					subject2 = new Subject(Integer.parseInt(subject_id));

					createSubjectTable();

					add(myTable);

				}

				else if ( subject_id == null && issue_category_id == null ) {
					// Ná í fjölda flokka undir þessum málaflokki;
					categories = (IssuesIssuesCategory[]) (new IssuesIssuesCategory()).findAllByColumnOrdered("issue_id",issue_id,"issue_category_id");

					// Búa til töflu
					createBoxTable(numberOfColumns);

					// Setja töflu í síðu
					add(myTable);
				}
			}

		}

	}

	private void listIssues() throws IOException,SQLException {

		myTable = new Table(2,2);
			myTable.setWidth("100%");
			myTable.mergeCells(1,1,2,1);
			myTable.setAlignment("left");

		Text malaflokkar = new Text(Lang[0]);
			malaflokkar.setBold();
			malaflokkar.setFontSize(3);

		myTable.add(malaflokkar,1,1);

		int b = 2;

		for ( int a = 0; a < issues.length; a++ ) {

			myTable.resize(2,b);

			Text issue_text = new Text(issues[a].getName());
				issue_text.setBold();

			Link issue_link = new Link(issue_text,boxURL);
				issue_link.addParameter("issue_id",String.valueOf(issues[a].getID()));

			myTable.addText(issues[a].getID()+": ",1,b);
			myTable.add(issue_link,2,b);

			b++;
		}

		if ( issues.length == 0 ) {

			myTable.mergeCells(1,2,2,2);

			myTable.addText(Lang[1],1,2);

		}

	}

	public Table getHeadlineTable() throws IOException,SQLException {

		Table headlineTable = new Table(1,1);
			headlineTable.setWidth("100%");
			headlineTable.setCellpadding(9);
			headlineTable.setCellspacing(9);

		Table innerTable = new Table(1,1);
			innerTable.setWidth("100%");
			innerTable.setCellpadding(2);
			innerTable.setCellspacing(2);
			innerTable.setAlignment("center");
			innerTable.setColor(issueColor);
			innerTable.setAlignment(1,1,"center");


		Text issueHeadline = new Text(getIssueName());
			issueHeadline.setFontSize(issueTextSize);
			issueHeadline.setFontColor(issueTextColor);
			issueHeadline.setBold();

			innerTable.add(issueHeadline,1,1);
			headlineTable.add(innerTable,1,1);

		return headlineTable;

	}

	public Image getIssueImage() throws IOException,SQLException {

		String iconURL = "/pics/jmodules/boxoffice/"+language+"/";
		switch (getIssueID())  {
			case 1 : iconURL += "iconUmhverfismal.jpg";
			break;

			case 2 : iconURL += "iconOryggismal.jpg";
			break;

			case 3 : iconURL += "iconGaedamal.jpg";
			break;

			case 4 : iconURL += "iconVidhaldsmal.jpg";
			break;

			case 5 : iconURL += "iconTaeknilegarUppl.jpg";
			break;

			case 6 : iconURL += "iconMyndir.jpg";
			break;

			case 7 : iconURL += "iconOrkukerfid.jpg";
			break;

			case 8 : iconURL += "iconTruflanir.jpg";
			break;

		}

		Image image = new Image(iconURL,getIssueName(),240,240);

		return image;
	}

	public int getIssueID() throws IOException {

		return Integer.parseInt(issue_id);

	}

	public String getIssueName() throws IOException,SQLException {

		Issues issues = new Issues(Integer.parseInt(issue_id));

		return issues.getIssueName();

	}

	private Table createBoxTable(int width1) throws IOException,SQLException {

		tableWidth = width1;

		int tableHeight = 0;

		if ( tableWidth > categories.length ) {
			tableWidth = categories.length;
		}

		int divide = 1;
		int modulus = 1;

		if ( categories.length != 0 ) {
			divide = categories.length / tableWidth;
			modulus = categories.length % tableWidth;
		}

		if ( modulus == 0 ) {
			tableHeight = divide;
		}

		else {
			tableHeight = divide + 1;
		}

		myTable = new Table(tableWidth,tableHeight);

		myTable.setAlignment(tableAlignment);
		myTable.setBorder(boxBorder);
		myTable.setCellpadding(0);
		myTable.setCellspacing(boxSpacing);
		myTable.setColor(boxTableColor);

		int height = myTable.getRows();
		int width = myTable.getColumns();

		for ( int a = 1; a <= height; a++ ) {
			myTable.setRowAlignment(a,"left");
			myTable.setRowVerticalAlignment(a,"top");
			myTable.setHeight(a,String.valueOf(boxHeight));
		}

		for ( int a = 1; a <= width; a++ ) {
			myTable.setWidth(a,String.valueOf(boxWidth));
		}

		getSubjects();

		return myTable;

	}

	private void createCategoryTable(String issue_category_id) throws IOException,SQLException {

		myTable = new Table(1,3);
                        myTable.setWidth("100%");
			myTable.setAlignment("left");
			myTable.setBorder(0);
			myTable.setCellpadding(1);
			myTable.setCellspacing(0);
                        myTable.setRowAlignment(1,"center");
			myTable.setRowVerticalAlignment(1,"top");
			myTable.setRowVerticalAlignment(2,"top");
			myTable.setColor(1,1,subjectHeadlineBgColor);
			myTable.setColor(1,2,subjectHeadlineBgColor);

		getCategories(issue_category_id);
	}

	private void createSubjectTable() throws IOException,SQLException {

		myTable = new Table(1,3);
			myTable.setWidth("100%");
			myTable.setAlignment("left");
                        myTable.setAlignment(1,1,"center");
			myTable.setBorder(0);
			myTable.setCellpadding(1);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,subjectHeadlineBgColor);
                        myTable.setRowColor(2,subjectHeadlineBgColor);
			myTable.setColor("FFFFFF");

		insertSubject();
	}

	private void getCategories(String issue_category_id) throws SQLException {

		Table categoryTable = new Table();
                        categoryTable.setWidth("100%");
			categoryTable.setAlignment("left");
			categoryTable.setBorder(0);
			categoryTable.setCellpadding(3);
			categoryTable.setCellspacing(3);
			categoryTable.setColor(subjectTextBgColor);

                Text category_text = new Text(issues_category.getCategoryName());
			category_text.setFontSize(categoryHeadlineSize);
			category_text.setFontColor(categoryHeadlineColor);
			category_text.setBold();

                Text subjectContentText = new Text(Lang[7]);
                  subjectContentText.setFontSize(1);
                  subjectContentText.setFontColor(categoryTextColor);
                Text subjectNameText = new Text(Lang[8]);
                  subjectNameText.setFontSize(1);
                  subjectNameText.setFontColor(categoryTextColor);
                Text subjectAuthorText = new Text(Lang[9]);
                  subjectAuthorText.setFontSize(1);
                  subjectAuthorText.setFontColor(categoryTextColor);
                Text subjectDateText = new Text(Lang[10]);
                  subjectDateText.setFontSize(1);
                  subjectDateText.setFontColor(categoryTextColor);

		Image back_image = new Image("/pics/jmodules/boxoffice/"+language+"/back.gif");

		for ( int a = 0; a < subject.length; a++ ) {

			categoryTable.setRowColor(a+2,subjectTextBgColor);
			categoryTable.setAlignment(1,a+2,"center");
			categoryTable.setWidth(2,a+2,"100%");

			Image content_image = new Image(subject[a].getContentImage());

			Text content_text = new Text(subject[a].getSubjectName());
				content_text.setFontSize(categoryTextSize);
				content_text.setFontColor(categoryTextColor);

                        Text author_text = new Text("Ókunnur");
                        if ( subject[a].getSubjectAuthor() != null ) {
                            author_text = new Text(subject[a].getSubjectAuthor());
                        }
                          author_text.setFontSize(categoryTextSize);
                          author_text.setFontColor(categoryTextColor);

                        idegaTimestamp stampurinn = new idegaTimestamp(subject[a].getSubjectDate());

                        Text date_text = new Text(stampurinn.getDate()+"/"+stampurinn.getMonth()+"/"+stampurinn.getYear());
                            date_text.setFontSize(categoryTextSize);
                            date_text.setFontColor(categoryTextColor);

			Link subject_link = new Link(content_text);

			String subjectValue = subject[a].getSubjectValue();

				if (subjectValue!=null){

					if ( (subject[a].getSubjectValue().indexOf("http")!=-1) && ( subject[a].getContentName().equalsIgnoreCase("Tengill") || subject[a].getContentName().equalsIgnoreCase("Link") || subject[a].getContentName().equalsIgnoreCase("Tenglar") || subject[a].getContentName().equalsIgnoreCase("Links") ) ) {
						subject_link.setURL(subjectValue);
						subject_link.setTarget("_new");
					}

					else if ( (subject[a].getIncludeImage().equals("Y")) )  {
						subject_link.setURL("/servlet/FileModule");
						subject_link.addParameter("file_id",subject[a].getImageId());
						subject_link.setTarget("_new");
					}

					else {
						subject_link.setURL(boxURL);
						subject_link.addParameter("issue_id",issue_id);
						subject_link.addParameter("subject_id",String.valueOf(subject[a].getID()));
									}
				}

				else if ( subjectValue == null ) {

					if ( (subject[a].getIncludeImage().equals("Y")) )  {
						subject_link.setURL("/servlet/FileModule");
						subject_link.addParameter("file_id",subject[a].getImageId());
						subject_link.setTarget("_new");
					}

					else {
						subject_link.setURL(boxURL);
						subject_link.addParameter("issue_id",issue_id);
						subject_link.addParameter("subject_id",String.valueOf(subject[a].getID()));
					}
				}

			categoryTable.add(content_image,1,a+2);
			categoryTable.add(subject_link,2,a+2);
                        categoryTable.add(author_text,3,a+2);
                        categoryTable.add(date_text,4,a+2);

			if ( isAdmin ) {
				Image changeImage = new Image("/pics/jmodules/boxoffice/"+language+"/change.gif",Lang[2]);
				Image deleteImage = new Image("/pics/jmodules/boxoffice/"+language+"/delete.gif",Lang[3]);

				Link change = new Link(changeImage,adminURL);
					change.addParameter("subject_id",String.valueOf(subject[a].getID()));
					change.addParameter("mode","update");

				Link delete = new Link(deleteImage,adminURL);
					delete.addParameter("subject_id",String.valueOf(subject[a].getID()));
					delete.addParameter("mode","delete");

				categoryTable.add(change,5,a+2);
				categoryTable.add(delete,6,a+2);
			}
		}

		if ( isAdmin ) {
			Image newImage = new Image("/pics/jmodules/boxoffice/"+language+"/new.gif",Lang[5]);
				newImage.setAttribute("align","left");

			Link adminLink = new Link(newImage,adminURL);
				adminLink.addParameter("issue_id",issue_id);
				adminLink.addParameter("issue_category_id",issue_category_id);

                        categoryTable.add(adminLink,1,categoryTable.getRows()+1);
			categoryTable.mergeCells(1,categoryTable.getRows(),categoryTable.getColumns(),categoryTable.getRows());
		}

                categoryTable.add(subjectContentText,1,1);
                categoryTable.add(subjectNameText,2,1);
                categoryTable.add(subjectAuthorText,3,1);
                categoryTable.add(subjectDateText,4,1);

		myTable.add(category_text,1,1);
                myTable.add(categoryTable,1,2);
                myTable.add(new BackButton(back_image),1,3);

	}

	private void insertSubject() throws IOException,SQLException {

		Table myTable2 = new Table();
                    myTable2.setCellpadding(3);
                    myTable2.setCellspacing(3);
                    myTable2.setWidth("100%");
                    myTable2.setColor(subjectTextBgColor);

                String subject_name = subject2.getSubjectName();
		String subject_value = formatSubject(subject2.getSubjectValue());

		Text subject_headline = new Text(subject_name);
			subject_headline.setFontSize(subjectHeadlineSize);
			subject_headline.setFontColor(subjectHeadlineColor);
			subject_headline.setBold();

		Text subject_text = new Text(subject_value);
			subject_text.setFontSize(subjectTextSize);
			subject_text.setFontColor(subjectTextColor);

		myTable2.add(subject_text,1,1);

		if ( isAdmin == true ) {

                         myTable2.setAlignment(2,2,"right");
                         myTable2.mergeCells(1,1,2,1);

			Image breytaImage = new Image("/pics/jmodules/boxoffice/"+language+"/change.gif",Lang[2]);
			Image deleteImage = new Image("/pics/jmodules/boxoffice/"+language+"/delete.gif",Lang[3]);

                        Link breyta = new Link(breytaImage,adminURL);
				breyta.addParameter("mode","update");
				breyta.addParameter("subject_id",String.valueOf(subject2.getID()));

			Link delete = new Link(deleteImage,adminURL);
				delete.addParameter("mode","delete");
				delete.addParameter("subject_id",String.valueOf(subject2.getID()));

			myTable2.add(breyta,1,2);
			myTable2.add(delete,2,2);

		}

		Image back_image = new Image("/pics/jmodules/boxoffice/"+language+"/back.gif");

		myTable.add(subject_headline,1,1);
                myTable.add(myTable2,1,2);
		myTable.add(new BackButton(back_image),1,3);

	}

	private void getSubjects() throws IOException,SQLException {


		for ( int a = 0; a < categories.length; a++ ) {

			int height = (a+tableWidth) / tableWidth;
			int width = a+1;

			if ( a+1 > tableWidth ) {

				width = a+1 - ( (height-1) * tableWidth);

			}

			String issue_category_id = String.valueOf(categories[a].getIssueCategoryId());

			Table categoryTable = new Table(1,2);
				categoryTable.setBorder(0);
				categoryTable.setCellpadding(0);
				categoryTable.setCellspacing(0);
				if ( outlineColor != "" ) {
					categoryTable.setColor(outlineColor);
				}

			if ( includeBackgroundImage ) {
				categoryTable.setWidth(String.valueOf(boxWidth));
				categoryTable.setHeight(1,1,String.valueOf(topBoxHeight));
				categoryTable.setHeight(String.valueOf(boxHeight));
				categoryTable.setBackgroundImage(new Image(categories[a].getIssueCategoryImage()));
			}

			if ( showCategoryHeadline ) {

				Table headlineTable = new Table(3,1);
					headlineTable.setCellpadding(0);
					headlineTable.setCellspacing(0);
					headlineTable.setVerticalAlignment(1,1,"top");
					headlineTable.setVerticalAlignment(2,1,"middle");
					headlineTable.setVerticalAlignment(3,1,"top");
					headlineTable.setAlignment(1,1,"left");
					headlineTable.setAlignment(2,1,"center");
					headlineTable.setAlignment(3,1,"right");
					headlineTable.setWidth(String.valueOf(boxWidth));
					headlineTable.setWidth(1,1,"17");
					headlineTable.setWidth(3,1,"17");
					headlineTable.setHeight(String.valueOf(headerSize));
					headlineTable.setHeight(String.valueOf(topBoxHeight));
					headlineTable.setColor(outlineColor);

				Text categoryHeadlineText = new Text(categories[a].getIssueCategoryName());
					categoryHeadlineText.setFontColor(boxCategoryHeadlineColor);
					categoryHeadlineText.setFontSize(boxCategoryHeadlineSize);
					categoryHeadlineText.setFontFace(boxCategoryHeadlineFont);
					categoryHeadlineText.setBold();

				if ( leftHeader ) {
					headlineTable.add(new Image("/pics/jmodules/boxoffice/leftcorner.gif","",17,17),1,1);
				}

				if ( headerLeft ) {
					Text spacer = new Text("&nbsp;");
						spacer.setFontSize(boxCategoryHeadlineSize);

					headlineTable.empty();
					headlineTable.mergeCells(1,1,2,1);
					headlineTable.setWidth(1,"100%");
					headlineTable.setVerticalAlignment(1,1,"middle");
					headlineTable.add(spacer,1,1);
					headlineTable.add(categoryHeadlineText,1,1);
				}

				else {
					headlineTable.add(categoryHeadlineText,2,1);
				}

				if ( rightHeader ) {
					headlineTable.add(new Image("/pics/jmodules/boxoffice/rightcorner.gif","",17,17),3,1);
				}

				categoryTable.add(headlineTable,1,1);
			}

			categoryTable.add(getSubject(issue_category_id),1,2);
			myTable.add(categoryTable,width,height);
		}

	}

	private Table getSubject(String issue_category_id) throws IOException,SQLException {

		Table outlineTable = new Table(1,1);
			outlineTable.setWidth("100%");
                        outlineTable.setHeight("100%");
			outlineTable.setCellspacing(boxOutline);
			if ( outlineColor != "" ) {
				outlineTable.setColor(outlineColor);
			}
			if ( inBoxColor != "" ) {
				outlineTable.setColor(1,1,inBoxColor);
			}

		Table innerTable = new Table(5,(numberOfDisplayed+1));
			innerTable.setWidth("100%");
			innerTable.setCellpadding(boxPadding);
			innerTable.setCellspacing(boxPadding);
			innerTable.setBorder(innerBoxBorder);
			innerTable.setColor(inBoxColor);
			innerTable.mergeCells(2,numberOfDisplayed+1,4,numberOfDisplayed+1);
			innerTable.setAlignment(2,numberOfDisplayed+1,"right");
			innerTable.setBackgroundImage(new Image(""));
			innerTable.setWidth(3,2,"100%");
			innerTable.add(new Image("/pics/jmodules/boxoffice/"+language+"/spot.gif","",leftBoxWidth,1),1,2);
			innerTable.add(new Image("/pics/jmodules/boxoffice/"+language+"/spot.gif","",rightBoxWidth,1),5,2);

		Subject[] subject = (Subject[]) (new Subject()).findAllByColumnOrdered("issue_id",issue_id,"issue_category_id",issue_category_id,"subject_date desc");
		IssuesCategory issueCategory = new IssuesCategory(Integer.parseInt(issue_category_id));
		String issueName = issueCategory.getName();

		int b = 0;

		for ( int a = 0; a < subject.length; a++ ) {

			b++;

			int subject_id = subject[a].getID();

			String subject_text = Lang[4];

			if ( subject[a].getSubjectName() != null ) {
				subject_text = subject[a].getSubjectName();
			}

				if ( isAdmin ) {
					if ( subject_text.length() > numberOfLettersAdmin ) {
						subject_text = subject_text.substring(0,numberOfLettersAdmin)+"...";
					}
				}
				else {
					if ( subject_text.length() > numberOfLetters ) {
						subject_text = subject_text.substring(0,numberOfLetters)+"...";
					}
				}

			Text subject_name = new Text(subject_text);
				subject_name.setFontSize(1);

			Link subject_link = new Link(subject_name);

			String subjectValue = subject[a].getSubjectValue();
			if (subjectValue!=null){

				if ( (subject[a].getSubjectValue().indexOf("http")!=-1) && ( subject[a].getContentName().equals("Tengill") || subject[a].getContentName().equals("Link") ) ) {
					subject_link.setURL(subjectValue);
					if ( usePopUp ) {
						subject_link.setTarget("_new");
					}
				}

				else if ( (subject[a].getIncludeImage().equals("Y")) )  {
					subject_link.setSessionId(false);
                                        subject_link.setURL("/servlet/FileModule?file_id="+subject[a].getImageId());
		                        //subject_link.addParameter("file_id",subject[a].getImageId());
					if ( usePopUp ) {
						subject_link.setTarget("_new");
					}
				}

				else {
					subject_link.setURL(boxURL);
					subject_link.addParameter("issue_id",issue_id);
					subject_link.addParameter("subject_id",String.valueOf(subject_id));
                                }
			}

			else if ( subjectValue == null ) {

				if ( (subject[a].getIncludeImage().equals("Y")) )  {
					subject_link.setSessionId(false);
                                        subject_link.setURL("/servlet/FileModule?file_id="+subject[a].getImageId());
					//subject_link.addParameter("file_id",subject[a].getImageId());
					if ( usePopUp ) {
						subject_link.setTarget("_new");
					}
				}

				else {
					subject_link.setURL(boxURL);
					subject_link.addParameter("issue_id",issue_id);
					subject_link.addParameter("subject_id",String.valueOf(subject_id));
				}
			}

			Image content_image = new Image(subject[a].getContentImage());
				content_image.setHeight(16);
				content_image.setWidth(16);
				content_image.setAttribute("alt",subject[a].getSubjectName());

			Image changeImage = new Image("/pics/jmodules/boxoffice/"+language+"/updatesmall.gif",Lang[2],10,10);
			Image deleteImage = new Image("/pics/jmodules/boxoffice/"+language+"/deletesmall.gif",Lang[3],10,10);

			Link change = new Link(changeImage,adminURL);
				change.addParameter("subject_id",String.valueOf(subject_id));
				change.addParameter("mode","update");

			Link delete = new Link(deleteImage,adminURL);
				delete.addParameter("subject_id",String.valueOf(subject_id));
				delete.addParameter("mode","delete");

			if ( b <= numberOfDisplayed ) {

				if ( isAdmin ) {
					innerTable.add(new Image("/pics/jmodules/boxoffice/"+language+"/arrow.gif",subject[a].getSubjectName(),8,9),2,b);
					innerTable.add(subject_link,3,b);
					innerTable.addBreak(3,b);
					innerTable.add(change,4,b);
					innerTable.addText("&nbsp;",4,b);
					innerTable.add(delete,4,b);
				}

				else {
					if ( mergeBox ) {
						innerTable.mergeCells(2,b,4,b);
						innerTable.add(subject_link,2,b);
						innerTable.addBreak(2,b);
					}

					else {
						innerTable.add(new Image("/pics/jmodules/boxoffice/"+language+"/arrow.gif",subject[a].getSubjectName(),8,9),2,b);
						innerTable.add(subject_link,3,b);
						innerTable.addBreak(3,b);
						innerTable.add(content_image,4,b);
					}
				}
			}

		}

		Text more_text = new Text(Lang[6]);
			more_text.setFontSize(1);

		Link more = new Link(more_text,boxURL);
			more.addParameter("issue_id",issue_id);
			more.addParameter("issue_category_id",issue_category_id);

		if ( isAdmin == true ) {

			Image newImage = new Image("/pics/jmodules/boxoffice/"+language+"/add.gif",Lang[5],10,10);
				newImage.setAttribute("align","left");

			Link adminLink = new Link(newImage,adminURL);
				adminLink.addParameter("issue_id",issue_id);
				adminLink.addParameter("issue_category_id",issue_category_id);

			if ( Integer.parseInt(issue_category_id) != 4 && Integer.parseInt(issue_category_id) != 3 ) {
				innerTable.add(adminLink,2,numberOfDisplayed+1);
			}

		}

		if ( subject.length > 0 ) {
			innerTable.add(more,2,numberOfDisplayed+1);
		}

		outlineTable.add(innerTable);

		return outlineTable;

	}

	private String formatSubject(String subject_value) {

		subject_value = TextSoap.findAndReplace(subject_value,"\n","<br>");
		subject_value = TextSoap.findAndReplace(subject_value,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		return subject_value;
	}

        /**
         * Sets the number of subjects to be displayed within each category box.
         */

        public void setNumberOfDisplayed(int numberOfDisplayed){
		this.numberOfDisplayed=numberOfDisplayed;
	}

	/**
         * Sets the number of boxes displayed in each row of the box table.
         */

        public void setNumberOfColumns(int numberOfColumns){
		this.numberOfColumns=numberOfColumns;
	}

	/**
         * Sets the number of letters displayed for each subject within the category boxes.
         */

        public void setNumberOfLetters(int numberOfLetters){
		this.numberOfLetters=numberOfLetters;
	}

	/**
         * Sets the width of the category boxes.
         */

        public void setBoxWidth(int boxWidth){
		this.boxWidth=boxWidth;
	}

	/**
         * Sets the height of the category boxes.
         */

        public void setBoxHeight(int boxHeight){
		if ( !showCategoryHeadline ) {
			this.boxHeight=boxHeight;
		}
	}

	/**
         * Sets true/false to display border on the boxes.
         */

        public void setBoxBorder(int boxBorder){
		this.boxBorder=boxBorder;
	}

	/**
         * Sets true/false to display border inside each category box.
         */

        public void setInnerBoxBorder(int innerBoxBorder){
		this.innerBoxBorder=innerBoxBorder;
	}

	/**
         * Sets the left margin within the category boxes.
         */

        public void setLeftBoxWidth(int leftBoxWidth){
		this.leftBoxWidth=leftBoxWidth;
	}

	/**
         * Sets the right margin within the category boxes.
         */

        public void setRightBoxWidth(int rightBoxWidth){
		this.rightBoxWidth=rightBoxWidth;
	}

	/**
         * Sets the top margin within the category boxes.
         */

        public void setTopBoxHeight(int topBoxHeight){
		this.topBoxHeight=topBoxHeight;
	}

	/**
         * Sets the background color of the issue headline.
         */

        public void setIssueColor(String issueColor){
		this.issueColor=issueColor;
	}

	/**
         * Sets the font color of the issue headline.
         */

        public void setIssueTextColor(String issueTextColor){
		this.issueTextColor=issueTextColor;
	}

	/**
         * Sets the font size of the issue headline.
         */

        public void setIssueTextSize(int issueTextSize){
		this.issueTextSize=issueTextSize;
	}

	/**
         * Sets the font color of the subject headline in subject view.
         */

        public void setSubjectHeadlineColor(String subjectHeadlineColor){
		this.subjectHeadlineColor=subjectHeadlineColor;
	}

	/**
         * Sets the font size of the subject headline in subject view.
         */

 	public void setSubjectHeadlineSize(int subjectHeadlineSize){
		this.subjectHeadlineSize=subjectHeadlineSize;
	}

	/**
         * Sets the background color of the subject headline in subject view.
         */

 	public void setSubjectHeadlineBgColor(String subjectHeadlineBgColor){
		this.subjectHeadlineBgColor=subjectHeadlineBgColor;
	}

	/**
         * Sets the font color of the subject text in subject view.
         */

 	public void setSubjectTextColor(String subjectTextColor){
		this.subjectTextColor=subjectTextColor;
	}

	/**
         * Sets the font size of the subject text in subject view.
         */

 	public void setSubjectTextSize(int subjectTextSize){
		this.subjectTextSize=subjectTextSize;
	}

	/**
         * Sets the background color of the subject text in subject view.
         */

 	public void setSubjectTextBgColor(String subjectTextBgColor){
		this.subjectTextBgColor=subjectTextBgColor;
	}

	/**
         * Sets the font size of the category text in category view.
         */

 	public void setCategoryTextSize(int categoryTextSize){
		this.categoryTextSize=categoryTextSize;
	}

	/**
         * Sets the font color of the category text in category view.
         */

 	public void setCategoryTextColor(String categoryTextColor){
		this.categoryTextColor=categoryTextColor;
	}

	/**
         * Sets the font size of the category headline in category view.
         */

 	public void setCategoryHeadlineSize(int categoryHeadlineSize){
		this.categoryHeadlineSize=categoryHeadlineSize;
	}

	/**
         * Sets the font color of the category headline in category view.
         */

 	public void setCategoryHeadlineColor(String categoryHeadlineColor){
		this.categoryHeadlineColor=categoryHeadlineColor;
	}

	/**
         * Sets true/false to display arrow and document icons in the category boxes.
         */

        public void setNoIcons(boolean mergeBox){
		this.mergeBox=mergeBox;
	}

	/**
         * Sets true/false to display background image in category boxes from database.
         */

        public void setIncludeBackgroundImage(boolean includeBackgroundImage){
		this.includeBackgroundImage=includeBackgroundImage;
		if ( includeBackgroundImage == false ) {
			this.showCategoryHeadline=true;
		}
	}

	/**
         * Sets the header image within the category boxes.
         */

        public void setHeaderImage(Image headerImage){
		this.headerImage=headerImage;
	}

	/**
         * Sets the outline of the subject boxes within the category boxes.
         */

        public void setBoxOutline(int boxOutline){
		this.boxOutline=boxOutline;
	}

	/**
         * Sets the outline color of the subject boxes within the category boxes.
         */

	public void setOutlineColor(String outlineColor){
		this.outlineColor=outlineColor;
	}

	/**
         * Sets the color of the subject boxes within the category boxes.
         */

        public void setInBoxColor(String inBoxColor){
		this.inBoxColor=inBoxColor;
	}

	/**
         * Sets the color of the category boxes.
         */

        public void setBoxTableColor(String boxTableColor){
		this.boxTableColor=boxTableColor;
	}

	/**
         * Sets the alignment of the category boxes on the page.
         */

        public void setTableAlignment(String tableAlignment){
		this.tableAlignment=tableAlignment;
	}

	/**
         * Sets the cellpadding within the subject boxes.
         */

        public void setBoxPadding(int boxPadding){
		this.boxPadding=boxPadding;
	}

	/**
         * Sets the cellspacing of the category boxes.
         */

        public void setBoxSpacing(int boxSpacing){
		this.boxSpacing=boxSpacing;
	}

	/**
         * Sets true/false to whether to display only the box view or the entire hoopla.
         */

        public void setBoxOnly(boolean boxOnly){
		this.boxOnly=boxOnly;
	}

	/**
         * Sets true/false to whether to display the headlines of each category box.
         */

        public void setShowCategoryHeadline(boolean showCategoryHeadline){
			this.showCategoryHeadline=showCategoryHeadline;
			this.boxHeight=1;


		if ( showCategoryHeadline == false ) {
			this.includeBackgroundImage=true;
		}
                else {
                        this.includeBackgroundImage=false;
                }
	}

	/**
         * Sets the color of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setBoxCategoryHeadlineColor(String boxCategoryHeadlineColor){
		this.boxCategoryHeadlineColor=boxCategoryHeadlineColor;
	}

	/**
         * Sets the size of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setBoxCategoryHeadlineSize(int boxCategoryHeadlineSize){
		this.boxCategoryHeadlineSize=boxCategoryHeadlineSize;
	}

	/**
         * Sets the font of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setBoxCategoryHeadlineFont(String boxCategoryHeadlineFont){
		this.boxCategoryHeadlineFont=boxCategoryHeadlineFont;
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

	/**
         * Sets rounded corners on the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setRoundedHeader(boolean roundedHeader){
		this.leftHeader=roundedHeader;
		this.rightHeader=roundedHeader;
	}

        public void setBoxURL(String boxURL){
		this.boxURL=boxURL;
	}

        public void setAdminURL(String adminURL){
		this.adminURL=adminURL;
	}

        public void setHeadlineLeft(){
		this.headerLeft=true;

	}

        public void setHeaderSize(int headerSize){
		this.headerSize=headerSize;

	}

        public void setUsePopUp(boolean usePopUp){
		this.usePopUp=usePopUp;

	}

}
