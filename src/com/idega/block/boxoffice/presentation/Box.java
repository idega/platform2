package com.idega.block.boxoffice.presentation;

import com.idega.block.IWBlock;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.HeaderTable;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.block.boxoffice.data.*;
import com.idega.block.boxoffice.business.*;

public class Box extends Block implements IWBlock {

private int _boxID = -1;
private boolean _isAdmin = false;
private String _attribute;
private int _iLocaleID;

public final static int BOX_VIEW = 1;
public final static int CATEGORY_VIEW = 2;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.boxoffice";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;

private Table _myTable;
private boolean _newObjInst = false;
private boolean _newWithAttribute = false;

private boolean _styles = true;
private int _numberOfColumns;
private String _headerColor;
private String _borderColor;
private String _inlineColor;
private String _boxWidth;
private String _boxHeight;
private int _boxSpacing;
private int _numberOfDisplayed;
private String _categoryStyle;
private String _linkStyle;
private String _visitedStyle;
private String _activeStyle;
private String _hoverStyle;
private String _name;

private String _target;

public Box(){
}

public Box(int boxID){
  this();
	_boxID = boxID;
}

public Box(String attribute){
  this();
	_attribute = attribute;
}

	public void main(IWContext iwc) throws Exception {
    _iwrb = getResourceBundle(iwc);
    _iwb = getBundle(iwc);

    _isAdmin = iwc.hasEditPermission(this);
    //_isAdmin = true;
    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

    String mode = iwc.getParameter(BoxBusiness.PARAMETER_MODE);
    if ( mode != null ) {
      doMode(mode,iwc);
    }

    BoxEntity box = null;

    _myTable = new Table(1,2);
      _myTable.setCellpadding(0);
      _myTable.setCellspacing(0);
      _myTable.setBorder(0);

    if(_boxID <= 0){
      String sBoxID = iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID);
      if(sBoxID != null)
        _boxID = Integer.parseInt(sBoxID);
      else if(getICObjectInstanceID() > 0){
        _boxID = BoxFinder.getObjectInstanceID(getICObjectInstance());
        if(_boxID <= 0 ){
          BoxBusiness.saveBox(_boxID,getICObjectInstanceID(),null);
          _newObjInst = true;
        }
      }
    }

    if ( _newObjInst ) {
      _boxID = BoxFinder.getObjectInstanceID(new ICObjectInstance(getICObjectInstanceID()));
    }

    if(_boxID > 0) {
      box = BoxFinder.getBox(_boxID);
    }
    else if ( _attribute != null ){
      box = BoxFinder.getBox(_attribute);
      if ( box != null ) {
        _boxID = box.getID();
      }
      else {
        BoxBusiness.saveBox(-1,-1,_attribute);
      }
      _newWithAttribute = true;
    }

    if ( _newWithAttribute ) {
      _boxID = BoxFinder.getBox(_attribute).getID();
    }

    int row = 1;
    if(_isAdmin){
      _myTable.add(getAdminPart(),1,row);
      row++;
    }

    _myTable.add(getBox(box),1,row);
    add(_myTable);
	}

  private Table getBox(BoxEntity box) {
    setDefaultValues();
    setStyles();

    Table boxTable = new Table();
      boxTable.setCellpadding(0);
      boxTable.setCellspacing(_boxSpacing);

    int row = 1;
    int column = 1;

    BoxCategory[] categories = BoxFinder.getCategoriesInBox(box);
    if ( categories != null ) {
      for ( int a = 0; a < categories.length; a++ ) {
        Table table = new Table();
          table.setCellpadding(3);
          table.setCellspacing(1);
          table.setWidth(_boxWidth);
          table.setHeight(_boxHeight);
          table.setHeight(2,"100%");
          table.setColor(_borderColor);
          table.setColor(1,1,_headerColor);
          table.setColor(1,2,_inlineColor);
          table.setAlignment(1,1,"center");
          table.setVerticalAlignment(1,1,"middle");
          table.setVerticalAlignment(1,2,"top");

        String categoryString = BoxBusiness.getLocalizedString(categories[a],_iLocaleID);
        if ( categoryString == null ) {
          categoryString = "$language$";
        }

        if ( _isAdmin ) {
          Image deleteImage = _iwb.getImage("shared/delete.gif");
            deleteImage.setAlignment("left");
          Link detachCategory = new Link(deleteImage);
            detachCategory.addParameter(BoxBusiness.PARAMETER_BOX_ID,_boxID);
            detachCategory.addParameter(BoxBusiness.PARAMETER_CATEGORY_ID,categories[a].getID());
            detachCategory.addParameter(BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_DETACH);
          table.add(detachCategory,1,1);
        }

        Text categoryText = new Text(categoryString);
          categoryText.setFontStyle(_categoryStyle);
        table.add(categoryText,1,1);

        Table linksTable = new Table();
          linksTable.setRows(_numberOfDisplayed+1);
          linksTable.setWidth("100%");
        table.add(linksTable,1,2);

        int linkRow = 1;

        BoxLink[] links = BoxFinder.getLinksInBox(box,categories[a]);
        int linksLength = _numberOfDisplayed;
        if ( links != null ) {
          if ( links.length < linksLength ) {
            linksLength = links.length;
          }

          for ( int b = 0; b < linksLength; b++ ) {
            String linkString = BoxBusiness.getLocalizedString(links[b],_iLocaleID);
            if ( linkString != null ) {
              Link link = new Link(linkString);
                if ( _styles ) {
                  link.setStyle(_name);
                }
                else {
                  link.setFontSize(1);
                }
                link.setOnMouseOver("window.status='"+linkString+"'; return true;");
                link.setOnMouseOut("window.status=''; return true;");
              linksTable.add(link,1,linkRow);
              linksTable.setWidth(1,linkRow,"100%");

              String URL = links[b].getURL();
              int fileID = links[b].getFileID();
              int pageID = links[b].getPageID();
              String target = links[b].getTarget();

              if ( URL != null ) {
                if ( URL.indexOf("http://") == -1 ) {
                  URL = "http://"+URL;
                }
                link.setURL(URL);
              }
              else if ( fileID != -1 ) {
                link.setFile(fileID);
              }
              else if ( pageID != -1 ) {
                link.setPage(pageID);
              }
              if ( target != null ) {
                link.setTarget(target);
              }

              Link editLink = new Link(_iwb.getImage("shared/edit.gif"));
                editLink.setWindowToOpen(BoxEditorWindow.class);
                editLink.addParameter(BoxBusiness.PARAMETER_LINK_ID,links[b].getID());
                editLink.addParameter(BoxBusiness.PARAMETER_BOX_ID,_boxID);
              Link deleteLink = new Link(_iwb.getImage("shared/delete.gif"));
                deleteLink.setWindowToOpen(BoxEditorWindow.class);
                deleteLink.addParameter(BoxBusiness.PARAMETER_LINK_ID,links[b].getID());
                deleteLink.addParameter(BoxBusiness.PARAMETER_BOX_ID,_boxID);
                deleteLink.addParameter(BoxBusiness.PARAMETER_DELETE,BoxBusiness.PARAMETER_TRUE);

              if ( _isAdmin ) {
                linksTable.add(editLink,2,linkRow);
                linksTable.add(deleteLink,3,linkRow);
              }
              linkRow++;
            }
          }

          Link addLink = new Link(_iwb.getImage("shared/add.gif"));
            addLink.setWindowToOpen(BoxEditorWindow.class);
            addLink.addParameter(BoxBusiness.PARAMETER_BOX_ID,_boxID);
            addLink.addParameter(BoxBusiness.PARAMETER_CATEGORY_ID,categories[a].getID());
            addLink.addParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE,BoxBusiness.PARAMETER_TRUE);
          if ( _isAdmin ) {
            linksTable.add(addLink,1,_numberOfDisplayed+1);
          }
        }

        if ( column % _numberOfColumns == 0 ) {
          boxTable.add(table,column,row);
          row++;
          column = 1;
        }
        else {
          boxTable.add(table,column,row);
          column++;
        }
      }
    }

    return boxTable;
  }

  private Link getAdminPart() {
    Link adminLink = new Link(_iwrb.getImage("boxmanager.gif"));
      adminLink.setWindowToOpen(BoxEditorWindow.class);
      adminLink.addParameter(BoxBusiness.PARAMETER_BOX_ID,_boxID);
      adminLink.addParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE,BoxBusiness.PARAMETER_TRUE);

    return adminLink;
  }

  private void setDefaultValues() {
    _numberOfColumns = 3;
    _headerColor = "#D8D8D8";
    _borderColor = "#6E6E6E";
    _inlineColor = "#FFFFFF";
    _boxWidth = "120";
    _boxHeight = "120";
    _boxSpacing = 5;
    _numberOfDisplayed = 4;
    _categoryStyle = "font-face: Arial, Helvetica, sans-serif; font-size: 8pt; font-weight: bold";
    _linkStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 8pt; color: #000000; text-decoration: none;";
    _visitedStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 8pt; color: #6E6E6E; text-decoration: none;";
    _activeStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 8pt; color: #D8D8D8; text-decoration: none;";
    _hoverStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 8pt; color: #D8D8D8; text-decoration: underline overline;";
    _target = Link.TARGET_TOP_WINDOW;
  }

  private void setStyles() {
    if ( _name == null )
      _name = this.getName();
    if ( _name == null ) {
      if ( _attribute == null )
        _name = "boxoffice_"+Integer.toString(_boxID);
      else
        _name = "boxoffice_"+_attribute;
    }

    if ( getParentPage() != null ) {
      getParentPage().setStyleDefinition("A."+_name+":link",_linkStyle);
      getParentPage().setStyleDefinition("A."+_name+":visited",_visitedStyle);
      getParentPage().setStyleDefinition("A."+_name+":active",_activeStyle);
      getParentPage().setStyleDefinition("A."+_name+":hover",_hoverStyle);
    }
    else {
      _styles = false;
    }
  }

  private void doMode(String mode, IWContext iwc) {
    if ( mode.equalsIgnoreCase(BoxBusiness.PARAMETER_DETACH) ) {
      String boxID = iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID);
      String boxCategoryID = iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID);
      if ( boxID != null && boxCategoryID != null ) {
        try {
          BoxBusiness.detachCategory(Integer.parseInt(boxID),Integer.parseInt(boxCategoryID));
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
      }
    }
  }

  public boolean deleteBlock(int ICObjectInstanceId) {
    BoxEntity box = BoxFinder.getObjectInstanceFromID(ICObjectInstanceId);
    if ( box != null ) {
      return BoxBusiness.deleteBox(box);
    }
    return false;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public Object clone() {
    Box obj = null;
    try {
      obj = (Box) super.clone();

      if ( this._myTable != null ) {
        obj._myTable = (Table) this._myTable.clone();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
