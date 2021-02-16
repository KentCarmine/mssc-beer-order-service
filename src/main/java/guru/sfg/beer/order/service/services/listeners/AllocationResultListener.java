package guru.sfg.beer.order.service.services.listeners;

import guru.sfg.beer.order.service.config.JMSConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_RESPONSE_QUEUE)
    private void listen(@Payload AllocateOrderResult allocateOrderResult) {
        System.out.println("### AllocationResultListener.listen called");
        System.out.println(allocateOrderResult.toString());
        if (!allocateOrderResult.getAllocationError() && !allocateOrderResult.getPendingInventory()) {
            // allocation successful
            System.out.println("### Allocation successful");
            beerOrderManager.beerOrderAllocationPassed(allocateOrderResult.getBeerOrderDto());
        } else if (!allocateOrderResult.getAllocationError() && allocateOrderResult.getPendingInventory()) {
            // pending inventory
            System.out.println("### Allocation pending inventory");
            beerOrderManager.beerOrderAllocationPendingInventory(allocateOrderResult.getBeerOrderDto());
        } else if (allocateOrderResult.getAllocationError()) {
            // allocation error
            System.out.println("### Allocation error");
            beerOrderManager.beerOrderAllocationFailed(allocateOrderResult.getBeerOrderDto());
        }
    }
}
