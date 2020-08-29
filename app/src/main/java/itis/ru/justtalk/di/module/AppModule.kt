package itis.ru.justtalk.di.module

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideLocationClient(context: Context) =
        LocationServices.getFusedLocationProviderClient(context)
}
