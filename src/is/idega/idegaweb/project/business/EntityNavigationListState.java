package is.idega.idegaweb.project.business;

import com.idega.event.GenericState;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class EntityNavigationListState extends GenericState {

  public static final int SelectedElementPos = 0;
  protected static final int ENLSmaxPosValue = SelectedElementPos; // value that subclasses can start from

  public EntityNavigationListState(PresentationObject obj, IWContext iwc ) {
    super(obj,iwc);
  }

  public EntityNavigationListState(PresentationObject obj) {
    super(obj);
  }


  public void setSelectedElementID( int id){
    this.setValue(0,Integer.toString(id));
  }

  public int getSelectedElementID(){
    Object obj = this.getValue(0);
    if(obj != null){
      try{
        return Integer.parseInt((String)obj);
      }catch(NumberFormatException e){
        return -1;
      }
    }else{
      return -1;
    }
  }



}
