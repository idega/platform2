package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.moduleobject.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.data.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.jmodule.banner.presentation.BannerContainer;

import java.sql.*;
import com.idega.projects.golf.entity.*;
import java.io.*;


public class GolfClubJSPModule2 extends GolfClubJSPModule{

        protected void User()throws SQLException,IOException{

                  getPage().setTextDecoration("none");
                  setTopMargin(5);
                  add( "top", golfHeader());
                  add("top", Top());
                  add("bottom", golfFooter());
                  add(Left(), Center());
                  setWidth(1, "" + LEFTWIDTH);
                  setWidth(2, "" + 556);
                  setContentWidth( "100%");

        }
}
