package com.personal.homestayfinder.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.personal.homestayfinder.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun  providesGoogleSignInOptions(@ApplicationContext context: Context) : GoogleSignInOptions{
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.my_web_client_id))
            .requestEmail().build()
    }

    @Provides
    @Singleton
    fun providesGoogleSignInClient(
       @ApplicationContext context: Context,
        gso: GoogleSignInOptions
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(context, gso)
    }
}