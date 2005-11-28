/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface SpouseOccupation extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#getDescriptionColumnName
	 */
	public String getDescriptionColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String key);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

}
