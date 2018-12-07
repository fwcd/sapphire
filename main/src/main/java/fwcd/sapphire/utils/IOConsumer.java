package fwcd.sapphire.utils;

import java.io.IOException;

@FunctionalInterface
public interface IOConsumer<I> {
	void accept(I input) throws IOException;
}
