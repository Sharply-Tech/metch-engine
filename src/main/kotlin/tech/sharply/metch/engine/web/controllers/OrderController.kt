package tech.sharply.metch.engine.web.controllers

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import tech.sharply.metch.orderbook.domain.model.types.OrderAction
import tech.sharply.metch.orderbook.domain.model.types.OrderType
import java.math.BigDecimal

@Controller
class OrderController {

    @MessageMapping("/orders/place")
    fun place(request: PlaceOrderRequest, auth: Authentication, @Header("simpSessionId") sessionId: String) {
        TODO("Implement")
    }

    @MessageMapping("/orders/{orderId}/update")
    fun update(request: UpdateOrderRequest, auth: Authentication, @Header("simpSessionId") sessionId: String,
        @DestinationVariable orderId: Long) {
        TODO("Implement")
    }

    @MessageMapping("/orders/{orderId}/cancel")
    fun cancel(auth: Authentication, @Header("simpSessionId") sessionId: String,
        @DestinationVariable orderId: Long) {
        TODO("Implement")
    }

}

data class PlaceOrderRequest(
    val instrumentId: Long,
    val type: OrderType,
    val action: OrderAction,
    val price: BigDecimal?,
    val size: BigDecimal,
)

data class UpdateOrderRequest(
    val price: BigDecimal,
    val size: BigDecimal
)
