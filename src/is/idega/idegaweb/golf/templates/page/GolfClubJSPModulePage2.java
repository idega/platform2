package is.idega.idegaweb.golf.templates.page;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.presentation.IWContext;


public class GolfClubJSPModulePage2 extends GolfClubJSPModulePage{

        protected void User(IWContext modinfo)throws SQLException,IOException{

                  this.setTextDecoration("none");
                  setTopMargin(5);
                  add( "top", golfHeader());
                  add("top", Top(modinfo));
                  add("bottom", golfFooter());
                  add(Left(modinfo), Center());
                  setWidth(1, "" + LEFTWIDTH);
                  setWidth(2, "" + 556);
                  setContentWidth( "100%");

        }
}
