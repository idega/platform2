package is.idega.tools.opentool;

import com.borland.primetime.*;
import com.borland.jbuilder.*;
import com.borland.primetime.ide.*;

import is.idega.tools.*;

import javax.swing.*;
import java.awt.event.*;

import com.borland.primetime.editor.*;
import com.borland.primetime.node.*;
import com.borland.primetime.vfs.*;
import com.borland.primetime.actions.*;


/**
 * Title:        idegaWeb Builder
 * Description:  idegaWeb Builder is a framework for building and rapid development of dynamic web applications
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class EJBLegacyWizardOpenTool extends EJBWizardOpenTool {

  public EJBLegacyWizardOpenTool() {
  }

    public EJBWizard getEJBWizardInstance(String className){
      EJBWizard instance = new EJBLegacyWizard(className);
      return instance;
    }

  public Class getEJBWizardClass(){
    return is.idega.tools.EJBLegacyWizard.class;
  }


  public Action getContextAction(Browser b,Node[] nodes) {
    /**@todo: Implement this com.borland.primetime.editor.EditorContextActionProvider method*/
    return new EditorAction(b,nodes,"[IDO Legacy] Update EJB classes");
  }

}