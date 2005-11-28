/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface CurrentResidency extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#getDescriptionColumnName
	 */
	public String getDescriptionColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#getRequiresExtranInfoColumnName
	 */
	public String getRequiresExtranInfoColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#getRequiresExtraInfo
	 */
	public boolean getRequiresExtraInfo();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#setRequiresExtranInfo
	 */
	public void setRequiresExtranInfo(boolean extraInfo);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String key);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

}
