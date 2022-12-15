package com.example.kelilink.di

import com.example.kelilink.core.domain.use_case.auth.AuthInteractor
import com.example.kelilink.core.domain.use_case.auth.AuthUseCase
import com.example.kelilink.core.domain.use_case.order.OrderInteractor
import com.example.kelilink.core.domain.use_case.order.OrderUseCase
import com.example.kelilink.core.domain.use_case.queue.QueueInteractor
import com.example.kelilink.core.domain.use_case.queue.QueueUseCase
import com.example.kelilink.core.domain.use_case.recommendation.RecommendationInteractor
import com.example.kelilink.core.domain.use_case.recommendation.RecommendationUseCase
import com.example.kelilink.core.domain.use_case.user.UserInteractor
import com.example.kelilink.core.domain.use_case.user.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideOrderUseCase(orderInteractor: OrderInteractor): OrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideQueueUseCase(queueInteractor: QueueInteractor): QueueUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideRecommendationUseCase(
        recommendationInteractor: RecommendationInteractor
    ): RecommendationUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase
}
