/*
 * Created on Mar 13, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.campus.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CreateBIPhoneFile {

	public static void main(String[] args) throws Exception {
		if (args.length != 2 && args.length != 3) {
			System.err.println("Syntax is CreateBIPhoneFile <inputFile> <outputFile> [giro_number]");
			
			return;
		}
		
		String giro_number = "100";
		if (args.length == 3)
			giro_number = args[2];
		
		FileReader reader = new FileReader(args[0]);
		BufferedReader bReader = new BufferedReader(reader);
		
		FileWriter writer = new FileWriter(args[1]);
		BufferedWriter bWriter = new BufferedWriter(writer);
		
		if (bReader.ready()) {
			String line = bReader.readLine();
			while (line != null) {				
				StringTokenizer tok = new StringTokenizer(line,";");
				
				String id = (String)tok.nextElement();
				String ssn = (String)tok.nextElement();
				String name = (String)tok.nextElement();
				String address = (String)tok.nextElement();
				String po = (String)tok.nextElement();
				String city = (String)tok.nextElement();
				String aprt_no = (String)tok.nextElement();
				String aprt_type = (String)tok.nextElement();
				String rent = (String)tok.nextElement();
				String house_fee = (String)tok.nextElement();
				String energy = (String)tok.nextElement();
				String used = (String)tok.nextElement();
				String cleaning = (String)tok.nextElement();
				String other = (String)tok.nextElement();
				String phone = (String)tok.nextElement();
				String internet = (String)tok.nextElement();
				String start_period = (String)tok.nextElement();
				String end_period = (String)tok.nextElement();
				String payment_due = (String)tok.nextElement();
				String payment_last_due = (String)tok.nextElement();
				
				//Add 700000 to all id's bacause of some silly shit down at BI.
				int iID = Integer.parseInt(id);
				iID += 700000;
				id = Integer.toString(iID);
				bWriter.write(id);
				bWriter.write('\t');
				
				bWriter.write(name);
				bWriter.write('\t');
				
				ssn = ssn.substring(0,6) + "-" + ssn.substring(6);
				bWriter.write(ssn);
				bWriter.write('\t');
				
				bWriter.write(address);
				bWriter.write('\t');

				bWriter.write(po);
				bWriter.write('\t');

				bWriter.write(city);
				bWriter.write('\t');

				bWriter.write(aprt_no);
				bWriter.write('\t');

				bWriter.write(aprt_type);
				bWriter.write('\t');

//				bWriter.write(rent);
				bWriter.write('\t');

//				bWriter.write(house_fee);
				bWriter.write('\t');

//				bWriter.write(energy);
				bWriter.write('\t');

//				bWriter.write(used);
				bWriter.write('\t');

//				bWriter.write(cleaning);
				bWriter.write('\t');

				float fOther = Float.parseFloat(other);
				int iOther = Math.round(fOther);
				bWriter.write(Integer.toString(iOther));
				bWriter.write('\t');

				float fPhone = Float.parseFloat(phone);
				int iPhone = Math.round(fPhone);

				bWriter.write(Integer.toString(iPhone));
				bWriter.write('\t');

//				bWriter.write(internet);
				bWriter.write('\t');

				bWriter.write(start_period);
//				bWriter.write("010303");
				bWriter.write('\t');

				bWriter.write(end_period);
//				bWriter.write("310303");
				bWriter.write('\t');

				bWriter.write(payment_due);
				bWriter.write('\t');

				bWriter.write(payment_last_due);
				bWriter.write('\t');

				bWriter.write(giro_number);
				bWriter.write(13);	
				bWriter.write(10);	
				
				line = bReader.readLine();						
			}
			 
			bReader.close();
			bWriter.close();
		}
	}
}