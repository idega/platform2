package is.idega.experimental;

import com.idega.jmodule.object.interfaceobject.AbstractChooserWindow;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.TreeViewer;
import com.idega.jmodule.object.textObject.Link;


/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class PageChooserWindow extends AbstractChooserWindow {

  public PageChooserWindow(){
    this.setName("PageChooser");
    this.setWidth(300);
    this.setHeight(500);
    add("Select page");
  }

  public void displaySelection(ModuleInfo modinfo){
    try{
      TreeViewer viewer = TreeViewer.getTreeViewerInstance(new com.idega.projects.golf.entity.Union(3),modinfo);
      add(viewer);
      viewer.setToMaintainParameter(SCRIPT_PREFIX_PARAMETER,modinfo);
      viewer.setToMaintainParameter(SCRIPT_SUFFIX_PARAMETER,modinfo);
      viewer.setToMaintainParameter(DISPLAYSTRING_PARAMETER_NAME,modinfo);
      viewer.setToMaintainParameter(VALUE_PARAMETER_NAME,modinfo);

      Link prototype = new Link();
      viewer.setToUseOnClick();
      viewer.setOnClick(SELECT_FUNCTION_NAME+"("+viewer.ONCLICK_DEFAULT_NODE_NAME_PARAMETER_NAME+","+viewer.ONCLICK_DEFAULT_NODE_ID_PARAMETER_NAME+")");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    /*Link link = new Link("tester");
    link.setURL("#");
    link.setOnClick(SELECT_FUNCTION_NAME+"('tester','tester')");
    add(link);*/
  }

}