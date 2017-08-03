package com.cg.myflow.consumer;

import java.util.function.Function;

import com.cg.myflow.core.Consumer;
import com.cg.myflow.core.Exchange;

public class To extends Consumer {

	public To(Function<Exchange, Exchange> processor) {
		super(processor);
	}

	public To() {
		super(null);
	}
}
