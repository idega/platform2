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

import com.borland.jbuilder.jot.*;

import java.io.*;

/**
 * Title:        idegaWeb Builder
 * Description:  idegaWeb Builder is a framework for building and rapid development of dynamic web applications
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class EJBWizardOpenTool implements EditorContextActionProvider,ContextActionProvider{

  public EJBWizardOpenTool() {
  }


  public static void initOpenTool(byte major,byte minor){

    /*if(major!= PrimeTime.CURRENT_MAJOR_VERSION)
      return;

    JBuilderMenu.GROUP_Help.add(new BrowserAction("Say Hello"){
        public void actionPerformed(Browser browser){
          JOptionPane.showConfirmDialog(null,"Hello, World!");
        }
      });
    */

    com.borland.primetime.ide.ContentManager.registerContextActionProvider(new EJBWizardOpenTool());
    com.borland.primetime.ide.ContentManager.registerContextActionProvider(new EJBLegacyWizardOpenTool());
    com.borland.primetime.ide.ContentManager.registerContextActionProvider(new IBOServiceWizardOpenTool());
    com.borland.primetime.ide.ContentManager.registerContextActionProvider(new IBOSessionWizardOpenTool());
    //com.borland.primetime.editor.EditorManager.registerContextActionProvider(new EJBWizardOpenTool());
  }
  public int getPriority() {
    return 1;
  }

  public EJBWizard getEJBWizardInstance(String className){
    EJBWizard instance = new EJBWizard(className);
    return instance;
  }

  public Class getEJBWizardClass(){
    return is.idega.tools.EJBWizard.class;
  }


  public Action getContextAction(EditorPane parm1) {
    return new EditorAction(parm1);
  }


  public Action getContextAction(Browser b,Node[] nodes) {
    /**@todo: Implement this com.borland.primetime.editor.EditorContextActionProvider method*/
    return new EditorAction(b,nodes,"[IDO] Update EJB classes");
  }


  public class EditorAction extends UpdateAction implements javax.swing.Action{

    private EditorPane pane;
    private boolean enabled=true;
    private Node[] nodes;
    private File file;
    private String javaClassName;


    public EditorAction(EditorPane pane){
      this.pane=pane;
      EditorDocument doc = (EditorDocument)pane.getDocument();
    }

    public EditorAction(Browser b,Node[] nodes,String nameInMenus){
      super(nameInMenus,'u',nameInMenus,BrowserIcons.ICON_BLANK,BrowserIcons.ICON_BLANK);
      this.nodes = nodes;
    }


    public void actionPerformed(ActionEvent e){
      try{
        Browser.getActiveBrowser().getStatusView().setText("Generating classes for the EJB object");
        Node node = nodes[0];
        FileNode fnode = (FileNode)node;
        com.borland.compiler.frontend.JavaCompiler jc;

        Url url = fnode.getUrl();
        file = url.getFileObject();
        Project project = node.getProject();
        com.borland.jbuilder.node.JBProject jbproj;
        jbproj = (com.borland.jbuilder.node.JBProject)project;
        JotPackages pkg = jbproj.getJotPackages();
        JotFile jotfile = pkg.getFile(url);
        JotClass jotclass = jotfile.getClasses()[0];
        javaClassName = jotclass.getName();
        String className = javaClassName;
        //EJBWizard instance = getEJBWizardInstance(className);
        //EJBWizard instance = new EJBWizard(className);
        //instance.setWorkingDirectory(file.getParentFile());
        //instance.doJavaFileCreate();
        File directoryToRunIn = file.getParentFile();
        //System.out.println("file.getParentFile()="+file.getParentFile().getAbsolutePath());
        String ejbWizardClass = getEJBWizardClass().getName();
        //String ejbWizardClass = instance.getClass().getName();
        //System.out.println("ejbWizardClass="+ejbWizardClass);
        //String ejbWizardClass = "is.idega.tools.EJBWizard";
        //String[] cmdArray = {"java",ejbWizardClass,className};
        String command = "java -cp "+System.getProperty("java.class.path")+" "+ejbWizardClass+" "+className;
        //System.out.println("Command="+command);
        String[] envArray = null;
        Process process = Runtime.getRuntime().exec(command,envArray,directoryToRunIn);

        //System.out.println("Done running the wizard");

        Browser.getActiveBrowser().getStatusView().setText("Finished generating classes for the EJB object");
      }
      catch(Exception ex){
        Browser.getActiveBrowser().getStatusView().setText("Failed generating classes for the EJB object: "+ex.getClass().getName()+" - \""+ex.getMessage()+"\"");
        ex.printStackTrace(System.err);
      }
    }

  }



}