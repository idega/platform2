package is.idega.idegaweb.project.presentation;

import is.idega.idegaweb.project.business.EntityNavigationListState;

import java.util.List;
import java.util.ListIterator;

import com.idega.data.EntityAttribute;
import com.idega.data.IDOLegacyEntity;
import com.idega.event.GenericState;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public abstract class EntityNavigationList extends Block {

  protected Table table = null;

  protected String[] linkColumns = null;

  protected int columns = 2;
  protected int extraRows = 0;
  protected int iterStartIndex = 1;
  protected int nameColumn = 2;
  protected int initSelectedElement = -1;
  protected int minimumNumberOfRows = 8;

  protected int cellspacing = 0;
  protected int cellpadding = 0;
  protected boolean linesBeetween = true;
  protected boolean bottomLine = true;
  protected boolean topLine = false;
  boolean addLinkBefore = false;


  protected String separator = " ";

  protected String sebracolor1 = "#FFFFFF";
  protected String sebracolor2 = "#CCCCCC";
  protected String selectedColor = "#E9E9B7";
  protected String lineColor = "#333333";

  protected String width = "181";
  protected String rowHeight = "20";

  public static final String _SELECTED_ENTITY_ID = "enav_selentid";

  private Object _firstLinkDisplayObj = "All";

  public EntityNavigationList() {
    super();
    table = new Table();
    linkColumns = new String[0];
    this.add(table);
    initDefaultState();
  }

  public void initDefaultState(){
    EntityNavigationListState defState = new EntityNavigationListState(this);
    defState.setSelectedElementID(-1);
    this.setDefaultState(defState);
  }

  public void setMinimumNumberOfRows(int number){
    minimumNumberOfRows = number;
  }

  public void setSebraColor(String color1, String color2){
    sebracolor1 = color1;
    sebracolor2 = color2;
  }

  public void setRowColor(String color){
    sebracolor1 = color;
    sebracolor2 = color;
  }

  public void setLineColor(String color){
    lineColor = color;
  }

  public void setWidth(String width){
    this.width = width;
  }

  public void setRowHeight(String rowHeight){
    this.rowHeight = rowHeight;
  }

  public void setSelectedColor(String color){
    selectedColor = color;
  }


  public void add(PresentationObject prObject, int xpos, int ypos){
    table.add(prObject,xpos,ypos);
  }

  public abstract List getEntityList(IWContext iwc) throws Exception;

  public abstract void initColumns(IWContext iwc) throws Exception;

  public void addLinkEntityColumn(String columnName){
    // increase length
    String[] newlinkColumns = new String[linkColumns.length+1];
    System.arraycopy(linkColumns,0,newlinkColumns,0,linkColumns.length);
    linkColumns = newlinkColumns;
    // done
    linkColumns[linkColumns.length-1] = columnName;
  }

  protected void addParameters(IWContext iwc, IDOLegacyEntity item, Link link){
    if( item != null){
      link.addParameter(_SELECTED_ENTITY_ID,item.getID());
    }else{
      link.addParameter(_SELECTED_ENTITY_ID,-1);
    }
  }

  private Object getDisplayedText(IDOLegacyEntity item){
    if(item != null){
      if(linkColumns != null){
        if(linkColumns.length == 1){
          Object toReturn = null;
          toReturn = item.getColumnValue(linkColumns[0]);
          if(toReturn instanceof PresentationObject || toReturn instanceof String){
            return toReturn;
          }else if(toReturn instanceof Integer){
            return ((Integer)toReturn).toString();
          }else{
            System.err.print(this+": datatype not instance of presentationObject, java.lang.String");
            return "link";
          }
        }else {
          String returnString = "";
          for (int i = 0; i < linkColumns.length; i++) {
            if(i!= 0){
              returnString += separator;
            }
            if(item.getStorageClassType(linkColumns[i]) == EntityAttribute.TYPE_JAVA_LANG_STRING){
              returnString += (String)item.getColumnValue(linkColumns[i]);
            } else if (item.getStorageClassType(linkColumns[i]) == EntityAttribute.TYPE_JAVA_LANG_INTEGER){
              returnString += ((Integer)item.getColumnValue(linkColumns[i])).toString();
            }else{
              System.err.println(this+": datatype not supported in multivalue mode");
            }
          }
          if(returnString.equals("")){
            returnString = "link";
          }
          return returnString;
        }
      } else {
        System.err.println(this+": entitycolumns are not defined ");
        return "link";
      }
    }else{
      return null;
    }
  }

  public void initializeInMain(IWContext iwc) throws Exception{
    super.initializeInMain(iwc);
    this.initColumns(iwc);
    if(addLinkBefore){
      extraRows += 1;
      initSelectedElement = 1;
      iterStartIndex += 1;
    }
  }

  public void setAddLinkBefore(boolean value){
    addLinkBefore = value;
  }

  public void setFirstLinkFace(PresentationObject face){
    _firstLinkDisplayObj = face;
  }

  public void setFirstLinkText(String localizedKey){
    _firstLinkDisplayObj = localizedKey;
  }

  public void main(IWContext iwc) throws Exception {

    IWBundle iwb = this.getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
    //System.err.println("EntityNavigationList hashcode: "+ this.hashCode());
    EntityNavigationListState state = (EntityNavigationListState)this.getState(iwc);
    //temp table.empty();
    //System.err.println("table.empty()");
    //initColumns(iwc);
    int selectedElement = -1;
    int selectedItem = state.getSelectedElementID();
    //System.err.println("selectedItem: "+selectedItem + " and getDefaultState()).getSelectedElementID():"+((EntityNavigationListState)this.getDefaultState()).getSelectedElementID());
    PresentationObject firstT = null;

    if(addLinkBefore){
        Object tObj = getDisplayedText(null);
        if(tObj == null){
          if(_firstLinkDisplayObj instanceof String){
            tObj = iwrb.getLocalizedString((String)_firstLinkDisplayObj,(String)_firstLinkDisplayObj);
          } else {
            tObj = _firstLinkDisplayObj;
          }
        }
        if(selectedItem <=0 && initSelectedElement == 1){
          selectedElement = 1;
        }
        if (tObj instanceof String){
          firstT = new Link((String)tObj);
          if(selectedElement == 1){
            ((Link)firstT).setBold(true);
          }
          ((Link)firstT).setFontSize(1);
        } else {
          firstT = new Link((PresentationObject)tObj);
        }
        addParameters(iwc, null, (Link)firstT);
    }

    List projects = getEntityList(iwc);

    if( projects != null && projects.size() > 0){
      //System.err.println("table.resize("+columns+","+Math.max(projects.size()+extraRows,minimumNumberOfRows)+")");
      table.resize(columns,Math.max(projects.size()+extraRows,minimumNumberOfRows));
      //System.err.println("table.getColuns()="+table.getColumns()+" table.getRows()="+table.getRows());
      selectedElement = (projects.size()>=initSelectedElement)?initSelectedElement:-1;
      ListIterator lIter = projects.listIterator();
      int toAddToIndex = (extraRows<iterStartIndex)?iterStartIndex-1:extraRows;
      if(addLinkBefore){
        table.add(firstT,nameColumn,1);
      }
      while (lIter.hasNext()) {
        int index = (lIter.nextIndex()+1)+toAddToIndex;
        IDOLegacyEntity lItem = (IDOLegacyEntity)lIter.next();
        if(selectedItem == lItem.getID()){
          selectedElement = index;
        }
        PresentationObject t = null;
        Object tObj = getDisplayedText(lItem);
        if(tObj == null){
          if(_firstLinkDisplayObj instanceof String){
            tObj = iwrb.getLocalizedString((String)_firstLinkDisplayObj,(String)_firstLinkDisplayObj);
          } else {
            tObj = _firstLinkDisplayObj;
          }
        }
        /*if(selectedElement > 0 && (selectedElement == index)){
          if (tObj instanceof String){
            t = new Text((String)tObj);
            ((Text)t).setFontSize(1);
          } else {
            t = (PresentationObject)tObj;
          }
        } else {*/
          if (tObj instanceof String){
            t = new Link((String)tObj);
            ((Link)t).setFontSize(1);
            if(selectedItem == lItem.getID()){
              ((Link)t).setBold(true);
            }
          } else {
            t = new Link((PresentationObject)tObj);
          }
          addParameters(iwc, lItem, (Link)t);
        //}
        table.add(t,nameColumn,index);
      }

      table.setHorizontalZebraColored(this.sebracolor1,this.sebracolor2);
      if(selectedElement > 0){
        table.setRowColor(selectedElement,this.selectedColor);
      }

    } else {
      int min = Math.max(1,minimumNumberOfRows);
      table.resize(columns,min);
      if(addLinkBefore){
        table.add(firstT,nameColumn,1);
      }
      table.setHorizontalZebraColored(this.sebracolor1,this.sebracolor2);
    }
    table.setCellpadding(this.cellpadding);
    table.setCellspacing(this.cellspacing);
    table.setWidth(1,"10");
    table.setLinesBetween(linesBeetween);
    //table.setBottomLine(bottomLine);
    table.setTopLine(topLine);
    table.setLineColor(lineColor);
    table.setWidth(this.width);
    //table.setBorder(1);


    for (int i = 1; i <= table.getRows(); i++) {
      table.setHeight(i,rowHeight);
    }




    //super._main(iwc);
  }



  public GenericState getStateInstance(IWContext iwc){
    return new EntityNavigationListState(this, iwc);
  }

  public String changeState(PresentationObject source, IWContext iwc){
    EntityNavigationListState oldState = (EntityNavigationListState)this.getState(iwc);

    if(this.equals(source)){
      String selected = iwc.getParameter(_SELECTED_ENTITY_ID);
      if(selected != null){
        try {
          oldState.setSelectedElementID(Integer.parseInt(selected));
        }
        catch (NumberFormatException ex) {
          //
        }
      }
    }
    return oldState.getStateString();
  }


  public synchronized Object clone(){
    EntityNavigationList obj = (EntityNavigationList)super.clone();
    if(table != null){
      obj.table = (Table)this.table.clone();
      obj.empty();
      obj.add(obj.table);
    }

    if(linkColumns != null){
      obj.linkColumns = (String[])this.linkColumns.clone();
    }

    obj.columns = this.columns;
    obj.extraRows = this.extraRows;
    obj.iterStartIndex = this.iterStartIndex;
    obj.nameColumn = this.nameColumn;
    obj.initSelectedElement = this.initSelectedElement;
    obj.minimumNumberOfRows = this.minimumNumberOfRows;

    obj.cellspacing = this.cellspacing;
    obj.cellpadding = this.cellpadding;
    obj.linesBeetween = this.linesBeetween;
    obj.bottomLine = this.bottomLine;
    obj.topLine = this.topLine;

    obj.separator = this.separator;

    obj.sebracolor1 = this.sebracolor1;
    obj.sebracolor2 = this.sebracolor2;
    obj.selectedColor = this.selectedColor;
    obj.lineColor = this.lineColor;

    obj.width = this.width;
    obj.rowHeight = this.rowHeight;

    return obj;
  }


}

