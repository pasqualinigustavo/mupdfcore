package br.com.tocalivros.mupdfsample.foobnix.tts;

import android.app.Activity;
import android.os.Bundle;

import br.com.tocalivros.mupdfsample.foobnix.pdf.info.wrapper.AppState;

public class TTSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppState.get().load(this);
        TTSService.playLastBook();
        finish();

    }

}
