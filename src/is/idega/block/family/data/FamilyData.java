package is.idega.block.family.data;

import java.util.ArrayList;
import java.util.Collection;
import com.idega.user.data.User;


/**
 * @author Joakim
 *
 */
public class FamilyData {
	private User husband;
	private User wife;
	private Collection children = new ArrayList();
	
	
	/**
	 * @return Returns the children.
	 */
	public Collection getChildren() {
		return children;
	}
	/**
	 * @param children The children to set.
	 */
	public void setChildren(Collection children) {
		this.children = children;
	}
	/**
	 * 
	 * @param child one child to add to the family
	 */
	public void addChild(User child) {
		children.add(child);
	}
	/**
	 * @return Returns the husband.
	 */
	public User getHusband() {
		return husband;
	}
	/**
	 * @param husband The husband to set.
	 */
	public void setHusband(User husband) {
		this.husband = husband;
	}
	/**
	 * @return Returns the wife.
	 */
	public User getWife() {
		return wife;
	}
	/**
	 * @param wife The wife to set.
	 */
	public void setWife(User wife) {
		this.wife = wife;
	}
}
