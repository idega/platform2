
/**
 * Title:        null<p>
 * Description:  null<p>
 * Copyright:    null<p>
 * Company:      idega multimedia<p>
 * @author null
 * @version null
 */
package is.idega.idegaweb.golf.templates.page;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;


public class GolfMainJSPModulePage2 extends GolfMainJSPModulePage{


  //private Table mainTable;



  public GolfMainJSPModulePage2(){
    super();
    //initMainTable();
  }






  protected void User(IWContext modinfo) throws SQLException,IOException{
    this.setTextDecoration("none");
    setTopMargin(5);
    add("middle", Center());
    Top(modinfo);
    add( "top", golfHeader());
    add("top", Top(modinfo));
    add("bottom", golfFooter());
  }




        protected void initCenter(){
          centerTable = new Table (1,1);
          centerTable.setVerticalAlignment(1,1,"top");
//          mainTable.setBorder(1);
          centerTable.setCellpadding(0);
          centerTable.setCellspacing(0);
          centerTable.setWidth("100%");
          centerTable.setHeight("100%");
        }



        public void setVerticalAlignment( String alignment ){
          centerTable.setVerticalAlignment( 1, 1, alignment);
        }



	public void add(PresentationObject objectToAdd){
	  centerTable.add(objectToAdd,1,1);
	}






}   //class GolfMainJSPModule2
