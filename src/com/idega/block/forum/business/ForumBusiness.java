package com.idega.block.forum.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.block.forum.data.ForumData;
import com.idega.block.forum.data.ForumDataHome;
import com.idega.core.data.ICTreeNode;
import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ForumBusiness {

	public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";

	public static final int FORUM_TOPICS = 1;
	public static final int FORUM_THREADS = 2;
	public static final int FORUM_COLLECTION = 3;
	public static final int TOPIC_COLLECTION = 4;

	public static final String PARAMETER_CLICKED = "clicked";
	public static final String PARAMETER_CLOSE = "close";
	public static final String PARAMETER_DELETE = "delete";
	public static final String PARAMETER_FALSE = "false";
	public static final String PARAMETER_MODE = "fo_mode";
	public static final String PARAMETER_PARENT_THREAD_ID = "fo_p_th_id";
	public static final String PARAMETER_SAVE = "save";
	public static final String PARAMETER_STATE = "fo_state";
	public static final String PARAMETER_THREAD_BODY = "fo_th_body";
	public static final String PARAMETER_THREAD_HEADLINE = "fo_th_headline";
	public static final String PARAMETER_THREAD_ID = "fo_th_id";
	public static final String PARAMETER_TOPIC_ID = "fo_to_id";
	public static final String PARAMETER_TOPIC_DESCRIPTION = "fo_to_desc";
	public static final String PARAMETER_TOPIC_NAME = "fo_to_name";
	public static final String PARAMETER_TRUE = "true";
	public static final String PARAMETER_USER_NAME = "fo_name";
	public static final String PARAMETER_USER_EMAIL = "fo_email";
	public static final String PARAMETER_OBJECT_INSTANCE_ID = "fo_o_i_id";

	public static final String PARAMETER_FIRST_THREAD = "fo_f_th";
	public static final String PARAMETER_LAST_THREAD = "fo_l_th";

	public List getChildThreads(ForumData thread) {
		Vector vector = new Vector();
		vector.add(thread);
		getChildren(vector, thread);
		return vector;
	}

	private void getChildren(List list, ICTreeNode node) {
		Iterator iter = node.getChildren();
		if (iter != null) {
			while (iter.hasNext()) {
				ForumData thread = (ForumData) iter.next();
				list.add(thread);
				getChildren(list, thread);
			}
		}
	}

	public void saveThread(int topicID, int threadID, int parentThreadID, int userID, String userName, String email, String headline, String body) {
		try {
			boolean update = false;
			ForumData thread = getForumHome().create();
			ForumData parentThread = null;
			if (threadID != -1) {
				thread = getForumData(threadID);
				update = true;
			}

			if (parentThreadID != -1) {
				parentThread = getForumData(parentThreadID);
				updateParent(parentThread, 1);
			}

			thread.setTopicID(topicID);
			thread.setParentThreadID(parentThreadID);
			if (headline != null && headline.length() > 0)
				thread.setThreadSubject(headline);
			if (body != null && body.length() > 0)
				thread.setThreadBody(body);
			if (userID != -1)
				thread.setUserID(userID);
			else {
				if (userName != null && userName.length() > 0)
					thread.setUserName(userName);
				if (email != null && email.length() > 0)
					thread.setUserEMail(email);
			}
			thread.setValid(true);
			if (!update)
				thread.setNumberOfResponses(0);
			thread.setThreadDate(new IWTimestamp().getTimestampRightNow());

			if (update) {
				try {
					thread.update();
				}
				catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			else {
				try {
					thread.insert();
				}
				catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (!update && parentThread != null) {
				parentThread.addChild(thread);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateParent(ForumData thread, int increase) {
		thread.setNumberOfResponses(thread.getNumberOfResponses() + increase);
		try {
			thread.update();
		}
		catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	public boolean hasPreviousThreads(int firstThread) {
		if (firstThread > 1)
			return true;
		return false;
	}

	public boolean hasNextThreads(ForumData[] threads, int lastThread) {
		if (threads != null) {
			if (threads.length > lastThread)
				return true;
			return false;
		}
		return false;
	}

	public void deleteThread(int threadID) {
		deleteThread(getForumData(threadID));
	}

	public void deleteThread(ForumData thread) {
		try {
			if (thread != null) {
				thread.setValid(false);
				thread.update();
				if (thread.getParentThreadID() != -1) {
					ForumData parentThread = getForumData(thread.getParentThreadID());
					updateParent(parentThread, -1);
					parentThread.removeChild(thread);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	public ForumData getForumData(int threadID) {
		try {
			return getForumHome().findByPrimaryKey(threadID);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public ForumDataHome getForumHome() {
		try {
			return (ForumDataHome) IDOLookup.getHome(ForumData.class);
		}
		catch (RemoteException rme) {
			return null;
		}
	}

	public ForumData[] getThreads(ICCategory category) {
		try {
			return (ForumData[]) getForumHome().findAllThreads(category).toArray(new ForumData[0]);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public List getThreads(ICCategory category, int numberOfReturns) {
		try {
			Collection collection = getForumHome().findAllThreads(category, numberOfReturns);
			return new Vector(collection);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public ForumData[] getThreads(ForumData[] threads, int fromThread, int toThread) {
		if (threads != null) {
			if (fromThread >= threads.length) {
				fromThread = threads.length;
			}
			if (toThread >= threads.length) {
				toThread = threads.length;
			}

			ForumData[] results = new ForumData[(toThread + 1) - fromThread];
			int k = 0;
			if (threads.length > 0) {
				for (int i = fromThread - 1; i < toThread; i++)
					results[k++] = threads[i];

				return results;
			}
			return null;
		}
		return null;
	}

	public int getNumberOfThreads(ICCategory category) {
		try {
			return getForumHome().getNumberOfThreads(category);
		}
		catch (EJBException e) {
			return 0;
		}
	}

	public List getThreadsInCategories(Collection categories, int numberOfReturns) {
		try {
			Collection collection = getForumHome().findThreadsInCategories(categories, numberOfReturns);
			return new Vector(collection);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public ForumData getNewestThreads(ICCategory category) {
		try {
			Vector vector = new Vector(getForumHome().findNewestThread(category));
			if (vector.size() > 0)
				return (ForumData) vector.get(0);
			return null;
		}
		catch (FinderException e) {
			return null;
		}
	}
}