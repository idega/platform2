package com.idega.block.dictionary.business;

import java.sql.SQLException;
import com.idega.presentation.Image;
import javax.ejb.*;

import com.idega.presentation.ui.DropdownMenu;
import java.util.*;
import com.idega.data.*;
import java.rmi.RemoteException;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.dictionary.data.*;

public class DictionaryBusiness {

  public static final int WORD_VIEW = 1;
  public static final int CATEGORY_COLLECTION = 2;
  public static final int RANDOM_WORD = 3;

  public static final String PARAMETER_WORD_ID = "di_w_id";
  public static final String PARAMETER_CATEGORY_ID = "di_c_id";
  public static final String PARAMETER_STATE = "di_st";

  public static final String PARAMETER_MODE = "di_mode";
  public static final String PARAMETER_NEW = "new";
  public static final String PARAMETER_DELETE = "delete";
  public static final String PARAMETER_EDIT = "edit";
  public static final String PARAMETER_SAVE = "save";
  public static final String PARAMETER_CLOSE = "close";

  public static final String PARAMETER_WORD = "di_word";
  public static final String PARAMETER_DESCRIPTION = "di_desc";
  public static final String PARAMETER_IMAGE_ID = "di_i_id";

  private static DictionaryBusiness instance;

  private DictionaryBusiness(){
  }

  public static DictionaryBusiness getDictionaryBusinessInstace(){
    if(instance==null){
      instance = new DictionaryBusiness();
    }
    return instance;
  }

  public Word getWord(int wordID) {
    try {
      return getWordHome().findByPrimaryKey(new Integer(wordID));
    }
    catch (FinderException e) {
      return null;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  public WordHome getWordHome(){
    try {
      return (WordHome) IDOLookup.getHome(Word.class);
    }
    catch (RemoteException rme) {
      return null;
    }
  }

  public void saveWord(int wordID,String categoryID,String wordName,String description,String imageID) {
    try {
      Word word = getWordHome().create();
      if ( wordID != -1 )
	word = getWordHome().findByPrimaryKey(new Integer(wordID));

      word.setWord(wordName);
      word.setDescription(description);

      try {
	int category = Integer.parseInt(categoryID);
	if ( category != -1 )
	  word.setCategoryID(category);
      }
      catch (NumberFormatException e) {
      }

      try {
	int image = Integer.parseInt(imageID);
	if ( image != -1 )
	  word.setImageID(image);
      }
      catch (NumberFormatException e) {
      }

      word.store();
    }
    catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }
    catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
    catch (RemoteException rme) {
      rme.printStackTrace(System.err);
    }
  }

  public void deleteWord(int wordID) {
    try {
      getWord(wordID).remove();
    }
    catch (IDORemoveException ire) {
      ire.printStackTrace(System.err);
    }
    catch (RemoveException re) {
      re.printStackTrace(System.err);
    }
    //catch (RemoteException re) {
    //  re.printStackTrace(System.err);
    //}
  }

  public Word getRandomWord(int[] categories) throws FinderException,RemoteException {
    List collection = new Vector(getWordHome().findAllWordsInCategories(categories));
    if ( collection != null && collection.size() > 0 ) {
      int wordNumber = (int) Math.round(Math.random() * (collection.size() - 1));
      return (Word) collection.get(wordNumber);
    }
    return null;
  }

  public Image getImage(int imageID) {
    try {
      return new Image(imageID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public DropdownMenu getCategoryMenu() throws IDOException,FinderException,RemoteException {
    List collection = CategoryFinder.getInstance().listOfCategories("dictionary");
    DropdownMenu menu = new DropdownMenu(PARAMETER_CATEGORY_ID);

    if ( collection != null ) {
      //Collections.sort(collection,new BookComparator(BookComparator.CATEGORY_NAME));
      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	ICCategory category = (ICCategory) iter.next();
	menu.addMenuElement(((Integer)category.getPrimaryKey()).intValue(),category.getName());
      }
    }

    return menu;
  }
}