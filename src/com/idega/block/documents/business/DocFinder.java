package com.idega.block.documents.business;



import com.idega.block.documents.data.DocLinkBMPBean;
import com.idega.block.documents.data.DocLink;

import com.idega.builder.data.IBPage;

import com.idega.core.data.ICFile;

import com.idega.core.data.ICInformationCategory;

import com.idega.core.data.ICInformationFolder;

import com.idega.core.data.ICObjectInstance;

import com.idega.data.EntityFinder;

import java.sql.SQLException;

import java.util.List;

import com.idega.core.business.InformationCategory;

import com.idega.core.business.InformationFolder;



/**

 *  Title: Description: Copyright: Copyright (c) 2001 Company:

 *

 *@author     gummi

 *@created    15. mars 2002

 *@version    1.0

 */



public class DocFinder {



    /**

     *  Gets the page attribute of the DocFinder class

     *

     *@param  pageID  Description of the Parameter

     *@return         The page value

     */

    public static IBPage getPage(int pageID) {

	try {

	    return ((com.idega.builder.data.IBPageHome)com.idega.data.IDOLookup.getHomeLegacy(IBPage.class)).findByPrimaryKeyLegacy(pageID);

	} catch (SQLException e) {

	    return ((com.idega.builder.data.IBPageHome)com.idega.data.IDOLookup.getHomeLegacy(IBPage.class)).createLegacy();

	}

    }





    /**

     *  Gets the file attribute of the DocFinder class

     *

     *@param  fileID  Description of the Parameter

     *@return         The file value

     */

    public static ICFile getFile(int fileID) {

	try {

	    return ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(fileID);

	} catch (SQLException e) {

	    return ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy();

	}

    }





//    /**

//     *@param  attribute  Description of the Parameter

//     *@return            The folder value

//     *@todo:             reimplement

//     */

//    public static ICInformationFolder getFolder(String attribute) {

//        /*

//         *  try {

//         *  List L = null; // EntityFinder.findAllByColumn(com.idega.core.data.ICInformationFolderBMPBean.getStaticInstance(ICInformationFolder.class),com.idega.core.data.ICInformationFolderBMPBean.getColumnNameAttribute(),attribute);

//         *  if(L!= null) {

//         *  return (ICInformationFolder) L.get(0);

//         *  }

//         *  return null;

//         *  }

//         *  catch (SQLException ex) {

//         *  ex.printStackTrace();

//         */

//        return null;

//        // }

//    }





    /**

     *  Gets the folder attribute of the DocFinder class

     *

     *@param  folderID  Description of the Parameter

     *@return           The folder value

     */

    public static ICInformationFolder getFolder(int folderID) {

	try {

	    return ((com.idega.core.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).findByPrimaryKeyLegacy(folderID);

	} catch (SQLException ex) {

	    return null;

	}

    }





    /**

     *  Gets the category attribute of the DocFinder class

     *

     *@param  infoCatID  Description of the Parameter

     *@return            The category value

     */

    public static ICInformationCategory getCategory(int infoCatID) {

	try {

	    return ((com.idega.core.data.ICInformationCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationCategory.class)).findByPrimaryKeyLegacy(infoCatID);

	} catch (SQLException e) {

	    return null;

	}

    }





    /**

     *  Gets the link attribute of the DocFinder class

     *

     *@param  boxLinkID  Description of the Parameter

     *@return            The link value

     */

    public static DocLink getLink(int boxLinkID) {

	try {

	    DocLink link = ((com.idega.block.documents.data.DocLinkHome)com.idega.data.IDOLookup.getHomeLegacy(DocLink.class)).findByPrimaryKeyLegacy(boxLinkID);

	    return link;

	} catch (SQLException e) {

	    e.printStackTrace();

	    return null;

	}

    }





    /*

     *  public static int getObjectInstanceID(ICObjectInstance eObjectInstance){

     *  try {

     *  List L = EntityFinder.findRelated(eObjectInstance,((com.idega.core.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).createLegacy());

     *  if(L!= null){

     *  return ((ICInformationFolder) L.get(0)).getID();

     *  }

     *  else

     *  return -1;

     *  }

     *  catch (SQLException ex) {

     *  ex.printStackTrace();

     *  return -2;

     *  }

     *  }

     */

    /**

     *  Gets the objectInstanceIdFromID attribute of the DocFinder class

     *

     *@param  folderID  Description of the Parameter

     *@return           The objectInstanceIdFromID value

     */

    public static int getObjectInstanceIdFromID(int folderID) {

	try {

	    ICInformationFolder box = ((com.idega.core.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).findByPrimaryKeyLegacy(folderID);

	    List L = EntityFinder.findRelated(box, ((com.idega.core.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());

	    if (L != null) {

		return ((ICObjectInstance) L.get(0)).getID();

	    } else {

		return -1;

	    }

	} catch (SQLException ex) {

	    ex.printStackTrace();

	    return -1;

	}

    }





    /**

     *  Gets the objectInstanceFromID attribute of the DocFinder class

     *

     *@param  ICObjectInstanceID  Description of the Parameter

     *@return                     The objectInstanceFromID value

     */

    public static ICInformationFolder getObjectInstanceFromID(int ICObjectInstanceID) {

	try {

	    ICObjectInstance ICObjInst = ((com.idega.core.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(ICObjectInstanceID);

	    List L = EntityFinder.findRelated(ICObjInst, com.idega.core.data.ICInformationFolderBMPBean.getStaticInstance(ICInformationFolder.class));

	    if (L != null) {

		return (ICInformationFolder) L.get(0);

	    } else {

		return null;

	    }

	} catch (SQLException ex) {

	    ex.printStackTrace();

	    return null;

	}

    }





    /**

     *@param  folder   Description of the Parameter

     *@param  infoCat  Description of the Parameter

     *@return          The linksInFolderCategory value

     *@todo:           reimplement

     */

//  public static List getCategoriesInDoc(ICInformationFolder box,int userID) {

//    try {

//      List list = null;

//      if ( box != null )

//        list = EntityFinder.findRelated(box,com.idega.core.data.ICInformationCategoryBMPBean.getStaticInstance(ICInformationCategory.class));

//      List userList = null; // EntityFinder.findAllByColumn(com.idega.core.data.ICInformationCategoryBMPBean.getStaticInstance(ICInformationCategory.class),com.idega.core.data.ICInformationCategoryBMPBean.getColumnNameUserID(),userID);

//      if ( userList != null ) {

//        if ( list != null ) {

//          for ( int a = 0; a < list.size(); a++ ) {

//            if ( !userList.contains(list.get(a)) )

//              userList.add(list.get(a));

//          }

//        }

//        return userList;

//      }

//      else {

//        if ( list != null ) {

//          return list;

//        }

//      }

//      return null;

//    }

//    catch (Exception e) {

//      e.printStackTrace(System.err);

//      return null;

//    }

//  }

//

//  public static List getCategoriesNotInUse(int folderID) {

//    try {

//      ICInformationFolder folder = DocFinder.getFolder(folderID);

//      if ( box != null )

//        return EntityFinder.findNonRelated(folder,com.idega.core.data.ICInformationCategoryBMPBean.getStaticInstance(ICInformationCategory.class));

//      return null;

//    }

//    catch (Exception e) {

//      e.printStackTrace(System.err);

//      return null;

//    }

//  }

//

//  public static List getCategoriesInUse(int folderID) {

//    try {

//      ICInformationFolder box = DocFinder.getFolder(folderID);

//      if ( box != null )

//        return EntityFinder.findRelated(box,com.idega.core.data.ICInformationCategoryBMPBean.getStaticInstance(ICInformationCategory.class));

//      return null;

//    }

//    catch (Exception e) {

//      return null;

//    }

//  }

//

//  public static ICInformationCategory[] getCategoriesInUse(ICInformationFolder box) {

//    try {

//      ICInformationCategory[] categories = (ICInformationCategory[]) box.findRelated(com.idega.core.data.ICInformationCategoryBMPBean.getStaticInstance(ICInformationCategory.class));

//      if ( categories != null ) {

//        return categories;

//      }

//      return null;

//    }

//    catch (Exception e) {

//      return null;

//    }

//  }



    /**

     *@param  folder   Description of the Parameter

     *@param  infoCat  Description of the Parameter

     *@return          The linksInFolderCategory value

     *@todo:           reimplement

     */

    public static DocLink[] getLinksInFolderCategory(InformationFolder folder, InformationCategory infoCat) {

	try {

	    DocLink staticLink = (DocLink) com.idega.block.documents.data.DocLinkBMPBean.getStaticInstance(DocLink.class);

	    DocLink[] links = (DocLink[]) staticLink.findAllByColumnOrdered(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameFolderID(), Integer.toString(folder.getID()), com.idega.block.documents.data.DocLinkBMPBean.getColumnNameCatID(), Integer.toString(infoCat.getID()), com.idega.block.documents.data.DocLinkBMPBean.getColumnNameCreationDate() + " desc", "=", "=");

	    if (links != null) {

		return links;

	    }

	    return null;

	} catch (Exception e) {

	    return null;

	}

    }

    public static DocLink[] getLinksInFolder(InformationFolder folder) {
	try {
	    DocLink staticLink = (DocLink) com.idega.block.documents.data.DocLinkBMPBean.getStaticInstance(DocLink.class);
	    DocLink[] links = (DocLink[]) staticLink.findAllByColumnOrdered(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameFolderID(), Integer.toString(folder.getID()), com.idega.block.documents.data.DocLinkBMPBean.getColumnNameCreationDate() + " desc");
	    if (links != null) {
		return links;
	    }
	    return null;
	} catch (Exception e) {
	    return null;
	}
    }

    public static int getNumberOfLinksInFolderCategory(InformationFolder folder, InformationCategory infoCat) {
      try {
	DocLink staticLink = (DocLink) com.idega.block.documents.data.DocLinkBMPBean.getStaticInstance(DocLink.class);
	int returnInt = staticLink.getNumberOfRecords("select count(*) from "+DocLinkBMPBean.getEntityTableName()+" where "+DocLinkBMPBean.getColumnNameFolderID()+" = "+Integer.toString(folder.getID())+" and "+DocLinkBMPBean.getColumnNameCatID()+" = "+Integer.toString(infoCat.getID()));
	return returnInt;
      }
      catch (Exception e) {
	return 0;
      }
    }

    public static int getNumberOfLinksInFolder(InformationFolder folder) {
      try {
	DocLink staticLink = (DocLink) com.idega.block.documents.data.DocLinkBMPBean.getStaticInstance(DocLink.class);
	int returnInt = staticLink.getNumberOfRecords("select count(*) from "+DocLinkBMPBean.getEntityTableName()+" where "+DocLinkBMPBean.getColumnNameFolderID()+" = "+Integer.toString(folder.getID()));
	return returnInt;
      }
      catch (Exception e) {
	return 0;
      }
    }





    /**

     *@param  infoCat  Description of the Parameter

     *@return          The linksInCategory value

     *@deprecated

     */

    public static DocLink[] getLinksInCategory(InformationCategory infoCat) {

	try {

	    DocLink[] links = null;

	    // (DocLink[]) com.idega.block.documents.data.DocLinkBMPBean.getStaticInstance(DocLink.class).findAllByColumnOrdered(infoCat.getColumnNameDocCategoryID(),Integer.toString(infoCat.getID()),com.idega.block.documents.data.DocLinkBMPBean.getColumnNameCreationDate()+" desc","=");

	    if (links != null) {

		return links;

	    }

	    return null;

	} catch (Exception e) {

	    return null;

	}

    }

}

