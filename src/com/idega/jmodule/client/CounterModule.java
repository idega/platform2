package com.idega.jmodule.client;

import java.lang.String;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.idega.jmodule.client.IWCount;
import com.idega.servlet.IWCoreServlet;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*
*
*/
public class CounterModule extends IWCoreServlet
{
  private Hashtable Counters;
  private String DateStarted;
  private int saveFile;

  public void init() throws ServletException {
    super.init();
    Counters = new Hashtable( 10 );
    DateStarted = new Date().toString();
    saveFile = 0;
    getSavedObjects();

  }

  public void doGet( HttpServletRequest request, HttpServletResponse response){
    doPost(request,response);
  }

  public void doPost( HttpServletRequest request, HttpServletResponse response){
    try {

      synchronized( Counters ){
        IWCount CountObject;

        String CounterID = request.getParameter( "counter_countid" );
        CountObject = (IWCount) (Counters.get( CounterID ));

        if( CountObject == null){
          CountObject = new IWCount( CounterID );
          Counters.put( CounterID, CountObject);
        }

        // Increment Counter
        String strIPcheck = request.getParameter( "counter_lastip");
        if( strIPcheck.compareTo("1") == 0){
          CountObject.increment( request.getRemoteAddr() );
          saveFile++;
        }

        String strSilent = request.getParameter("counter_silent");
        if( strSilent.compareTo( "1" ) == 0)
          return;

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String strGetAll = request.getParameter("counter_getall");
        if(strGetAll.compareTo( "1" ) == 0 ){
          Enumeration CountersE = Counters.elements();
          while( CountersE.hasMoreElements()){
            CountObject = (IWCount) CountersE.nextElement();
            out.println(CountObject.getID()+" : " + CountObject.getHit() +
            " Last IP : " + CountObject.getIP() +
            " Last Update : " + CountObject.getDate());
          }
        out.flush();
        return;
        }

        String strHitCount = request.getParameter("counter_hitcount");
        if(strHitCount.compareTo( "1" ) == 0)
          out.println( CountObject.getHit());

        strHitCount = request.getParameter("counter_lastip");
        if(strHitCount.compareTo( "1" ) == 0)
          out.println( CountObject.getHit());

        strHitCount = request.getParameter("counter_hitcount");
        if(strHitCount.compareTo( "1" ) == 0)
          out.println( CountObject.getHit());
      }

    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }

  }

  private void saveObjects(){
    try{
    String fileName = "counter";
    FileOutputStream out = new FileOutputStream(fileName);
    ObjectOutputStream s = new ObjectOutputStream(out);
    s.writeObject(Counters);
    s.flush();
    }
    catch (IOException e){}


  }

  private void getSavedObjects(){
    try{
    String fileName = "counter";
    FileInputStream in = new FileInputStream(fileName);
    ObjectInputStream s = new ObjectInputStream(in);
    Counters = (Hashtable)s.readObject();
    }
    catch (IOException e){}
    catch (ClassNotFoundException e){}
  }




}
