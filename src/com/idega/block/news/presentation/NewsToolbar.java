package com.idega.block.news.presentation;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import com.idega.util.*;

public class NewsToolbar extends JModuleObject{


private Table myTable = new Table(5, 1);

private ModuleInfo modinfo;

public NewsToolbar(){}

public void main(ModuleInfo modinfo){

	this.modinfo = modinfo;

        String news_id = modinfo.getRequest().getParameter("news_id");
	Window categoryWindow = new Window("Gluggi", 420, 370, "/news/insertnewscategories.jsp");
	Window delCategoryWindow = new Window("Gluggi", 270, 420, "/news/delnewscategories.jsp");
	//Window insertNewsImageWindow = new Window("Gluggi", 480, 420, "/news/insertimage.jsp?submit=new");

	Form delCategoryForm = new Form(delCategoryWindow);
	Form categoryForm = new Form(categoryWindow);
	//Form updateNewsForm =  new Form("/news/editor.jsp?mode=update");
	Form updateNewsForm =  new Form();
	updateNewsForm.add(new HiddenInput("mode","update"));
	//Form insertNewsImageForm =  new Form(insertNewsImageWindow);
	Form newNewsForm =  new Form("/news/editor.jsp");
        Form deleteNewsForm =  new Form("/news/editor.jsp");
        deleteNewsForm.add(new HiddenInput("mode","delete"));
        if(news_id!=null) deleteNewsForm.add(new HiddenInput("news_id",news_id));


	Table categoryTable = new Table(1, 1);
	Table delCategoryTable = new Table(1 ,1);
	Table updateNewsTable = new Table(1 ,1);
	Table newNewsTable = new Table(1 ,1);
	Table insertNewsImageTable = new Table(1, 1);
	Table deleteNewsTable = new Table(1,1);


	categoryTable.add(new SubmitButton("Nýr flokkur"), 1, 1);
	delCategoryTable.add(new SubmitButton("Eyða flokki"), 1, 1);
	updateNewsTable.add(new SubmitButton("Breyta frétt"), 1, 1);
	newNewsTable.add(new SubmitButton("Ný frétt"), 1, 1);
        deleteNewsTable.add(new SubmitButton("Eyða frétt"), 1, 1);
	//insertNewsImageTable.add(new SubmitButton("submit","Ný mynd"),1,1);

	categoryForm.add(categoryTable);
	delCategoryForm.add(delCategoryTable);
	updateNewsForm.add(updateNewsTable);
	newNewsForm.add(newNewsTable);
        deleteNewsForm.add(deleteNewsTable);
	//insertNewsImageForm.add(insertNewsImageTable);

	myTable.add(newNewsForm, 1, 1);
	myTable.add(updateNewsForm, 2, 1);
	myTable.add(categoryForm, 3, 1);
	myTable.add(delCategoryForm, 4, 1);
 myTable.add(deleteNewsForm, 5, 1);


	//myTable.add(insertNewsImageForm, 5, 1);
	myTable.setWidth("100%");
	myTable.setAlignment("left");


	add(myTable);
}



}
