package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.IWContext;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur</a>
*@version 1.0
*/
 public class TournamentAdmin extends JModuleAdminWindow {

    public TournamentAdmin(){
      super();
      super.setMenubar(false);
    }

    public void _main(IWContext modinfo)throws Exception{
       this.empty();
       super._main(modinfo);
    }


}// class PaymentViewer


