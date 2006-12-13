package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Configuration;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.ssl.SSLFTPClient;

public class MasterCardSendFile implements CreditCardSendFile {

	public boolean sendFile(Configuration configuration, Batch batch) {

		SSLFTPClient ftp = null;

		try {
			// create client
			ftp = new SSLFTPClient();
			ftp.setConfigFlags(SSLFTPClient.ConfigFlags.DISABLE_SSL_CLOSURE);

			// set remote host
			ftp.setRemoteHost(configuration.getSendFTPServer());

			// set implicit mode
			ftp.setImplicitFTPS(true);

			// turn off server validation
			ftp.setValidateServer(false);

			// connect to the server
			ftp.connect();

			// some servers supporting implicit SSL require
			// this to be called. You may need to comment these
			// lines out
			try {
				ftp.auth(SSLFTPClient.PROT_PRIVATE);
			} catch (FTPException ex) {
				ex.printStackTrace();
			}

			// log in
			ftp.login(configuration.getSendFTPUser(), configuration.getSendFTPPassword());

			ftp.setConnectMode(FTPConnectMode.ACTIVE);
			ftp.setType(FTPTransferType.ASCII);

			ftp.put(batch.getCreditCardFile().getFileValue(), batch.getCreditCardFileName());

			// Shut down client
			ftp.quit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}