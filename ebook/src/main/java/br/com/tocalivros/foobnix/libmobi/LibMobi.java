package br.com.tocalivros.foobnix.libmobi;

public class LibMobi {
    // static {
    // System.loadLibrary("mobi");
    // }

    public static native int convertToEpub(String input, String output);
}
