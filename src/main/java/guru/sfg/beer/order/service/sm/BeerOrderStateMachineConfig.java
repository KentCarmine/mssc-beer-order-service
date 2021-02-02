package guru.sfg.beer.order.service.sm;


import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class BeerOrderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {
    
    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> states) throws Exception {
        states.withStates()
                .initial(BeerOrderStatusEnum.NEW) // Set state machine starting state
                .states(EnumSet.allOf(BeerOrderStatusEnum.class))  // set list of valid states for this state machine
                .end(BeerOrderStatusEnum.PICKED_UP) // set as terminal state
                .end(BeerOrderStatusEnum.DELIVERED) // set as terminal state
                .end(BeerOrderStatusEnum.DELIVERY_EXCEPTION) // set as terminal state
                .end(BeerOrderStatusEnum.VALIDATION_EXCEPTION) // set as terminal state
                .end(BeerOrderStatusEnum.ALLOCATION_EXCEPTION); // set as terminal state
    }

}
