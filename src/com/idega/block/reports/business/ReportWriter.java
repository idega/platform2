package com.idega.block.reports.business;

import java.io.*;
import java.util.*;

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
}
