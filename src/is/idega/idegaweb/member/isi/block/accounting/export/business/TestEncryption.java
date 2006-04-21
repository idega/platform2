package is.idega.idegaweb.member.isi.block.accounting.export.business;

public class TestEncryption {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestEncryption test = new TestEncryption();
		
		String pwd = "áéýúíóþæðöÁÉÝÚÍÓÞÆÐÖ";
		System.out.println("plain text = " + pwd);
		String encrypted = test.encrypt(pwd);
		System.out.println("encrypted = " + encrypted);
		String decrypted = test.decrypt(encrypted);
		System.out.println("decrypted = " + decrypted);
		String decryptedFromDatabase = test.decrypt("e1e9fdfaedf3fee6f0f6c1c9dddacdd3dec6d0d6");
		System.out.println("decrypted from database= " + decryptedFromDatabase);		
	}

	public String decrypt(String str) {
		if (str != null) {
			char[] pass = new char[str.length() / 2];
			try {
				for (int i = 0; i < pass.length; i++) {
					pass[i] = (char) Integer.decode(
							"0x" + str.charAt(i * 2) + str.charAt((i * 2) + 1))
							.intValue();
				}
				return String.valueOf(pass);
			} catch (Exception ex) {
				ex.printStackTrace();
				return str;
			}
		}
		return str;
	}
	
	public String encrypt(String str) {
		 try {
			char[] pass = str.toCharArray();
			String encrypted = new String("");
			for (int i = 0; i < pass.length; i++) {
				String hex = Integer.toHexString((int) pass[i]);
				while (hex.length() < 2) {
					String s = "0";
					s += hex;
					hex = s;
				}
				encrypted += hex;
			}
			if (encrypted.equals("") && !str.equals("")) {
				encrypted = null;
			}
			
			return encrypted;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return str;
		}
	}
}
