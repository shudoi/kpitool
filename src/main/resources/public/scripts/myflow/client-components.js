function direct(routeId) {
    return function (exchange) {
        exchange = exchange || new Exchange();
        exchange = new Endpoints().sendTo(routeId, exchange);
        return exchange;
    };
}

function formToExchange(formElement) {
    return function (exchange) {
        exchange = exchange || new Exchange();
        toArray(formElement.querySelectorAll('[data-header], [data-header-to]'))
                .forEach(function (element) {
                    var key = element.getAttribute('data-header') || element.getAttribute('data-header-to');
                    exchange.setHeader(key, element.value);
                });
        return exchange;
    };
}

function exchangeToElement(formElement) {
    return function (exchange) {
        var targetElement = formElement ? ev(formElement, exchange) : exchange.getElement();
        toArray(targetElement.querySelectorAll('[data-header], [data-header-from]'))
                .forEach(function (element) {
                    var key = element.getAttribute('data-header') || element.getAttribute('data-header-from');
                    var value = exchange.getHeader(key, '');
                    if (element.tagName.toLowerCase() == 'select') {
                        if (isNaN(Number(value))) {
                            toArray(element.querySelectorAll('option')).forEach(function (option, index) {
                                if (option.value === value) {
                                    element.selectedIndex = index;
                                }
                            });
                        } else {
                            element.selectedIndex = value || 0;
                        }
                    } else if ('value' in element) {
                        element.value = value;
                    } else {
                        element.innerText = value;
                    }
                });
        return exchange;
    };
}

function log(params) {
    params = params || {};
    var logName = 'log';
    if (params['name']) {
        logName += ':' + params['name'];
    }
    var showBody = params['body'] || true;
    var showHeader = params['header'] || true;
    return function (exchange) {
        if (showBody) {
            console.log(logName, 'body', exchange.getBody());
        }
        if (showHeader) {
            console.log(logName, 'header', exchange.getHeaders());
        }
        return exchange;
    };
}

function request(uri, headers) {
    return function (exchange, func) {
        if (headers) {
            exchange.setHeaders(headers);
        }
        var xhr = new XMLHttpRequest();
        var url = uri;
        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                exchange.update(JSON.parse(xhr.responseText));
                func();
            }
        };
        xhr.send(exchange.toJson());
    };
}

function server(path) {
    return function (exchange, func) {
        console.log('request for ' + exchange.getHeader('__context_name') + ' ...');
        var xhr = new XMLHttpRequest();
        xhr.open("POST", path || env.context.path, true);
        if (docCookies.hasItem("jwt")) {
            exchange.setHeader("jwt", docCookies.getItem("jwt"));
        }
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                exchange.update(JSON.parse(xhr.responseText));
                func();
            }
        };
        xhr.send(exchange.toJson());
    };
}

function buttonEnable(elements, bool) {
    var disable = bool === false;
    if (typeof elements === 'string') {
        return function (exchange) {
            selectAll(elements).forEach(function (element) {
                element.disabled = disable;
            });
            return exchange;
        };
    } else if (!elements.forEach) {
        elements = [elements];
    }
    return function (exchange) {
        elements.forEach(function (element) {
            element.disabled = disable;
        });
        return exchange;
    };
}

function pageReload() {
    return function (exchange) {
        document.location.reload();
    };
}

function pageTop() {
    return function (exchange) {
        window.scrollTo(0, 0);
        return exchange;
    };
}

function delay(t) {
    if (Number.isInteger(t)) {
        t = 0;
    }
    t *= 1000;
    return function (exchange, func) {
        window.setTimeout(func, t);
    };
}

function cookie(action, key, value) {
    if (action === 'get') {
        return function (exchange) {
            var v = new CookieObject().get(key, value);
            exchange.setHeader(key, v);
            return exchange;
        };
    } else if (action === 'set') {
        return function (exchange) {
            var v = exchange.getHeader(key, value);
            new CookieObject().set(key, v);
            return exchange;
        };

    } else {
        return function (exchange) {
            return exchange;
        };
    }
}

function modal(modalElement, action) {
    if (action === 'open') {
        return function (exchange) {
            modalElement.style.visibility = 'visible';
            return exchange;
        };
    } else if (action === 'close') {
        return function (exchange) {
            toArray(modalElement.querySelectorAll('[data-header], [data-header-to]'))
                    .forEach(function (element) {
                        var key = element.getAttribute('data-header') || element.getAttribute('data-header-to');
                        var tagName = element.tagName.toLocaleLowerCase();
                        if (tagName === 'input' || tagName === 'textarea' || tagName === 'select') {
                            exchange.setHeader(key, element.value);
                        } else {
                            exchange.setHeader(key, element.textContent);
                        }
                    });
            if (modalElement.style.visibility === 'visible') { // 操作でキャンセルされてたら続きをやらない
                modalElement.style.visibility = 'hidden';
                return exchange;
            }
        };
    } else if (action === 'all-close') {
        return function (exchange) {
            toArray(document.querySelectorAll('.modal')).forEach(function (element) {
                element.style.visibility = 'hidden';
            });
            return exchange;
        };
    }
}

function loadElement(element, loadFields) {
    var isTargetField;
    if (Array.isArray(loadFields) && loadFields.length > 0) {
        var obj = {};
        loadFields.forEach(function (field) {
            obj[field] = true;
        });
        isTargetField = function (key) {
            return obj[key];
        };
    } else {
        isTargetField = function (key) {
            return true;
        };
    }

    return function (exchange) {
        var targetElement = ev(element, exchange) || exchange.getElement();
        toArray(targetElement.querySelectorAll('[data-header], [data-header-to]')).forEach(function (node) {
            var tagName = node.tagName.toLocaleLowerCase();
            var key = node.getAttribute('data-header') || node.getAttribute('data-header-to');
            if (isTargetField(key)) {
                if (tagName === 'input' || tagName === 'textarea' || tagName === 'select') {
                    exchange.setHeader(key, node.value);
                } else {
                    exchange.setHeader(key, node.textContent);
                }
            }
        });
        return exchange;
    };
}

function dataTransfer() {
    return function (exchange) {
        element = exchange.getElement();
        while (!element.ondrop) {
            element = element.parentNode;
        }
        exchange = Exchange.fromJson(exchange.getHeader('dataTransfer').getData('application/json'), element);
        return exchange;
    };
}

function updateHeader(obj) {
    return function (exchange) {
        Object.keys(obj).forEach(function (key) {
            exchange.setHeader(key, ev(obj[key], exchange));
        });
        return exchange;
    };
}

function updateElement(expression) {
    return function (exchange) {
        exchange.setElement(ev(expression, exchange));
        return exchange;
    };
}

function setText(elements, text) {
    if (!Array.isArray(elements)) {
        elements = [elements];
    }
    return function (exchange) {
        elements.forEach(function (element) {
            element.innerText = text;
        });
        return exchange;
    };
}

function elementsToExchange(elementsQuery) { //buggy...
    return function (exchange) {
        var copiedHeaders = copyByJson(exchange.getHeaders());
        var data = selectAll(elementsQuery)
                .reduce(function (prev, current) {
                    var ex = loadElement(current)(new Exchange());
                    prev.push(ex.getHeaders());
                    return prev;
                }, []);
        copiedHeaders['data'] = copyByJson(data);
        exchange.setHeaders(copiedHeaders);
        return exchange;
    };
}

function dataHeaderToTable(headerName, tableQuery, trTemplate) {
    if (!tableQuery.endsWith(" tbody")) {
        tableQuery += " tbody";
    }
    var ete = exchangeToElement();
    return function (exchange) {
        var tbody = document.querySelector(tableQuery);
        while (tbody.firstChild) {
            tbody.removeChild(tbody.firstChild);
        }
        var tableData = exchange.getHeader(headerName);
        tableData.forEach(function (data) {
            var child = document.createElement("tr");
            child.innerHTML = trTemplate;
            ete(new Exchange(null, data, child));
            tbody.appendChild(child);
        });
        return exchange;
    };
}
