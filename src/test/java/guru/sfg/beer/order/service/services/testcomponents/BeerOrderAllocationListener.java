package guru.sfg.beer.order.service.services.testcomponents;

import guru.sfg.beer.order.service.config.JMSConfig;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.model.events.AllocateOrderRequest;
import guru.sfg.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeerOrderAllocationListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(Message message) {
        boolean hasValidationError = false;
        boolean hasPendingInventory = false;

        AllocateOrderRequest request = (AllocateOrderRequest) message.getPayload();

        System.out.println("### in BeerOrderAllocationListener.listen. beerOrderDto ID: " + request.getBeerOrderDto().getId());
        String customerRef = request.getBeerOrderDto().getCustomerRef();

        if (customerRef != null && customerRef.equals("allocation-exception")) {
            hasValidationError = true;
        }

        if (customerRef != null && customerRef.equals("partial-allocation")) {
            hasPendingInventory = true;
        }

        for (BeerOrderLineDto bolDto : request.getBeerOrderDto().getBeerOrderLines()) {
            if (hasPendingInventory) {
                bolDto.setQuantityAllocated(bolDto.getOrderQuantity() - 1);
            } else {
                bolDto.setQuantityAllocated(bolDto.getOrderQuantity());
            }
        }

        jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, AllocateOrderResult.builder()
                .allocationError(hasValidationError)
                .pendingInventory(hasPendingInventory)
                .beerOrderDto(request.getBeerOrderDto())
                .build());
    }
}
