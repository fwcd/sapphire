package fwcd.sapphire.utils;

import java.io.IOException;

@FunctionalInterface
public interface IOFunction<I, O> {
	O apply(I input) throws IOException;
}
