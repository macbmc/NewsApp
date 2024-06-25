package com.loc.newsapp.domain.useCases

import com.loc.newsapp.data.entity.OnBoardPage
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.utils.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkOnBoardPageUseCase(private val networkRepository: NetworkRepository) {

    suspend fun execute():Flow<List<OnBoardPage>?>
    {
        return flow {
            when(val executeResponse = networkRepository.getOnBoardData())
            {
                is Either.Success->
                {
                    emit(executeResponse.data)
                }
                is Either.Failed ->
                {
                    emit(null)
                }
            }
        }
    }

}