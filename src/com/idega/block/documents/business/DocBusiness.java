package com.idega.block.documents.business;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.category.data.ICInformationCategory;
import com.idega.block.category.data.ICInformationFolder;
import com.idega.block.category.data.InformationCategory;
import com.idega.block.category.data.InformationFolder;
import com.idega.block.documents.data.DocLink;
import com.idega.block.documents.data.DocLinkBMPBean;
import com.idega.business.IBOServiceBean;
import com.idega.core.builder.data.ICPage;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 *@author     <a href="gummi@idega.is">Gudmundur Agust Saemundsson</a>
 * @version 1.0
 */

public class DocBusiness extends IBOServiceBean {

	public static final String PARAMETER_BOX_VIEW = "doc_view";
	public static final String PARAMETER_FOLDER_ID = "doc_id";
	public static final String PARAMETER_CATEGORY_ID = "doc_cat_id";
	public static final String PARAMETER_OBJECT_INSTANCE_ID = "doc_inst_id";
	public static final String PARAMETER_OBJECT_ID = "doc_obj_id";
	public static final String PARAMETER_CONTENT_LOCALE_IDENTIFIER = "doc_content_locale_id";
	public static final String PARAMETER_CATEGORY_DROPDOWN_ID = "doc_cat_ddown_id";
	public static final String PARAMETER_CATEGORY_NAME = "doc_cat_name";
	public static final String PARAMETER_CLOSE = "close";
	public static final String PARAMETER_DELETE = "delete";
	public static final String PARAMETER_DETACH = "detach";
	public static final String PARAMETER_FALSE = "false";
	public static final String PARAMETER_FILE_ID = "file_id";
	public static final String PARAMETER_LINK_ID = "link_id";
	public static final String PARAMETER_LINK_NAME = "link_name";
	public static final String PARAMETER_LINK_URL = "link_url";
	public static final String PARAMETER_LOCALE_DROP = "locale_drop";
	public static final String PARAMETER_LOCALE_ID = "locale_id";
	public static final String PARAMETER_MODE = "mode";
	public static final String PARAMETER_NEW_ATTRIBUTE = "new_attribute";
	public static final String PARAMETER_NEW_OBJECT_INSTANCE = "new_obj_inst";
	public static final String PARAMETER_PAGE_ID = "page_id";
	public static final String PARAMETER_SAVE = "save";
	public static final String PARAMETER_TARGET = "target";
	public static final String PARAMETER_TRUE = "true";
	public static final String PARAMETER_TYPE = "type";

	public static final String CATEGORY_SELECTION = "related_groups";

	public static final int LINK = 1;
	public static final int FILE = 2;
	public static final int PAGE = 3;

	/**
	 * @todo check performance
	 *
	 *
	 * @param userID
	 * @param catId
	 * @param folderId
	 * @param linkID
	 * @param boxLinkName
	 * @param fileID
	 * @param pageID
	 * @param boxLinkURL
	 * @param target
	 * @param iLocaleID
	 * @return
	 */
	public static int saveLink(int userID, int catId, int folderId, int linkID, String boxLinkName, int fileID, int pageID, String boxLinkURL, String target, int iLocaleID) {
		System.out.println("saveLink(int userID,int catId,int folderId,int linkID,String boxLinkName,int fileID,int pageID,String boxLinkURL,String target,int iLocaleID)");
		System.out.println(userID + ", " + catId + ", " + folderId + ", " + linkID + ", " + boxLinkName + ", " + fileID + ", " + pageID + ", " + boxLinkURL + ", " + target + ", " + iLocaleID);
		boolean update = false;
		int _linkID = -1;

		DocLink link = null;
		if (linkID != -1) {
			update = true;
			link = DocBusiness.getLink(linkID);
			if (link != null) {
				if (boxLinkURL != null) {
					if (update) {
						try {
							//link.setColumnAsNull(com.idega.block.documents.data.DocLinkBMPBean.getColumnNamePageID());
							//link.setColumnAsNull(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameFileID());
							link.removeFromColumn(com.idega.block.documents.data.DocLinkBMPBean.getColumnNamePageID());
							link.removeFromColumn(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameFileID());
							link.update();
						} catch (SQLException e) {
							e.printStackTrace(System.err);
						}
					}
				} else if (fileID != -1) {
					if (update) {
						try {
							//link.setColumnAsNull(com.idega.block.documents.data.DocLinkBMPBean.getColumnNamePageID());
							//link.setColumnAsNull(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameURL());
							link.removeFromColumn(com.idega.block.documents.data.DocLinkBMPBean.getColumnNamePageID());
							link.removeFromColumn(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameURL());
							link.update();
						} catch (SQLException e) {
							e.printStackTrace(System.err);
						}
					}
				} else if (pageID != -1) {
					if (update) {
						try {
							//link.setColumnAsNull(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameFileID());
							//link.setColumnAsNull(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameURL());
							link.removeFromColumn(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameFileID());
							link.removeFromColumn(com.idega.block.documents.data.DocLinkBMPBean.getColumnNameURL());
							link.update();
						} catch (SQLException e) {
							e.printStackTrace(System.err);
						}
					}
				}
			}
		} else {
			link = ((com.idega.block.documents.data.DocLinkHome)com.idega.data.IDOLookup.getHomeLegacy(DocLink.class)).createLegacy();
		}

		if (update) {
			link = DocBusiness.getLink(linkID);
			if (link == null) {
				link = ((com.idega.block.documents.data.DocLinkHome)com.idega.data.IDOLookup.getHomeLegacy(DocLink.class)).createLegacy();
				update = false;
			}
		}

		if (boxLinkName != null) {
			link.setName(boxLinkName);
		} else {
			link.setName("Untitled");
		}

		if (catId != -1) {
			link.setCategoryID(catId);
		}

		if (target != null) {
			link.setTarget(target);
		}

		if (boxLinkURL != null) {
			link.setURL(boxLinkURL);
		}

		if (fileID != -1) {
			link.setFileID(fileID);
		}

		if (pageID != -1) {
			link.setPageID(pageID);
		}

		if (!update) {
			try {
				link.setCreationDate(new IWTimestamp().getTimestampRightNow());
				if (folderId != -1) {
					link.setFolderID(folderId);
				}
				if (userID != -1) {
					link.setUser(((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(userID));
				}
				link.insert();
				_linkID = link.getID();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		} else {
			try {
				link.update();
				_linkID = link.getID();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}
		/*
		    LocalizedText locText = TextFinder.getLocalizedText(link,iLocaleID);
		    if ( locText == null ) {
		      locText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
		      newLocText = true;
		    }
		
		    locText.setHeadline(boxLinkName);
		
		    if ( newLocText ) {
		      locText.setLocaleId(iLocaleID);
		      try {
		        locText.insert();
		        locText.addTo(link);
		      }
		      catch (SQLException e) {
		        e.printStackTrace(System.err);
		      }
		    }
		    else {
		      try {
		        locText.update();
		      }
		      catch (SQLException e) {
		        e.printStackTrace(System.err);
		      }
		    }
		
		    */

		return _linkID;
	}

	public static void deleteLink(DocLink link) {
		try {
			if (link != null) {
				/**
				 * @todo: mark as deleted, not delete it
				 */
				link.delete();
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	public static void deleteLink(int linkID) {
		deleteLink(DocBusiness.getLink(linkID));
	}

	//  public static int saveCategory(int userID,int boxCategoryID,String categoryName,int iLocaleID) {
	//    boolean update = false;
	//    boolean newLocText = false;
	//    int _boxCategoryID = -1;
	//
	//    if ( boxCategoryID != -1 ) {
	//      update = true;
	//    }
	//
	//    ICInformationCategory category = ((com.idega.core.data.ICInformationCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationCategory.class)).createLegacy();
	//    if ( update ) {
	//      category = DocFinder.getCategory(boxCategoryID);
	//      if ( category == null ) {
	//        category = ((com.idega.core.data.ICInformationCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationCategory.class)).createLegacy();
	//        update = false;
	//      }
	//
	//    }
	//
	//    if ( !update ) {
	//      try {
	//        /**
	//         * @todo check if ok to comment out category.setUser(((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(userID));
	//         */
	//        //category.setUser(((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(userID));
	//        category.insert();
	//        _boxCategoryID = category.getID();
	//      }
	//      catch (SQLException e) {
	//        e.printStackTrace(System.err);
	//      }
	//    }
	//    else {
	//      try {
	//        category.update();
	//        _boxCategoryID = category.getID();
	//      }
	//      catch (SQLException e) {
	//        e.printStackTrace(System.err);
	//      }
	//    }
	//
	//    LocalizedText locText = TextFinder.getLocalizedText(category,iLocaleID);
	//    if ( locText == null ) {
	//      locText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
	//      newLocText = true;
	//    }
	//
	//    locText.setHeadline(categoryName);
	//		locText.setBody("");
	//		locText.setCreated(com.idega.util.IWTimestamp.getTimestampRightNow());
	//
	//    if ( newLocText ) {
	//      locText.setLocaleId(iLocaleID);
	//      try {
	//        locText.insert();
	//        locText.addTo(category);
	//      }
	//      catch (SQLException e) {
	//        e.printStackTrace(System.err);
	//      }
	//    }
	//    else {
	//      try {
	//        locText.update();
	//      }
	//      catch (SQLException e) {
	//        e.printStackTrace(System.err);
	//      }
	//    }
	//    return _boxCategoryID;
	//  }

	//  public static void deleteCategory(int boxCategoryID) {
	//    try {
	//      ICInformationCategory category = DocFinder.getCategory(boxCategoryID);
	//      if ( category != null ) {
	//        deleteLinks(category);
	//        category.delete();
	//      }
	//    }
	//    catch (SQLException e) {
	//      e.printStackTrace(System.err);
	//    }
	//  }
	/*
	  public static DropdownMenu getCategories(String name, int iLocaleId, ICInformationFolder folder, int userID) {
	    DropdownMenu drp = new DropdownMenu(name);
	
	    List list = DocFinder.getCategoriesInDoc(folder,userID);
	    if( list != null ) {
	      for ( int a = 0; a < list.size(); a++) {
	        LocalizedText locText = TextFinder.getLocalizedText((ICInformationCategory)list.get(a),iLocaleId);
	        String locString = "$language$";
	        if ( locText != null ) {
	          locString = locText.getHeadline();
	        }
	        drp.addMenuElement(((ICInformationCategory)list.get(a)).getID(),locString);
	      }
	    }
	
	    return drp;
	  }
	*/
	//  public static void detachCategory(int boxID, int boxCategoryID) {
	//    try {
	//      ICInformationFolder boxEntity = DocFinder.getFolder(boxID);
	//      ICInformationCategory boxCategory = DocFinder.getCategory(boxCategoryID);
	//
	//      if ( boxEntity != null && boxCategory != null ) {
	//        boxEntity.removeFrom(boxCategory);
	//      }
	//    }
	//    catch (Exception e) {
	//      e.printStackTrace(System.err);
	//    }
	//  }

	//  public static void deleteLinks(ICInformationCategory boxCategory) {
	//    try {
	//      DocLink[] links = DocFinder.getLinksInCategory(boxCategory);
	//      if ( links != null ) {
	//        for ( int a = 0; a < links.length; a++ ) {
	//          deleteLink(links[a]);
	//        }
	//      }
	//    }
	//    catch (Exception e) {
	//      e.printStackTrace(System.err);
	//    }
	//  }

	//  public static String getLocalizedString(IDOLegacyEntity entity, int iLocaleID) {
	//    String locString = null;
	//
	//    if ( entity != null ) {
	//      LocalizedText locText = TextFinder.getLocalizedText(entity,iLocaleID);
	//      if ( locText != null ) {
	//        locString = locText.getHeadline();
	//      }
	//    }
	//
	//    return locString;
	//  }

	//  /**
	//   * @todo finde out what attribute does
	//   */
	//  public static void saveDoc(int boxID,int InstanceId,String attribute){
	//    try {
	//      boolean update = false;
	//
	//      ICInformationFolder folder = ((com.idega.core.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).createLegacy();
	//      if ( boxID != -1 ) {
	//        update = true;
	//        folder = DocFinder.getFolder(boxID);
	//        if ( folder == null ) {
	//          folder = ((com.idega.core.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).createLegacy();
	//          update = false;
	//        }
	//      }
	//
	//      if(attribute != null){
	//        ICInformationFolder boxAttribute = DocFinder.getFolder(attribute);
	//        if ( boxAttribute != null ) {
	//          folder = boxAttribute;
	//          update = true;
	//        }
	//        //folder.setAttribute(attribute);
	//      }
	//
	//      if ( update ) {
	//        try {
	//          folder.update();
	//        }
	//        catch (SQLException e) {
	//          e.printStackTrace(System.err);
	//        }
	//      }
	//      else {
	//        folder.insert();
	//        if(InstanceId > 0){
	//          ICObjectInstance objIns = ((com.idega.core.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(InstanceId);
	//          folder.addTo(objIns);
	//        }
	//      }
	//    }
	//    catch(Exception e) {
	//      e.printStackTrace();
	//    }
	//  }

	//  public static void addToDoc(ICInformationFolder folder, int boxCategoryID) {
	//    try {
	//      ICInformationCategory category = DocFinder.getCategory(boxCategoryID);
	//      if ( category != null ) {
	//        ICInformationCategory[] categories = (ICInformationCategory[]) folder.findRelated(category);
	//        if ( categories == null || categories.length == 0 ) {
	//          folder.addTo(category);
	//        }
	//      }
	//    }
	//    catch (Exception e) {
	//      e.printStackTrace(System.err);
	//    }
	//  }
	//
	//  public static boolean deleteDoc(ICInformationFolder folder) {
	//    try {
	//      if ( folder != null ) {
	//        folder.delete();
	//      }
	//      return true;
	//    }
	//    catch (SQLException e) {
	//      e.printStackTrace(System.err);
	//      return false;
	//    }
	//  }
	//
	//  public static boolean deleteDoc(int iObjectInstanceId) {
	//    ICInformationFolder folder = DocFinder.getObjectInstanceFromID(iObjectInstanceId);
	//    if(folder !=null){
	//      return deleteDoc(folder,iObjectInstanceId);
	//    }
	//    return false;
	//  }
	//
	//  public static boolean deleteDoc(int iDocId,int iObjectInstanceId) {
	//          try{
	//      ICInformationFolder folder= ((com.idega.core.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).findByPrimaryKeyLegacy(iDocId);
	//                  if(folder !=null){
	//                          return deleteDoc(folder,iObjectInstanceId);
	//                  }
	//          }
	//          catch(SQLException ex){
	//
	//          }
	//          return false;
	//
	//  }
	//
	//  public static boolean deleteDoc(ICInformationFolder folder,int iObjectInstanceId) {
	//    try {
	//      if (folder !=null ) {
	//
	//				disconnectDoc(folder,iObjectInstanceId);
	//        folder.delete();
	//      }
	//      return true;
	//    }
	//    catch (SQLException e) {
	//      e.printStackTrace(System.err);
	//      return false;
	//    }
	//  }

	//  public static boolean disconnectDoc(int instanceid){
	//		ICInformationFolder folder = DocFinder.getObjectInstanceFromID(instanceid);
	//    if(folder!= null){
	//			return disconnectDoc(folder,instanceid);
	//
	//    }
	//    return false;
	//
	//  }
	//
	//  public static boolean disconnectDoc(ICInformationFolder folder,int iObjectInstanceId){
	//    try {
	//      if(iObjectInstanceId > 0  ){
	//        ICObjectInstance obj = ((com.idega.core.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
	//        folder.removeFrom(obj);
	//      }
	//      return true;
	//    }
	//    catch (SQLException ex) {
	//
	//    }
	//    return false;
	//  }

	//DocFinder ....

	/**
		 *  Gets the page attribute of the DocFinder class
		 *
		 *@param  pageID  Description of the Parameter
		 *@return         The page value
		 */
	public static ICPage getPage(int pageID) {
		try {
			return ((com.idega.core.builder.data.ICPageHome)com.idega.data.IDOLookup.getHomeLegacy(ICPage.class)).findByPrimaryKeyLegacy(pageID);
		} catch (SQLException e) {
			return ((com.idega.core.builder.data.ICPageHome)com.idega.data.IDOLookup.getHomeLegacy(ICPage.class)).createLegacy();
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
			return ((com.idega.core.file.data.ICFileHome)com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(fileID));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			try {
				return ((com.idega.core.file.data.ICFileHome)com.idega.data.IDOLookup.getHome(ICFile.class)).create();
			} catch (IDOLookupException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
		 *  Gets the folder attribute of the DocFinder class
		 *
		 *@param  folderID  Description of the Parameter
		 *@return           The folder value
		 */
	public static ICInformationFolder getFolder(int folderID) {
		try {
			return ((com.idega.block.category.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).findByPrimaryKeyLegacy(folderID);
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
			return ((com.idega.block.category.data.ICInformationCategoryHome)com.idega.data.IDOLookup.getHome(ICInformationCategory.class)).findByPrimaryKey(new Integer(infoCatID));
		} catch (IDOLookupException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  Gets the link attribute of the DocFinder class
	 *
	 *@param  docLinkID  Description of the Parameter
	 *@return            The link value
	 */
	public static DocLink getLink(int docLinkID) {
		try {
			DocLink link = ((com.idega.block.documents.data.DocLinkHome)com.idega.data.IDOLookup.getHomeLegacy(DocLink.class)).findByPrimaryKeyLegacy(docLinkID);
			return link;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  Gets the objectInstanceIdFromID attribute of the DocFinder class
	 *
	 *@param  folderID  Description of the Parameter
	 *@return           The objectInstanceIdFromID value
	 */
	public static int getObjectInstanceIdFromID(int folderID) {
		try {
			ICInformationFolder box = ((com.idega.block.category.data.ICInformationFolderHome)com.idega.data.IDOLookup.getHomeLegacy(ICInformationFolder.class)).findByPrimaryKeyLegacy(folderID);
			List L = EntityFinder.findRelated(box, ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
			if (L != null) {
				return ((ICObjectInstance)L.get(0)).getID();
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
			ICObjectInstance ICObjInst = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(ICObjectInstanceID);
			List L = EntityFinder.findRelated(ICObjInst, com.idega.block.category.data.ICInformationFolderBMPBean.getStaticInstance(ICInformationFolder.class));
			if (L != null) {
				return (ICInformationFolder)L.get(0);
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
	public static DocLink[] getLinksInFolderCategory(InformationFolder folder, InformationCategory infoCat) {
		try {
			DocLink staticLink = (DocLink)DocLinkBMPBean.getStaticInstance(DocLink.class);
			DocLink[] links = (DocLink[])staticLink.findAllByColumnOrdered(DocLinkBMPBean.getColumnNameFolderID(), Integer.toString(folder.getID()), DocLinkBMPBean.getColumnNameCatID(), Integer.toString(infoCat.getID()), DocLinkBMPBean.getColumnNameCreationDate() + " desc", "=", "=");
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
			DocLink staticLink = (DocLink)DocLinkBMPBean.getStaticInstance(DocLink.class);
			DocLink[] links = (DocLink[])staticLink.findAll("select * from " + DocLinkBMPBean.getEntityTableName() + " where " + DocLinkBMPBean.getColumnNameFolderID() + " = " + Integer.toString(folder.getID()) + " order by " + DocLinkBMPBean.getColumnNameCreationDate() + " desc");
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
			DocLink staticLink = (DocLink)DocLinkBMPBean.getStaticInstance(DocLink.class);
			int returnInt = staticLink.getNumberOfRecords("select count(*) from " + DocLinkBMPBean.getEntityTableName() + " where " + DocLinkBMPBean.getColumnNameFolderID() + " = " + Integer.toString(folder.getID()) + " and " + DocLinkBMPBean.getColumnNameCatID() + " = " + Integer.toString(infoCat.getID()));
			return returnInt;
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getNumberOfLinksInFolder(InformationFolder folder) {
		try {
			DocLink staticLink = (DocLink)DocLinkBMPBean.getStaticInstance(DocLink.class);
			int returnInt = staticLink.getNumberOfRecords("select count(*) from " + DocLinkBMPBean.getEntityTableName() + " where " + DocLinkBMPBean.getColumnNameFolderID() + " = " + Integer.toString(folder.getID()));
			return returnInt;
		} catch (Exception e) {
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
