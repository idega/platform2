package com.idega.block.finance.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface AccountHome extends IDOHome {
	public Account create() throws CreateException;

	public Account findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByUserId(int userId) throws FinderException;

	public Collection findAllByUserIdAndType(int userId, String type) throws FinderException;

	public Account findByUserAndType(User user, String type) throws FinderException;

	public Collection findBySearch(String id, String name, String pid, String type, int iCategoryId) throws FinderException;

	public Collection findByAssessmentRound(int roundid) throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;

	public int countByTypeAndCategory(String type, Integer categoryID) throws IDOException;

	public int countByAssessmentRound(Integer roundID) throws IDOException;

	public Collection findByAssessmentRound(Integer roundID, int resultSize, int startindex) throws FinderException;
}