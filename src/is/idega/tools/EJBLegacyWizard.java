package is.idega.tools;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class EJBLegacyWizard extends EJBWizard {

	public EJBLegacyWizard(String className) {
		super(className);
		setLegacyIDO(true);
		setRemoteInterfaceSuperInterface("com.idega.data.IDOLegacyEntity");
	}

	public EJBLegacyWizard(Class entityClass) {
		super(entityClass);
		setLegacyIDO(true);
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
				EJBLegacyWizard instance = new EJBLegacyWizard(className);
				instance.setWorkingDirectory(javaFile.getParentFile());
				instance.doJavaFileCreate();
			} else {
				System.out.println("className is " + className);
				String currentDir = System.getProperty("user.dir");
				File workingDir = new File(currentDir);
				EJBLegacyWizard instance = new EJBLegacyWizard(className);
				instance.setWorkingDirectory(workingDir);
				instance.doJavaFileCreate();
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			System.out.println("EJBLegacyWizard: You have to supply a valid ClassName as an argument");
		}
	}

	protected void setClassCreatorProperties(EJBWizardClassCreator inst) {
		super.setClassCreatorProperties(inst);
		inst.setToThrowRemoteExceptions(false);
		//inst.setRemoteInterfaceSuperInterface("com.idega.data.IDOLegacyEntity");
	}
}
