package itis.ru.justtalk.di.module

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import itis.ru.justtalk.R

@Module
class AuthModule {
    @Provides
    fun provideSignInOptions(context: Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.resources.getString(R.string.google_api_token))
            .requestEmail()
            .build()
    }

    @Provides
    fun provideGoogleSignInClient(
        activity: AppCompatActivity,
        options: GoogleSignInOptions
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(activity, options)
    }
}
