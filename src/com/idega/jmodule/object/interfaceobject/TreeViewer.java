package com.idega.jmodule.object.interfaceobject;

import java.util.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.util.text.*;
import com.idega.core.ICTreeNode;
import com.idega.idegaweb.IWBundle;



public class TreeViewer extends ModuleObjectContainer{

private static final String NODE_PARAMETER="iw_tv_node_id";
private static final String NODE_ACTION_OPEN_PARAMETER="iw_tv_open";
private static final String NODE_ACTION_CLOSE_PARAMETER="iw_tv_close";
private static final String NODE_ACTION_NODE_SELECTED="iw_tv_node";

 private static final String TREEVIEW_PREFIX = "treeviewer_";
 private static final String TREEVIEW_MINUS="minus_";
 private static final String TREEVIEW_PLUS="plus_";
 private static final String TREEVIEW_LINE="line_";
 private static final String TREEVIEW_LINE_SIMPLE="line";

 private static final String GIF_SUFFIX=".gif";

 private static final String TREEVIEWER_NODE_LEAF = "node_leaf";
 private static final String TREEVIEWER_NODE_OPEN = "node_open";
 private static final String TREEVIEWER_NODE_CLOSED = "node_closed";

 public static final String ONCLICK_FUNCTION_NAME="treenodeselect";
 public static final String ONCLICK_DEFAULT_NODE_ID_PARAMETER_NAME="iw_node_id";
 public static final String ONCLICK_DEFAULT_NODE_NAME_PARAMETER_NAME="iw_node_name";


private ICTreeNode startNode;
private Table mainTable;
private int nestLevelAtOpen=0;
private Link linkPrototype;
private String target;
private int tableColumns=10;
private int tableRows=1;
private String nodeActionParameter=NODE_ACTION_NODE_SELECTED;
private boolean usesOnClick=false;


private TreeViewer(ICTreeNode startNode){
  this.startNode=startNode;
}

public static TreeViewer getTreeViewerInstance(ICTreeNode node,ModuleInfo modinfo){
    TreeViewer viewer = new TreeViewer(node);
    return viewer;
}

private ICTreeNode getStartNode(){
  return startNode;
}

private Table getTable(){
    if(mainTable==null){
      mainTable = new Table(tableColumns,tableRows);
      mainTable.setResizable(true);
    }
    return mainTable;
}


public void main(ModuleInfo modinfo){
    IWBundle bundle = getBundle(modinfo);
    Table tab = getTable();
    add(tab);
    ICTreeNode startNode = getStartNode();
    int[] parentArray = {};
    addNode(modinfo,tab,1,1,startNode,parentArray,'F',0,bundle);
}

private ModuleObject getNodeText(ICTreeNode node,boolean nodeIsOpen,IWBundle bundle){
  Link proto = (Link)getLinkPrototype().clone();
  String nodeName = node.getNodeName();
  proto.setText(nodeName);
  if(usesOnClick){
    proto.setURL("#");
    proto.setOnClick(ONCLICK_FUNCTION_NAME+"('"+nodeName+"','"+node.getNodeID()+"')");

  }
  else{
    proto.addParameter(nodeActionParameter,node.getNodeID());
  }
  return proto;
}

private ModuleObject getNodeIcon(ICTreeNode node,boolean nodeIsOpen,IWBundle bundle){
  Link proto = (Link)getLinkPrototype().clone();
  Image image = null;
  if(this.isLeafNode(node)){
    image = bundle.getImage(TREEVIEW_PREFIX+TREEVIEWER_NODE_LEAF+GIF_SUFFIX);
  }
  else if(nodeIsOpen){
    image = bundle.getImage(TREEVIEW_PREFIX+TREEVIEWER_NODE_OPEN+GIF_SUFFIX);
  }
  else{
    image = bundle.getImage(TREEVIEW_PREFIX+TREEVIEWER_NODE_CLOSED+GIF_SUFFIX);
  }
  proto.setModuleObject(image);
  if(usesOnClick){
    String nodeName = node.getNodeName();
    proto.setURL("#");
    proto.setOnClick(ONCLICK_FUNCTION_NAME+"('"+nodeName+"','"+node.getNodeID()+"')");
  }
  else{
    proto.addParameter(nodeActionParameter,node.getNodeID());
  }
  return proto;
}


private ModuleObject getTreeLines(ICTreeNode node,boolean nodeOpen, int[] parentarray,char typeOfNode,IWBundle bundle){

  if(isLeafNode(node)){
      Image image = bundle.getImage(TREEVIEW_PREFIX+TREEVIEW_LINE+typeOfNode+GIF_SUFFIX);
      return image;
  }
  else{
    if(nodeOpen){
      Image image = bundle.getImage(TREEVIEW_PREFIX+TREEVIEW_MINUS+typeOfNode+GIF_SUFFIX);
      Link link = new Link(image);
      link.setToMaintainGlobalParameters();
      link.addParameter(NODE_ACTION_CLOSE_PARAMETER,Integer.toString(node.getNodeID()));
      addParametersToLink(link,parentarray);
      return link;
    }
    else{
      Image image = bundle.getImage(TREEVIEW_PREFIX+TREEVIEW_PLUS+typeOfNode+GIF_SUFFIX);
      Link link = new Link(image);
      link.setToMaintainGlobalParameters();
      link.addParameter(NODE_ACTION_OPEN_PARAMETER,Integer.toString(node.getNodeID()));
      addParametersToLink(link,parentarray);
      return link;
    }
  }
}

private ModuleObject getSimpleTreeLine(IWBundle bundle){
  return bundle.getImage(TREEVIEW_PREFIX+TREEVIEW_LINE_SIMPLE+GIF_SUFFIX);
}

private void addParametersToLink(Link link,int[] parentArray){
  for (int i = 0; i < parentArray.length; i++) {
      link.addParameter(NODE_PARAMETER,Integer.toString(parentArray[i]));
  }
}

private int addNode(ModuleInfo modinfo,Table table,int xpos,int ypos,ICTreeNode node,int[] parentarray,char typeOfNode,int recurseLevel,IWBundle bundle){

  boolean nodeIsOpen = isNodeOpen(node,recurseLevel,modinfo);
  int[] newparentarray = new int[parentarray.length+1];
  System.arraycopy(parentarray,0,newparentarray,0,parentarray.length);
  newparentarray[parentarray.length]=node.getNodeID();

  tableRows=ypos;

  if((xpos+2)>tableColumns){
    tableColumns=xpos+2;
    getTable().resize(tableColumns,tableRows);
  }
  table.add(getNodeIcon(node,nodeIsOpen,bundle),xpos+1,ypos);
  table.add(getNodeText(node,nodeIsOpen,bundle),xpos+2,ypos);
  table.mergeCells(xpos+2,tableRows,tableColumns,tableRows);


  table.add(getTreeLines(node,nodeIsOpen,parentarray,typeOfNode,bundle),xpos,ypos);
  if(recurseLevel>1){
    int dummy = recurseLevel-1;;
    while(dummy>0){
        table.add(getSimpleTreeLine(bundle),xpos-dummy,ypos);
        dummy--;
    }
  }

  if(nodeIsOpen){

    Iterator iter = node.getChildren();
    ICTreeNode oldNode = null;
    int oldypos=1;
    if(iter!=null){
    while (iter.hasNext()) {
      ICTreeNode newNode = (ICTreeNode)iter.next();
      if(oldNode==null){

      }
      else{

        ypos = addNode(modinfo,table,xpos+1,oldypos,oldNode,newparentarray,'M',recurseLevel+1,bundle);

      }
      oldypos=ypos+1;
      oldNode=newNode;
      ypos++;
      tableRows=ypos;
    }


    addNode(modinfo,table,xpos+1,oldypos,oldNode,newparentarray,'L',recurseLevel+1,bundle);


  }
  }
  return ypos;

}

public void setTarget(String target){
  this.target=target;
}

public String getTarget(){
  return target;
}

public void setLinkProtototype(Link link){
  linkPrototype=link;
}

private Link getLinkPrototype(){
  if(linkPrototype==null){
    linkPrototype=new Link();
    String target=getTarget();
    if(target!=null){
      linkPrototype.setTarget(target);
    }
  }
  return linkPrototype;
}

private boolean isLeafNode(ICTreeNode node){
  boolean theReturn = node.isLeaf();
  return theReturn;
}

private boolean isNodeOpen(ICTreeNode node,int nestLevel,ModuleInfo modinfo){
  String actionClose=modinfo.getParameter(NODE_ACTION_CLOSE_PARAMETER);
  String actionOpen=modinfo.getParameter(NODE_ACTION_OPEN_PARAMETER);

  boolean doingOpen = actionOpen!=null;
  boolean doingClose = actionClose!=null;

  boolean check = (doingOpen)||(doingClose);

  if(check){
  String[] parvalues = modinfo.getParameterValues(NODE_PARAMETER);
    if(parvalues!=null){
      for (int i = 0; i < parvalues.length; i++) {
        try{
          if(node.getNodeID()==Integer.parseInt(parvalues[i])){
            if(doingClose){
              if(Integer.parseInt(actionClose)==node.getNodeID()){
                return false;
              }
              else{
                return true;
              }
            }
            else{
              return true;
            }
          }
          else if(doingClose){
            if(Integer.parseInt(actionClose)==node.getNodeID()){
              return false;
            }
          }
          else if(doingOpen){
            if(Integer.parseInt(actionOpen)==node.getNodeID()){
              return true;
            }
          }
        }
        catch(NumberFormatException e){
        }
      }
      return false;
    }
    else if(doingClose){
      if(Integer.parseInt(actionClose)==node.getNodeID()){
        return false;
      }
    }
    else if(doingOpen){
      if(Integer.parseInt(actionOpen)==node.getNodeID()){
        return true;
      }
    }
    return false;
  }
  else{
    if(nestLevel < getNestLevelAtOpen()){
      return true;
    }
    return false;
  }

}


public void setNestLevelAtOpen(int nodesDown){
  int nestLevelAtOpen=nodesDown;
}

public int getNestLevelAtOpen(){
  return nestLevelAtOpen;
}

public void setNodeActionParameter(String parameterName){
  nodeActionParameter=parameterName;
}

public void setToUseOnClick(){
  setToUseOnClick(ONCLICK_DEFAULT_NODE_NAME_PARAMETER_NAME,ONCLICK_DEFAULT_NODE_ID_PARAMETER_NAME);
}

public void setToUseOnClick(String NodeNameParameterName,String NodeIDParameterName){
  usesOnClick=true;
  getAssociatedScript().addFunction(ONCLICK_FUNCTION_NAME,"function "+ONCLICK_FUNCTION_NAME+"("+NodeNameParameterName+","+NodeIDParameterName+"){ }");

}

public void setOnClick(String action){
   this.getAssociatedScript().addToFunction(ONCLICK_FUNCTION_NAME,action);
}

}
