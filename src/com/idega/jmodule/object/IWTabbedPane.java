package com.idega.jmodule.object;

import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.plaf.IWTabbedPaneUI;
import com.idega.jmodule.object.plaf.basic.BasicTabbedPaneUI;
import com.idega.jmodule.object.plaf.TabbedPaneFrame;
import com.idega.util.IWColor;

import javax.swing.SwingConstants;
import javax.swing.SingleSelectionModel;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.Icon;

import java.util.Vector;
import java.util.Locale;

/**
 * Title:        idegaWeb project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author
 * @version 1.0
 */

public class IWTabbedPane extends ModuleObjectContainer implements SwingConstants {

    private static final String uiClassID = "IWTabbedPaneUI";
    protected int tabPlacement = TOP;
    protected SingleSelectionModel model;
    protected ChangeListener changeListener = null;
    public Vector pages;
    protected ChangeEvent changeEvent = null;
    private static String TabbedPaneAttributeString = "-IWTabbedPane";

    private Form mainForm;
    private TabbedPaneFrame objectFrame;

    private boolean OKButtonDisabled;
    private boolean CancelButtonDisabled;
    private boolean ApplyButtonDisabled;

    private IWTabbedPaneUI ui;


//    private IWTabbedPane() {
//        this(TOP);
//    }

    private IWTabbedPane(int tabPlacement) {
        setTabPlacement(tabPlacement);
        pages = new Vector(1);
        setModel(new DefaultSingleSelectionModel());
        this.mainForm = new Form();
        updateUI();
        this.objectFrame = getUI().getFrame();
        this.mainForm.add(objectFrame);
    }

    public static IWTabbedPane getInstance(String key, ModuleInfo modinfo, int tabPlacement ){
      Object  obj = modinfo.getSessionAttribute(key+TabbedPaneAttributeString);
      if(obj != null && obj instanceof IWTabbedPane){
        return (IWTabbedPane)obj;
      }else{
        IWTabbedPane tempTab = new IWTabbedPane(tabPlacement);
        modinfo.setSessionAttribute(key+TabbedPaneAttributeString, tempTab);
        return tempTab;
      }
    }

    public static IWTabbedPane getInstance(String key, ModuleInfo modinfo){
      return getInstance(key, modinfo, TOP);
    }


    public IWTabbedPaneUI getUI() {
        return (IWTabbedPaneUI)ui;
    }

    public void setUI(IWTabbedPaneUI ui) {
        this.ui = ui;
    }

    public void updateUI() {
        //setUI((IWTabbedPaneUI)UIManager.getUI(this));
        setUI((IWTabbedPaneUI) new BasicTabbedPaneUI());
    }


    public String getUIClassID() {
        return uiClassID;
    }


    protected class ModelListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }

    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireStateChanged() {
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

    public SingleSelectionModel getModel() {
        return model;
    }

    public void setModel(SingleSelectionModel model) {
        SingleSelectionModel oldModel = getModel();

        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
            changeListener = null;
        }

        this.model = model;

        if (model != null) {
            changeListener = createChangeListener();
            model.addChangeListener(changeListener);
        }

        firePropertyChange("model", oldModel, model);
//        repaint();
    }

    /**
     * unimplemented
     * @todo implement
     */
    public void firePropertyChange(String key, SingleSelectionModel oldModel, SingleSelectionModel model){

    }


    public int getTabPlacement() {
        return tabPlacement;
    }

    public void setTabPlacement(int tabPlacement) {
        if (tabPlacement != TOP && tabPlacement != LEFT &&
            tabPlacement != BOTTOM && tabPlacement != RIGHT) {
            throw new IllegalArgumentException("illegal tab placement: must be TOP, BOTTOM, LEFT, or RIGHT");
        }
        if (this.tabPlacement != tabPlacement) {
            int oldValue = this.tabPlacement;
            this.tabPlacement = tabPlacement;
            firePropertyChange("tabPlacement", oldValue, tabPlacement);
//            revalidate();
//            repaint();
        }
    }

    /**
     * unimplemented
     */
    public void firePropertyChange(String Key, int oldValue, int tabPlacement){
      /**
       * @todo implement
       */
    }


    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }

    public void setSelectedIndex(int index) {
        int oldIndex = model.getSelectedIndex();

        model.setSelectedIndex(index);

        if ((oldIndex >= 0) && (oldIndex != index)) {
            Page oldPage = (Page) pages.elementAt(oldIndex);
//            if (accessibleContext != null) {
//                accessibleContext.firePropertyChange(
//                        AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
//                        AccessibleState.SELECTED, null);
//            }
        }
        if ((index >= 0) && (oldIndex != index)) {
            Page newPage = (Page) pages.elementAt(index);
//            if (accessibleContext != null) {
//                accessibleContext.firePropertyChange(
//                        AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
//                        null, AccessibleState.SELECTED);
//            }
        }
    }

    public ModuleObject getSelectedComponent() {
        int index = getSelectedIndex();
        if (index == -1) {
            return null;
        }
        return getComponentAt(index);
    }

    public void setSelectedComponent(ModuleObject c) {
        int index = indexOfComponent(c);
        if (index != -1) {
            setSelectedIndex(index);
        } else {
            throw new IllegalArgumentException("moduleobject not found in tabbed pane");
        }
    }

    public void insertTab(String title, Icon icon, ModuleObject moduleobject, String tip, int index) {

        // If moduleobject already exists, remove corresponding
        // tab so that new tab gets added correctly
        // Note: we are allowing moduleobject=null because of compatibility,
        // but we really should throw an exception because much of the
        // rest of the JTabbedPane implementation isn't designed to deal
        // with null components for tabs.
        int i;
        if (moduleobject != null && (i = indexOfComponent(moduleobject)) != -1) {
            removeTabAt(i);
        }

        pages.insertElementAt(new Page(this, title != null? title : " --- ", moduleobject, tip), index);
        if (moduleobject != null) {
//            moduleobject.setVisible(false);
//            addImpl(moduleobject, null, -1);
        }

        if (pages.size() == 1) {
            setSelectedIndex(0);
        }
    }

     public void insertTab(String title, ModuleObject moduleobject, int index) {
        insertTab(title, null, moduleobject, null, pages.size());
     }

    public void addTab(String title, Icon icon, ModuleObject moduleobject, String tip) {
        insertTab(title, icon, moduleobject, tip, pages.size());
    }

    public void addTab(String title, Icon icon, ModuleObject moduleobject) {
        insertTab(title, icon, moduleobject, null, pages.size());
    }

    public void addTab(String title, ModuleObject moduleobject) {
        insertTab(title, null, moduleobject, null, pages.size());
    }

    public void addTab(ModuleObject moduleobject) {
        insertTab(moduleobject.getName(), null, moduleobject, null, pages.size());
    }


    public void add(ModuleObject moduleobject) {
        addTab(moduleobject.getName(), moduleobject);
    }

    public void add(String title, ModuleObject moduleobject) {
        addTab(title, moduleobject);
    }

    public ModuleObject add(ModuleObject moduleobject, int index) {
        // Container.add() interprets -1 as "append", so convert
        // the index appropriately to be handled by the vector
        insertTab(moduleobject.getName(), null, moduleobject, null,
                  index == -1? getTabCount() : index);
        return moduleobject;
    }


    public void removeTabAt(int index) {
        // If we are removing the currently selected tab AND
        // it happens to be the last tab in the bunch, then
        // select the previous tab
        int tabCount = getTabCount();
        int selected = getSelectedIndex();
        if (selected >= (tabCount - 1)) {
            setSelectedIndex(selected - 1);
        }

        ModuleObject moduleobject = getComponentAt(index);

        pages.removeElementAt(index);
    }

    public void remove(ModuleObject moduleobject) {
        int index = indexOfComponent(moduleobject);
        if (index != -1) {
            removeTabAt(index);
        }
    }

    public void remove(int index) {
        removeTabAt(index);
    }

    public void removeAll() {
        setSelectedIndex(-1);

        int tabCount = getTabCount();
        for (int i = 0; i < tabCount; i++) {
            ModuleObject moduleobject = getComponentAt(i);
            // Reset visibility
            if (moduleobject != null) {
//                moduleobject.setVisible(true);
            }
        }
        pages.removeAllElements();
    }

    public int getTabCount() {
        return pages.size();
    }

    public int getTabRunCount() {
        if (ui != null) {
//            return ((IWTabbedPaneUI)ui).getTabRunCount(this);
        }
        return 0;
    }


// Getters for the Pages

    public String getTitleAt(int index) {
        return ((Page)pages.elementAt(index)).title;
    }

    public String getToolTipTextAt(int index) {
        return ((Page)pages.elementAt(index)).tip;
    }

//    public IWColor getBackgroundAt(int index) {
//        return ((Page)pages.elementAt(index)).getBackground();
//    }
//
//    public IWColor getForegroundAt(int index) {
//        return ((Page)pages.elementAt(index)).getForeground();
//    }
//
//    public boolean isEnabledAt(int index) {
//        return ((Page)pages.elementAt(index)).isEnabled();
//    }

    public ModuleObject getComponentAt(int index) {
        return ((Page)pages.elementAt(index)).moduleobject;
    }


// Setters for the Pages

    public void setTitleAt(int index, String title) {
        String oldTitle =((Page)pages.elementAt(index)).title;
        ((Page)pages.elementAt(index)).title = title;

//        if ((oldTitle != title) && (accessibleContext != null)) {
//            accessibleContext.firePropertyChange(
//                    AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
//                    oldTitle, title);
//        }
        if (title == null || oldTitle == null ||!title.equals(oldTitle)) {
//            revalidate();
//            repaint();
        }
    }


    public void setToolTipTextAt(int index, String toolTipText) {
        String oldToolTipText =((Page)pages.elementAt(index)).tip;
        ((Page)pages.elementAt(index)).tip = toolTipText;

//        if ((oldToolTipText != toolTipText) && (accessibleContext != null)) {
//            accessibleContext.firePropertyChange(
//                    AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
//                    oldToolTipText, toolTipText);
//        }
//        if (!haveRegistered && toolTipText != null) {
//            ToolTipManager.sharedInstance().registerComponent(this);
//            haveRegistered = true;
//        }
    }
/*
//    public void setBackgroundAt(int index, IWColor background) {
//        IWColor oldBg = ((Page)pages.elementAt(index)).background;
        ((Page)pages.elementAt(index)).setBackground(background);
//        if (background == null || oldBg == null ||
//            !background.equals(oldBg)) {
            Rectangle tabBounds = getBoundsAt(index);
            if (tabBounds != null) {
                repaint(tabBounds);
            }
//        }
//    }
//
//    public void setForegroundAt(int index, IWColor foreground) {
//        IWColor oldFg = ((Page)pages.elementAt(index)).foreground;
//        ((Page)pages.elementAt(index)).setForeground(foreground);
//        if (foreground == null || oldFg == null ||
//            !foreground.equals(oldFg)) {
            Rectangle tabBounds = getBoundsAt(index);
//            if (tabBounds != null) {
                repaint(tabBounds);
//            }
//        }
//    }

    public void setEnabledAt(int index, boolean enabled) {
        boolean oldEnabled = ((Page)pages.elementAt(index)).isEnabled();
        ((Page)pages.elementAt(index)).setEnabled(enabled);
        if (enabled != oldEnabled) {
            repaint(getBoundsAt(index));
        }
    }
*/
    public int indexOfTab(String title) {
        for(int i = 0; i < getTabCount(); i++) {
            if (getTitleAt(i).equals(title == null? "" : title)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfComponent(ModuleObject moduleobject) {
        for(int i = 0; i < getTabCount(); i++) {
            ModuleObject c = getComponentAt(i);
            if ((c != null && c.equals(moduleobject)) ||
                (c == null && c == moduleobject)) {
                return i;
            }
        }
        return -1;
    }

    /*
    protected String paramString() {
        String tabPlacementString;
        if (tabPlacement == TOP) {
            tabPlacementString = "TOP";
        } else if (tabPlacement == BOTTOM) {
            tabPlacementString = "BOTTOM";
        } else if (tabPlacement == LEFT) {
            tabPlacementString = "LEFT";
        } else if (tabPlacement == RIGHT) {
            tabPlacementString = "RIGHT";
        } else tabPlacementString = "";
        String haveRegisteredString = (haveRegistered ?
				       "true" : "false");

	return super.paramString() +
        ",haveRegistered=" + haveRegisteredString +
        ",tabPlacement=" + tabPlacementString;
    }
*/




    private class Page{
      String title;
      IWColor background;
      IWColor foreground;
      Icon icon;
      Icon disabledIcon;
      IWTabbedPane parent;
      ModuleObject moduleobject;
      String tip;
      boolean enabled = true;
      boolean needsUIUpdate;



//      Page(IWTabbedPane parent, String title, Icon icon, Icon disabledIcon, ModuleObject moduleobject, String tip) {
//        this.title = title;
//        this.icon = icon;
//        this.disabledIcon = disabledIcon;
//        this.parent = parent;
//        this.moduleobject = moduleobject;
//        this.tip = tip;
//
//      }

      Page(IWTabbedPane parent, String title,ModuleObject moduleobject, String tip) {
        this.title = title;
        this.parent = parent;
        this.moduleobject = moduleobject;
        this.tip = tip;

      }



//      public Locale getLocale() {
//          return parent.getLocale();
//      }
//
//
//      public IWColor getBackground() {
//          return background != null? background : parent.getBackground();
//      }
//
//      public void setBackground(IWColor c) {
//          background = c;
//      }
//
//      public IWColor getForeground() {
//          return foreground != null? foreground : parent.getForeground();
//      }
//
//      public void setForeground(IWColor c) {
//          foreground = c;
//      }


//        public Font getFont() {
//            return parent.getFont();
//        }
//
//        public void setFont(Font f) {
//            parent.setFont(f);
//        }


//      public boolean isEnabled() {
//          return enabled;
//      }
//
//      public void setEnabled(boolean b) {
//          enabled = b;
//      }

//      public boolean isVisible() {
//          return parent.isVisible();
//      }
//
//      public void setVisible(boolean b) {
//          parent.setVisible(b);
//      }
//
//      public boolean isShowing() {
//          return parent.isShowing();
//      }


  } // InnerClass Page

}   // Class IWTabbedPane






/**
 *
 *
 * @todo Kalla á TabPagePresentation hlut frá ui-inu og setja viðeigani síðu í hann.
 * @todo Selecta viðeigandi hlut og láta TabPresentatnion vita.  Nota alltaf
 * sama TabPresentation hlutinn en kalla á fireStateChange() fallið.
 * adda í Tabpresentationhlutinn um leið og hlut er addað í TabbedPane.  Veldur vandræðum
 * ef að skipta á um UI því þá þarf að adda öllu í TabPresentation aftur.
 *
 *
 *
 */
