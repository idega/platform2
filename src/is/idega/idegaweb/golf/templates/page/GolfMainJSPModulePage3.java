package is.idega.idegaweb.golf.templates.page;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;


public class GolfMainJSPModulePage3 extends GolfMainJSPModulePage{


  private Table insertTable;
  private String align;


  public GolfMainJSPModulePage3(){
    super();
    initCenter();
  }



       protected void User(IWContext modinfo)throws SQLException,IOException{
              this.setTextDecoration("none");
              setTopMargin(5);
              if( !isDemoSite ){
                centerTable.add(Left(modinfo), 1, 1);
              }
              else{
                centerTable.add(LeftDemoSite(modinfo), 1, 1);
              }

              add( "top", golfHeader());
              add("top", Top(modinfo));
              add("bottom", golfFooter());
              add("middle", Center());
	}


        protected void initCenter(){
          if (centerTable==null){
          	centerTable = new Table(2,1);

          	centerTable.setWidth(1 ,"" + LEFTWIDTH);
          	centerTable.setWidth(2 ,"" + (SIDEWIDTH-LEFTWIDTH));
//        	  centerTable.setWidth("100%");
          	centerTable.setCellpadding(0);
          	centerTable.setCellspacing(0);
          	setVerticalAlignment( "top" );
          }//else centerTable.empty();

        }


        public void setVerticalAlignment( String alignment ){
          centerTable.setVerticalAlignment( alignment );
          centerTable.setVerticalAlignment( 1, 1, alignment);
          centerTable.setVerticalAlignment( 2, 1, alignment);
        }


	public void add(PresentationObject objectToAdd){
	  centerTable.add(objectToAdd,2,1);
	}


}  // class GolfMainJSPModule3
