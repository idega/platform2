package com.idega.projects.projectscene.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.data.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000 idega.is All Rights Reserved
 * Company:      idega multimedia
 * @author idega 2000 - <a href="mailto:idega@idega.is">idega team</a>
 * @version 1.0
 */


public abstract class RafMainTemplate extends ProjectMainTemplate{

	Table contentTable;
	Table contentTable2;


	public void initializePage(){
          super.initializePage();
	}




	public Table content()throws IOException {
		if (contentTable == null){
			contentTable = new Table(1,1);
			contentTable.setBorder(0);
			contentTable.setVerticalAlignment(1,1,"top");
			contentTable.setWidth("100%");
			contentTable.setHeight("100%");
			contentTable.setCellpadding(0);
			contentTable.setCellspacing(0);
		}
		return contentTable;
	}

	public Table content2()throws IOException {
		if (contentTable2 == null){
			contentTable2 = new Table(1,1);
			contentTable2.setBorder(0);
			contentTable2.setVerticalAlignment(1,1,"top");
			contentTable2.setAlignment(1,1,"left");
			contentTable2.setWidth("240");
			contentTable2.setHeight("100%");
			contentTable2.setCellpadding(0);
			contentTable2.setCellspacing(0);
		}
		return contentTable2;
	}


	public void add2(PresentationObject objectToAdd){
		try{
			content2().add(objectToAdd,1,1);
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
	}














}
