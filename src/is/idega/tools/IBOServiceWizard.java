package is.idega.tools;

import java.io.*;
import java.util.*;
import java.lang.reflect.Method;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class IBOServiceWizard extends EJBWizard {

	//protected String beanClassSuffix="Bean";

	public IBOServiceWizard(String className) {
		super(className);
		setRemoteInterfaceSuperInterface("com.idega.business.IBOService");
	}

	public IBOServiceWizard(Class entityClass) {
		super(entityClass);
		setLegacyIDO(true);
	}

	public String getBeanSuffix() {
		return "Bean";
	}

	protected static String getClassName(String[] args) {
		String className = args[0];
		try{
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
					}
					else if (token.equals("class")) {
						if (tok.hasMoreTokens())
							clss = tok.nextToken();
						break;
					}
					//System.out.println("line"+nr++);
				}

			}

			className = pack + "." + clss;

		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return className;
	}

	public static void main(String[] args) throws Exception {
		try {
			String firstArg=args[0];
			String className = getClassName(args);
			IBOServiceWizard instance = new IBOServiceWizard(className);
			if (firstArg.endsWith(".java") || firstArg.endsWith(".JAVA")) {
				File javaFile = new File(firstArg);
				instance.setWorkingDirectory(javaFile.getParentFile());
			}
			instance.doJavaFileCreate();
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("IBOWizard: You have to supply a valid ClassName as an argument");
		}
	}

	/**
	 * Overrided in sublcasses
	 * @param inst
	 */
	protected void setClassCreatorProperties(EJBWizardClassCreator inst) {
		inst.setFactorySuperClass("com.idega.business.IBOHomeImpl");
		inst.setHomeSuperInterface("com.idega.business.IBOHome");
	}

	public boolean finderMethodsAllowed() {
		return false;
	}

	public String[] getInternalMethodImplementations(ClassIntrospector introspector) {
		int length = 1;
		String[] returningMethods = new String[length];
		for (int i = 0; i < length; i++) {
			String methodString = " protected Class getBeanInterfaceClass()";
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
		length += 1;
		String[] returningMethods = new String[length];
		int i = 0;
		for (i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodString = finderMethodStrings[i] + "{\n";
			//methodString += "\tcom.idega.business.IBOService service = this.idoCheckOutPooledEntity();\n";
			methodString += "\tcom.idega.business.IBOService service = this.iboCheckOutPooledBean();\n";
			methodString += "\t((" + introspector.getEntityBeanName() + ")service)." + method.getName() + "(" + introspector.getParametersInForMethod(method) + ");\n";
			//methodString += "\t(("+introspector.getEntityBeanName()+")entity)."+introspector.getPostCreateMethodName(method.getName())+"("+introspector.getParametersInForMethod(method)+");\n";
			//methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
			methodString += "\treturn((" + introspector.getEntityBeanName() + ")service);\n";
			methodString += "\tcatch(Exception e){\n\t\tthrow new com.idega.data.IDOCreateException(e);\n\t}\n";
			methodString += "}\n";
			returningMethods[i] = methodString;
		}

		String codeString = " public " + introspector.getShortName() + " create() throws javax.ejb.CreateException";
		codeString += "{\n";
		codeString += "  return (" + introspector.getShortName() + ") super.createIBO();";
		codeString += "\n }\n\n";
		returningMethods[i] = codeString;
		i++;

		return returningMethods;
	}

}
