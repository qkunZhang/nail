package com.back.util.id;

import com.hy.corecode.idgen.WFGIdGenerator;
import org.springframework.stereotype.Component;

@Component
public class IdUtil {
    private final WFGIdGenerator IdGenerator;

    public IdUtil(WFGIdGenerator idGenerator) {
        IdGenerator = idGenerator;
    }

    public Long getId() {
        return IdGenerator.next();
    }
}
