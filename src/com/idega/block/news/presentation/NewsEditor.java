package com.idega.block.news.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.news.data.*;
import com.idega.data.*;
import com.idega.util.text.*;
import javax.servlet.http.*;
import com.idega.util.text.TextSoap;


public class NewsEditor extends JModuleObject{


private String Error;
private boolean isAdmin=false;



private String language = "IS";
private String[] Lang = {"Fréttastjóri","Fyrirsögn", "Frétt", "Flokkur","Höfundur", "Heimild", "Fjöldi sýnilegra daga", "Mynd"};
private String[] IS = {"Fréttastjóri","Fyrirsögn", "Frétt", "Flokkur","Höfundur", "Heimild", "Fjöldi sýnilegra daga", "Mynd"};
private String[] EN = {"News Editor","Headline", "News", "Category","Author", "Source", "Number of visible days", "Image"};



private Text headlinestring;
private Text newsstring;
private Text categorystring;
private Text authorstring;
private Text sourcestring;
private Text daysshownstring;
private Text imagestring;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;



public NewsEditor(){

}

public NewsEditor(boolean isAdmin){

	this.isAdmin=isAdmin;

}

private void setSpokenLanguage(ModuleInfo modinfo){

String language2 = modinfo.getRequest().getParameter("language");
    if (language2==null) language2 = ( String ) modinfo.getSession().getAttribute("language");
    if ( language2 != null) language = language2;

 // language = modinfo.getSpokenLanguage();
// if(language!=null){
  if (language.equalsIgnoreCase("IS")){
    Lang = IS;
  }else{
    Lang = EN;
  }

 // }

}


public void setConnectionAttributes(String attributeName, int attributeId) {
            this.attributeName = attributeName;
            this.attributeId = attributeId;
}

public void setConnectionAttributes(String attributeName, String attributeId) {
            this.attributeName = attributeName;
            this.attributeId = Integer.parseInt(attributeId);
}

public String getColumnString(NewsCategoryAttributes[] attribs){

String values = "";

	for (int i = 0 ; i < attribs.length ; i++) {

		values += " news_category_id = '"+attribs[i].getNewsCategoryId()+"'" ;
                if( i!= (attribs.length-1) ) values += " OR ";

	}

return values;

}

public void main(ModuleInfo modinfo)throws Exception{
        this.isAdmin=this.isAdministrator(modinfo);

        setSpokenLanguage(modinfo);
	String mode = modinfo.getRequest().getParameter("mode");


        headlinestring = new Text("<b>"+Lang[1]+"</b>");
        newsstring = new Text("<b>"+Lang[2]+"</b>");
        categorystring = new Text("<b>"+Lang[3]+"</b>");
        authorstring = new Text("<b>"+Lang[4]+"</b>");
        sourcestring = new Text("<b>"+Lang[5]+"</b>");
        daysshownstring = new Text("<b>"+Lang[6]+"</b>");
        imagestring = new Text("<b>"+Lang[7]+"</b>");

	try{


		//check to see if we are doing anything special
		if( mode != null){

			//are we updating
			if( mode.equals("update") ){
				//add(drawBrowseTable());
			}
			else if( mode.equals("save")){
				if( storeNews(modinfo) ) add(feedBack(true));
				else add(feedBack(false));
			}
                        else if( mode.equals("delete") ){ add(deleteNews(modinfo.getRequest().getParameter("news_id"))); }
		}

		//else we are either writing something new or we are updating something we have selected
		else{
			if(isAdmin){
			add(editorTable(modinfo));
			}
			else add(new Text("<center><b></b>Login First!</b></center>") );
			//add(editorTable(modinfo));
		}
	}
	catch(Exception e){
		add(new Text("villa : "+e.getMessage()) );//something went wrong
	}


}

public Text deleteNews(String news_id) throws SQLException{
  News news = new News(Integer.parseInt(news_id));
  news.delete();

return new Text("Deleted the news");
}


public Table editorTable(ModuleInfo modinfo)throws SQLException, IOException
{
	HttpSession Session = modinfo.getSession();

	Table mainTable = new Table(1, 2);
//	Table topToolTable = (new NewsToolbar()).getToolbarTable();//4,1
	Table topToolTable = new Table(1,1);

	Text frettastjori = new Text(Lang[0]);//Lang[0]=newseditor...
	frettastjori.setBold();
	frettastjori.setFontColor("#FFFFFF");
	frettastjori.setFontSize("3");


	topToolTable.add(frettastjori,1,1);
	topToolTable.setColor("4d6476");
	topToolTable.setWidth("100%");

	Table mainPanel = new Table(2,4);
	Table sideToolTable = new Table(1,11);

	Form myForm = new Form();


	boolean update=false;
	//interface starts

	myForm.add(mainPanel);


	//mainTable.add(new NewsToolbar(),1,1);
	mainTable.add(topToolTable,1,1);

	mainTable.add(myForm,1,2);
	mainPanel.add(headlinestring,1,1);
	mainPanel.add(newsstring,1,3);
	mainPanel.add(sideToolTable,2,1);

	mainTable.setColor("#EFEFEF");
	mainPanel.setVerticalAlignment(1,1,"top");
	mainPanel.setVerticalAlignment(1,2,"top");
	mainPanel.setVerticalAlignment(1,3,"top");
	mainPanel.setVerticalAlignment(1,4,"top");
	mainPanel.mergeCells(2,1,2,4);

	//mainTable.setBorder("1");
	//mainPanel.setBorder("1");



		String news_id = modinfo.getRequest().getParameter("news_id");
		String category_id = modinfo.getRequest().getParameter("category_id");
		/*String mode = modinfo.getRequest().getParameter("mode");
		String mode = modinfo.getRequest().getParameter("mode");*/

	//draw everything first and then if we already selected a news then
	//fill up the selection boxes with that
	News news=null;
	HiddenInput newsHidden;

		if(news_id != null){
			news = new News(Integer.parseInt(news_id));
			update = true;
			newsHidden = new HiddenInput("news_id",news_id);

		}
		else newsHidden = new HiddenInput("news_id","-1");

		mainPanel.add(newsHidden,1,4);

		//main inputbox and headline

		//check if news and/or headline is already there
		TextArea headlineArea;

                if(update) {
                  String theheadline = news.getHeadline();
                  if( theheadline!=null )  headlineArea = insertTextArea("NewsHeader",theheadline, 0, 60);
		  else headlineArea = insertTextArea("NewsHeader","", 0, 60);
                }
		else headlineArea = insertTextArea("NewsHeader","", 0, 60);

		mainPanel.add(headlineArea,1,2);

		TextArea newsArea;
		if(update) {
                  String thenewstext=news.getText();
                  if ( thenewstext!=null) newsArea = insertTextArea("NewsText",thenewstext,15, 60);
                  else newsArea = insertTextArea("NewsText","",15, 60);

                }
		else newsArea = insertTextArea("NewsText","",15, 60);

           //     newsArea.setNoWrap();

		mainPanel.add(newsArea,1,4);

		//category menu

		DropdownMenu newsCategoryMenu = new DropdownMenu("category_id");

                NewsCategoryAttributes[] attribs = (NewsCategoryAttributes[]) (new NewsCategoryAttributes()).findAllByColumn("attribute_name",attributeName,"attribute_id",""+attributeId);

                String categoryString = getColumnString(attribs);

                if( (categoryString!=null) && !(categoryString.equals("")) ) categoryString += " AND ";

                 // PrintWriter out = modinfo.getResponse().getWriter();
                  //out.println("categoryString :"+categoryString);

		NewsCategory newscat = new NewsCategory();
		NewsCategory[] newscats = (NewsCategory[]) newscat.findAll("select * from news_category where "+categoryString+ " valid='Y'");

		if( newscats!=null || newscats.length!=0){
				for ( int i=0 ; i < newscats.length; i++){
					newsCategoryMenu.addMenuElement(newscats[i].getID(), newscats[i].getName());
				}
			newsCategoryMenu.addMenuElement("-1", "Veldu flokk" );
		}
		else newsCategoryMenu.addMenuElement("Finn engan flokk" );

		if(update) newsCategoryMenu.setSelectedElement(""+news.getNewsCategoryId());
		else newsCategoryMenu.setSelectedElement("-1" );
		newsCategoryMenu.keepStatusOnAction();
		sideToolTable.add(categorystring,1,1);
		sideToolTable.add(newsCategoryMenu,1,2);


		/*myDropdown.setSelectedElement("Y");
			myDropdown.setToSubmit();
			myDropdown.keepStatusOnAction();
			myDropdown.addMenuElement("-1", "Veldu fréttaflokk" );*/


		//author

		TextInput authorBox;

		if(update) {
                  String theauthor = news.getAuthor();
                  if( theauthor!=null ) authorBox = insertEditBox("author",theauthor);
		  else authorBox = insertEditBox("author","");

                }
                else authorBox = insertEditBox("author");

		sideToolTable.add(authorstring,1,3);
		sideToolTable.add(authorBox,1,4);

		//source

		TextInput sourceBox;

		if(update) {
                  String thesource = news.getSource();
                  if( thesource!=null ) sourceBox = insertEditBox("source",thesource);
		  else sourceBox = insertEditBox("source","");

                }
                else sourceBox = insertEditBox("source");

		sideToolTable.add(sourcestring,1,5);
		sideToolTable.add(sourceBox,1,6);

		//days shown

		DropdownMenu counterMenu = counterDropdown( "daysShown", 1, 30);
		counterMenu.addMenuElement("-1", "óákveðið" );

		if(update) counterMenu.setSelectedElement(""+news.getDaysShown());
		else counterMenu.setSelectedElement("-1");

		sideToolTable.add(daysshownstring,1,7);
		sideToolTable.add(counterMenu,1,8);

		//image

		sideToolTable.addText("<b>Mynd</b>", 1, 9);

		Window insertNewsImageWindow = new Window("Nymynd", 480, 420, "/news/insertimage.jsp?submit=new");
		//Link insertImageLink =  new Link(new Image("/servlet/imageModule?image_id=1"),insertNewsImageWindow);
		//check if we updated the picture
		String image_session_id = (String) Session.getAttribute("image_id");
		String image_id = null;
		Table imageTable = new Table(1,2);
		imageTable.setAlignment("center");


		if(image_session_id != null){
			image_id = image_session_id;
			mainPanel.add(new HiddenInput("includeImage", "Y"), 1,4);
			imageTable.add(new Link(new Image("/servlet/imageModule?image_id="+image_id,"Fréttamynd"),insertNewsImageWindow),1,1);
			imageTable.add(new Text("smelltu á myndina<br>til að breyta henni"),1,2);
			sideToolTable.add(imageTable, 1, 10);
		}
	else {

						if(update){
							image_id = ""+news.getImageId();
							if(news.getIncludeImage().equals("Y")){
								Session.setAttribute("image_id",image_id);
								mainPanel.add(new HiddenInput("includeImage", "Y"), 1,4);

								imageTable.add(new Link(new Image("/servlet/imageModule?image_id="+image_id,"Fréttamynd"),insertNewsImageWindow),1,1);

							}
							else {
								image_id="/pics/news/x.gif";//ef engin mynd
								imageTable.add(new Link(new Image(image_id,"Engin mynd"),insertNewsImageWindow),1,1);
							}

							//imageTable.add(new Text("<b>Mynd úr grunni</b>"),1,2);
							imageTable.add(new Text("smelltu á myndina<br>til að breyta henni"),1,2);
							sideToolTable.add(imageTable, 1, 10);
						}

						if (image_id==null){
							image_id="/pics/news/x.gif";//ef engin mynd
							imageTable.add(new Link(new Image(image_id,"Engin mynd"),insertNewsImageWindow),1,1);
							//imageTable.add(new Text("<b>Smelltu á ný mynd</b>"),1,2);
							imageTable.add(new Text("smelltu á myndina<br>til að breyta henni"),1,2);


							sideToolTable.add(imageTable, 1, 10);
						}

		}

		//sideToolTable.add(insertImageLink, 1,11);

		/////



		//save button

		SubmitButton mySubmit = new SubmitButton("mode", "save");
		sideToolTable.add(mySubmit, 1,11);
		//mainPanel.add(mySubmit,2,5);


		/////

		//myTable.add(categoryTable, 1, 3);
		//myTable.addText("Mynd með? : ", 1, 1);
		//myTable.addText("Já", 2, 1);
		//myTable.add(new RadioButton("IncludeImage", "N"), 4, 2);
		//myTable.add(insertNewsImageForm, 5, 2);
		//myTable.setColumnAlignment(5, "right");




	//myForm.setMethod("Get");

	//add(outerTable);
	//add(myForm);

	/*Table returnTable = new Table(1,1);
	returnTable.add(myForm,1,1);*/
	mainPanel.setVerticalAlignment(2,1,"top");
	return mainTable;

}

public boolean storeNews(ModuleInfo modinfo)throws SQLException, IOException
{

	HttpServletRequest Request = modinfo.getRequest();
	HttpServletResponse Response = modinfo.getResponse();
	HttpSession Session = modinfo.getSession();
	boolean update=false;

			//debug
			PrintWriter out = Response.getWriter();

	String news_id = Request.getParameter("news_id");
//	out.println("newsid :"+news_id);

	if ( (news_id!=null) && !(news_id.equals("-1"))) update=true;
	//	out.println("update :"+update);


		//init parameters
		//first the required ones


		String newsHeader = Request.getParameter("NewsHeader");
		//out.println("<br> fyrirsogn: "+newsHeader);

		String newsText = Request.getParameter("NewsText");

                newsText = TextSoap.findAndReplace(newsText, "“","\"");
                newsText = TextSoap.findAndReplace(newsText, "'","´");

		//out.println("<br> text: "+newsText);

		String category_id = Request.getParameter("category_id");
		//out.println("<br> category_id: "+category_id);


		if( (newsHeader==null||newsHeader=="") || (newsText==null||newsText=="") || (category_id==null||category_id=="-1") ){
			return false;
		}

		News news = null;
		//make the news object and fill-it up
		if (update) news = new News(Integer.parseInt(news_id));
		else news = new News();

		news.setHeadline(newsHeader);

		news.setText(newsText);

		news.setNewsCategoryId(new Integer(category_id));

/*
                NewsCategoryAttributes newsattr = new NewsCategoryAttributes();
                newsattr.setNewsCategoryId(Integer.parseInt(category_id));
                newsattr.setAttributeName("union_id");
                newsattr.setAttributeId(Integer.parseInt(union_id));*/

		//then the optional ones
		String source = Request.getParameter("source");
		//out.println("<br> source: "+source);
		if ( source!=null ) news.setSource(source);

		String author = Request.getParameter("author");
		//out.println("<br> author: "+author);
		if ( author!=null ) news.setAuthor(author);

		String daysShown = Request.getParameter("daysShown");
		//out.println("<br> daysShown: "+daysShown);
		if ( daysShown!=null ) news.setDaysShown(new Integer(daysShown));


		String includeImage = Request.getParameter("includeImage");
		//out.println("<br> includeImage: "+includeImage);
		if( includeImage!=null ) news.setIncludeImage(includeImage);
		else news.setIncludeImage("N");

		String image_id = (String) Session.getAttribute("image_id");
                Session.removeAttribute("image_id");

		//laga a edison
		//if(image_id == null) image_id="-1";//ef engin mynd
		if(image_id == null) image_id="-1";//ef engin mynd

		news.setImageId(new Integer(image_id));

		idegaTimestamp date = new idegaTimestamp();

                if (!update) news.setDate( date.getTimestampRightNow());

		//end init params

		//and save or update

		try{
			if( update ) {
                          news.update();
                     //     news.attr.insert();


                        }
                        else
                        {
                          news.insert();
                       //   newsattr.insert();
                        }
			//Session.removeAttribute("image_id");
		}
		catch(Exception e){
			return false;
		}

		return true;
}


public TextArea insertTextArea(String TextAreaName, String text, int Height, int Width)
{
	TextArea textArea =  new TextArea(TextAreaName);
	textArea.setHeight(Height);
	textArea.setWidth(Width);
	textArea.setContent(text);
	textArea.keepStatusOnAction();
	return textArea;
}



public TextArea insertTextArea(String TextAreaName, int Height, int Width)
{
	TextArea textArea =  new TextArea(TextAreaName);
	textArea.setHeight(Height);
	textArea.setWidth(Width);
	textArea.keepStatusOnAction();
	return textArea;
}

public TextInput insertEditBox(String name, String text)
{
	TextInput myInput = new TextInput(name);
	myInput.setContent(text);
	myInput.keepStatusOnAction();
	return myInput;
}

public TextInput insertEditBox(String name)
{
	TextInput myInput = new TextInput(name);
	myInput.keepStatusOnAction();
	return myInput;
}


public Table feedBack(boolean isOk)
{
	Table myTable = new Table(2, 2);
	myTable.mergeCells(1, 1, 2, 1);
	Form actionForm = new Form();
	Form backForm = new Form();
	backForm.add(new BackButton("Til baka"));
	actionForm.add(new SubmitButton("Skoða fréttir"));
	actionForm.setAction("/index.jsp");
	myTable.setAlignment("center");
	//myTable.setBorder(1);

	if(isOk)
	{
		myTable.addText("Fréttin hefur verið skráð", 1, 1);
		myTable.add(backForm, 1, 2);
		myTable.add(actionForm, 2, 2);

	}
	else
	{
		myTable.addText("Fylla verður í alla reiti auk þess að velja flokk", 1, 1);
		myTable.add(new BackButton("Til baka"), 1, 2);
	}

	return myTable;
}
/*
public void writeToHeadlines(String site, String Header, String news_url){

	try{
    	Class.forName("org.gjt.mm.mysql.Driver").newInstance();  //loads the driver

     	String url = "jdbc:mysql://einstein.idega.is:3306/idegaweb?user=root&password=crazy0nes";
    	Connection Conn2 = DriverManager.getConnection(url);

        Statement MysqlStmt = Conn2.createStatement();

        MysqlStmt.executeUpdate("insert into headlines(group_id,site,headline,theURL) values(3,'"+site+"','"+Header+"','"+news_url+"')");
		MysqlStmt.close();
		Conn2.close();
		}
		catch (SQLException E) {
		System.err.print("SQLException: " + E.getMessage());
       	System.err.print("SQLState:     " + E.getSQLState());
		}
		catch (Exception E) {
   		E.printStackTrace();
   		}
}

*/

public FileInput insertFileInput(String FileInputName)
{
	FileInput myFileInput = new FileInput(FileInputName);
	return myFileInput;
}



public DropdownMenu insertDropdown(String dropdownName)throws IOException, SQLException
{
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		myDropdown.addMenuElement("Y", "Frétt");
		myDropdown.addMenuElement("N", "Tilkynning");

		myDropdown.setSelectedElement("Y");
		myDropdown.setToSubmit();
		myDropdown.keepStatusOnAction();

	return myDropdown;
}

public DropdownMenu counterDropdown(String dropdownName, int countFrom, int countTo)
{
	String from = Integer.toString(countFrom);
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

	for(; countFrom <= countTo; countFrom++)
	{
		myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
	}
	myDropdown.keepStatusOnAction();

	return myDropdown;
}

public DropdownMenu newsDropdown(String newsDropdownName, String categoryDropdownName, Connection Conn, ModuleInfo modinfo)throws IOException, SQLException
{
	//PrintWriter out = modinfo.getResponse().getWriter();
	DropdownMenu myDropdown = new DropdownMenu(newsDropdownName);
	String cotgegoryId = modinfo.getRequest().getParameter(categoryDropdownName);


		Statement Stmt = Conn.createStatement();
		ResultSet RS = Stmt.executeQuery("select news_id, headline from news where news_category_id = '"+cotgegoryId+"' ORDER BY headline;");
	 	myDropdown.addMenuElement("-1", "Veldu frétt" );
		while(RS.next())
        {
			myDropdown.addMenuElement(RS.getString("news_id"), RS.getString("headline"));
        }
		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		RS.close();
		Stmt.close();

	return myDropdown;
}


public DropdownMenu categoryDropdown(String categoryDropdownName, Connection Conn)throws IOException, SQLException
{
	//PrintWriter out = modinfo.getResponse().getWriter();
	DropdownMenu myDropdown = new DropdownMenu(categoryDropdownName);

	try
	{
		Statement Stmt = Conn.createStatement();
		ResultSet RS = Stmt.executeQuery("select news_category_id, news_category_name from news_category where valid = 'Y' ORDER BY news_category_id;");
	 	myDropdown.addMenuElement("-1", "Veldu fréttaflokk" );
		while(RS.next())
        {
			myDropdown.addMenuElement(RS.getString("news_category_id"), RS.getString("news_category_name") );
        }
		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		RS.close();
		Stmt.close();
	}
	catch (SQLException E) {
		System.err.print("SQLException: " + E.getMessage());
        System.err.print("SQLState:     " + E.getSQLState());
        System.err.print("VendorError:  " + E.getErrorCode());
    }
	return myDropdown;
}

}
