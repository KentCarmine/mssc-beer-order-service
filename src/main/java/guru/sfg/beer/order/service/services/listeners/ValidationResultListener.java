package guru.sfg.beer.order.service.services.listeners;

import guru.sfg.beer.order.service.config.JMSConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JMSConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listen(@Payload ValidateOrderResult validateOrderResult) {
        final UUID beerOrderId = validateOrderResult.getOrderId();

        log.error("### Validation result for Order Id: " + beerOrderId + " is : " + validateOrderResult.getIsValid());

        beerOrderManager.processValidationResult(beerOrderId, validateOrderResult.getIsValid());
    }

}
