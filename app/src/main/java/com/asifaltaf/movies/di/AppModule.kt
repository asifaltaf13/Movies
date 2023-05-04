package com.asifaltaf.movies.di

import android.app.Application
import androidx.room.Room
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.asifaltaf.movies.data.MovieRepository
import com.asifaltaf.movies.data.data_source.MovieApi
import com.asifaltaf.movies.data.data_source.MovieDatabase
import com.asifaltaf.movies.domain.MovieRepositoryAbstract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        db: MovieDatabase,
        api: MovieApi
    ): MovieRepositoryAbstract {
        return MovieRepository(
            dao = db.movieDao,
            api = api
        )
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        app: Application
    ): ImageLoader {
        return ImageLoader.Builder(context = app)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder(context = app)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(app.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.025)
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()
    }
}