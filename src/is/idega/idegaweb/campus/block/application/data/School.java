/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface School extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SchoolBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SchoolBMPBean#getName
	 */
	public String getName();

}
