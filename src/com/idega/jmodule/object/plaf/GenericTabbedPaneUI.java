package com.idega.jmodule.object.plaf;

import com.idega.jmodule.object.Table;
import java.util.Vector;
import com.idega.jmodule.object.ModuleObject;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.util.EventListener;
import javax.swing.SingleSelectionModel;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.util.IWColor;

/**
 * Title:        idegaWeb project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author
 * @version 1.0
 */

public abstract class GenericTabbedPaneUI implements IWTabbedPaneUI {


  private TabbedPaneFrame frame;
  private TabPresentation tab;
  private TabPagePresentation tabpage;
  private IWColor MainColor;


  public GenericTabbedPaneUI(){
    setMainColor(new IWColor(212,208,200));
//    initFrame();
    initTab();
    initTabPage();
  }


//  public abstract void initFrame();
  public abstract void initTab();
  public abstract void initTabPage();

//  public void setFrame(TabbedPaneFrame frame){
//    this.frame = frame;
//  }

  public void setTab(TabPresentation tab){
    this.tab = tab;
  }

  public void setTabPage(TabPagePresentation page){
    this.tabpage = page;
  }


//  public TabbedPaneFrame getFrame(){
//    if (frame == null)
//      initFrame();
//    return frame;
//  }

  public TabPresentation getTabPresentation(){
    if (tab == null)
      initTab();
    return tab;
  }

  public TabPagePresentation getTabPagePresentation(){
    if (tabpage == null)
      initTabPage();
    return tabpage;
  }

  public void setMainColor(IWColor color){
    this.MainColor = color;
  }

  public IWColor getMainColor(){
    return MainColor;
  }







  public abstract class GenericTabPresentation extends Table implements TabPresentation {

    protected Vector AllTabsAdded;
    protected ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    private int index = -1;

    public GenericTabPresentation(){
      super();
      AllTabsAdded = new Vector();
    }


    public void add(ModuleObject obj, int index){
      this.AllTabsAdded.insertElementAt(obj, index);
    }

    public void empty(int index){
      this.AllTabsAdded.remove(index);
    }

//  public void empty(ModuleObject obj){}

    public abstract void setWidth(String width);
    public abstract void SetHeight(String height);

    public Vector getAddedTabs(){
      return AllTabsAdded;
    }

    public void setAddedTabs(Vector tabs){
      AllTabsAdded = tabs;
    }


    // SingleSelectionModel methods

    public int getSelectedIndex() {
        return index;
    }

    public void setSelectedIndex(int index) {
        if (this.index != index) {
            this.index = index;
	    fireStateChanged();
        }
    }

    public void clearSelection() {
        setSelectedIndex(-1);
    }

    public boolean isSelected() {
	boolean ret = false;
	if (getSelectedIndex() != -1) {
	    ret = true;
	}
	return ret;
    }

    public void addChangeListener(ChangeListener l) {
	listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
	listenerList.remove(ChangeListener.class, l);
    }

    public void fireStateChanged() {
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==ChangeListener.class) {
		// Lazily create the event:
		if (changeEvent == null)
		    changeEvent = new ChangeEvent(this);
		((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
	    }
	}
    }

    public EventListener[] getListeners(Class listenerType) {
	return listenerList.getListeners(listenerType);
    }





  } // InnerClass GenericTabPresentation

  public abstract class GenericTabPagePresentation  extends Table implements TabPagePresentation {

    public GenericTabPagePresentation(){
      super();
    }

//  public void add(ModuleObject obj){}
//  public void empty(){}
    public abstract void setWidth(String width);
    public abstract void setHeight(String height);
    public void fireContentChange(){}

  } // InnerClass GenericTabPagePresentation





//  public abstract class GenericTabbedPaneFrame extends Table implements TabbedPaneFrame {
//
//    public SubmitButton OK;
//    public SubmitButton Cancel;
//    public SubmitButton Apply;
//
//    public GenericTabbedPaneFrame(){
//      super();
//      initTabbedPaneFrame();
//    }
//
//    public abstract void initTabbedPaneFrame();
//    public abstract void initOKButton();
//    public abstract void initCancelButton();
//    public abstract void initApplyButton();
//
//    public void addOKButton(){
//      initOKButton();
//      this.add(OK);
//    }
//
//    public void addCancelButton(){
//      initCancelButton();
//      this.add(Cancel);
//    }
//
//    public void addApplyButton(){
//      initApplyButton();
//      this.add(Apply);
//    }
//
//    public void main(ModuleInfo modinfo) throws Exception {
//
//    }
//
//  } // InnerClass GenericTabbedPaneFrame




} // Class GenericTabbedPaneUI