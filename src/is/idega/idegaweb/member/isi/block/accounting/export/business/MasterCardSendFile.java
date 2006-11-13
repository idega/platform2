package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Configuration;

import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.ssl.SSLFTPClient;

public class MasterCardSendFile implements CreditCardSendFile {

	public boolean sendFile(Configuration configuration, Batch batch) {
		// batch.getCreditCardFile().getFileValueForWrite()

		SSLFTPClient ftp = null;

		try {
			// create client
			//log.info("Creating FTPS (explicit) client");
			ftp = new SSLFTPClient(configuration.getSendFTPServer(),
					SSLFTPClient.ConfigFlags.DISABLE_SSL_CLOSURE);
			// NOTE: The DISABLE_SSL_CLOSURE flag is included in
			// this example
			// for the sake of compatibility with as wide a
			// range of servers as
			// possible. If possible it should be avoided as it
			// opens the
			// possibility of truncation attacks (i.e. attacks
			// where data is
			// compromised through premature disconnection).

			// turn off server validation
			//log.info("Turning off server validation");
			ftp.setValidateServer(false);

			// connect to the server
			//log.info("Connecting to server " + host);
			ftp.connect();

			// switch to SSL on control channel
			//log.info("Switching to FTPS (explicit mode)");
			ftp.auth(SSLFTPClient.AUTH_SSL);
			// ftp.auth(SSLFTPClient.AUTH_TLS_C);

			// log in
			//log.info("Logging in with username=" + username + " and password="
			//		+ password);
			ftp.login(configuration.getSendFTPUser(), configuration.getSendFTPPassword());
			ftp.ccc();

			// set up passive ASCII transfers
			//log.info("Setting up passive, ASCII transfers");
			ftp.setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.ACTIVE);
			// ftp.setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);
			ftp.setType(FTPTransferType.ASCII);
			ftp.prot(SSLFTPClient.PROT_PRIVATE);

			// copy file to server
			//log.info("Putting " + filename + " to server");
			ftp.put(batch.getCreditCardFile().getFileValue(), batch.getCreditCardFileName());

			// Shut down client
			//log.info("Quitting client");
			ftp.quit();

			//log.info("Example complete");
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}

		return false;
	}
}
