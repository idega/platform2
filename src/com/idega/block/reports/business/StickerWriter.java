package com.idega.block.reports.business;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class StickerWriter
{

  public StickerWriter(){
  }

  public static void print(OutputStream os,StickerList stickers ){
    Rectangle paperSize = stickers.getPageSize();

    if(stickers.getRotation()){
      paperSize = paperSize.rotate();
    }

    Document document = new Document(paperSize);
    try
    {
      PdfWriter writer = PdfWriter.getInstance(document, os);
      document.open();
      float stickerHeight = stickers.getStickerHeight();
      float stickerWidth = stickers.getStickerWidth();
      int pageColumns = (int)( paperSize.width() / stickerWidth );
      int pageRows = (int)(paperSize.height() / stickerHeight );
      //System.err.println("Paper height :"+paperSize.height()+" cell heigth :"+height+" rows :"+pageRows);
      //System.err.println("Paper width :"+paperSize.width()+" cell width :"+width+" cols :"+pageColumns);

      int totalWidth = (int) (pageColumns*stickerWidth);
      if(pageColumns == 0) {
		pageColumns = 1;
	}

      PdfPTable table = getPdfPTable(stickers,pageColumns,totalWidth);
      Paragraph p;
      int fitsPage = pageColumns*pageRows;
      int size = stickers.size();
      for (int i = 1; i <= size; i++) {
        p = (Paragraph) stickers.get(i-1);
        if(i > 0 && i%fitsPage == 0){
          table.writeSelectedRows(0, -1, stickers.margin(), paperSize.height()-stickers.margin(), writer.getDirectContent());
          document.newPage();
          table = getPdfPTable(stickers,pageColumns,totalWidth);
        }
        if(i == size-1) {
			table.writeSelectedRows(0, -1, stickers.margin(), paperSize.height()-stickers.margin(), writer.getDirectContent());
		}
        table.addCell(p);
      }

      document.close();
    }
    catch(Exception de)
    {
        de.printStackTrace();
    }
  }

  private static PdfPTable getPdfPTable(StickerList stickers,int pageColumns,float totalWidth){
    PdfPTable table = new PdfPTable(pageColumns);
    table.setTotalWidth(totalWidth);
    table.getDefaultCell().setFixedHeight(stickers.getStickerHeight());
    table.getDefaultCell().setIndent(stickers.getDefaultCell().getIndent());
    table.getDefaultCell().setBorder(stickers.border());
    table.getDefaultCell().setLeading(stickers.getDefaultCell().getLeading(),stickers.getDefaultCell().getMultipliedLeading());
    return table;
  }

  public static void main(String args[]){
    boolean landscape = args.length > 1;
    StickerList list = new StickerList();
    list.setStickerHeight(108);
    list.setStickerWidth(184);
    list.setRotation(landscape);
    list.setOverAllMargin(10);
    int count = 50;
    for (int i = 0; i < count; i++) {
      Paragraph p = new Paragraph("");
      p.add(new Chunk(String.valueOf(100+i),new Font(Font.TIMES_ROMAN,20)));
      p.add(new Chunk("\nThis is tenant",new Font(Font.TIMES_ROMAN,12)));
      list.add(p);
    }
    try{
    print(new FileOutputStream(args[0]),list);
    }
    catch(Exception ex){ex.printStackTrace();}
    //doSomeShit(new File(args[0]), PageSize.A4, 108, 184,landscape);
  }
}
