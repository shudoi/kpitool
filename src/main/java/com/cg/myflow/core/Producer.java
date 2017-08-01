package com.cg.myflow.core;

import com.cg.myflow.core.Exchange;
import com.cg.myflow.core.Consumer;
import java.util.function.Function;

public class Producer {

    protected Function<Exchange, Exchange> processor;
    protected Producer nextProducer;
    protected Consumer consumer;

    public Producer(Function<Exchange, Exchange> processor, Consumer consumer) {
        this.processor = processor;
        this.consumer = consumer;
    }

    public Exchange produce(Exchange exchange) {
        if (this.processor != null) {
            exchange = this.processor.apply(exchange);
        }
        if (this.nextProducer != null) {
            exchange = this.nextProducer.produce(exchange);
        }
        return exchange;
    }

    public Producer to(Function<Exchange, Exchange> processor) {
        this.nextProducer = new Producer(processor, this.consumer);
        return this.nextProducer;
    }
}
