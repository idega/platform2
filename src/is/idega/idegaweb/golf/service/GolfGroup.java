package is.idega.idegaweb.golf.service;

import java.util.*;
import java.sql.*;
import is.idega.idegaweb.golf.entity.*;


public class GolfGroup {

private int member_id = 1;

  public GolfGroup(int member_id) {
  	this.member_id=member_id;
  }

  public GolfGroup(String member_id) {
  	this.member_id=Integer.parseInt(member_id);
  }

	public boolean getCanWrite() throws SQLException {

		Member member = new Member(member_id);

		String gender = member.getGender();
		float grunn = member.getHandicap();
		boolean canWrite = true;

		if ( gender.equalsIgnoreCase("M") ) {
			if (grunn <= 4.4) {
				canWrite = false;
			}
			else {
				canWrite = true;
			}
		}

		else if ( gender.equalsIgnoreCase("F") ) {
			if (grunn <= 10.4) {
				canWrite = false;
			}
			else {
				canWrite = true;
			}

		}

		return canWrite;

	}

}