package com.kartikasw.kelilink.core.di

import com.kartikasw.kelilink.core.data.repository.*
import com.kartikasw.kelilink.core.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun provideOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    abstract fun provideQueueRepository(queueRepositoryImpl: QueueRepositoryImpl): QueueRepository

    @Binds
    abstract fun provideRecommendationRepository(
        recommendationRepositoryImpl: RecommendationRepositoryImpl
    ): RecommendationRepository

    @Binds
    abstract fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}