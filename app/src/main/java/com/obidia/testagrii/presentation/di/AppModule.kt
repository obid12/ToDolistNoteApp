package com.obidia.testagrii.presentation.di

import com.obidia.testagrii.domain.usecase.NoteUseCase
import com.obidia.testagrii.domain.usecase.NoteUseCaseImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
  @Binds
  @Singleton
  abstract fun provideUseCase(useCaseImplementation: NoteUseCaseImplementation): NoteUseCase
}