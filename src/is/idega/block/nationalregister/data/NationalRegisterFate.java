/**
 * 
 */
package is.idega.block.nationalregister.data;




import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface NationalRegisterFate extends IDOEntity {
	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterFateBMPBean#setFateCode
	 */
	public void setFateCode(String fateCode);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterFateBMPBean#setFateString
	 */
	public void setFateString(String fateString);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterFateBMPBean#getFateCode
	 */
	public String getFateCode();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterFateBMPBean#getFateString
	 */
	public String getFateString();

}
