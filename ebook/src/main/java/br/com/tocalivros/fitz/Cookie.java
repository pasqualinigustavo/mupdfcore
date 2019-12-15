package br.com.tocalivros.fitz;

public class Cookie
{
	static {
		Context.init();
	}

	private long pointer;

	protected native void finalize();

	public void destroy() {
		finalize();
		pointer = 0;
	}

	private native long newNative();

	public Cookie() {
		pointer = newNative();
	}

	public native void abort();
}