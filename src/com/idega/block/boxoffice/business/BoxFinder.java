package com.idega.block.boxoffice.business;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.boxoffice.data.BoxCategory;
import com.idega.block.boxoffice.data.BoxEntity;
import com.idega.block.boxoffice.data.BoxLink;
import com.idega.core.builder.data.ICPage;
import com.idega.core.component.business.ICObjectBusiness;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookupException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BoxFinder {

	public static ICPage getPage(int pageID) {
		try {
			return ((com.idega.core.builder.data.ICPageHome) com.idega.data.IDOLookup.getHomeLegacy(ICPage.class)).findByPrimaryKeyLegacy(pageID);
		}
		catch (SQLException e) {
			return ((com.idega.core.builder.data.ICPageHome) com.idega.data.IDOLookup.getHomeLegacy(ICPage.class)).createLegacy();
		}
	}

	public static ICFile getFile(int fileID) {
		try {
			return ((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(fileID));
		} catch (IDOLookupException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			try {
				return ((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).create();
			} catch (IDOLookupException e1) {
				e1.printStackTrace();
				return null;
			} catch (CreateException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	public static BoxEntity getBox(String attribute) {
		try {
			List L =
				EntityFinder.findAllByColumn(
					com.idega.block.boxoffice.data.BoxEntityBMPBean.getStaticInstance(BoxEntity.class),
					com.idega.block.boxoffice.data.BoxEntityBMPBean.getColumnNameAttribute(),
					attribute);
			if (L != null) {
				return (BoxEntity) L.get(0);
			}
			return null;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static BoxEntity getBox(int boxID) {
		try {
			return ((com.idega.block.boxoffice.data.BoxEntityHome) com.idega.data.IDOLookup.getHomeLegacy(BoxEntity.class)).findByPrimaryKeyLegacy(boxID);
		}
		catch (SQLException ex) {
			return null;
		}
	}

	public static BoxCategory getCategory(int boxCategoryID) {
		try {
			return ((com.idega.block.boxoffice.data.BoxCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(BoxCategory.class)).findByPrimaryKeyLegacy(boxCategoryID);
		}
		catch (SQLException e) {
			return null;
		}
	}

	public static BoxLink getLink(int boxLinkID) {
		try {
			BoxLink link = ((com.idega.block.boxoffice.data.BoxLinkHome) com.idega.data.IDOLookup.getHomeLegacy(BoxLink.class)).findByPrimaryKeyLegacy(boxLinkID);
			return link;
		}
		catch (SQLException e) {
			return null;
		}
	}

	public static List getCategoriesInBox(BoxEntity box, int userID) {
		try {
			List list = null;
			if (box != null)
				list = EntityFinder.findRelated(box, com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
			List userList =
				EntityFinder.findAllByColumn(
					com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class),
					com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameUserID(),
					userID);
			if (userList != null) {
				if (list != null) {
					for (int a = 0; a < list.size(); a++) {
						if (!userList.contains(list.get(a)))
							userList.add(list.get(a));
					}
				}
				return userList;
			}
			else {
				if (list != null) {
					return list;
				}
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static List getCategoriesNotInBox(int boxID) {
		try {
			BoxEntity box = BoxFinder.getBox(boxID);
			if (box != null) {
				EntityFinder.debug = true;
				List list = EntityFinder.findNonRelated(box, com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
				return list;
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static List getAllCategories() {
		try {
			EntityFinder.debug = true;
			List list = EntityFinder.findAll(com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
			return list;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static List getCategoriesInBox(int boxID) {
		try {
			BoxEntity box = BoxFinder.getBox(boxID);
			if (box != null)
				return EntityFinder.findRelated(box, com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static BoxCategory[] getCategoriesInBox(BoxEntity box) {
		try {
			BoxCategory[] categories = (BoxCategory[]) box.findRelated(com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
			if (categories != null) {
				return categories;
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static BoxLink[] getLinksInBox(BoxEntity box, BoxCategory boxCategory) {
		try {
			BoxLink[] links =
				(BoxLink[]) com.idega.block.boxoffice.data.BoxLinkBMPBean.getStaticInstance(BoxLink.class).findAllByColumnOrdered(
					com.idega.block.boxoffice.data.BoxEntityBMPBean.getColumnNameBoxID(),
					Integer.toString(box.getID()),
					com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameBoxCategoryID(),
					Integer.toString(boxCategory.getID()),
					com.idega.block.boxoffice.data.BoxLinkBMPBean.getColumnNameCreationDate() + " desc",
					"=",
					"=");
			if (links != null) {
				return links;
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}


	/**
	 * Get all links in box marked by the user_id of the user that is logged in. 
	 * 
	 * @todo Fix this so that this is done in the db, not in code.
	 * 
	 * @param box
	 * @param boxCategory
	 * @param userId
	 * @return
	 */
	public static BoxLink[] getLinksInBoxByUser(BoxEntity box, BoxCategory boxCategory, int userId) {
		BoxLink links[] = getLinksInBox(box, boxCategory);
		Vector ret = new Vector();
		if (links != null) {
			if (userId > 0) {
				for (int i = 0; i < links.length; i++) {
					BoxLink link = (BoxLink) links[i];
					if (link.getUserID() == userId) {
						ret.add(link);
					}
				}
				
				if (ret.size() > 0) {
					links = new BoxLink[ret.size()];
					Iterator it = ret.iterator();
					int i = 0;
					while (it.hasNext()) {
						links[i] = (BoxLink)it.next();
						i++;
					}
				}
				else {
					links = null;
				}
				
				return links;
			}
		}
		return null;
	}

	public static BoxLink[] getLinksInCategory(BoxCategory boxCategory) {
		try {
			BoxLink[] links =
				(BoxLink[]) com.idega.block.boxoffice.data.BoxLinkBMPBean.getStaticInstance(BoxLink.class).findAllByColumnOrdered(
					com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameBoxCategoryID(),
					Integer.toString(boxCategory.getID()),
					com.idega.block.boxoffice.data.BoxLinkBMPBean.getColumnNameCreationDate() + " desc",
					"=");
			if (links != null) {
				return links;
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	// BEGIN COPY PASTE CRAP

	/**@todo make some sence into this crap**/
	public static BoxEntity getObjectInstanceFromID(int ICObjectInstanceID) {
		try {
			ICObjectBusiness icob = ICObjectBusiness.getInstance();
			ICObjectInstance ICObjInst = icob.getICObjectInstance(ICObjectInstanceID);
			return (BoxEntity) icob.getRelatedEntity(ICObjInst, BoxEntity.class);
		}
		catch (com.idega.data.IDOFinderException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int getRelatedEntityId(ICObjectInstance eObjectInstance) {
		ICObjectBusiness bis = ICObjectBusiness.getInstance();
		return bis.getRelatedEntityId(eObjectInstance, BoxEntity.class);
	}

	public static int getObjectInstanceIdFromID(int boxID) {
		try {
			BoxEntity box = ((com.idega.block.boxoffice.data.BoxEntityHome) com.idega.data.IDOLookup.getHomeLegacy(BoxEntity.class)).findByPrimaryKeyLegacy(boxID);
			List L = EntityFinder.findRelated(box, ((com.idega.core.component.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
			if (L != null) {
				return ((ICObjectInstance) L.get(0)).getID();
			}
			else
				return -1;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return -1;

		}
	}

}
