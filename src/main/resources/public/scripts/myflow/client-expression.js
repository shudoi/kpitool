function ev(expression, exchange) {
    if (typeof expression === 'function') {
        return expression(exchange);
    } else {
        return expression;
    }
}
function add() {
    var len = arguments.length;
    var args = arguments;
    return function (exchange) {
        if (len < 1) {
            return null;
        } else {
            var result = ev(args[0], exchange);
            for (var i = 1; i < len; i++) {
                result += ev(args[i], exchange);
            }
            return result;
        }
    };
}

function header() {
    var len = arguments.length;
    var args = arguments;
    if (len === 0) {
        return null;
    } else if (len === 1) {
        return function (exchange) {
            return exchange.getHeader(ev(args[0], exchange));
        };
    } else {
        return function (exchange) {
            var headerName = ev(args[0], exchange);
            return headerName in exchange.getHeaders() ? exchange.getHeader(headerName) : ev(args[1], exchange);
        };
    }
}

function body() {
    return function (exchange) {
        return exchange.getBody();
    };
}

function get() {
    var len = arguments.length;
    var args = arguments;
    if (len === 0) {
        return false;
    } else if (len === 1) {
        return function (exchange) {
            return ev(args[0], exchange);
        };
    } else if (len === 2) {
        return function (exchange) {
            return ev(args[0], exchange)[ev(args[1], exchange)];
        };
    } else if (len === 3) {
        return function (exchange) {
            return ev(args[0], exchange)[ev(args[1], exchange)] === ev(args[2], exchange);
        };
    }
}

function _in() {
    var len = arguments.length;
    var args = arguments;
    if (len === 2) {
        return function (exchange) {
            var value = ev(args[0], exchange);
            var array = ev(args[1], exchange);
            for (var i = 0; i < array.length; i++) {
                if (array[i] === value) {
                    return true;
                }
            }
            return false;
        };
    }
}

function element() {
    var len = arguments.length;
    var args = arguments;
    if (len === 0) {
        return function (exchange) {
            return exchange.getElement();
        };
    } else if (len === 1) {
        return function (exchange) {
            exchange.setElement(ev(args[0], exchange));
            return exchange;
        };
    }
}

function attr() {
    var len = arguments.length;
    var args = arguments;
    if (len === 1) {
        // attr(attrName) => return exchange-element attribute value
        return function (exchange) {
            return exchange.getElement().getAttribute(ev(args[0], exchange));
        };
    } else if (len === 2) {
        // attr(element, attrName) => return args-element attribute value
        // or
        // attr(attrName, attrValue) => return result to set exchange-element attr value  
        return function (exchange) {
            var arg0 = ev(args[0], exchange);
            if (arg0.getAttribute) {
                return arg0.getAttribute(ev(args[1], exchange));
            } else {
                return exchange.getElement().setAttribute(arg0, ev(args[1], exchange));
            }
        };
    } else if (len === 3) {
        // attr(element, attrName, attrValue) => return result to set args-element attr value  
        return function (exchange) {
            return ev(args[0], exchange).setAttribute(ev(args[1], exchange), ev(args));
        };
    }
}

function removeAttr() {
    var len = arguments.length;
    var args = arguments;
    if (len === 1) {
        return function (exchange) {
            return exchange.getElement().removeAttribute(ev(args[0], exchange));
        };
    } else if (len === 2) {
        return function (exchange) {
            return ev(args[0], exchange).removeAttribute(ev(args[1], exchange));
        };
    }
}

function text() {
    var len = arguments.length;
    var args = arguments;
    if (len === 0) {
        // text() => return exchange-element text
        return function (exchange) {
            return exchange.getElement().innerText;
        };
    } else if (len === 1) {
        // text(element) => return args-element text
        // or
        // text(str) => return str
        return function (exchange) {
            var arg0 = ev(args[0], exchange);
            if ('innerText' in arg0) {
                return arg0.innerText;
            } else {
                return arg0;
            }
        };
    } else if (len === 2) {
        // text(element, str) => return result to set element text
        return function (exchange) {
            return ev(args[0], exchange).innerText = ev(args[1], exchange);
        };
    }
}

function parent() {
    var len = arguments.length;
    var args = arguments;
    if (len === 1) {
        return function (exchange) {
            return ev(args[0], exchange).parentNode;
        };
    } else if (len === 2) {
        return function (exchange) {
            var times = ev(args[1], exchange);
            var result = ev(args[0], exchange);
            for (var i = 0; i < times; i++) {
                result = result.parentNode;
            }
            return result;
        };
    }
}

function _alert() {
    var args = arguments;
    return function (exchange) {
        var message = ev(args[0], exchange);
        return window.alert(message);
    };
}

function _confirm() {
    var args = arguments;
    return function (exchange) {
        var message = ev(args[0], exchange);
        return window.confirm(message);
    };
}

function query() {
    var len = arguments.length;
    var args = arguments;
    if (len === 1) {
        return function (exchange) {
            var queryString = ev(args[0], exchange);
            var result = toArray(document.querySelectorAll(queryString));
            if (result.length === 1) {
                return result[0];
            } else {
                return result;
            }
        };
    } else if (len === 2) {
        return function (exchange) {
            var element = ev(args[0], exchange);
            var queryString = ev(args[1], exchange);
            var result = toArray(element.querySelectorAll(queryString));
            if (result.length === 1) {
                return result[0];
            } else {
                return result;
            }
        };
    }
}

function findOne() {
    var len = arguments.length;
    var args = arguments;
    if (len === 2) {
        return function (exchange) {
            var trs = ev(args[0], exchange);
            if (!Array.isArray(trs)) {
                trs = [trs];
            }
            var result = null;
            trs.forEach(function (element) {
                if (result) {
                    return;
                } else if (ev(args[1], new Exchange(exchange.getBody(), exchange.getHeaders(), element))) {
                    result = element;
                }
            });
            return result;
        };
    }
}

function eq() {
    var len = arguments.length;
    var args = arguments;
    if (len === 2) {
        return function (exchange) {
            return ev(args[0], exchange).toString() === ev(args[1], exchange).toString();
        };
    }
}

function notEq() {
    var len = arguments.length;
    var args = arguments;
    if (len === 2) {
        return function (exchange) {
            return ev(args[0], exchange).toString() !== ev(args[1], exchange).toString();
        };
    }
}

function seqNo() {
    var len = arguments.length;
    var args = arguments;
    if (len === 1) {
        return function (exchange) {
            var elements = ev(args[0], exchange);
            if (!Array.isArray(elements)) {
                elements = [elements];
            }
            var i = 1;
            elements.forEach(function (element) {
                element.innerText = i++;
            });
        };
    } else if (len === 2) {
        return function (exchange) {
            var elements = ev(args[0], exchange);
            if (!Array.isArray(elements)) {
                elements = [elements];
            }
            var i = ev(args[1], exchange);
            elements.forEach(function (element) {
                element.innerText = i++;
            });
        };
    }
}

function all() {
    var len = arguments.length;
    var args = arguments;
    if (len > 1) {
        return function (exchange) {
            var trs = ev(args[0], exchange);
            if (!Array.isArray(trs)) {
                trs = [trs];
            }
            var result = true;
            trs.forEach(function (element) {
                if (!result) {
                    return;
                } else if (ev(args[1], new Exchange(exchange.getBody(), exchange.getHeaders(), element))) {
                    return;
                } else {
                    result = false;
                    if (len > 2) {
                        ev(args[2], exchange);
                    }
                }
            });
            return result;
        };
    }
}

function or() {
    var len = arguments.length;
    var args = arguments;
    if (len > 0) {
        return function (exchange) {
            for (var i = 0; i < len; i++) {
                if (ev(args[i], exchange)) {
                    return true;
                }
            }
            return false;
        };
    }
}

function redirect(path) {
    return function () {
        document.location.href = path;
    };
}