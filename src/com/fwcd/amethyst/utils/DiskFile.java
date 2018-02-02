package com.fwcd.amethyst.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import com.fwcd.amethyst.exception.FileException;

/**
 * Represents a file on your disk.
 * 
 * @author Fredrik
 * 
 */
public class DiskFile extends TemplateFile {
	private final File file;
	
	public DiskFile(URI uri) {
		this(new File(uri));
	}
	
	public DiskFile(String path) {
		this(new File(path));
	}
	
	public DiskFile(File file) {
		this.file = file;
	}
	
	@Override
	protected InputStream openStream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new FileException(e);
		}
	}
}
