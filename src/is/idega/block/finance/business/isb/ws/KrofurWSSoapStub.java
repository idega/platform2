/**
 * KrofurWSSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

import javax.xml.soap.SOAPException;
import net.vitale.filippo.axis.handlers.WsseClientHandler;
import org.apache.axis.message.MessageElement;

public class KrofurWSSoapStub extends org.apache.axis.client.Stub implements is.idega.block.finance.business.isb.ws.KrofurWSSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();
    
    /*added - birna*/
    private String username;
    private String password;

    static org.apache.axis.description.OperationDesc [] _operations; 

    static {
        _operations = new org.apache.axis.description.OperationDesc[18];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("StofnaKrofu");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws.Krafa.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EndurvekjaKrofu");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws.Krafa.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("FellaKrofu");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws.Krafa.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("BreytaKrofu");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws.Krafa.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("StofnaKrofubunka");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krofur"), new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrafa"), is.idega.block.finance.business.isb.ws.ArrayOfKrafa.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        oper.setReturnClass(java.math.BigDecimal.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofubunkaResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofubunkasvar");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "bunkanumer"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"), java.math.BigDecimal.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfUppreiknudKrafa"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofubunkasvarResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaGreidslurKrafnaTimabil");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "audkenni"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "dagsetningFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "dagsetningTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidslurKrafnaTimabilResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaGreidslurKrafna");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "audkenni"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidslurKrafnaResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaGreidsluKrofu");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "banki"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "hofudbok"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krofunumer"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagi"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidsluKrofuResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofur");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "audkenni"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagiFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagiTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "astand"), new javax.xml.namespace.QName("http://ws.isb.is", "AstandKrofu"), is.idega.block.finance.business.isb.ws.AstandKrofu.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfUppreiknudKrafa"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofurResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[9] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofu");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "banki"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "hofudbok"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "krofunumer"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagi"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.UppreiknudKrafa.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofuResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.setReturnHeader(true);
        _operations[10] = oper;
        

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("StofnaKrofuskra");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "tegund"), new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofuskra"), is.idega.block.finance.business.isb.ws.TegundKrofuskra.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        oper.setReturnClass(java.math.BigDecimal.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofuskraResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofuskrasvar");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "tegund"), new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofuskra"), is.idega.block.finance.business.isb.ws.TegundKrofuskra.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "bunkanumer"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"), java.math.BigDecimal.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofuupplysingaskra");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "tegundFyrirspurnar"), new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofufyrirspurnar"), is.idega.block.finance.business.isb.ws.TegundKrofufyrirspurnar.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaAllarBeingreidslubeidnir");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaAllarBeingreidslubeidnirResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaBeingreidslubeidnir");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "textalykill"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaBeingreidslubeidnirResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaBeingreidslubeidnaHreyfingar");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "textalykill"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaBeingreidslubeidnaHreyfingarResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaAllarBeingreidslubeidnaHreyfingar");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaAllarBeingreidslubeidnaHreyfingarResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[17] = oper;

    }

    public KrofurWSSoapStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public KrofurWSSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public KrofurWSSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Krafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.Krafa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "AstandKrofu");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.AstandKrofu.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrofulidur");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.ArrayOfKrofulidur.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.Beingreidslubeidni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfUppreiknudKrafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "StadaKrofu");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.StadaKrofu.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "BeingreidslubeidniStada");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.BeingreidslubeidniStada.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.Greidsla.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofuskra");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.TegundKrofuskra.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "PrentunKrofu");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.PrentunKrofu.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofufyrirspurnar");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.TegundKrofufyrirspurnar.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.UppreiknudKrafa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.Krofulidur.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws.ArrayOfKrafa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            
            _call.setProperty(WsseClientHandler.PASSWORD_OPTION,
                WsseClientHandler.PASSWORD_CLEARTEXT);
            _call.setClientHandlers(new WsseClientHandler(), null);
            _call.setUsername(username);
            _call.setPassword(password);
            
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(username);//super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(password);//super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle("http://ws.isb.is");
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public void stofnaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/StofnaKrofu");
        _call.setEncodingStyle("http://ws.isb.is");
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofu"));
                
        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void endurvekjaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/EndurvekjaKrofu");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "EndurvekjaKrofu"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void fellaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/FellaKrofu");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "FellaKrofu"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void breytaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/BreytaKrofu");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "BreytaKrofu"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.math.BigDecimal stofnaKrofubunka(is.idega.block.finance.business.isb.ws.ArrayOfKrafa krofur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/StofnaKrofubunka");
        _call.setEncodingStyle("http://ws.isb.is");
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofubunka"));
        
        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krofur});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.math.BigDecimal) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.math.BigDecimal) org.apache.axis.utils.JavaUtils.convert(_resp, java.math.BigDecimal.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa saekjaKrofubunkasvar(java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofubunkasvar");
        _call.setEncodingStyle("http://ws.isb.is");
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofubunkasvar"));
        
        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {bunkanumer});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfGreidsla saekjaGreidslurKrafnaTimabil(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar dagsetningFra, java.util.Calendar dagsetningTil, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaGreidslurKrafnaTimabil");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidslurKrafnaTimabil"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, audkenni, dagsetningFra, dagsetningTil, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfGreidsla) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfGreidsla) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfGreidsla saekjaGreidslurKrafna(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaGreidslurKrafna");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidslurKrafna"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, audkenni, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfGreidsla) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfGreidsla) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfGreidsla saekjaGreidsluKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaGreidsluKrofu");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidsluKrofu"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(banki), new java.lang.Integer(hofudbok), new java.lang.Integer(krofunumer), gjalddagi});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfGreidsla) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfGreidsla) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfGreidsla.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa saekjaKrofur(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar gjalddagiFra, java.util.Calendar gjalddagiTil, is.idega.block.finance.business.isb.ws.AstandKrofu astand, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
      
      _call.setOperation(_operations[9]);
      _call.setUseSOAPAction(true);
      _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofur");
      _call.setEncodingStyle("http://ws.isb.is");
      _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
      _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
      _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
      _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofur"));

      setRequestHeaders(_call);
      setAttachments(_call);
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, audkenni, gjalddagiFra, gjalddagiTil, astand, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.UppreiknudKrafa saekjaKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofu");
        _call.setEncodingStyle("http://ws.isb.is");
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofu"));
               
        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(banki), new java.lang.Integer(hofudbok), new java.lang.Integer(krofunumer), gjalddagi});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.UppreiknudKrafa) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.UppreiknudKrafa) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.UppreiknudKrafa.class);
            }
        }
    }

    public java.math.BigDecimal stofnaKrofuskra(is.idega.block.finance.business.isb.ws.TegundKrofuskra tegund) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/StofnaKrofuskra");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofuskra"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tegund});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.math.BigDecimal) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.math.BigDecimal) org.apache.axis.utils.JavaUtils.convert(_resp, java.math.BigDecimal.class);
            }
        }
    }

    public void saekjaKrofuskrasvar(is.idega.block.finance.business.isb.ws.TegundKrofuskra tegund, java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofuskrasvar");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofuskrasvar"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tegund, bunkanumer});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void saekjaKrofuupplysingaskra(is.idega.block.finance.business.isb.ws.TegundKrofufyrirspurnar tegundFyrirspurnar) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofuupplysingaskra");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofuupplysingaskra"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tegundFyrirspurnar});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaAllarBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaAllarBeingreidslubeidnir");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaAllarBeingreidslubeidnir"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaBeingreidslubeidnir");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaBeingreidslubeidnir"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, textalykill, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaBeingreidslubeidnaHreyfingar");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaBeingreidslubeidnaHreyfingar"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, textalykill, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
            }
        }
    }

    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaAllarBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaAllarBeingreidslubeidnaHreyfingar");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaAllarBeingreidslubeidnaHreyfingar"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni.class);
            }
        }
    }
    
    
  	private MessageElement createUsernameToken(String usernameS, String passwordS, String passwordOption) throws SOAPException {
  		MessageElement usernameToken = new MessageElement("","wsse:UsernameToken");
		MessageElement username = new MessageElement("", "wsse:Username");
		username.setObjectValue(usernameS);
		usernameToken.addChild(username);
		
		MessageElement password = new MessageElement("", "wsse:Password");
		password.setObjectValue(passwordS);
		usernameToken.addChild(password);
		return usernameToken;

  	}
  	
  	/*added - birna*/
  	public void setUsername(String username) {
  		this.username = username;
  	}
  	public String getUsername() {
  		return this.username;
  	}
  	public void setPassword(String password) {
  		this.password = password;
  	}
  	public String getPassword() {
  		return this.password;
  	}

}
