package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.jmodule.login.presentation.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.boxoffice.presentation.*;
import java.sql.*;
import com.idega.projects.golf.entity.*;
import java.io.*;


public class ISITemplate extends MainSideJSPModule{



  protected Login login;
  protected Table centerTable;
  protected String align;

  protected final int SIDEWIDTH = 720;
  protected final int LEFTWIDTH = 163;
  protected final int RIGHTWIDTH = 148;


  public void initializePage(){
    super.initializePage();

    setTitle("Íþróttasamband Íslands");

    setLinkColor("black");
    setVlinkColor("black");

    setMarginWidth(0);
    setMarginHeight (0);
    setLeftMargin(0);
    setTopMargin(0);

    try{
     User();
    }
    catch (SQLException E) {
    }
    catch (IOException E) {
    }
  }



      protected void User() throws SQLException,IOException{
          setTopMargin(5);
          add( "top", golfHeader());
          add("top", Top());
          //add("bottom", golfFooter());
          add(Left(), Center(), Right());
	  setWidth(1, "" + LEFTWIDTH);
	  setContentWidth( "100%");
	  setWidth(3, "" + RIGHTWIDTH);

      }



      protected Table Top() throws SQLException,IOException{
          Table topTable = new Table(3,1);
          topTable.setCellpadding(0);
          topTable.setCellspacing(0);
          topTable.setHeight("90");

          topTable.setAlignment(2,1,"center");
          topTable.setAlignment(3,1,"center");
          topTable.setVerticalAlignment(1,1, "top");
          topTable.setVerticalAlignment(2,1, "middle");
          topTable.setVerticalAlignment(3,1, "middle");

          topTable.add(new Image("/pics/isi/login.gif"),1,1);
          topTable.add(new Image("/pics/isi/ad.gif"),2,1);
          topTable.add(new Image("/pics/isi/logo.gif"),3,1);

          topTable.setWidth(1, "" + LEFTWIDTH);
          topTable.setWidth("100%");
          topTable.setWidth(3, "" + RIGHTWIDTH);

          return topTable;
      }

        protected Table Left() throws SQLException,IOException{
          Table leftTable = new Table(1,3);
//          leftTable.setBorder(1);
          leftTable.setVerticalAlignment("top");
          leftTable.setVerticalAlignment(1,1,"top");
          leftTable.setVerticalAlignment(1,2,"top");
          leftTable.setVerticalAlignment(1,3,"top");
          leftTable.setHeight("100%");

          leftTable.add(new Image("/pics/isi/samstarfsadilar.gif"),1,1);
          leftTable.add(new Image("/pics/isi/ymislegt.gif"),1,3);

          leftTable.setColumnAlignment(1, "left");

          leftTable.setWidth("" + LEFTWIDTH);

          leftTable.setCellpadding(0);
          leftTable.setCellspacing(0);

          return leftTable;
        }

        protected Table Center() throws SQLException,IOException{

          if (centerTable == null){
            centerTable = new Table(1,1);

            centerTable.setWidth("100%");
            centerTable.setCellpadding(0);
            centerTable.setCellspacing(0);
            centerTable.setAlignment(1,1, "left");

            setVerticalAlignment( "top" );
          }

          return centerTable;
        }


        protected Table Right() throws SQLException,IOException{
          Table rightTable = new Table(1,3);
          rightTable.setWidth("" + RIGHTWIDTH);
          rightTable.setCellpadding(0);
          rightTable.setCellspacing(0);

          rightTable.setVerticalAlignment(1,1,"top");
          rightTable.setVerticalAlignment(1,2,"top");
          rightTable.setVerticalAlignment(1,3,"top");

          rightTable.setColumnAlignment(1, "center");

          rightTable.add(new Image("/pics/isi/erlendarfrettir.gif"),1,1);
          rightTable.add(new Image("/pics/isi/skodanakonnun.gif"),1,3);

          return rightTable;
        }



      protected Table golfHeader(){
          Table golfHeader = new Table(1,2);

          Text zero = new Text("");
          zero.setFontSize("1");

          golfHeader.add(zero ,1,2);

          Table linkTable = new Table(2,1);

          golfHeader.setHeight( 1, "68");
          golfHeader.setHeight( 2, "15");
          setTopHeight("84");
          golfHeader.setWidth("720");
          golfHeader.setCellpadding(0);
          golfHeader.setCellspacing(0);
          golfHeader.setBackgroundImage(1, 1, new Image("/pics/isi/banner.gif"));
          golfHeader.setVerticalAlignment(1,2, "top");

          linkTable.setHeight("14");
          linkTable.setCellpadding(0);
          linkTable.setCellspacing(0);
          linkTable.setWidth("720");
          linkTable.setVerticalAlignment("top");
          linkTable.setRowVerticalAlignment(1,"top");

          Link club = new Link(new Image("/pics/isi/sersambond.gif","Sérsambönd",100,15), "sambond.jsp");
          linkTable.add(club, 1, 1);
          Link other = new Link(new Image("/pics/isi/takkar.gif","Gagnasöfn"), "gagnasofn.html");
          linkTable.add(other,2,1);

          golfHeader.setAlignment(1,2, "top");
          golfHeader.add(linkTable, 1, 2 );

          return golfHeader;
      }


      protected Table golfFooter(){
          Table golfFooter = new Table(6,1);
          golfFooter.setHeight("21");
          setBottomHeight( "21");
          golfFooter.setWidth("720");
          golfFooter.setCellpadding(0);
          golfFooter.setCellspacing(0);

          golfFooter.add(new Image("/pics/templates/botn1.gif"),1,1);
          golfFooter.add(new Link (new Image("/pics/templates/botn2.gif", "Heim"), "/index.jsp"),2,1);
          golfFooter.add(new Image("/pics/templates/botn3.gif"),3,1);
          Image back = new Image("/pics/templates/botn4.gif", "til baka");
          back.setAttribute("OnClick", "history.go(-1)");
          golfFooter.add(back,4,1);
          golfFooter.add(new Image("/pics/templates/botn5.gif"),5,1);
          golfFooter.add(new Link (new Image("/pics/templates/botn6.gif", "golf@idega.is"), "mailto: golf@idega.is"),6,1);

          return golfFooter;
      }


        // ###########  Public - Föll

        public void setVerticalAlignment( String alignment ){
          centerTable.setVerticalAlignment( alignment );
          centerTable.setVerticalAlignment( 1, 1, alignment);

        }


	public void add(ModuleObject objectToAdd){
		//centerTable.add(objectToAdd,1,2);
		try{
			Center().add(objectToAdd,1,1);
		}
		catch(SQLException ex){
			System.err.println(ex.getMessage());
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
	}

	public Member getMember(){
		return (Member)getModuleInfo().getSession().getAttribute("member_login");
	}



        public boolean isAdmin() {

          try{
            return com.idega.jmodule.login.business.AccessControl.isAdmin(getModuleInfo());
          }catch (SQLException E) {
            /*
            out.print("SQLException: " + E.getMessage());
            out.print("SQLState:     " + E.getSQLState());
            out.print("VendorError:  " + E.getErrorCode());*/
          }catch (Exception E) {
		E.printStackTrace();
	  }finally {
	  }
          return false;
        }



}  // class GolfMainJSPModule
