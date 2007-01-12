package com.idega.block.email.client.business;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.ParseException;

/**
 * Used to store attachment information.
 */
public class AttachmentInfo {
    private Part part;
    private int num;


    /**
     * Returns the attachment's content type.
     */
    public String getAttachmentType() throws MessagingException {
        String contentType;
        if ((contentType = this.part.getContentType()) == null) {
			return "invalid part";
		}
		else {
			return contentType;
		}
    }

    /**
     * Returns the attachment's content (if it is plain text).
     */
    public String getContent() throws java.io.IOException, MessagingException {
        if (hasMimeType("text/plain")) {
			return (String)this.part.getContent();
		}
		else {
			return "";
		}
    }

    /**
     * Returns the attachment's description.
     */
    public String getDescription() throws MessagingException {
        String description;
        if ((description = this.part.getDescription()) != null) {
			return description;
		}
		else {
			return "";
		}
    }

    /**
     * Returns the attachment's filename.
     */
    public String getFilename() throws MessagingException {
        String filename;
        if ((filename = this.part.getFileName()) != null) {
			return filename;
		}
		else {
			return "";
		}
    }

    /**
     * Returns the attachment number.
     */
    public String getNum() {
        return (Integer.toString(this.num));
    }

    /**
     * Method for checking if the attachment has a description.
     */
    public boolean hasDescription() throws MessagingException {
        return (this.part.getDescription() != null);
    }

    /**
     * Method for checking if the attachment has a filename.
     */
    public boolean hasFilename() throws MessagingException {
        return (this.part.getFileName() != null);
    }

    /**
     * Method for checking if the attachment has the desired mime type.
     */
    public boolean hasMimeType(String mimeType) throws MessagingException {
        return this.part.isMimeType(mimeType);
    }

    /**
     * Method for checking the content disposition.
     */
    public boolean isInline() throws MessagingException {
        if (this.part.getDisposition() != null) {
			return this.part.getDisposition().equals(Part.INLINE);
		}
		else {
			return true;
		}
    }

    /**
     * Method for mapping a message part to this AttachmentInfo class.
     */
    public void setPart(int num, Part part)
        throws MessagingException, ParseException {

        this.part = part;
        this.num = num;
    }
}

