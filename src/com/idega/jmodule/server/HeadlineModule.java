package com.idega.jmodule.server;

import com.idega.servlet.IWCoreServlet;
import com.idega.util.idegaTimestamp;
import com.idega.jmodule.server.Headlines;
import com.idega.jmodule.server.HeadlineGroup;
import com.idega.util.text.*;
import java.util.*;
import com.idega.io.FileGrabber;
import com.idega.io.OutputWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import com.idega.util.database.*;
import com.idega.data.*;
import java.sql.*;

public class HeadlineModule extends IWCoreServlet implements Runnable{

  //private PoolManager poolMgr;

  Vector headlines;

  private String filterType = "database";
  private String filterSource = "Filters.xml";
  private String filterpage = new String("");
  private String page = new String("");
  private String startTag = new String("<news>");
  private String endTag = new String("</news>");
  private String theSiteB = new String("<site>");
  private String theSiteE = new String("</site>");
  private String theSiteURLB = new String("<siteurl>");
  private String theSiteURLE = new String("</siteurl>");
  private String newsPageB = new String("<newsurl>");
  private String newsPageE = new String("</newsurl>");
  private String startTag1 = new String("<findandcenterbegin>");
  private String startTag2 = new String("</findandcenterbegin>");
  private String endTag1 = new String("<findandcenterend>");
  private String endTag2 = new String("</findandcenterend>");
  private String headlineBegins1 = new String("<headlinebegins>");
  private String headlineBegins2 = new String("</headlinebegins>");
  private String headlineEnds1 = new String("<headlineends>");
  private String headlineEnds2 = new String("</headlineends>");
  private String linkBegins1 = new String("<linkbegins>");
  private String linkBegins2 = new String("</linkbegins>");
  private String linkEnds1 = new String("<linkends>");
  private String linkEnds2 = new String("</linkends>");
  private String Site = new String("");
  private String SiteURL = new String("");
  private String NewsPage = new String("");
  private String startCenterB = new String("");
  private String startCenterE = new String("");
  private String headlineB = new String("");
  private String headlineE = new String("");
  private String linkB = new String("");
  private String linkE = new String("");

  private Thread grabber;



public void init() throws ServletException{
  //Create a thread to do the Headline grabbing
  grabber = new Thread(this);
  grabber.setPriority(Thread.MIN_PRIORITY);
  grabber.start();
}

public void stop(){
  grabber = null;
}

public void run(){

  Thread thisThread = Thread.currentThread();
  while(grabber == thisThread){

  if (filterType.equalsIgnoreCase("file")){
    //headlines = new Vector();
    headlines = getHeadlinesFromAll(filterSource);
          //Filters the headlines into a vector from a source filter file
  }
  else{

    try{
      getHeadlinestoDB();
    }
    catch(SQLException E){
            E.printStackTrace();
    }
    catch(Exception E){
            E.printStackTrace();
    }

  }

  try{
    grabber.sleep(60*1000*120); //sleep for 120 minutes.
  }
  catch(InterruptedException ignored){
    ignored.printStackTrace();
  }

  }


}


public void destroy(){
	this.stop();
	super.destroy();

}


public void getHeadlinestoDB() throws Exception,SQLException{
  FileGrabber myurlgrabber;
  Vector filterArray;// = new Vector(10);
  int n=0;
  int k=0;
  int errorFlag;

  HeadlineGroup[] headlinegroups = (HeadlineGroup[]) (new HeadlineGroup()).findAll();
  if( (headlinegroups!=null) && (headlinegroups.length>0) ){

    System.out.println("Getting headlines...");

    for (int i = 0; i < headlinegroups.length; i++) {

      int group_id = ((HeadlineGroup) headlinegroups[i]).getID();
      String myFiltersFile = ((HeadlineGroup)headlinegroups[i]).getFileName();
      System.out.println("Filter: "+myFiltersFile);

      String filterpage = "";
      String page = "";
      Enumeration enum;
      Enumeration enum2;
      Vector headline = new Vector(10);
      Vector link = new Vector(10);

      //get the filters file
      myurlgrabber = new FileGrabber();
      filterpage = myurlgrabber.getTheURL(myFiltersFile);

      //if nothing fails fill arrays with the info
      if ( !(filterpage.equalsIgnoreCase("Error 1")) && !(filterpage.equalsIgnoreCase("Error 2")) ){
        Vector pageArray = new Vector(10);
        filterArray=TextSoap.FindAllBetween(filterpage,startTag,endTag);//array with the different filters
        enum=filterArray.elements();

        while( enum.hasMoreElements() ){
          //in the filters page
          Site = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteB,theSiteE).firstElement().toString();
          SiteURL = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteURLB,theSiteURLE).firstElement().toString();
          NewsPage = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),newsPageB,newsPageE).firstElement().toString();
          startCenterB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),startTag1,startTag2).firstElement().toString();
          startCenterE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),endTag1,endTag2).firstElement().toString();
          headlineB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineBegins1,headlineBegins2).firstElement().toString();
          headlineE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineEnds1,headlineEnds2).firstElement().toString();
          linkB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkBegins1,linkBegins2).firstElement().toString();
          linkE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkEnds1,linkEnds2).firstElement().toString();

          //the real page
          myurlgrabber = new FileGrabber();
          page = myurlgrabber.getTheURL(NewsPage);

          //if nothing fails filter the page
          if (!(page.equalsIgnoreCase("Error 1")) && !(page.equalsIgnoreCase("Error 2")))	{
            pageArray =  TextSoap.FindAllBetween(page,startCenterB,startCenterE);//make so multiple

            enum2=pageArray.elements();
            String site;
            String theHeadline;
            String theHeadlineurl;
            boolean successful;

            while(enum2.hasMoreElements()) {

            try{
              //Check if there is really a headline or a url in the news text bit.
              if (TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).size() != 0 && TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).size() != 0){
                      //check if the site url base-ref is included with the site's links
                      if ((TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString()).indexOf(SiteURL) != -1) {
                        site = Site;

                        theHeadline=TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).firstElement().toString();
                        //remove any link on the headline
                        theHeadline = cleanHeadline(theHeadline);
                        theHeadlineurl=TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString();
                        if ( theHeadlineurl == null ) {
                                theHeadlineurl = SiteURL;
                        }

                      }
                      else{
                        site= Site;
                        theHeadline=TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).firstElement().toString();
                        //remove any link on the headline
                        theHeadline = cleanHeadline(theHeadline);
                        theHeadlineurl=SiteURL+TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString();
                      }

                      //Check if the headline has been inserted before
                      boolean before = false;

                      //Headlines[] headlineBefore = (Headlines[]) (new Headlines()).findAll("site",site,"headline",theHeadline);
                      Headlines[] headlineBefore = (Headlines[]) (new Headlines()).findAll("select * from headlines where site='"+site+"' and headline='"+theHeadline+"'");
                      if ( (headlineBefore!=null) && (headlineBefore.length > 0) ) {
                          before=true;
                      }

                     idegaTimestamp stampurinn = new idegaTimestamp();
                      Headlines headlineSQL = new Headlines();
                      headlineSQL.setClip("Track");
                      headlineSQL.setHeadline(theHeadline);
                      headlineSQL.setTheURL(theHeadlineurl);
                      headlineSQL.setSite(site);
                      headlineSQL.setGroupID(group_id);
                      headlineSQL.setTimestamp(stampurinn.getTimestampRightNow());

                      if( ! before){
                        headlineSQL.insert();
                      }
              }//end if (TextSoap.Findall....

              enum2.nextElement();
              n++;

            }catch(Exception E){
                    E.printStackTrace(System.out);
            }
            }//end of while

            pageArray.removeAllElements();
            link.removeAllElements();
            headline.removeAllElements();
            n=0;
          }//end of if
          else {
            System.err.println("Couldn't find/read the news url:"+NewsPage);
          }

          enum.nextElement();
          i++;

        }//end of while
      }//end of if filterespage

    }//end of for loop
    System.out.println("Done getting headlines...");

  }//end of if
  else System.err.println("Couldn't find/read filter file");
}





/*
*
*
*/

public String cleanHeadline(String theHeadline){

Vector theHeadlineArray;
int end;

	//remove any link on the headline
	if( theHeadline.startsWith("<")){

		theHeadlineArray = TextSoap.FindAllBetween(theHeadline,">","</");
		theHeadline = theHeadlineArray.firstElement().toString();

		if( theHeadline.startsWith("<")){

		end = theHeadline.indexOf(">");
		theHeadline = theHeadline.substring(end+1,theHeadline.length());

		}
	}

	theHeadline = TextSoap.cleanText(theHeadline);

return theHeadline;

}

public Vector getHeadlinesFromAll(String myFiltersFile){
//


	FileGrabber myurlgrabber;

	Vector pageArray ;//= new Vector(10);
	Vector filterArray;// = new Vector(10);

	int i=0;
	int n=0;
	int k=0;

	Enumeration enum;
	Enumeration enum2;

	Vector headline = new Vector(10);
	Vector link = new Vector(10);
	Vector output = new Vector(10);

	//get the filters file

	myurlgrabber = new FileGrabber();
	filterpage = myurlgrabber.getTheURL(myFiltersFile);


	//if nothing fails fill arrays with the info
	if ( !(filterpage.equalsIgnoreCase("Error 1")) && !(filterpage.equalsIgnoreCase("Error 2")))	{


		filterArray=TextSoap.FindAllBetween(filterpage,startTag,endTag);//array with the different filters

		enum=filterArray.elements();

		while( enum.hasMoreElements() ){

			System.err.println("FilterArray : "+i+" "+filterArray.elementAt(i).toString() +"\n");


			//in the filters page

			Site = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteB,theSiteE).firstElement().toString();
			System.err.println(Site);
			SiteURL = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteURLB,theSiteURLE).firstElement().toString();
			System.err.println(SiteURL);
			NewsPage = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),newsPageB,newsPageE).firstElement().toString();
			System.err.println(NewsPage+"\n");
			startCenterB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),startTag1,startTag2).firstElement().toString();
			System.err.println(startCenterB+"\n");
			startCenterE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),endTag1,endTag2).firstElement().toString();
			System.err.println(startCenterE+"\n");

			headlineB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineBegins1,headlineBegins2).firstElement().toString();
			System.err.println(headlineB+"\n");
			headlineE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineEnds1,headlineEnds2).firstElement().toString();
			System.err.println(headlineE+"\n");

			linkB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkBegins1,linkBegins2).firstElement().toString();
			System.err.println(linkB+"\n");

			linkE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkEnds1,linkEnds2).firstElement().toString();
			System.err.println(linkE+"\n");


			//the real page
			myurlgrabber = new FileGrabber();

			page = myurlgrabber.getTheURL(NewsPage);

			//if nothing fails filter the page
			if ((page != "Error 1") && (page != "Error 2"))	{


				pageArray =  TextSoap.FindAllBetween(page,startCenterB,startCenterE);//make so multiple

				System.err.println(" :possible strings with headlines and links found.");

				output.addElement("<a href="+SiteURL+">"+Site+"</a><br>\n");//ex. mbl.is

				enum2=pageArray.elements();

				while(enum2.hasMoreElements()) {

				try{
					headline.addElement(TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).firstElement().toString());
					System.err.println(" : headers found.");
					link.addElement(TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString());
					System.err.println(" : links found.");

					if (link.elementAt(n).toString().indexOf(SiteURL) != -1) {
					output.addElement("<a href="+link.elementAt(n).toString()+">"+headline.elementAt(n).toString()+"</a><br>\n");
					}
					else output.addElement("<a href="+SiteURL+link.elementAt(n).toString()+">"+headline.elementAt(n).toString()+"</a><br>\n");

					//System.err.print(output[n]);

					enum2.nextElement();
					n++;

				}catch(Exception e){



					output.trimToSize();return output;}

				}//end of while
			pageArray.removeAllElements();
			link.removeAllElements();
			headline.removeAllElements();
			n=0;
			}//end of if
			else output.addElement("Couldn't find/read url file");

		enum.nextElement();
		i++;
		}//end of while

	i=0;
	}//end of if
	else output.addElement("Couldn't find/read filter file");


output.trimToSize();
return output;
}//end of method








public Vector getHeadlinesFrom(String myFiltersFile,String mySite){
	FileGrabber myurlgrabber;


	Vector pageArray ;//= new Vector(10);
	Vector filterArray;// = new Vector(10);

	int i=0;
	int n=0;
	int k=0;


	String filterpage = "";
	String page = "";


	String startTag = "<news>";
	String endTag = "</news>";

	String theSiteB ="<site>";
	String theSiteE = "</site>";

	String theSiteURLB ="<siteurl>";
	String theSiteURLE = "</siteurl>";

	String newsPageB ="<newsurl>";
	String newsPageE = "</newsurl>";

	String startTag1 ="<findandcenterbegin>";
	String startTag2 = "</findandcenterbegin>";
	String endTag1 ="<findandcenterend>";
	String endTag2 = "</findandcenterend>";


	String headlineBegins1 = "<headlinebegins>";
	String headlineBegins2 = "</headlinebegins>";
	String headlineEnds1 = "<headlineends>";
	String headlineEnds2 = "</headlineends>";
	String linkBegins1 = "<linkbegins>";
	String linkBegins2 = "</linkbegins>";
	String linkEnds1 = "<linkends>";
	String linkEnds2 = "</linkends>";

	String Site = "";
	String SiteURL = "";
	String NewsPage = "";
	String startCenterB = "";
	String startCenterE = "";
	String headlineB = "";
	String headlineE = "";
	String linkB = "";
	String linkE = "";
	Enumeration enum;
	Enumeration enum2;

	Vector headline = new Vector(10);
	Vector link = new Vector(10);
	Vector output = new Vector(10);

	//get the filters file

	myurlgrabber = new FileGrabber();
	filterpage = myurlgrabber.getTheURL(myFiltersFile);


	//if nothing fails fill arrays with the info
	if ((filterpage != "Error 1") && (filterpage != "Error 2"))	{


		filterArray=TextSoap.FindAllBetween(filterpage,startTag,endTag);//array with the different filters

		enum=filterArray.elements();

		while( enum.hasMoreElements() ){

			System.err.println("FilterArray : "+i+" "+filterArray.elementAt(i).toString() +"\n");


			//in the filters page

			Site = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteB,theSiteE).firstElement().toString();

			System.err.println("\n"+Site);

			System.err.println(mySite);

			if( Site.equals(mySite) ){

			System.err.println("\n\nGoing through the if statement now");

			SiteURL = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteURLB,theSiteURLE).firstElement().toString();
			System.err.println(SiteURL);
			NewsPage = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),newsPageB,newsPageE).firstElement().toString();
			System.err.println(NewsPage+"\n\t\t\t\t");
			startCenterB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),startTag1,startTag2).firstElement().toString();
			System.err.println(startCenterB+"\n\t\t\t\t");
			startCenterE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),endTag1,endTag2).firstElement().toString();
			System.err.println(startCenterE+"\n\t\t\t\t");

			headlineB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineBegins1,headlineBegins2).firstElement().toString();
			System.err.println(headlineB+"\n\t\t\t\t");
			headlineE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineEnds1,headlineEnds2).firstElement().toString();
			System.err.println(headlineE+"\n\t\t\t\t");

			linkB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkBegins1,linkBegins2).firstElement().toString();
			System.err.println(linkB+"\n\t\t\t\t");

			linkE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkEnds1,linkEnds2).firstElement().toString();
			System.err.println(linkE+"\n\t\t\t\t");


			//the real page
			myurlgrabber = new FileGrabber();

			page = myurlgrabber.getTheURL(NewsPage);

			//if nothing fails filter the page
			if ((page != "Error 1") && (page != "Error 2"))	{


				pageArray =  TextSoap.FindAllBetween(page,startCenterB,startCenterE);//make so multiple

				System.err.println(" :possible strings with headlines and links found.");

				//output.addElement("<a href="+SiteURL+">"+Site+"</a><br>\n\t\t\t\t");//ex. mbl.is

				enum2=pageArray.elements();

				while(enum2.hasMoreElements()) {

				try{
					headline.addElement(TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).firstElement().toString());
					System.err.println(" : headers found.");
					link.addElement(TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString());
					System.err.println(" : links found.");

					if (link.elementAt(n).toString().indexOf(SiteURL) != -1) {
					output.addElement("<a href="+link.elementAt(n).toString()+">"+headline.elementAt(n).toString()+"</a><br>\n\t\t\t\t");
					}
					else output.addElement("<a href="+SiteURL+link.elementAt(n).toString()+">"+headline.elementAt(n).toString()+"</a><br>\n\t\t\t\t");

					//System.err.print(output[n]);

					enum2.nextElement();
					n++;

				}
				catch(Exception e){

					output.removeAllElements();
					output.addElement("Error");
					output.trimToSize();
					return output;
				}

				}//end of while
			pageArray.removeAllElements();
			link.removeAllElements();
			headline.removeAllElements();
			n=0;
			}//end of if page != Error ...
			else output.addElement("Couldn't find/read url file");
			}//end of if( site...
		enum.nextElement();
		i++;

		}//end of while enum.hasMoreElements ...

	i=0;


	}//end of if
	else output.addElement("Couldn't find/read filter file");


output.trimToSize();
return output;
}//end of method


//Makes a text file for Flash Generator
public int getFlashHeadlinesFromAll(String myFiltersFile,String myFileName, String myFilePath){

	OutputWriter myWriter;
	FileGrabber myurlgrabber;

	Vector pageArray ;//= new Vector(10);
	Vector filterArray;// = new Vector(10);

	int i=0;
	int n=0;
	int k=0;
	int errorFlag;

	String filterpage = "";
	String page = "";


	String startTag = "<news>";
	String endTag = "</news>";

	String theSiteB ="<site>";
	String theSiteE = "</site>";

	String theSiteURLB ="<siteurl>";
	String theSiteURLE = "</siteurl>";

	String newsPageB ="<newsurl>";
	String newsPageE = "</newsurl>";

	String startTag1 ="<findandcenterbegin>";
	String startTag2 = "</findandcenterbegin>";
	String endTag1 ="<findandcenterend>";
	String endTag2 = "</findandcenterend>";


	String headlineBegins1 = "<headlinebegins>";
	String headlineBegins2 = "</headlinebegins>";
	String headlineEnds1 = "<headlineends>";
	String headlineEnds2 = "</headlineends>";
	String linkBegins1 = "<linkbegins>";
	String linkBegins2 = "</linkbegins>";
	String linkEnds1 = "<linkends>";
	String linkEnds2 = "</linkends>";

	String Site = "";
	String SiteURL = "";
	String NewsPage = "";
	String startCenterB = "";
	String startCenterE = "";
	String headlineB = "";
	String headlineE = "";
	String linkB = "";
	String linkE = "";
	Enumeration enum;
	Enumeration enum2;

	Vector headline = new Vector(10);
	Vector link = new Vector(10);
	String output = new String("Clip,site,headline,theURL"+"\n");

	//get the filters file

	myurlgrabber = new FileGrabber();
	myWriter = new OutputWriter();

	filterpage = myurlgrabber.getTheURL(myFiltersFile);


	//if nothing fails fill arrays with the info
	if ((filterpage != "Error 1") && (filterpage != "Error 2"))	{



		filterArray=TextSoap.FindAllBetween(filterpage,startTag,endTag);//array with the different filters

		enum=filterArray.elements();

		while( enum.hasMoreElements() ){

			//in the filters page

			Site = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteB,theSiteE).firstElement().toString();
			SiteURL = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),theSiteURLB,theSiteURLE).firstElement().toString();
			NewsPage = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),newsPageB,newsPageE).firstElement().toString();
			startCenterB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),startTag1,startTag2).firstElement().toString();
			startCenterE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),endTag1,endTag2).firstElement().toString();
			headlineB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineBegins1,headlineBegins2).firstElement().toString();
			headlineE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),headlineEnds1,headlineEnds2).firstElement().toString();
			linkB = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkBegins1,linkBegins2).firstElement().toString();
			linkE = TextSoap.FindAllBetween(filterArray.elementAt(i).toString(),linkEnds1,linkEnds2).firstElement().toString();


			//the real page
			myurlgrabber = new FileGrabber();

			page = myurlgrabber.getTheURL(NewsPage);

			//if nothing fails filter the page
			if ((page != "Error 1") && (page != "Error 2"))	{


				pageArray =  TextSoap.FindAllBetween(page,startCenterB,startCenterE);//make so multiple

				enum2=pageArray.elements();

				while(enum2.hasMoreElements()) {

				try{
					if ((TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString()).indexOf(SiteURL) != -1) {
					output += "\"Track\",\""+Site+"\","+"\""+TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).firstElement().toString()+"\","+"\""+TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString()+"\""+"\n";

					}
					else output += "\"Track\",\""+Site+"\","+"\""+TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),headlineB,headlineE).firstElement().toString()+"\","+"\""+SiteURL+TextSoap.FindAllBetween(pageArray.elementAt(n).toString(),linkB,linkE).firstElement().toString()+"\""+"\n";
					//System.err.print(output[n]);

					enum2.nextElement();
					n++;

				}catch(Exception e){
					//
					}

				}//end of while
			pageArray.removeAllElements();
			link.removeAllElements();
			headline.removeAllElements();
			n=0;
			}//end of if
			//else output += "\"Track\",\""+"No News"+"\","+"\""+" "+"\","+"\""+""+"\""+"\n";


		enum.nextElement();
		i++;
		}//end of while

	i=0;
	}//end of if
	else System.err.println("Couldn't find/read filter file");

//output += "\""+endOfList+"\"\n";
errorFlag = myWriter.WriteToFile(output, myFileName, myFilePath);

return errorFlag;

}//end of method

}//end of class
