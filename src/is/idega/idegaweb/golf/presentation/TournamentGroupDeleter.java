package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.ui.*;
import is.idega.idegaweb.golf.moduleobject.GolfDialog;
import com.idega.presentation.*;
import java.sql.SQLException;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur</a>
*@version 1.0
*/
 public class TournamentGroupDeleter extends TournamentAdmin {

    public TournamentGroupDeleter(){
        super();
        //System.out.println("TournamentGroupDeleter()");
    }


    public void main(IWContext iwc)throws SQLException{
        add("TournamentGroupDeleter");
    }


}// class TournamentGroupDeleter


