package is.idega.idegaweb.golf.templates.page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import is.idega.idegaweb.golf.*;
import is.idega.idegaweb.golf.moduleobject.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.jmodule.text.data.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.jmodule.banner.presentation.BannerContainer;

import java.sql.*;
import is.idega.idegaweb.golf.entity.*;
import java.io.*;


public class GolfClubJSPModulePage2 extends GolfClubJSPModulePage{

        protected void User(IWContext iwc)throws SQLException,IOException{

                  this.setTextDecoration("none");
                  setTopMargin(5);
                  add( "top", golfHeader());
                  add("top", Top(iwc));
                  add("bottom", golfFooter());
                  add(Left(iwc), Center());
                  setWidth(1, "" + LEFTWIDTH);
                  setWidth(2, "" + 556);
                  setContentWidth( "100%");

        }
}
