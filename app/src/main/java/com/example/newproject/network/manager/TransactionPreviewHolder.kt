package com.example.newproject.network.manager

import com.example.newproject.network.model.response.OrderHistoryResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionPreviewHolder @Inject constructor() {
    private var order: OrderHistoryResponse? = null

    fun set(order: OrderHistoryResponse) {
        this.order = order
    }

    fun get(): OrderHistoryResponse? = order

    fun clear() {
        order = null
    }
}
