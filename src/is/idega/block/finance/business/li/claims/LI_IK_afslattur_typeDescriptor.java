/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.4</a>, using an XML
 * Schema.
 * $Id: LI_IK_afslattur_typeDescriptor.java,v 1.1 2005/03/15 11:35:05 birna Exp $
 */

package is.idega.block.finance.business.li.claims;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/


/**
 * Class LI_IK_afslattur_typeDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/03/15 11:35:05 $
 */
public class LI_IK_afslattur_typeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public LI_IK_afslattur_typeDescriptor() {
        super();
        xmlName = "LI_IK_afslattur_type";
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _vidmidun
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type.class, "_vidmidun", "vidmidun", org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                LI_IK_afslattur_type target = (LI_IK_afslattur_type) object;
                return target.getVidmidun();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    LI_IK_afslattur_type target = (LI_IK_afslattur_type) object;
                    target.setVidmidun( (is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(is.idega.block.finance.business.li.claims.types.LI_IK_vidmidun_dags_type.class, handler));
        desc.setImmutable(true);
        desc.setRequired(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _vidmidun
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _tegund_afslattar
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type.class, "_tegund_afslattar", "tegund_afslattar", org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                LI_IK_afslattur_type target = (LI_IK_afslattur_type) object;
                return target.getTegund_afslattar();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    LI_IK_afslattur_type target = (LI_IK_afslattur_type) object;
                    target.setTegund_afslattar( (is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(is.idega.block.finance.business.li.claims.types.LI_IK_tegund_afslattar_og_vanskilagjalds_type.class, handler));
        desc.setImmutable(true);
        desc.setRequired(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _tegund_afslattar
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors
        
        //-- _items
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(is.idega.block.finance.business.li.claims.LI_IK_afslattur_typeItem.class, "_items", (String)null, org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                LI_IK_afslattur_type target = (LI_IK_afslattur_type) object;
                return target.getLI_IK_afslattur_typeItem();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    LI_IK_afslattur_type target = (LI_IK_afslattur_type) object;
                    target.addLI_IK_afslattur_typeItem( (is.idega.block.finance.business.li.claims.LI_IK_afslattur_typeItem) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new is.idega.block.finance.business.li.claims.LI_IK_afslattur_typeItem();
            }
        } );
        desc.setHandler(handler);
        desc.setContainer(true);
        desc.setClassDescriptor(new is.idega.block.finance.business.li.claims.LI_IK_afslattur_typeItemDescriptor());
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _items
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- is.idega.block.finance.business.li.claims.LI_IK_afslattur_typeDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    public java.lang.Class getJavaClass()
    {
        return is.idega.block.finance.business.li.claims.LI_IK_afslattur_type.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
