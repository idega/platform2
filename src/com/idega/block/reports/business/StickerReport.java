package com.idega.block.reports.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.idega.block.reports.data.Report;
import com.idega.block.reports.data.ReportColumnInfo;
import com.idega.block.reports.data.ReportInfo;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryOutputStream;
import com.idega.util.database.ConnectionBroker;
import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class StickerReport {

  public StickerReport(Report report) {

  }

  public static MemoryFileBuffer writeStickerList(Report report,ReportInfo info){
    Connection Conn = null;

    MemoryFileBuffer buffer = new MemoryFileBuffer();
    MemoryOutputStream mos = new MemoryOutputStream(buffer);

    try {
        String[] Headers = report.getHeaders();
        int Hlen = Headers.length;
        String sql = report.getSQL();
        //String file = realpath;
        List cinfos = ReportFinder.listOfReportColumnInfo(report.getID());
        ReportColumnInfo rinfo;

        String[] endstrings = new String[Hlen];
        Font[] fonts = new Font[Hlen];
        int[] spans = new int[Hlen];

        int listsize = cinfos!=null?cinfos.size():0;
        for (int i = 0; i < Hlen; i++) {
          if(i < listsize){
            rinfo = (ReportColumnInfo)cinfos.get(i);
            fonts[i] = getFont(rinfo);
            /** @todo endstring fix */
            endstrings[i] = "\n";
            spans[i] = rinfo.getColumnSpan();
          }
          else{
            fonts[i] = getFont(null);
            endstrings[i] = "\n";
            spans[i] = 1;
          }
        }

        Conn = com.idega.util.database.ConnectionBroker.getConnection();
        Statement stmt = Conn.createStatement();
        ResultSet RS  = stmt.executeQuery(sql);
        StickerList list = new StickerList();
        list.setStickerHeight(info.getHeight());
        list.setStickerWidth(info.getWidth());
        list.setBorder(info.getBorder());
        list.setRotation(info.getLandscape());
        list.setPageSize(ReportFinder.getPageSize(info.getPagesize()));

        Paragraph parag;
        while(RS.next()){
          parag = new Paragraph();

          for(int i = 1; i <= Hlen; i++){
          	String s = RS.getString(i);
            //if(!RS.wasNull())
          	if(s!=null)
          		parag.add(new Chunk(s,fonts[i-1]));
          		//parag.add(new Chunk(RS.getString(i),fonts[i-1]));
            parag.add(endstrings[i-1]);
          }
          list.add(parag);
        }

        RS.close();
        stmt.close();

        StickerWriter.print(mos,list);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      ConnectionBroker.freeConnection(Conn);
    }
    buffer.setMimeType("application/pdf");
    return buffer;
  }

  public static Paragraph getParagraph(Report report,boolean withHeaders){
    return null;

  }

  public static Font[] getFonts(List listOfInfos,int size){
    Font[] fonts = new Font[size];
    int listsize = listOfInfos.size();
    for (int i = 0; i < size; i++) {
      if(i < listsize)
        fonts[i] = getFont((ReportColumnInfo)listOfInfos.get(i));
      else
        fonts[i] = getFont(null);
    }
    return fonts;
  }

  public static Font getFont(ReportColumnInfo info){
    if(info!=null){
      return new Font(info.getFontFamily(),info.getFontSize(),info.getFontStyle());
    }
    else
      return new Font();
  }
}
