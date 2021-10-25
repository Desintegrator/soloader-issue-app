package test.app;

import com.facebook.react.ReactActivity;
import com.yandex.metrica.YandexMetrica;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends ReactActivity {
    @Override
    protected String getMainComponentName() {
        return "TestApp";
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            YandexMetrica.reportAppOpen(this);
        }
    }

    @Override
    public void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        YandexMetrica.reportAppOpen(this);
    }
}
