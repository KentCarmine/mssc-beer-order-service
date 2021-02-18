package guru.sfg.beer.order.service.services.testcomponents;

import guru.sfg.beer.order.service.config.JMSConfig;
import guru.sfg.brewery.model.events.ValidateOrderRequest;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.VALIDATE_ORDER_QUEUE)
    public void listen(Message msg) {
        boolean isValid = true;

        ValidateOrderRequest request = (ValidateOrderRequest)msg.getPayload();

//        System.out.println("################### I RAN ################");
//        System.out.println("In Test BeerOrderValidationListener. Order ID = " + request.getBeerOrderDto().getId());

        // Condition to fail validation
        if (request.getBeerOrderDto().getCustomerRef() != null && request.getBeerOrderDto().getCustomerRef().equals("fail-validation")) {
            isValid = false;
        }

        jmsTemplate.convertAndSend(JMSConfig.VALIDATE_ORDER_RESPONSE_QUEUE, ValidateOrderResult.builder()
                .isValid(isValid)
                .orderId(request.getBeerOrderDto().getId())
                .build());
    }

}
