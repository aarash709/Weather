package com.experiment.weather.domain.di

//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Singleton
//    @Provides
//    fun provideUseCase(
//        repository: WeatherRepository,
//    ): WeatherUseCases {
//        return WeatherUseCases(
//            geoSearch = GeoSearch(repository),
//            getOneCall = GetOneCall(repository)
//        )
//    }
//
//}