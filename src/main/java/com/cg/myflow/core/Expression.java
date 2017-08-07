package com.cg.myflow.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Expression {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static Object evaluate(Object expression, Exchange exchange) {
        if (expression instanceof Function) {
            return ((Function<Exchange, ?>) expression).apply(exchange);
        } else {
            return expression;
        }
    }

    public static <T> T evaluate(Object expression, Exchange exchange, Class<T> clazz) {
        return clazz.cast(evaluate(expression, exchange));
    }

    public static Function<Exchange, Object> header(String key) {
        return (exchange) -> {
            return exchange.getHeader(key);
        };
    }

    public static <T> Function<Exchange, T> header(String key, Class<T> clazz) {
        return (exchange) -> {
            return exchange.getHeader(key, clazz);
        };
    }

    public static Function<Exchange, Void> header(String key, Object value) {
        return (exchange) -> {
            exchange.setHeader(key, evaluate(value, exchange));
            return null;
        };
    }

    public static Function<Exchange, Object> header(Object... expressions) {
        switch (expressions.length) {
            case 1:
                return (exchange) -> {
                    return exchange.getHeader(evaluate(expressions[0], exchange).toString());
                };
            case 2:
                return (exchange) -> {
                    String key = evaluate(expressions[0], exchange).toString();
                    Object value = evaluate(expressions[1], exchange);
                    exchange.setHeader(key, value);
                    return null;
                };
            default:
                return (exchange) -> {
                    return null;
                };
        }
    }

    public static Function<Exchange, Object> body() {
        return (exchange) -> {
            return exchange.getBody();
        };
    }

    public static Function<Exchange, Void> body(Object expression) {
        return (exchange) -> {
            exchange.setBody(evaluate(expression, exchange));
            return null;
        };
    }

    public static Function<Exchange, Long> now() {
        return (exchange) -> {
            return System.currentTimeMillis();
        };
    }

    public static Function<Exchange, Object> map(Object... expressions) {
        switch (expressions.length) {
            case 0:
                return (exchange) -> {
                    return new LinkedHashMap<>();
                };
            case 1:
                return (exchange) -> {
                    return (Map) evaluate(expressions[0], exchange);
                };
            case 2:
                return (exchange) -> {
                    Map map = (Map) evaluate(expressions[0], exchange);
                    if (map == null) {
                        return null;
                    } else {
                        return map.get(evaluate(expressions[1], exchange));
                    }
                };
            case 3:
                return (exchange) -> {
                    Map map = (Map) evaluate(expressions[0], exchange);
                    if (map != null) {
                        map.put(evaluate(expressions[1], exchange),
                                evaluate(expressions[2], exchange));
                    }
                    return null;
                };
            default:
                return (exchange) -> {
                    return null;
                };
        }
    }

    public static <T> Function<Exchange, List<T>> list(Object expression, Class clazz) {
        return (exchange) -> {
            return (List<T>) evaluate(expression, exchange);
        };
    }

    public static Function<Exchange, Object> list(Object... expressions) {
        switch (expressions.length) {
            case 0:
                return (exchange) -> {
                    return new ArrayList<>();
                };
            case 1:
                return (exchange) -> {
                    return (List) evaluate(expressions[0], exchange);
                };
            case 2:
                return (exchange) -> {
                    return (List) evaluate(expressions[0], exchange);
                };
            default:
                return (exchange) -> {
                    return null;
                };
        }
    }

    public static Function<Exchange, Void> page(String page) {
        return (exchange) -> {
            exchange.setPage(page);
            return null;
        };
    }

    public static Function<Exchange, Boolean> and(Object... expressions) {
        return (exchange) -> {
            Boolean result = true;
            for (Object expression : expressions) {
                Object value = evaluate(expression, exchange);
                result = MyFlowUtil.trueOrNotNull(value);
                if (!result) {
                    break;
                }
            }
            return result;
        };
    }

    public static Function<Exchange, Object> claims(Object... expressions) {
        switch (expressions.length) {
            case 1:
                return (exchange) -> {
                    return exchange.getClaims().get(evaluate(expressions[0], exchange).toString());
                };
            default:
                return (exchange) -> {
                    return exchange.getClaims();
                };
        }
    }

    public static Function<Exchange, Boolean> not(Object expression) {
        return (exchange) -> {
            return !MyFlowUtil.trueOrNotNull(evaluate(expression, exchange));
        };
    }

    public static Function<Exchange, Object> model(Object... expressions) {
        return (exchange) -> {
            exchange.getModel().put(evaluate(expressions[0], exchange).toString(), evaluate(expressions[1], exchange));
            return null;
        };
    }

    public static Function<Exchange, Void> produceEnd() {
        return (exchange) -> {
            exchange.setProduceEnd(true);
            return null;
        };
    }

    public static Function<Exchange, Object> orElse(Object... expressions) {
        return (exchange) -> {
            for (Object expression : expressions) {
                Object evaluate = evaluate(expression, exchange);
                if (evaluate != null) {
                    return evaluate;
                }
            }
            return null;
        };
    }

    public static Function<Exchange, Object> today() {
        return (exchange) -> {
            System.out.println(sdf.format(new Date()));
            return sdf.format(new Date());
        };
    }

    public static Function<Exchange, Object> rawData(Object expression) {
        return (exchange) -> {
            return exchange.getRawData(evaluate(expression, exchange, Class.class));
        };
    }
}
