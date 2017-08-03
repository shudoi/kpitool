package com.cg.myflow.core;

import static com.cg.myflow.core.Expression.*;

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
		if (this.processor != null && !exchange.isProduceEnd()) {
			exchange = this.processor.apply(exchange);
		}
		if (this.nextProducer != null && !exchange.isProduceEnd()) {
			exchange = this.nextProducer.produce(exchange);
		}
		return exchange;
	}

	public Producer to(Function<Exchange, Exchange> processor) {
		this.nextProducer = new Producer(processor, this.consumer);
		return this.nextProducer;
	}

	public Producer update(Function<Exchange, ?>... func) {
		this.nextProducer = new Producer(createUpdateProcessor(func), this.consumer);
		return consumer;

	}

	private <T> Function<Exchange, Exchange> createUpdateProcessor(Function<Exchange, T>... func) {

		return (exchange) -> {
			for (Function<Exchange, T> f : func) {
				f.apply(exchange);
			}
			return exchange;
		};
	}

	public Producer when(Object[]... predicates) {
		this.nextProducer = new Producer(createWhenProcessor(predicates), this.consumer);
		return this.nextProducer;

	}

	public Producer produceEnd() {
		this.nextProducer = new Producer((exchange) -> {
			exchange.setProduceEnd(true);
			return exchange;
		}, this.consumer);
		return this.nextProducer;
	}

	public Function<Exchange, Exchange> createWhenProcessor(Object[]... predicates) {

		return (exchange) -> {
			for (Object[] predicate : predicates) {
				Object result = evaluate(predicate[0], exchange);
				if (result instanceof Boolean) {
					if (!((Boolean) result)) {
						continue;
					}
				}
				if (predicate[1] instanceof Producer) {
					((Producer) predicate[1]).consumer.consume(exchange);
				}
				break;
			}
			return exchange;
		};
	}
}
