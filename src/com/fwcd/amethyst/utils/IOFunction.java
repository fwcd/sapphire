package com.fwcd.amethyst.utils;

import java.io.IOException;

@FunctionalInterface
public interface IOFunction<I, O> {
	O apply(I input) throws IOException;
}
