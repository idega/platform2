package com.idega.block.reports.business;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
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
      Connection Conn = null;

      try{
        String[] Headers = report.getHeaders();
        String sql = report.getSQL();

        String file = realpath;
        FileWriter out = new FileWriter(file);

        char[] c  = null;

        Conn = com.idega.util.database.ConnectionBroker.getConnection();
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
        out.close();

        returner = true;
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      finally {
        ConnectionBroker.freeConnection(Conn);
      }
      return returner;
  }

  public static boolean writePDF(Report report,String realpath){
    boolean returner = false;
    Connection Conn = null;

    try {
        String[] Headers = report.getHeaders();
        int Hlen = Headers.length;
        String sql = report.getSQL();
        String file = realpath;

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.addTitle(report.getName());
        document.addAuthor("Idega Reports");
        document.addSubject(report.getInfo());
        document.open();

        Table datatable = new Table(Headers.length);
        datatable.setCellpadding(0);
        datatable.setCellspacing(3);
        datatable.setBorder(Rectangle.NO_BORDER);
        datatable.setWidth(100);

        for (int i = 0; i < Hlen; i++) {
          //datatable.addCell(Headers[i]);
          datatable.addCell(new Phrase(Headers[i], new Font(Font.HELVETICA, 14, Font.BOLD)));
        }
        datatable.endHeaders();

        Conn = com.idega.util.database.ConnectionBroker.getConnection();
        Statement stmt = Conn.createStatement();
        ResultSet RS  = stmt.executeQuery(sql);
        ResultSetMetaData MD = RS.getMetaData();
        String temp = null;
        StringBuffer sb = null;
        while(RS.next()){
          sb = new StringBuffer();
          for(int i = 1; i <= Hlen; i++){

            temp = RS.getString(i);
            temp = temp!=null?temp:"";
            sb.append(temp);
            Cell C = new Cell(temp);
            datatable.addCell(C);
          }
        }

        RS.close();
        stmt.close();

        document.add(datatable);
        document.close();
        returner = true;
    }
    catch (Exception ex) {
      returner = false;
    }
    finally {
      ConnectionBroker.freeConnection(Conn);
    }

    return returner;
  }
}
