package org.ebookdroid.droids;

import java.util.Map;

import org.ebookdroid.core.codec.CodecDocument;
import org.ebookdroid.droids.mupdf.codec.MuPdfDocument;
import org.ebookdroid.droids.mupdf.codec.PdfContext;

import br.com.tocalivros.mupdfsample.foobnix.android.utils.LOG;
import br.com.tocalivros.mupdfsample.foobnix.ext.CacheZipUtils;
import br.com.tocalivros.mupdfsample.foobnix.ext.FooterNote;
import br.com.tocalivros.mupdfsample.foobnix.ext.TxtExtract;

public class TxtContext extends PdfContext {

    @Override
    public CodecDocument openDocumentInner(String fileName, String password) {
        Map<String, String> notes = null;
        try {
            FooterNote extract = TxtExtract.extract(fileName, CacheZipUtils.CACHE_BOOK_DIR.getPath());
            fileName = extract.path;
            notes = extract.notes;
            LOG.d("new file name", fileName);
        } catch (Exception e) {
            LOG.e(e);
        }

        MuPdfDocument muPdfDocument = new MuPdfDocument(this, MuPdfDocument.FORMAT_PDF, fileName, password);
        muPdfDocument.setFootNotes(notes);
        return muPdfDocument;
    }
}
