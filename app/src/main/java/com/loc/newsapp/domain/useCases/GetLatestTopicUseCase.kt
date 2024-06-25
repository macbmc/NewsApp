package com.loc.newsapp.domain.useCases

import com.loc.newsapp.data.entity.LateTopics
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.utils.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLatestTopicUseCase(private val networkRepository: NetworkRepository) {

    fun execute(): Flow<List<LateTopics>?> {
        return flow {
            when (val response = networkRepository.getLatestTopics()) {
                is Either.Success -> {
                    emit(response.data)
                }
                else ->{}
            }
        }
    }
}