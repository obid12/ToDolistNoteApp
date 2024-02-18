package com.obidia.testagrii.data.di

import com.obidia.testagrii.data.repository.NoteRepositoryImplementation
import com.obidia.testagrii.domain.repo.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
  @Binds
  abstract fun provideRepository(
    repositoryImplementation: NoteRepositoryImplementation
  ): NoteRepository
}