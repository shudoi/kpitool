package com.cg.myflow;

import java.util.function.Function;

public abstract class Consumer extends Producer {

    public Consumer(Function<Exchange, Exchange> processor) {
        super(processor, null);
        this.consumer = this;
    }

    public Exchange consume(Exchange exchange) {
        return this.produce(exchange);
    }
}
