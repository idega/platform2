package com.idega.jmodule.boxoffice.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.boxoffice.data.*;
import com.idega.jmodule.file.data.*;
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class BoxEditor extends Block{

private boolean isAdmin = false;
private boolean update = false;
private boolean save = false;
private Table outerTable;
private String color = "#DDDDDD";
private String textColor = "#000000";
private String headlineColor = "#000000";
private String headlineBgColor = "#FFFFFF";
private boolean isGolf = false;
private int unionID = 1;
private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.boxoffice";
protected IWResourceBundle iwrb;
protected IWBundle iwb;

public BoxEditor(){
}

public BoxEditor(boolean isAdmin){
	this.isAdmin=isAdmin;
}

	public void main(IWContext iwc) throws IOException,SQLException {
    iwrb = getResourceBundle(iwc);
		if ( isAdmin ) {

			String mode = iwc.getRequest().getParameter("mode");
			String action = iwc.getRequest().getParameter("action");
				if ( action == null ) { action = "none"; }

			if ( mode == null ) {
				mode = "new";
				newSubject(iwc);
			}

			else if ( mode.equals("update") ) {
				update = true;
				newSubject(iwc);
			}

			else if ( mode.equals(iwrb.getLocalizedString("save","Save")) ) {
				save = true;

				if ( action.equals("update") ) { update = true; }

				saveSubject(iwc);
			}

			else if ( mode.equals("delete") ) {
				deleteSubject(iwc);
			}

			if ( action.equals("delete") ) {
				confirmDelete(iwc);
			}

			add(outerTable);

		}

		else {

			noAccess();
			add(outerTable);

		}

	}

	private void noAccess() throws IOException,SQLException {

		outerTable = new Table(1,2);

		Text noDice = new Text(iwrb.getLocalizedString("no_rights","You must log on first"));

		Form backForm = new Form("/index.jsp");
			backForm.add(new HiddenInput("issue_id","1"));
			backForm.add(new SubmitButton(iwrb.getLocalizedString("save","Save")));

		outerTable.add(noDice,1,1);
		outerTable.add(backForm,1,2);

	}

	private void newSubject(IWContext iwc) throws IOException,SQLException {

		HttpSession Session = iwc.getSession();

		int subject_id = -1;

		if ( update ) {
			String subject_id2 = iwc.getRequest().getParameter("subject_id");
				if ( subject_id2 == null ) { subject_id2 = "-1"; }

			subject_id = Integer.parseInt(subject_id2);
		}

		Subject subject = new Subject();

		if ( subject_id != -1 ) {
			subject = new Subject(subject_id);
		}

		outerTable = new Table(1,3);
			outerTable.setCellpadding(2);
			outerTable.setCellspacing(2);
			outerTable.setColor(color);

		Form myForm = new Form("/boxoffice/boxadmin.jsp");

		Table myTable = new Table(2,6);
			myTable.setBorder(0);
			myTable.setWidth("100%");
			myTable.setCellpadding(6);
			myTable.setCellspacing(6);
			myTable.mergeCells(1,2,1,6);
			myTable.setAlignment(2,6,"right");
			myTable.setVerticalAlignment(2,6,"bottom");

			TextInput subject_name = new TextInput("subject_name");
				subject_name.setLength(60);
				subject_name.setMaxlength(255);

                        TextInput subject_author = new TextInput("subject_author");
                                subject_author.setLength(25);
                                subject_author.setMaxlength(60);

			TextArea subject_value = new TextArea("subject_value",50,22);
				if ( update ) {
					if ( subject.getSubjectValue() != null ) {
						subject_value.setContent(subject.getSubjectValue());
					}
					if ( subject.getSubjectName() != null ) {
						subject_name.setValue(subject.getSubjectName());
					}
                                        if ( subject.getSubjectAuthor() != null ) {
                                                subject_author.setValue(subject.getSubjectName());
                                        }

					myForm.add(new HiddenInput("action","update"));
					myForm.add(new HiddenInput("subject_id",String.valueOf(subject_id)));
				}

			SubmitButton vista = new SubmitButton("mode",iwrb.getLocalizedString("save","Save"));



			Text content_text = new Text(iwrb.getLocalizedString("type","Document type"));
				content_text.setFontColor(textColor);
			Text issue_text = new Text(iwrb.getLocalizedString("issue","Issue"));
				issue_text.setFontColor(textColor);
			Text category_text = new Text(iwrb.getLocalizedString("category","Category"));
				category_text.setFontColor(textColor);
			Text name_text = new Text(iwrb.getLocalizedString("file_name","Filename"));
				name_text.setFontColor(textColor);
			Text value_text = new Text(iwrb.getLocalizedString("content","Content/Link"));
				value_text.setFontColor(textColor);
			Text author_text = new Text(iwrb.getLocalizedString("author","Author"));
				value_text.setFontColor(textColor);

			myTable.add(name_text,1,1);
			myTable.addBreak(1,1);
			myTable.add(subject_name,1,1);

			myTable.add(value_text,1,2);
			myTable.addBreak(1,2);
			myTable.add(subject_value,1,2);

			myTable.add(vista,2,6);

			myTable.add(issue_text,2,1);
			myTable.addBreak(2,1);
			myTable.add(createIssueMenu(subject_id,iwc),2,1);

			myTable.add(category_text,2,2);
			myTable.addBreak(2,2);
			myTable.add(createCategoryMenu(subject_id,iwc),2,2);

			myTable.add(content_text,2,3);
			myTable.addBreak(2,3);
			myTable.add(createContentMenu(subject_id),2,3);

                        myTable.add(author_text,2,4);
                        myTable.addBreak(2,4);
                        myTable.add(subject_author,2,4);

			//Image dót!!
			Window insertNewsImageWindow = new Window(iwrb.getLocalizedString("attachment","Attachment"), 480, 420, "/news/insertfile.jsp?submit=new");
			String image_session_id = (String) Session.getAttribute("file_id");
			String image_id = null;
			Table imageTable = new Table(1,2);
			imageTable.setAlignment("center");



			FileEntity file;
			String file_name = iwrb.getLocalizedString("file_unknown","Unknown");

			if(image_session_id != null ){
				image_id = image_session_id;

				file = new FileEntity(Integer.parseInt(image_id));
				file_name = file.getName();

				myForm.add(new HiddenInput("include_image", "Y"));
				imageTable.addText(iwrb.getLocalizedString("has_attachment","Attachment")+file_name,1,1);
				imageTable.add(new Link(iwrb.getImage("attachment.gif"),insertNewsImageWindow),1,2);
				myTable.add(imageTable, 2, 5);
			}
			else {
				if(update){
					image_id = String.valueOf(subject.getImageId());


					if ( subject.getIncludeImage().equals("Y") ) {
						file = new FileEntity(Integer.parseInt(image_id));

						Session.setAttribute("file_id",image_id);
						myForm.add(new HiddenInput("include_image", "Y"));
						imageTable.addText(iwrb.getLocalizedString("has_attachment","Attachment: ")+file.getName(),1,1);
						imageTable.add(new Link(iwrb.getImage("attachment.gif"),insertNewsImageWindow),1,2);
					}
					else {
						imageTable.add(new Link(iwrb.getImage("attachment.gif"),insertNewsImageWindow),1,1);
					}
					myTable.add(imageTable, 2, 5);
				}

				if (image_id==null||image_id.equals("")){
					image_id="-1";//ef engin mynd
					imageTable.add(new Link(iwrb.getImage("attachment.gif"),insertNewsImageWindow),1,1);
					myTable.add(imageTable, 2, 5);
				}

			}

			myForm.add(new HiddenInput("image_id",image_id));

		Session.removeAttribute("file_id");

		myForm.add(myTable);

		outerTable.setRowColor(1,headlineBgColor);

		Text headline_text = new Text(iwrb.getLocalizedString("box_editor","Box Editor"));
			headline_text.setBold();
			headline_text.setFontSize(3);
			headline_text.setFontColor(headlineColor);

		outerTable.add(headline_text,1,1);
		outerTable.add(new BoxToolbar().getToolbarTable(),1,2);
		outerTable.add(myForm,1,3);

	}

	private void saveSubject(IWContext iwc) throws IOException,SQLException {

		boolean check = true;

		HttpServletRequest request = iwc.getRequest();

		String subject_id = iwc.getRequest().getParameter("subject_id");

		String subject_name = request.getParameter("subject_name");
			if ( subject_name == null || subject_name.equals("") ) { check = false; }

		String subject_value = request.getParameter("subject_value");
			//if ( subject_value == null ) { subject_value = ""; }

		String issue_id = request.getParameter("issue_id");
		String issue_category_id = request.getParameter("issue_category_id");
		String content_id = request.getParameter("content_id");

		String include_image = request.getParameter("include_image");
			if ( include_image == null ) { include_image = "N"; }

		String image_id = request.getParameter("image_id");

		idegaTimestamp date = new idegaTimestamp();

                String subject_author = request.getParameter("subject_author");

		Subject subject = new Subject();

		if ( update ) { subject = new Subject(Integer.parseInt(subject_id)); }

		subject.setIssueId( Integer.parseInt(issue_id) );
		subject.setIssueCategoryId( Integer.parseInt(issue_category_id) );
		subject.setSubjectName(subject_name);
		subject.setContentId( Integer.parseInt(content_id) );
		subject.setSubjectValue(subject_value);
		subject.setIncludeImage(include_image);
		subject.setImageId( Integer.parseInt(image_id) );
		subject.setSubjectDate( date.getTimestampRightNow());
                subject.setSubjectAuthor(subject_author);

		if ( check == true ) {

			if ( update ) { subject.update(); }
			else { subject.insert(); }

			outerTable = new Table(1,2);

			outerTable.addText(iwrb.getLocalizedString("saved","Document saved"),1,1);

			Form myForm = new Form("/index.jsp");
				myForm.add(new HiddenInput("issue_id",issue_id));
				myForm.add(new SubmitButton(iwrb.getLocalizedString("back","Back")));

			outerTable.add(myForm,1,2);
		}

		else {

			outerTable = new Table(1,2);

			BackButton back = new BackButton(iwrb.getLocalizedString("back","Back"));

			outerTable.addText(iwrb.getLocalizedString("no_title","Document has no title"),1,1);
			outerTable.add(back,1,2);

		}
	}

	private void deleteSubject(IWContext iwc) throws IOException,SQLException {

		String subject_id = iwc.getRequest().getParameter("subject_id");

		Subject subject = new Subject(Integer.parseInt(subject_id));

		outerTable = new Table(1,3);
			outerTable.setCellpadding(3);
			outerTable.setCellspacing(3);

		outerTable.addText(iwrb.getLocalizedString("delete_confirm","Are you sure you want to delete this document?"),1,1);

		Text subject_text = new Text("- "+subject.getSubjectName());
			subject_text.setFontSize(3);
			subject_text.setBold();

		outerTable.add(subject_text,1,2);

		Form myForm = new Form("/boxoffice/boxadmin.jsp");
			myForm.add(new HiddenInput("subject_id",subject_id));
			myForm.add(new HiddenInput("action","delete"));
			myForm.add(new HiddenInput("mode","delete"));
			myForm.add(new SubmitButton(iwrb.getLocalizedString("delete_document","Delete document")));

		outerTable.add(myForm,1,3);

	}

	private void confirmDelete(IWContext iwc) throws IOException,SQLException {

		String subject_id = iwc.getRequest().getParameter("subject_id");

		Subject subject = new Subject(Integer.parseInt(subject_id));

		subject.delete();

		outerTable = new Table(1,2);
			outerTable.setCellpadding(3);
			outerTable.setCellspacing(3);

		outerTable.addText(iwrb.getLocalizedString("deleted","Document deleted"),1,1);

		Form myForm = new Form("/index.jsp");
			myForm.add(new HiddenInput("issue_id",String.valueOf(subject.getIssueId())));
			myForm.add(new SubmitButton(iwrb.getLocalizedString("back","Back")));

		outerTable.add(myForm,1,2);

	}

	private DropdownMenu createContentMenu(int subject_id) throws IOException,SQLException {

		Content[] content = (Content[]) (new Content()).findAllOrdered("content_id");

		DropdownMenu content_menu = new DropdownMenu("content_id");

		for ( int a = 0; a < content.length; a++ ) {

			content_menu.addMenuElement(content[a].getID(),content[a].getContentValue());
		}

		if ( update ) {
			content_menu.setSelectedElement(String.valueOf(new Subject(subject_id).getContentId()));
		}

		return content_menu;
	}

	private DropdownMenu createIssueMenu(int subject_id,IWContext iwc) throws IOException,SQLException {

		String issue_id = iwc.getRequest().getParameter("issue_id");
			if ( issue_id == null ) { issue_id = "1"; }


		DropdownMenu issue_menu = new DropdownMenu("issue_id");

		if ( isGolf ) {

			issue_menu.addMenuElement(String.valueOf(unionID),new Issues(unionID).getIssueName());

		}

		else {

			Issues[] issues = (Issues[]) (new Issues()).findAll();

			for ( int a = 0; a < issues.length; a++ ) {

				issue_menu.addMenuElement(issues[a].getID(),issues[a].getIssueName());
			}

				issue_menu.setSelectedElement(issue_id);

		}

		if ( update ) {
			issue_menu.setSelectedElement(String.valueOf(new Subject(subject_id).getIssueId()));
		}

		return issue_menu;
	}

	private DropdownMenu createCategoryMenu(int subject_id,IWContext iwc) throws IOException,SQLException {

		String issue_category_id = iwc.getRequest().getParameter("issue_category_id");
			if ( issue_category_id == null ) { issue_category_id = "1"; }


		DropdownMenu category_menu = new DropdownMenu("issue_category_id");

		if ( isGolf ) {

			IssuesIssuesCategory[] issues_category = (IssuesIssuesCategory[]) (new IssuesIssuesCategory()).findAllByColumn("issue_id",String.valueOf(unionID));

			for ( int a = 0; a < issues_category.length; a++ ) {

				category_menu.addMenuElement(issues_category[a].getIssueCategoryId(),new IssuesCategory(issues_category[a].getIssueCategoryId()).getCategoryName());
			}

				category_menu.setSelectedElement(issue_category_id);
		}

		else {

			IssuesCategory[] issues_category = (IssuesCategory[]) (new IssuesCategory()).findAll();

			for ( int a = 0; a < issues_category.length; a++ ) {

				category_menu.addMenuElement(issues_category[a].getID(),issues_category[a].getCategoryName());
			}

				category_menu.setSelectedElement(issue_category_id);
		}

		if ( update ) {
			category_menu.setSelectedElement(String.valueOf(new Subject(subject_id).getIssueCategoryId()));
		}

		return category_menu;
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

	public void setUnionID(int unionID){
		this.unionID=unionID;
		this.isGolf = true;
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}

