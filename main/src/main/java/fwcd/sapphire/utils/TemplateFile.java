package fwcd.sapphire.utils;

import java.io.IOException;
import java.io.InputStream;

import fwcd.sapphire.exception.FileException;

public abstract class TemplateFile implements AnyFile {
	protected abstract InputStream openStream();
	
	@Override
	public <T> T mapStream(IOFunction<InputStream, T> mapper) {
		try (InputStream in = openStream()) {
			return mapper.apply(in);
		} catch (IOException e) {
			throw new FileException(e);
		}
	}
}
