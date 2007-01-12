package com.idega.block.blog.business;

import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.blog.data.BlogEntity;
import com.idega.block.blog.data.BlogEntityBMPBean;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.ContentBMPBean;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.LocalizedTextBMPBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.data.IDOLookupException;
import com.idega.data.query.AND;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 * @deprecated replaced by BlogBusinessBean
 */

public class BlogFinder {

	public static final int PUBLISHISING = 1, UNPUBLISHED = 2, PUBLISHED = 3;

	public BlogFinder() {

	}

	public static List listOfAllBlogEntityInCategory(int[] blogCategoryId, int iLocaleId) {
		return listOfPublishingBlog(blogCategoryId, iLocaleId, true);
	}

	public static List listOfBlogEntityInCategory(int[] blogCategoryId, int iLocaleId) {
		return listOfPublishingBlog(blogCategoryId, iLocaleId, false);
	}

	public static List listOfBlogEntityInCategory(int blogCategoryId) {
		return listOfPublishingBlog(blogCategoryId, false);
	}

	public static List listOfPublishingBlog(int blogFolderId, boolean ignorePublishingDates) {
		Table blog = new Table(com.idega.block.blog.data.BlogEntityBMPBean.getEntityTableName(), "n");
		Table content = new Table(com.idega.block.text.data.ContentBMPBean.getEntityTableName(), "c");

		SelectQuery query = new SelectQuery(blog);
		query.addColumn(new WildCardColumn(blog));
		query.addColumn(new WildCardColumn(content));

		query.addJoin(blog, BlogEntityBMPBean.getColumnNameContentId(), content, ContentBMPBean.getEntityTableName() + "_ID");
		query.addCriteria(new MatchCriteria(blog, BlogEntityBMPBean.getColumnNameFolderID(), MatchCriteria.EQUALS, blogFolderId));
		if (!ignorePublishingDates) {
			IWTimestamp today = IWTimestamp.RightNow();
			MatchCriteria from = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishFrom(), MatchCriteria.LESSEQUAL, today.getTimestamp());
			MatchCriteria to = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishTo(), MatchCriteria.GREATEREQUAL, today.getTimestamp());
			query.addCriteria(new AND(from, to));
		}
		query.addOrder(content, ContentBMPBean.getColumnNameCreated(), false);
		try {
			return ListUtil.convertCollectionToList((((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class))).findAll(query.toString()));
		} catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List listOfPublishingBlog(int[] blogFolderIds, int iLocaleId, boolean ignorePublishingDates) {
		String middleTable = ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).createLegacy().getLocalizedTextMiddleTableName(((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy(), ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).createLegacy());
		Table blog = new Table(com.idega.block.blog.data.BlogEntityBMPBean.getEntityTableName(), "n");
		Table content = new Table(com.idega.block.text.data.ContentBMPBean.getEntityTableName(), "c");
		Table text = new Table(com.idega.block.text.data.LocalizedTextBMPBean.getEntityTableName(), "t");
		Table middle = new Table(middleTable, "m");

		SelectQuery query = new SelectQuery(blog);
		query.addColumn(new WildCardColumn(blog));
		query.addColumn(new WildCardColumn(content));

		query.addJoin(blog, BlogEntityBMPBean.getColumnNameContentId(), content, ContentBMPBean.getEntityTableName() + "_ID");
		query.addJoin(content, ContentBMPBean.getEntityTableName() + "_ID", middle, ContentBMPBean.getEntityTableName() + "_ID");
		query.addJoin(content, ContentBMPBean.getEntityTableName() + "_ID", middle, ContentBMPBean.getEntityTableName() + "_ID");
		query.addJoin(middle, LocalizedTextBMPBean.getEntityTableName() + "_ID", text, LocalizedTextBMPBean.getEntityTableName() + "_ID");

		query.addCriteria(new InCriteria(blog, BlogEntityBMPBean.getColumnNameFolderID(), blogFolderIds));
		query.addCriteria(new MatchCriteria(text, LocalizedTextBMPBean.getColumnNameLocaleId(), MatchCriteria.EQUALS, iLocaleId));
		if (!ignorePublishingDates) {
			IWTimestamp today = IWTimestamp.RightNow();
			MatchCriteria from = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishFrom(), MatchCriteria.LESSEQUAL, today.getTimestamp());
			MatchCriteria to = new MatchCriteria(content, ContentBMPBean.getColumnNamePublishTo(), MatchCriteria.GREATEREQUAL, today.getTimestamp());
			query.addCriteria(new AND(from, to));
		}
		query.addOrder(content, ContentBMPBean.getColumnNameCreated(), false);

		try {
			return ListUtil.convertCollectionToList((((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class))).findAll(query.toString()));
		} catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List listOfBlogHelpersInCategory(int[] blogCategoryId, int maxNumberOfBlog, Locale locale) {
		return listOfBlogHelpersInCategory(blogCategoryId, maxNumberOfBlog, getLocaleId(locale));
	}

	public static List listOfAllBlogHelpersInCategory(int[] blogCategoryId, int maxNumberOfBlog, Locale locale) {
		int iLocaleId = getLocaleId(locale);
		List L = listOfAllBlogEntityInCategory(blogCategoryId, iLocaleId);
		return listOfBlogHelpersInCategory(L, blogCategoryId, maxNumberOfBlog, iLocaleId);
	}

	public static List listOfBlogHelpersInCategory(int[] blogCategoryId, int maxNumberOfBlog, int iLocaleId) {
		List L = listOfBlogEntityInCategory(blogCategoryId, iLocaleId);
		return listOfBlogHelpersInCategory(L, blogCategoryId, maxNumberOfBlog, iLocaleId);
	}

	private static List listOfBlogHelpersInCategory(List L, int[] blogCategoryId, int maxNumberOfBlog, int iLocaleId) {
		if (L != null) {
			int len = L.size();
			Vector V = new Vector();
			for (int i = 0; i < len && i < maxNumberOfBlog; i++) {
				BlogEntity blog = (BlogEntity) L.get(i);
				BlogHelper nh = getBlogHelper(blog, iLocaleId);
				if (nh != null) {
					V.add(nh);
				}
			}
			return V;
		}
		return null;
	}

	public static BlogHelper getBlogHelper(BlogEntity blog) {
		BlogHelper NH = new BlogHelper();
		BlogEntity N = blog;
		if (N != null) {
			ContentHelper ch = ContentFinder.getContentHelper(N.getContentId());
			NH.setBlog(N);
			NH.setContentHelper(ch);
			return NH;
		}
		else {
			return null;
		}
	}

	public static BlogHelper getBlogHelper(BlogEntity blog, int iLocaleId) {
		BlogHelper NH = new BlogHelper();
		BlogEntity N = blog;
		if (N != null) {
			ContentHelper ch = ContentFinder.getContentHelper(N.getContentId(), iLocaleId);
			NH.setBlog(N);
			NH.setContentHelper(ch);
			return NH;
		}
		else {
			return null;
		}
	}

	public static BlogHelper getBlogHelper(int iBlogEntityId) {
		BlogEntity N = getBlog(iBlogEntityId);
		return getBlogHelper(N);
	}

	public static BlogEntity getBlog(int iBlogId) {
		try {
			return ((com.idega.block.blog.data.BlogEntityHome) com.idega.data.IDOLookup.getHome(BlogEntity.class)).findByPrimaryKey(new Integer(iBlogId));
		} catch (IDOLookupException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getLocaleId(Locale locale) {
		return ICLocaleBusiness.getLocaleId(locale);
	}

	public static Locale getLocale(int iLocaleId) {
		Locale L = ICLocaleBusiness.getLocale(iLocaleId);
		if (L == null) {
			L = new Locale("is", "IS");
		}
		return L;
	}

}
