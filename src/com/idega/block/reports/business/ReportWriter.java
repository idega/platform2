package com.idega.block.reports.business;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.idega.block.reports.data.Report;
import com.idega.util.database.ConnectionBroker;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ReportWriter {

  public ReportWriter(){}

  public static boolean writeXLSReport(String[] Headers,String[][] Content, OutputStream out){
      boolean returner = false;
      try{
        int[] c  = null;
        OutputStreamWriter fout = new OutputStreamWriter(out);
        StringBuffer data;
        int len = Content.length;
        data = new StringBuffer();
        for (int j = 0; j < Headers.length; j++) {
            data.append(Headers[j]);
            data.append("\t");
        }
        data.append("\n");
        fout.write(data.toString());
        for(int i = 0; i < len; i++){
          data = new StringBuffer();
          for (int j = 0; j < Content[i].length; j++) {
            data.append(Content[i][j]);
            data.append("\t");
          }
          data.append("\n");
          fout.write(data.toString());
        }
        returner = true;
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      finally{
        try{
        out.close();
        }
        catch(IOException io){
          io.printStackTrace();
          returner = false;
        }
        return returner;
      }
  }

  public static boolean writePDFReport(String[] Headers,String[][] Content, OutputStream out){
    return false;
  }
  public static boolean writeXLS(Report report,String realpath){
      boolean returner = false;
      try{
        String[] Headers = report.getHeaders();
        String sql = report.getSQL();

        String file = realpath;
        FileWriter out = new FileWriter(file);

        char[] c  = null;

        Connection Conn = com.idega.util.database.ConnectionBroker.getConnection();
        Statement stmt = Conn.createStatement();
        ResultSet RS  = stmt.executeQuery(sql);
        ResultSetMetaData MD = RS.getMetaData();
        int count = MD.getColumnCount();
        String temp;
        StringBuffer data = new StringBuffer();
        for (int i = 0; i < Headers.length; i++) {
          data.append(Headers[i]);
          data.append("\t");
        }
        data.append("\n");
        out.write(data.toString().toCharArray());

        while(RS.next()){
           data = new StringBuffer();
          for(int i = 1; i <= count; i++){
            temp = RS.getString(i);
            temp = temp!=null?temp:"";
            data.append(temp);
            data.append("\t");
          }
          data.append("\n");
          out.write(data.toString().toCharArray());
        }
        RS.close();
        stmt.close();
        ConnectionBroker.freeConnection(Conn);
        out.close();

        returner = true;
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      return returner;
  }

  public static boolean writePDF(Report report){
    return false;
  }
}
