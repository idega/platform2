/*
 * $Id: ForumData.java,v 1.6 2005/04/18 11:30:40 gummi Exp $
 * Created on 14.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.forum.data;

import java.sql.Timestamp;
import java.util.Iterator;
import com.idega.block.category.data.ICCategory;
import com.idega.data.TreeableEntity;


/**
 * 
 *  Last modified: $Date: 2005/04/18 11:30:40 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.6 $
 */
public interface ForumData extends TreeableEntity {

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getParentThreadID
	 */
	public int getParentThreadID();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getTopic
	 */
	public ICCategory getTopic();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getTopicID
	 */
	public int getTopicID();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getThreadSubject
	 */
	public String getThreadSubject();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getThreadBody
	 */
	public String getThreadBody();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getUserName
	 */
	public String getUserName();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getUserEMail
	 */
	public String getUserEMail();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getThreadDate
	 */
	public Timestamp getThreadDate();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getNumberOfResponses
	 */
	public int getNumberOfResponses();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getTopParentID
	 */
	public int getTopParentID();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getNumberOfSubThreads
	 */
	public int getNumberOfSubThreads();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#isValid
	 */
	public boolean isValid();

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setParentThreadID
	 */
	public void setParentThreadID(int parentThreadID);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setTopicID
	 */
	public void setTopicID(int topicID);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setThreadSubject
	 */
	public void setThreadSubject(String threadSubject);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setThreadBody
	 */
	public void setThreadBody(String threadBody);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setUserName
	 */
	public void setUserName(String userName);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setUserEMail
	 */
	public void setUserEMail(String email);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setThreadDate
	 */
	public void setThreadDate(Timestamp threadDate);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setNumberOfResponses
	 */
	public void setNumberOfResponses(int numberOfResponses);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setTopParentID
	 */
	public void setTopParentID(int topParentID);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setNumberOfSubThreads
	 */
	public void setNumberOfSubThreads(int numberOfSubThreads);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#setValid
	 */
	public void setValid(boolean valid);

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#getChildrenIterator
	 */
	public Iterator getChildrenIterator();
}
