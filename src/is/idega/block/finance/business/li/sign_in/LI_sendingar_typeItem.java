/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_sendingar_typeItem.java,v 1.1 2005/03/15 11:35:07 birna Exp $
 */

package is.idega.block.finance.business.li.sign_in;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/


/**
 * Class LI_sendingar_typeItem.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:07 $
 */
public class LI_sendingar_typeItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _snigill
     */
    private is.idega.block.finance.business.li.sign_in.types.LI_er_til_type _snigill;

    /**
     * Field _tolvupostur
     */
    private java.lang.String _tolvupostur;

    /**
     * Field _sms
     */
    private java.lang.String _sms;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_sendingar_typeItem() {
        super();
    } //-- is.idega.block.finance.business.li.sign_in.LI_sendingar_typeItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'sms'.
     * 
     * @return String
     * @return the value of field 'sms'.
     */
    public java.lang.String getSms()
    {
        return this._sms;
    } //-- java.lang.String getSms() 

    /**
     * Returns the value of field 'snigill'.
     * 
     * @return LI_er_til_type
     * @return the value of field 'snigill'.
     */
    public is.idega.block.finance.business.li.sign_in.types.LI_er_til_type getSnigill()
    {
        return this._snigill;
    } //-- is.idega.block.finance.business.li.sign_in.types.LI_er_til_type getSnigill() 

    /**
     * Returns the value of field 'tolvupostur'.
     * 
     * @return String
     * @return the value of field 'tolvupostur'.
     */
    public java.lang.String getTolvupostur()
    {
        return this._tolvupostur;
    } //-- java.lang.String getTolvupostur() 

    /**
     * Sets the value of field 'sms'.
     * 
     * @param sms the value of field 'sms'.
     */
    public void setSms(java.lang.String sms)
    {
        this._sms = sms;
    } //-- void setSms(java.lang.String) 

    /**
     * Sets the value of field 'snigill'.
     * 
     * @param snigill the value of field 'snigill'.
     */
    public void setSnigill(is.idega.block.finance.business.li.sign_in.types.LI_er_til_type snigill)
    {
        this._snigill = snigill;
    } //-- void setSnigill(is.idega.block.finance.business.li.sign_in.types.LI_er_til_type) 

    /**
     * Sets the value of field 'tolvupostur'.
     * 
     * @param tolvupostur the value of field 'tolvupostur'.
     */
    public void setTolvupostur(java.lang.String tolvupostur)
    {
        this._tolvupostur = tolvupostur;
    } //-- void setTolvupostur(java.lang.String) 

}
