//idega 2000 - Laddi

package is.idega.idegaweb.golf.block.boxoffice.data;

import java.sql.*;
import com.idega.data.*;

public class IssuesBMPBean extends GenericEntity{

	public IssuesBMPBean(){
		super();
	}

	public IssuesBMPBean(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("issue_name", "Nafn", true, true, "java.lang.String");
		addAttribute("image_id", "Issue mynd", true, true, "java.lang.Integer");
	}

	public String getIDColumnName(){
		return "issue_id";
	}

	public String getEntityName(){
		return "Issues";
	}

        public void setDefaultValues() {
            this.setImageId(-1);
            this.setIssueName(" ");
        }

	public String getName(){
		return getIssueName();
	}

	public String getIssueName(){
		return getStringColumnValue("issue_name");
	}

	public void setIssueName(String issue_name){
			setColumn("issue_name", issue_name);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(int image_id){
			setColumn("image_id", image_id);
	}


}
