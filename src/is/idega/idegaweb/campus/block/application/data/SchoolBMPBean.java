package is.idega.idegaweb.campus.block.application.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

public class SchoolBMPBean extends GenericEntity implements School {

	private static final String ENTITY_NAME = "cam_school";

	protected static final String SCHOOL_NAME = "name";
	
	public SchoolBMPBean() {
		super();
	}

	public SchoolBMPBean(int id) throws SQLException {
		super(id);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(SCHOOL_NAME, "Description", true, true, "java.lang.String");
	}
	
	public void setName(String name) {
		setColumn(SCHOOL_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(SCHOOL_NAME);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}
}