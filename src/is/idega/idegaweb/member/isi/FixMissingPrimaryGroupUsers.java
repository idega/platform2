/*
 * $Id: FixMissingPrimaryGroupUsers.java,v 1.1 2005/06/29 11:46:40 eiki Exp $ Created on
 * Jun 8, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.member.isi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.idega.util.database.ConnectionBroker;

public class FixMissingPrimaryGroupUsers {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection connection = null;
		try {
			int totalChanged = 0;
			String sql = "select u.ic_user_id,r.ic_group_id from ic_user u,ic_group_relation r,ic_group g where u.primary_group is null and r.group_relation_status = 'ST_ACTIVE' and r.relationship_type='GROUP_PARENT' and u.ic_user_id = r.related_ic_group_id and g.ic_group_id = r.ic_group_id";
			connection = ConnectionBroker.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			for (int row = 0; resultSet.next(); row++) {
				int userId = resultSet.getInt(1);
				int groupId = resultSet.getInt(2);
				
				Statement stmt = connection.createStatement();
				totalChanged += stmt.executeUpdate("update ic_user set primary_group ="+groupId+" where ic_user_id="+userId);
				
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionBroker.freeConnection(connection);
		}
	}
}
