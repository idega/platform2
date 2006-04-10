/**
 * KrofurWSSoap_BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws2;

public class KrofurWSSoap_BindingStub extends org.apache.axis.client.Stub implements is.idega.block.finance.business.isb.ws2.KrofurWSSoap_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[18];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("StofnaKrofu");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws2.Krafa.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EndurvekjaKrofu");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws2.Krafa.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("FellaKrofu");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws2.Krafa.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("BreytaKrofu");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"), is.idega.block.finance.business.isb.ws2.Krafa.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("StofnaKrofubunka");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krofur"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrafa"), is.idega.block.finance.business.isb.ws2.Krafa[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Krafa"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        oper.setReturnClass(java.math.BigDecimal.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofubunkaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofubunkasvar");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "bunkanumer"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"), java.math.BigDecimal.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfUppreiknudKrafa"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofubunkasvarResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaGreidslurKrafnaTimabil");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "audkenni"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "dagsetningFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "dagsetningTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Greidsla[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidslurKrafnaTimabilResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaGreidslurKrafna");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "audkenni"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Greidsla[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidslurKrafnaResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaGreidsluKrofu");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "banki"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "hofudbok"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krofunumer"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Greidsla[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaGreidsluKrofuResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofur");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "audkenni"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagiFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagiTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "astand"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "AstandKrofu"), is.idega.block.finance.business.isb.ws2.AstandKrofu.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfUppreiknudKrafa"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofurResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofu");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "banki"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "hofudbok"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "krofunumer"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "gjalddagi"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.UppreiknudKrafa.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofuResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("StofnaKrofuskra");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "tegund"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofuskra"), is.idega.block.finance.business.isb.ws2.TegundKrofuskra.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        oper.setReturnClass(java.math.BigDecimal.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofuskraResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofuskrasvar");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "tegund"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofuskra"), is.idega.block.finance.business.isb.ws2.TegundKrofuskra.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "bunkanumer"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"), java.math.BigDecimal.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaKrofuupplysingaskra");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "tegundFyrirspurnar"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofufyrirspurnar"), is.idega.block.finance.business.isb.ws2.TegundKrofufyrirspurnar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaAllarBeingreidslubeidnir");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaAllarBeingreidslubeidnirResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaBeingreidslubeidnir");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "textalykill"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaBeingreidslubeidnirResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaBeingreidslubeidnaHreyfingar");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "textalykill"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaBeingreidslubeidnaHreyfingarResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaekjaAllarBeingreidslubeidnaHreyfingar");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "kennitalaKrofuhafa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaFra"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://ws.isb.is", "faerslaTil"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni"));
        oper.setReturnClass(is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaAllarBeingreidslubeidnaHreyfingarResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

    }

    public KrofurWSSoap_BindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public KrofurWSSoap_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public KrofurWSSoap_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfBeingreidslubeidni");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni");
            qName2 = new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfGreidsla");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Greidsla[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla");
            qName2 = new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Krafa[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Krafa");
            qName2 = new javax.xml.namespace.QName("http://ws.isb.is", "Krafa");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfKrofulidur");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Krofulidur[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur");
            qName2 = new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "ArrayOfUppreiknudKrafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa");
            qName2 = new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "AstandKrofu");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.AstandKrofu.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Beingreidslubeidni");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Beingreidslubeidni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "BeingreidslubeidniStada");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.BeingreidslubeidniStada.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Greidsla");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Greidsla.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Krafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Krafa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "Krofulidur");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.Krofulidur.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "PrentunKrofu");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.PrentunKrofu.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "StadaKrofu");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.StadaKrofu.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofufyrirspurnar");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.TegundKrofufyrirspurnar.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "TegundKrofuskra");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.TegundKrofuskra.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://ws.isb.is", "UppreiknudKrafa");
            cachedSerQNames.add(qName);
            cls = is.idega.block.finance.business.isb.ws2.UppreiknudKrafa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
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
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public void stofnaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/StofnaKrofu");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofu"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void endurvekjaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void fellaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void breytaKrofu(is.idega.block.finance.business.isb.ws2.Krafa krafa) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krafa});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.math.BigDecimal stofnaKrofubunka(is.idega.block.finance.business.isb.ws2.Krafa[] krofur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/StofnaKrofubunka");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "StofnaKrofubunka"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {krofur});

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
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[] saekjaKrofubunkasvar(java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofubunkasvar");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofubunkasvar"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {bunkanumer});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Greidsla[] saekjaGreidslurKrafnaTimabil(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar dagsetningFra, java.util.Calendar dagsetningTil, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, audkenni, dagsetningFra, dagsetningTil, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Greidsla[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Greidsla[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Greidsla[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Greidsla[] saekjaGreidslurKrafna(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, audkenni, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Greidsla[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Greidsla[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Greidsla[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Greidsla[] saekjaGreidsluKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(banki), new java.lang.Integer(hofudbok), new java.lang.Integer(krofunumer), gjalddagi});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Greidsla[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Greidsla[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Greidsla[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[] saekjaKrofur(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar gjalddagiFra, java.util.Calendar gjalddagiTil, is.idega.block.finance.business.isb.ws2.AstandKrofu astand, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofur");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofur"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, audkenni, gjalddagiFra, gjalddagiTil, astand, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.UppreiknudKrafa[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.UppreiknudKrafa saekjaKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.isb.is/SaekjaKrofu");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.isb.is", "SaekjaKrofu"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(banki), new java.lang.Integer(hofudbok), new java.lang.Integer(krofunumer), gjalddagi});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.UppreiknudKrafa) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.UppreiknudKrafa) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.UppreiknudKrafa.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.math.BigDecimal stofnaKrofuskra(is.idega.block.finance.business.isb.ws2.TegundKrofuskra tegund) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tegund});

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
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void saekjaKrofuskrasvar(is.idega.block.finance.business.isb.ws2.TegundKrofuskra tegund, java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tegund, bunkanumer});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void saekjaKrofuupplysingaskra(is.idega.block.finance.business.isb.ws2.TegundKrofufyrirspurnar tegundFyrirspurnar) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tegundFyrirspurnar});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaAllarBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, textalykill, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, textalykill, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[] saekjaAllarBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException {
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
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {kennitalaKrofuhafa, new java.lang.Integer(faerslaFra), new java.lang.Integer(faerslaTil)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[]) org.apache.axis.utils.JavaUtils.convert(_resp, is.idega.block.finance.business.isb.ws2.Beingreidslubeidni[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
