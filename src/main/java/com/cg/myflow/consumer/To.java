package com.cg.myflow.consumer;

import com.cg.myflow.core.Consumer;
import com.cg.myflow.core.Exchange;
import java.util.function.Function;

public class To extends Consumer {

    public To(Function<Exchange, Exchange> processor) {
        super(processor);
    }
}
