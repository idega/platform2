package is.idegaweb.campus.phone.business;

import java.io.*;
import com.idega.block.finance.data.*;
import com.idega.jmodule.object.interfaceobject.*;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import java.lang.StringBuffer;
import com.idega.util.idegaTimestamp;
import is.idegaweb.campus.entity.CampusPhone;
import is.idegaweb.campus.allocation.business.ContractFinder;
import is.idegaweb.campus.tariffs.business.AccountFinder;
import is.idegaweb.campus.entity.PhoneFileInfo;

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


  public void process(File PhoneFile){

    Map M = PhoneFinder.mapOfAccountsByPhoneNumber();
    // If we can assess something
    if( M != null ){
      try{

        FileReader fin = new FileReader(PhoneFile);
        LineNumberReader lin = new LineNumberReader(fin);

        javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
        try{
          t.begin();

          String line = null;
          StringTokenizer st;
          StringBuffer sbError = new StringBuffer(),sbNoAccount = new StringBuffer();;
          String anumber,snumber,bnumber;
          int nightsec,daysec,sec;
          float price,totPrice = 0;
          idegaTimestamp stamp;
          int count = 10;
          int linecount = 1, noAccountCount = 0,errorCount = 0,numberCount = 0;
          Vector vError = new Vector();
          Vector vNoAccount = new Vector();
          Hashtable phoneNumbers = new Hashtable();
          AccountPhoneEntry ape;
          Integer iAccountId;
          Account eAccount;
          boolean cont = false;
          while( (line = lin.readLine()) != null ){//&& count != 0){
            cont = false;
            st = new StringTokenizer(line,";");
            if(st.countTokens() == 8){
              ape = new AccountPhoneEntry();
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
                cont = true;
              }
              catch(Exception ex){
                System.err.println("error in line "+linecount);
                cont = false;
              }
              // valid line in file
              if(cont){
                String number = ape.getSubNumber();
                // account for phonenumber exist
                if(phoneNumbers.containsKey(number)){
                  Integer ncount = (Integer) phoneNumbers.get(number);
                  phoneNumbers.put( number , new Integer(ncount.intValue()+1));
                }
                else{
                  phoneNumbers.put( number, new Integer(1));
                  numberCount++;
                }

                if(M!=null && M.containsKey(number)){
                  //iAccountId = (Integer) M.get(number);
                  eAccount = (Account) M.get(number);
                  ape.setAccountId(eAccount.getID());
                  ape.setStatus(ape.statusRead);
                  eAccount.addAmount(new Float(ape.getPrice()));
                  ape.insert();
                  eAccount.update();
                  totPrice += ape.getPrice();
                }
                // account for phonenumber doesn´t exist
                else{
                  sbNoAccount.append(line);
                  sbNoAccount.append("\n");
                  noAccountCount++;
                }
              }
              // invalid line in file
              else{
                 System.err.println("error in line "+linecount +" : parsing error");
                 sbError.append(line);
                 sbError.append("\n");
                 errorCount++;
              }


            }
            else{
              System.err.println("error in line "+linecount +" : too few columns");
              sbError.append(line);
              sbError.append("\n");
              errorCount++;
            }
            count--;
            linecount++;
          }// while
          PhoneFileInfo pfi = new PhoneFileInfo();
          pfi.setDateRead(idegaTimestamp.getTimestampRightNow());
          pfi.setLineCount(linecount-1);
          pfi.setErrorCount(errorCount );
          pfi.setNoAccountCount(noAccountCount );
          pfi.setFileName(PhoneFile.getName());
          pfi.setNumberCount(numberCount);
          pfi.setTotalAmount(totPrice);
          pfi.insert();

          if(errorCount > 0){
            FileWriter out = new FileWriter(new File(PhoneFile.getParentFile(),"err"+PhoneFile.getName()));
            BufferedWriter bout = new BufferedWriter(out);
            bout.write(sbError.toString());
            bout.close();
            out.close();
          }
          if(noAccountCount > 0){
            FileWriter out = new FileWriter(new File(PhoneFile.getParentFile(),"noacc"+PhoneFile.getName()));
            BufferedWriter bout = new BufferedWriter(out);
            bout.write(sbNoAccount.toString());
            bout.close();
            out.close();
          }


          t.commit();
        }
        catch(Exception e){
          try {
              t.rollback();
            }
            catch(javax.transaction.SystemException ex) {
              ex.printStackTrace();
            }
            e.printStackTrace();
        }
      }
      catch(FileNotFoundException fnfe){
        fnfe.printStackTrace();
      }
      catch(IOException fnfe){
        fnfe.printStackTrace();
      }
    }
    else{
      System.err.println("no accounts behind phonenumbers");
    }
  }

  public void processFile(String fileName){
    System.err.println(fileName);
    if(fileName != null){
      try {
        File phoneFile = new File(fileName);
        process(phoneFile);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    else{
      System.err.println("no filename in Campus phonefilehandler");
    }
  }
}