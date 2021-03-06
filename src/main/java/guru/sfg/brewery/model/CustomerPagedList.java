package guru.sfg.brewery.model;


import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class CustomerPagedList extends PageImpl<CustomerDto> {
    public CustomerPagedList(List<CustomerDto> customerDtos, Pageable pageable, long total) {
        super(customerDtos, pageable, total);
    }

    public CustomerPagedList(List<CustomerDto> customerDtos) {
        super(customerDtos);
    }
}
