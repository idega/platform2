/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Description: Account key entity, debet (d) or credit (c)<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportAccountKeyBMPBean extends GenericEntity implements WorkReportAccountKey{
	protected final static String ENTITY_NAME = "ISI_WR_ACCOUNT_KEY";
	protected final static String COLUMN_NAME_KEY_NAME = "KEY_NAME";
	protected final static String COLUMN_NAME_KEY_NUMBER = "KEY_NUMBER";
	protected final static String COLUMN_NAME_KEY_TYPE = "KEY_TYPE";
	protected final static String COLUMN_NAME_KEY_DEBET_OR_CREDIT = "DEB_OR_CRED";//values d/c
	
	public WorkReportAccountKeyBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_KEY_NAME,"Account key name",true,true,String.class);
		addAttribute(COLUMN_NAME_KEY_NUMBER,"Account key number ",true,true,String.class);
		addAttribute(COLUMN_NAME_KEY_TYPE,"Account key type",true,true,String.class);
		addAttribute(COLUMN_NAME_KEY_DEBET_OR_CREDIT,"Debet or Credit (d/c)",true,true,String.class);
	}
	
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
}
