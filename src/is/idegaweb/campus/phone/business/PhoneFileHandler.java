package is.idegaweb.campus.phone.business;

import java.io.*;
import com.idega.block.finance.data.*;
import com.idega.jmodule.object.interfaceobject.*;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.lang.StringBuffer;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class PhoneFileHandler {

  public PhoneFileHandler() {

  }

  public List parseFile(File PhoneFile){

    List L = null;
    try{
      FileReader fin = new FileReader(PhoneFile);
      LineNumberReader lin = new LineNumberReader(fin);
      String line = null;
      StringTokenizer st;
      StringBuffer sb;
      String anumber,snumber,bnumber;
      int nightsec,daysec,sec;
      float price;
      idegaTimestamp stamp;
      int count = 10;
      int linecount = 1;
      Vector V = new Vector();
      AccountPhoneEntry ape;
      while( (line = lin.readLine()) != null ){//&& count != 0){
        st = new StringTokenizer(line,";");
        sb = new StringBuffer();
        if(st.countTokens() == 8){
          try{
            anumber = st.nextToken().trim();
            snumber = st.nextToken().trim();
            bnumber = st.nextToken().trim();
            String s = st.nextToken().trim();
            stamp = parseStamp(s);
            nightsec = Integer.parseInt(st.nextToken());
            daysec = Integer.parseInt(st.nextToken());
            sec = Integer.parseInt(st.nextToken());
            price = Float.parseFloat(st.nextToken());

            ape = new AccountPhoneEntry();
            ape.setLastUpdated(idegaTimestamp.getTimestampRightNow());
            ape.setDayDuration(daysec);
            ape.setNightDuration(nightsec);
            ape.setDuration(sec);
            ape.setMainNumber(anumber);
            ape.setPhonedStamp(stamp.getTimestamp());
            ape.setPhoneNumber(bnumber);
            ape.setPrice(price);
            ape.setSubNumber(snumber);
            ape.setStatus(ape.statusUnread);
            V.add(ape);
          }
          catch(Exception ex){
            System.err.println("error in line "+linecount);
          }
          L = V;

        }
        else{
          System.err.println("error in line "+linecount +" : too few columns");
        }


        System.err.println(sb.toString());
        count--;
        linecount++;
      }
    }
    catch(FileNotFoundException fnfe){
      fnfe.printStackTrace();
    }
    catch(IOException fnfe){
      fnfe.printStackTrace();
    }
    return L;
  }

  public idegaTimestamp parseStamp(String stamp){
    StringBuffer st = new StringBuffer();
    if(stamp!= null && stamp.length() >= 19){
      st.append(stamp.substring(0,10));
      st.append(" ");
      st.append(stamp.substring(11,13));
      st.append(":");
      st.append(stamp.substring(14,16));
      st.append(":");
      st.append(stamp.substring(17,19));
    }
    return new idegaTimestamp(st.toString());
  }


}