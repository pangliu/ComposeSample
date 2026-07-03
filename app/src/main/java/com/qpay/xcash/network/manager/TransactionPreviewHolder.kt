package com.qpay.xcash.network.manager

import com.qpay.xcash.network.model.response.OrderHistoryResponse
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
