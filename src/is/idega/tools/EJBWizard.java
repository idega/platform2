package is.idega.tools;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;
import com.idega.xml.XMLParser;


/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class EJBWizard {
	protected boolean legacyIDO = false;
	//protected String beanClassSuffix = "BMPBean";
	private String className;
	private File workingDirectory;
	private String superInterface;
	private boolean generatedeploymentdescriptors=false;
	public EJBWizard(String className) {
		setEntityClassName(className);
		initialize();
	}
	public EJBWizard(Class entityClass) {
		setEntityClass(entityClass);
		initialize();
	}
	public void initialize() {
		String currentDir = System.getProperty("user.dir");
		File workingDir = new File(currentDir);
		this.setWorkingDirectory(workingDir);
	}
	public static void main(String[] args) throws Exception {
		try {
			String className = args[0];
			// file check:
			if (className.endsWith(".java") || className.endsWith(".JAVA")) {
				File javaFile = new File(className);
				FileReader reader = new FileReader(javaFile);
				LineNumberReader linereader = new LineNumberReader(new FileReader(javaFile));
				String line;
				String pack = "";
				String clss = "";
				int nr = 0;
				while ((line = linereader.readLine()) != null) {
					StringTokenizer tok = new StringTokenizer(line, " ;");
					while (tok.hasMoreTokens()) {
						String token = tok.nextToken();
						if (token.equals("package")) {
							if (tok.hasMoreTokens())
								pack = tok.nextToken();
						} else if (token.equals("class")) {
							if (tok.hasMoreTokens())
								clss = tok.nextToken();
							break;
						}
						//System.out.println("line"+nr++);
					}
				}
				className = pack + "." + clss;
				//System.out.println(className);
				EJBWizard instance = new EJBWizard(className);
				instance.setWorkingDirectory(javaFile.getParentFile());
				instance.doJavaFileCreate();
			} else {
				System.out.println("className is " + className);
				String currentDir = System.getProperty("user.dir");
				File workingDir = new File(currentDir);
				EJBWizard instance = new EJBWizard(className);
				instance.setWorkingDirectory(workingDir);
				instance.doJavaFileCreate();
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			System.out.println("EJBWizard: You have to supply a valid ClassName as an argument");
		}
	}
	
	protected boolean getIfGenerateDeploymentDescriptors(){
		return this.generatedeploymentdescriptors;	
	}

	
	public void setLegacyIDO(boolean ifLegacy) {
		this.legacyIDO = ifLegacy;
	}
	public String getBeanSuffix() {
		return "BMPBean";
	}
	public void doJavaFileCreate() throws Exception {
		EJBWizardClassCreator inst = new EJBWizardClassCreator(className, this);
		if (this.superInterface != null) {
			inst.setRemoteInterfaceSuperInterface(superInterface);
		}
		inst.setWorkingDirectory(getWorkingDirectory());
		inst.setLegacyIDO(this.legacyIDO);
		setClassCreatorProperties(inst);
		inst.createAllFiles();
		if(this.getIfGenerateDeploymentDescriptors()){
			mergeAllDeploymentDescriptors();
			createDeploymentDescriptor(inst);
		}
		
	}
	public void setEntityClassName(String className) {
		this.className = className;
	}
	public void setEntityClass(Class entityClass) {
		this.setEntityClassName(entityClass.getName());
	}
	public void setWorkingDirectory(File directory) {
		System.out.println("Setting working dir=" + directory.getAbsolutePath());
		this.workingDirectory = directory;
	}
	public File getWorkingDirectory() {
		return this.workingDirectory;
	}
	public void setRemoteInterfaceSuperInterface(String interfaceClass) {
		//System.out.println("EJBWizard - Setting RemoteSuperInterface: "+interfaceClass+" for "+this.className);
		this.superInterface = interfaceClass;
	}
	/**
	 * Overrided in sublcasses
	 * @param inst
	 */
	protected void setClassCreatorProperties(EJBWizardClassCreator inst) {
		inst.setToThrowRemoteExceptions(false);
		inst.setFactorySuperClass("com.idega.data.IDOFactory");
		inst.setHomeSuperInterface("com.idega.data.IDOHome");
	}
	public boolean finderMethodsAllowed() {
		return true;
	}
	public String[] getInternalMethodImplementations(ClassIntrospector introspector) {
		int length = 1;
		String[] returningMethods = new String[length];
		for (int i = 0; i < length; i++) {
			String methodString = " protected Class getEntityInterfaceClass()";
			methodString += "{\n";
			methodString += "  return " + introspector.getShortName() + ".class;";
			methodString += "\n }\n\n";
			returningMethods[i] = methodString;
		}
		return returningMethods;
	}
	public String[] getCreateMethodImplementations(ClassIntrospector introspector) {
		String[] finderMethodStrings = introspector.getCreateMethods();
		Method[] methods = introspector.getCreateMethodsArray();
		int length = methods.length;
		if (legacyIDO) {
			length += 2;
		} else {
			length += 1;
		}
		String[] returningMethods = new String[length];
		int i = 0;
		for (i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodString = finderMethodStrings[i] + "{\n";
			methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
			methodString += "\tObject pk = (("
				+ introspector.getEntityBeanName()
				+ ")entity)."
				+ method.getName()
				+ "("
				+ introspector.getParametersInForMethod(method)
				+ ");\n";
			methodString += "\t(("
				+ introspector.getEntityBeanName()
				+ ")entity)."
				+ introspector.getPostCreateMethodName(method.getName())
				+ "("
				+ introspector.getParametersInForMethod(method)
				+ ");\n";
			methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
			methodString += "\ttry{\n\t\treturn this.findByPrimaryKey(pk);\n\t}\n";
			methodString += "\tcatch(javax.ejb.FinderException fe){\n\t\tthrow new com.idega.data.IDOCreateException(fe);\n\t}\n";
			methodString += "\tcatch(Exception e){\n\t\tthrow new com.idega.data.IDOCreateException(e);\n\t}\n";
			methodString += "}\n";
			returningMethods[i] = methodString;
		}
		String codeString = " public " + introspector.getShortName() + " create() throws javax.ejb.CreateException";
		codeString += "{\n";
		codeString += "  return (" + introspector.getShortName() + ") super.createIDO();";
		codeString += "\n }\n\n";
		returningMethods[i] = codeString;
		i++;
		if (legacyIDO) {
			codeString = " public " + introspector.getShortName() + " createLegacy()";
			codeString += "{\n";
			codeString += "\ttry{\n";
			codeString += "\t\treturn create();\n";
			codeString += "\t}\n";
			codeString += "\tcatch(javax.ejb.CreateException ce){\n";
			codeString += "\t\tthrow new RuntimeException(\"CreateException:\"+ce.getMessage());\n";
			codeString += "\t}\n";
			codeString += "\n }\n\n";
			returningMethods[i] = codeString;
			i++;
		}
		return returningMethods;
	}
	public String[] getFinderMethodImplementations(ClassIntrospector introspector) {
		String[] finderMethodStrings = introspector.getFinderMethods();
		Method[] methods = introspector.getFinderMethodsArray();
		int length = methods.length;
		if (legacyIDO) {
			length += 3;
		} else {
			length += 1;
		}
		String[] returningMethods = new String[length];
		int i = 0;
		for (i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodString = null;
			if (method.getReturnType().equals(java.util.Collection.class)) {
				methodString = finderMethodStrings[i] + "{\n";
				methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
				methodString += "\tjava.util.Collection ids = (("
					+ introspector.getEntityBeanName()
					+ ")entity)."
					+ method.getName()
					+ "("
					+ introspector.getParametersInForMethod(method)
					+ ");\n";
				methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
				methodString += "\treturn this.getEntityCollectionForPrimaryKeys(ids);\n";
				methodString += "}\n";
			} else if (method.getReturnType().equals(java.util.Set.class)) {
				methodString = finderMethodStrings[i] + "{\n";
				methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
				methodString += "\tjava.util.Set ids = (("
					+ introspector.getEntityBeanName()
					+ ")entity)."
					+ method.getName()
					+ "("
					+ introspector.getParametersInForMethod(method)
					+ ");\n";
				methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
				methodString += "\treturn this.getEntitySetForPrimaryKeys(ids);\n";
				methodString += "}\n";
			} else {
				methodString = finderMethodStrings[i] + "{\n";
				methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
				methodString += "\tObject pk = (("
					+ introspector.getEntityBeanName()
					+ ")entity)."
					+ method.getName()
					+ "("
					+ introspector.getParametersInForMethod(method)
					+ ");\n";
				methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
				methodString += "\treturn this.findByPrimaryKey(pk);\n";
				methodString += "}\n";
			}
			returningMethods[i] = methodString;
		}
		String codeString = " public " + introspector.getShortName() + " findByPrimaryKey(Object pk) throws javax.ejb.FinderException";
		codeString += "{\n";
		codeString += "  return (" + introspector.getShortName() + ") super.findByPrimaryKeyIDO(pk);";
		codeString += "\n }\n\n";
		returningMethods[i] = codeString;
		i++;
		if (legacyIDO) {
			codeString = " public " + introspector.getShortName() + " findByPrimaryKey(int id) throws javax.ejb.FinderException";
			codeString += "{\n";
			codeString += "  return (" + introspector.getShortName() + ") super.findByPrimaryKeyIDO(id);";
			codeString += "\n }\n\n";
			returningMethods[i] = codeString;
			i++;
			codeString = " public " + introspector.getShortName() + " findByPrimaryKeyLegacy(int id) throws java.sql.SQLException";
			codeString += "{\n";
			codeString += "\ttry{\n";
			codeString += "\t\treturn findByPrimaryKey(id);\n";
			codeString += "\t}\n";
			codeString += "\tcatch(javax.ejb.FinderException fe){\n";
			codeString += "\t\tthrow new java.sql.SQLException(fe.getMessage());\n";
			codeString += "\t}\n";
			codeString += "\n }\n\n";
			returningMethods[i] = codeString;
			i++;
		}
		return returningMethods;
	}
	public String[] getHomeMethodImplementations(ClassIntrospector introspector) {
		String[] finderMethodStrings = introspector.getHomeMethods();
		Method[] methods = introspector.getHomeMethodsArray();
		int length = methods.length;
		String[] returningMethods = new String[length];
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String returnType = introspector.getClassParameterToString(method.getReturnType());
			String methodString = finderMethodStrings[i] + "{\n";
			methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
			methodString += "\t"
				+ returnType
				+ " theReturn = (("
				+ introspector.getEntityBeanName()
				+ ")entity)."
				+ method.getName()
				+ "("
				+ introspector.getParametersInForMethod(method)
				+ ");\n";
			methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
			methodString += "\treturn theReturn;\n";
			methodString += "}\n";
			returningMethods[i] = methodString;
		}
		return returningMethods;
	}
	
	

	protected void createDeploymentDescriptor(EJBWizardClassCreator classCreator) {
		ClassIntrospector introspector = classCreator.getClassIntrospector();
		String beanName = introspector.getEntityBeanName();
		String beanNameWithPackage = introspector.getPackage() + "." + beanName;
		String ejbName = beanNameWithPackage;		
		String fileName = beanName + "-ejb-jar.xml";
		File file = new File(getWorkingDirectory(), fileName);
		OutputStream stream = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
				
				stream = new FileOutputStream(file);
				//XMLParser parser = new XMLParser();
				//XMLDocument doc = parser.parse(reader);
				XMLElement ejbJarElement = new XMLElement("ejb-jar");
				XMLElement enterpriseBeansElement = new XMLElement("enterprise-beans");
				ejbJarElement.addContent(enterpriseBeansElement);
				
				XMLElement entityElement = getBeanspecificElement(classCreator);
				enterpriseBeansElement.addContent(entityElement);
	
				XMLElement assemblyDescriptorElement = new XMLElement("assembly-descriptor");
				ejbJarElement.addContent(assemblyDescriptorElement);
				XMLElement containerTransactionElement = new XMLElement("container-transaction");
				
				assemblyDescriptorElement.addContent(containerTransactionElement);
				XMLElement methodElement = new XMLElement("method");
				containerTransactionElement.addContent(methodElement);
				XMLElement ejbNameElement2 = new XMLElement("ejb-name");
				String ejbBeanName = ejbName;
				ejbNameElement2.addContent(ejbBeanName);
				methodElement.addContent(ejbNameElement2);
				XMLElement methodNameElement = new XMLElement("method-name");
				String methodsValue = "*";
				methodNameElement.addContent(methodsValue);
				methodElement.addContent(methodNameElement);
				XMLElement transAttributeElement = new XMLElement("trans-attribute");
				String transAttribute = "NotSupported";
				transAttributeElement.addContent(transAttribute);
				containerTransactionElement.addContent(transAttributeElement);
				
				XMLDocument doc = saveRootElementToFile(ejbJarElement,fileName);
			}	
			
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		
	}
	
	
	protected XMLElement getBeanspecificElement(EJBWizardClassCreator classCreator){
			ClassIntrospector introspector = classCreator.getClassIntrospector();
			String beanName = introspector.getEntityBeanName();
			String beanNameWithPackage = introspector.getPackage() + "." + beanName;
			String ejbName = beanNameWithPackage;
			String homeNameWithPackage = introspector.getPackage() + "." + classCreator.getHomeName();
			String interfaceNameWithPackage = introspector.getPackage() + "." + introspector.getShortName();
		
			XMLElement entityElement = new XMLElement("entity");
			
			addBeanGeneralElements(entityElement,"",ejbName,beanNameWithPackage,homeNameWithPackage,interfaceNameWithPackage);
	
	/*
			XMLElement localHomeElement = new XMLElement("local-home");
			localHomeElement.addContent(homeNameWithPackage);
			entityElement.addContent(localHomeElement);
	
			XMLElement localElement = new XMLElement("local");
			localElement.addContent(interfaceNameWithPackage);
			entityElement.addContent(localElement);
*/
			XMLElement primKeyClassElement = new XMLElement("prim-key-class");
			String primKeyClass = getBeanPrimaryKeyClass(classCreator).getName();
			primKeyClassElement.addContent(primKeyClass);
			entityElement.addContent(primKeyClassElement);
			XMLElement persistanceTypeElement = new XMLElement("persistence-type");
			String persistanceType = "Bean";
			persistanceTypeElement.addContent(persistanceType);
			entityElement.addContent(persistanceTypeElement);
			/*
			XMLElement transactionTypeElement = new XMLElement("transaction-type");
			String transactionType = "Container";
			transactionTypeElement.addContent(transactionType);
			entityElement.addContent(transactionTypeElement);
			*/
			XMLElement reentrantElement = new XMLElement("reentrant");
			boolean isReentrant = false;
			reentrantElement.addContent(String.valueOf(isReentrant));
			entityElement.addContent(reentrantElement);
			return entityElement;
	}
	
	private Class getBeanPrimaryKeyClass(EJBWizardClassCreator classCreator){
			try{
				/**
				 *@todo: Revise implementation
				 **/
				com.idega.data.EntityControl.setAutoCreationOfEntities(false);
				Object instance = classCreator.getClassIntrospector().sourceClass.newInstance();
				return ((com.idega.data.GenericEntity)instance).getPrimaryKeyClass();
			}
			catch(Exception e){
				System.err.println("Error instanciating Data class. Message: "+e.getMessage());
				e.printStackTrace();
			}
			return Integer.class;
	}
	
	
	protected void addBeanGeneralElements(XMLElement beanElement,String description, String ejbName,String ejbClassName,String remoteHomeClass,String remoteClass){
			
			XMLElement descriptionElement = new XMLElement("description");
			descriptionElement.addContent("");
			beanElement.addContent(descriptionElement);
			XMLElement ejbNameElement = new XMLElement("ejb-name");
			ejbNameElement.addContent(ejbName);
			beanElement.addContent(ejbNameElement);
			XMLElement ejbClassElement = new XMLElement("ejb-class");
			ejbClassElement.addContent(ejbClassName);
			beanElement.addContent(ejbClassElement);
			if(remoteHomeClass!=null){
				XMLElement homeElement = new XMLElement("home");
				homeElement.addContent(remoteHomeClass);
				beanElement.addContent(homeElement);
			}			
			if(remoteClass!=null){
				XMLElement remoteElement = new XMLElement("remote");
				remoteElement.addContent(remoteClass);
				beanElement.addContent(remoteElement);
			}
	}
	
	

	protected void mergeAllDeploymentDescriptors() throws IOException {
		final String ejbjarxml = "-ejb-jar.xml";
		XMLParser parser = new XMLParser();
		XMLDocument doc = null;
		//XMLDocument mainDocument = null;
		XMLElement mainEjbJarElement = new XMLElement("ejb-jar");
		XMLElement mainAssemblyDescriptorElement = new XMLElement("assembly-descriptor");
		XMLElement mainEnterpriseBeansElement = new XMLElement("enterprise-beans");
		mainEjbJarElement.addContent(mainEnterpriseBeansElement);
		mainEjbJarElement.addContent(mainAssemblyDescriptorElement);
		//mainDocument.setRootElement(mainEjbJarElement);

		//System.out.println("here0");
		File[] files = getWorkingDirectory().listFiles();
		for (int i = 0; i < files.length; i++) {
			try {
				
				//System.out.println("here1");
				File file = files[i];
				if(file.getName().endsWith(ejbjarxml)){
					doc = parser.parse(file);
					XMLElement ejbJarElement = doc.getRootElement();
					XMLElement enterpriseBeansElement = ejbJarElement.getChild("enterprise-beans");
					List entities = enterpriseBeansElement.getChildren();
					if (entities != null) {
						Iterator iter = entities.iterator();
						while(iter.hasNext()){
							//System.out.println("here2");
							XMLElement elem = (XMLElement)iter.next();
							mainEnterpriseBeansElement.addContent((XMLElement)elem.clone());	
						}
					}
					XMLElement assemblyDescriptorElement = ejbJarElement.getChild("assembly-descriptor");
					entities = assemblyDescriptorElement.getChildren();
					if (entities != null) {
						Iterator iter = entities.iterator();
						while(iter.hasNext()){
							XMLElement elem = (XMLElement)iter.next();
							mainAssemblyDescriptorElement.addContent((XMLElement)elem.clone());	
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String fileName = "ejb-jar.xml";
		saveRootElementToFile(mainEjbJarElement,fileName);
	}
	
	protected XMLDocument saveRootElementToFile(XMLElement rootElement,String fileName)throws IOException{
			XMLDocument doc = new XMLDocument(rootElement);
			//_rootElement = doc.getRootElement();
			//_xmlDocument.setRootElement(_rootElement);
			//this.setXMLDocument(doc);
			XMLOutput output = new XMLOutput("  ", true);
			output.setLineSeparator(System.getProperty("line.separator"));
			output.setTextNormalize(true);
			File file = new File(getWorkingDirectory(),fileName);
			FileOutputStream stream = new FileOutputStream(file);
			output.output(doc, stream);
			stream.close();
			return doc;
	}
	
	
	
	

}
