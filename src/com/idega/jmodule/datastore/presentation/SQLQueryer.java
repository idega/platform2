//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/
package com.idega.jmodule.datastore.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.jmodule.news.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
public class SQLQueryer extends JModuleObject{

    private static String queryParameter="SQLQUERYSTRING";
    private FramePane queryPane;
    private FramePane resultsPane;

    public SQLQueryer(){
      queryPane = new FramePane("Query");
      super.add(queryPane);
      Form form = new Form();
      queryPane.add(form);
      TextArea input = new TextArea(queryParameter);
      input.setWidth(50);
      input.setHeight(4);
      Table innertTable =new Table(1,2);
      form.add(innertTable);
      innertTable.add(input,1,1);
      innertTable.add(new SubmitButton("Execute"),1,2);

      resultsPane = new FramePane("Result");
      super.add(resultsPane);

      setWidth(500);


    }

    public void add(ModuleObject obj){
      resultsPane.add(obj);
    }


    public void setWidth(int width){
      this.queryPane.setWidth(width);
      this.resultsPane.setWidth(width);
    }

    public void main(ModuleInfo modinfo)throws Exception{
                Connection conn=getConnection();
                String  queryString = modinfo.getParameter (queryParameter);
                try {
	    if ((queryString != "") && (queryString != null))
		{
                                                add("Your query was:");
                                                add(Text.getBreak());
                                                Text text = new Text(queryString);
						text.setBold();
						add(text);
                                                addBreak();
						Table table = new Table();
                                                table.setColor("white");
						add(table);
                                                Statement stmt = conn.createStatement();
                                                if(queryString.toLowerCase().indexOf("select")==-1){
                                                    int i = stmt.executeUpdate(queryString);
                                                    //if (i>0){
                                                            add(i+" rows altered");
                                                    //}
                                                    //else{

                                                    //}
                                                }
                                                else{


                                                    ResultSet rs = stmt.executeQuery(queryString);
                                                    ResultSetMetaData rsMeta = rs.getMetaData();
                                                    // Get the N of Cols in the ResultSet
                                                    int noCols = rsMeta.getColumnCount();
                                                    //out.println("<tr>");
                                                    int y=1;
                                                    int x=1;
                                                    for (int c=1; c<=noCols; c++) {
                                                        String el = rsMeta.getColumnLabel(c);
                                                        //out.println("<th> " + el + " </th>");
                                                        table.add(el,x,y);
                                                        x++;
                                                    }
                                                    //out.println("</tr>");
                                                    y++;

						    table.setRowColor(1,"#D0D0D0");
						    while (rs.next()) {
                                                        //out.println("<tr>");
                                                        x=1;
                                                        for (int c=1; c<=noCols; c++) {
                                                            String el = rs.getString(c);
                                                            table.add(el,x,y);
                                                            x++;
                                                            //out.println("<td> " + el + " </td>");

                                                        }
                                                        y++;
                                                        //out.println("</tr>");
                                                    }
                                                }
		    //out.println("</table>");
		}
	} catch (SQLException ex ) {
	    //out.println ( "<P><PRE>" );
	      while (ex != null) {
		  add("Message:   " + ex.getMessage ());
                                              this.addBreak();
		  add("SQLState:  " + ex.getSQLState ());
                                              this.addBreak();
		  add("ErrorCode: " + ex.getErrorCode ());
                                              this.addBreak();
		  ex = ex.getNextException();
		  //out.println("");
	      }
	      //out.println ( "</PRE><P>" );
	}
                      finally{
                        this.freeConnection(conn);
                      }
	//out.println ("<hr>You can now try to retrieve something.");
	//out.println("<FORM METHOD=POST ACTION=\"/servlet/CoffeeBreakServlet\">");
	//out.println("<FORM METHOD=POST ACTION=\""+req.getRequestURI()+"\">");
                      //out.println("Query: <INPUT TYPE=TEXT SIZE=50 NAME=\"QUERYSTRING\"> ");
	//out.println("<INPUT TYPE=SUBMIT VALUE=\"GO!\">");
	//out.println("</FORM>");
	//out.println("<hr><pre>e.g.:");
	//out.println("SELECT * FROM COFFEES");
	//out.println("SELECT * FROM COFFEES WHERE PRICE > 9");
	//out.println("SELECT PRICE, COF_NAME FROM COFFEES");
	//out.println("<pre>");

	//out.println ("<hr><a href=\""+req.getRequestURI()+"\">Query again ?</a>");// | Source: <A HREF=\"/develop/servlets-ex/coffee-break/CoffeeBreakServlet.java\">CoffeeBreakServlet.java</A>");
	//out.println ( "</body></html>" );


    }
}
