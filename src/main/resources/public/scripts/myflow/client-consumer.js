function Consumer(processor, consumer) {
    Producer.call(this, processor, consumer);
}

Consumer.prototype = Object.create(Producer.prototype, {
    value: {
        constructor: Consumer
    }
});

Consumer.prototype.consume = function (exchange) {
    return this.produce(exchange);
};

function Endpoints() {
    if (Endpoints.dict === undefined) {
        Endpoints.dict = {};
    }
    Endpoints.prototype.putEndpoint = function (endpointName, producer) {
        Endpoints.dict[endpointName] = producer;
    };
    Endpoints.prototype.getEndpoint = function (endpointName) {
        return Endpoints.dict[endpointName];
    };
    Endpoints.prototype.sendTo = function (endpointName, exchange) {
        exchange = exchange || new Exchange();
        return this.getEndpoint(endpointName).produce(exchange);
    };
}

function EventGroup() {
    if (EventGroup.dict === undefined) {
        EventGroup.dict = {};
    }
    EventGroup.prototype.putGroupEvent = function (groupName, eventName, func) {
        var group = EventGroup.dict[groupName] || {};
        group[eventName] = func;
        EventGroup.dict[groupName] = group;
    };
    EventGroup.prototype.getGroupEvents = function (groupName) {
        return EventGroup.dict[groupName];
    };
    EventGroup.prototype.setEvent = function (element, groupName) {
        var dict = this.getGroupEvents(groupName) || {};
        var keys = Object.keys(dict);
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            element[key] = dict[key];
        }
    };
    EventGroup.prototype.setEvents = function (element) {
        var keySelector = Object.keys(EventGroup.dict);
        keySelector.forEach(function (selector) {
            var group = EventGroup.dict[selector];
            toArray((element.content || element).querySelectorAll(selector)).forEach(function (el) {
                Object.keys(group).forEach(function (eventName) {
                    el[eventName] = group[eventName];
                });
            });
        });
    };
}

function To(processor) {
    Consumer.call(this, processor, this);
}

To.prototype = Object.create(Consumer.prototype, {
    value: {
        constructor: To
    }
});

function RouteId(routeId) {
    Consumer.call(this, null, this);
    new Endpoints().putEndpoint(routeId, this);
}

RouteId.prototype = Object.create(Consumer.prototype, {
    value: {
        constructor: RouteId
    }
});

function Listener(eventName, elements, attributes) {
    Consumer.call(this, null, this);
    console.assert(typeof eventName === 'string', '1st argument must be string. (Listener)');
    var consumer = this;
    var groupName = false;
    if (typeof elements === 'string') {
        groupName = elements;
        elements = selectAll(elements);
    }
    if (!elements.forEach) {
        elements = [elements];
    }
    var func;
    if (attributes && attributes.length > 0) {
        func = function (event) {
            //event.preventDefault();
            var header = {};
            attributes.forEach(function (key) {
                header[key] = event[key];
            });
            consumer.consume(new Exchange({}, header, event.target));
            //return false;
        };
    } else {
        func = function (event) {
            //event.preventDefault();
            consumer.consume(new Exchange({}, {}, event.target));
            //return false;
        };
    }
    elements.forEach(function (element) {
        element[eventName] = func;
    });
    if (groupName) {
        new EventGroup().putGroupEvent(groupName, eventName, func);
    }
}

Listener.prototype = Object.create(Consumer.prototype, {
    value: {
        constructor: Listener
    }
});
