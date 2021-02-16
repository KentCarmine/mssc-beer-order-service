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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeerOrderAllocationListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(Message message) {
        AllocateOrderRequest request = (AllocateOrderRequest) message.getPayload();

        System.out.println("### in BeerOrderAllocationListener.listen. beerOrderDto ID: " + request.getBeerOrderDto().getId());

        for (BeerOrderLineDto bolDto : request.getBeerOrderDto().getBeerOrderLines()) {
            bolDto.setQuantityAllocated(bolDto.getOrderQuantity());
        }

        jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, AllocateOrderResult.builder()
                .allocationError(false)
                .pendingInventory(false)
                .beerOrderDto(request.getBeerOrderDto())
                .build());
    }
}
