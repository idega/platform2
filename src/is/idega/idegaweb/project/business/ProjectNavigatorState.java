package is.idega.idegaweb.project.business;

import is.idega.idegaweb.project.business.EntityNavigationListState;
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

public class ProjectNavigatorState extends EntityNavigationListState {

  public final static int FilterPos = ENLSmaxPosValue+1;

  protected static final int PNSmaxPosValue = FilterPos;  // value that subclasses can start from

  private int[] selectedCategories = null;

  public ProjectNavigatorState(PresentationObject obj, IWContext iwc) {
    super(obj,iwc);
  }

  public ProjectNavigatorState(PresentationObject obj) {
    super(obj);
  }


  public void addFilter(int categoryTypeID, int categoryId){
    //System.err.println("adding filter cattype: "+categoryTypeID+" cat: "+categoryId);
    String[] toModify = (String[])this.getValue(1);
    String catTypeID = Integer.toString(categoryTypeID);
    String catID = Integer.toString(categoryId);
    if(toModify == null){
      toModify = new String[2];
      toModify[0] = catTypeID;
      toModify[1] = catID;
      this.setValue(FilterPos,toModify);
    } else {
      int index = -1;
      for (int i = 0; i < toModify.length; i+=2) {
        if(toModify[i].equals(catTypeID)){
          index = i;
        }
      }
      //System.err.println("index = "+index);
      if(index > -1){
        if(!catID.equals("-1")){
          toModify[index+1] = catID;
        } else {
          int length = toModify.length;
          String[] newArray = new String[length-2];
          System.arraycopy(toModify,0,newArray,0,index);
          System.arraycopy(toModify,index+2,newArray,index,((length-2)-index));
          this.setValue(FilterPos,newArray);
        }
      } else {
        int length = toModify.length;
        String[] newArray = new String[length+2];
        System.arraycopy(toModify,0,newArray,0,length);
        newArray[length] = catTypeID;
        newArray[length+1] = catID;
        this.setValue(FilterPos,newArray);
      }
    }
    selectedCategories = null;
    /*
    System.err.println("StringArray is now");
    String[] tmp = (String[])this.getValue(1);
    for (int i = 0; i < tmp.length; i++) {
      System.err.println(i+" = "+tmp[i]);
    }
*/
  }

  public String[] getFilters(){
    return (String[])this.getValue(FilterPos);
  }

  public int[] getSelectedCategories(){
    if(selectedCategories == null){
      String[] tmp = (String[])this.getFilters();
      if(tmp != null){
        int length = tmp.length;
        selectedCategories = new int[length/2];
        int j = 0;
        for (int i = 0; i < tmp.length; i+=2) {
          try {
            selectedCategories[j++] = Integer.parseInt(tmp[i+1]);
          }
          catch (NumberFormatException ex) {
            length -=2;
            int[] cats = new int[length/2];
            System.arraycopy(selectedCategories,0,cats,0,length/2);
            selectedCategories = cats;
          }

        }

      }
    }
    return selectedCategories;
  }




}