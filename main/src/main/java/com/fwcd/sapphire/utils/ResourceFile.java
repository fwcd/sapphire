package com.fwcd.sapphire.utils;

import java.io.InputStream;

/**
 * Represents a file that is packaged inside the JAR.
 * 
 * @author fwcd
 *
 */
public class ResourceFile extends TemplateFile {
	private final String fullResourceURL;
	
	public ResourceFile(String fullResourceURL) {
		this.fullResourceURL = fullResourceURL;
	}
	
	@Override
	protected InputStream openStream() {
		return ResourceFile.class.getResourceAsStream(fullResourceURL);
	}
}
