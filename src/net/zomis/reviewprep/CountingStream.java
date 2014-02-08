package net.zomis.reviewprep;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An output stream that keeps track of how many bytes that has been written to it.
 */
public class CountingStream extends FilterOutputStream {
	private final AtomicInteger bytesWritten;
	
	public CountingStream(OutputStream out) {
		super(out);
		this.bytesWritten = new AtomicInteger();
	}
	
	@Override
	public void write(int b) throws IOException {
		bytesWritten.incrementAndGet();
		super.write(b);
	}
	public int getBytesWritten() {
		return bytesWritten.get();
	}
}
