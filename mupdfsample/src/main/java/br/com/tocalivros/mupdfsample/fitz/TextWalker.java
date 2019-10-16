package br.com.tocalivros.mupdfsample.fitz;

public interface TextWalker
{
	public void showGlyph(Font font, Matrix trm, int glyph, int unicode, boolean wmode);
}
