package com.idega.block.blog.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.blog.data.BlogEntity;
import com.idega.block.category.data.InformationCategory;
import com.idega.block.text.business.ContentBusiness;
import com.idega.block.text.data.Content;
import com.idega.data.IDOLookupException;

/**
* @deprecated replaced by BlogBusinessBean
*/

public class BlogBusinessOLD {

	public static BlogEntity getBlog(int iBlogId) throws CreateException, IDOLookupException, FinderException {

		BlogEntity BG = ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).create();

		if (iBlogId > 0) {

			BG = ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).findByPrimaryKey(new Integer(iBlogId));

		} else {

			BG = null;

		}

		return BG;

	}

	private static void deleteBlogEntity(BlogEntity bgBlog) throws SQLException, EJBException, RemoveException {

		int contentId = bgBlog.getContentId();

		bgBlog.remove();

		ContentBusiness.deleteContent(contentId);

	}

	public static boolean deleteBlog(int iBlogId) {

		try {

			deleteBlogEntity(((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).findByPrimaryKey(new Integer(iBlogId)));

			return true;

		} catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (EJBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static BlogEntity saveBlog(int iBlogEntityId, int iLocalizedTextId, int iWorkFolderId, String sHeadline, String sTitle, String sAuthor, String sSource, String sBody, int iLocaleId, int iUserId, int InstanceId, Timestamp tsPubFrom, Timestamp tsPubTo, List listOfFiles, Timestamp blogDate) throws CreateException, IDOLookupException, FinderException {


		BlogEntity eBlogEntity = ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).create();

		if (iBlogEntityId > 0) {

			eBlogEntity = ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).findByPrimaryKey(new Integer(iBlogEntityId));

		}

		Content eContent = ContentBusiness.saveContent(eBlogEntity.getContentId(), iLocalizedTextId, iLocaleId, iUserId, tsPubFrom, tsPubTo, sHeadline, sBody, sTitle, listOfFiles, blogDate);
		if (eContent != null) {

			if (eContent.getID() > 0)
				eBlogEntity.setContentId(eContent.getID());

			if (sAuthor != null)
				eBlogEntity.setAuthor(sAuthor);

			if (sSource != null)
				eBlogEntity.setSource(sSource);

			eBlogEntity.setBlogFolderId(iWorkFolderId);

			eBlogEntity.store();

			return eBlogEntity;

		}

		return null;

	}

	public static InformationCategory saveBlogCategory(int iCategoryId, String sName, String sDesc, int iObjectInstanceId) throws RemoteException {
		throw new RuntimeException("Not Implemented in Blog");
		//return CategoryBusiness.getInstance().saveCategory(iCategoryId,
		// sName, sDesc, iObjectInstanceId,
		// ((com.idega.block.blog.data.BlogCategoryHome)
		// com.idega.data.IDOLookup.getHome(BlogCategory.class)).create().getCategoryType());

	}

	public static int createBlogCategory(int iObjectInstanceId) throws RemoteException {

		return saveBlogCategory(-1, "Blog", "Blog", iObjectInstanceId).getID();

	}

}
