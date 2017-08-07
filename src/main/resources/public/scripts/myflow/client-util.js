if (!String.prototype.startsWith) {
    String.prototype.startsWith = function (searchString, position) {
        position = position || 0;
        return this.substr(position, searchString.length) === searchString;
    };
}

if (!String.prototype.endsWith) {
    String.prototype.endsWith = function (searchString, position) {
        var subjectString = this.toString();
        if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
            position = subjectString.length;
        }
        position -= searchString.length;
        var lastIndex = subjectString.lastIndexOf(searchString, position);
        return lastIndex !== -1 && lastIndex === position;
    };
}

function copyByJson(obj) {
    return JSON.parse(JSON.stringify(obj));
}

Number.isInteger = Number.isInteger || function (value) {
    return typeof value === "number" &&
            isFinite(value) &&
            Math.floor(value) === value;
};

function byid(id) {
    return document.getElementById(id);
}

function element(tagName) {
    return document.createElement(tagName);
}

function selectAll(selector, element) {
    var elements = (element || document).querySelectorAll(selector);
    return toArray(elements);
}

function selectOne(selector) {
    return document.querySelector(selector);
}

function getOr(object, attr, value) {
    if (object && attr in object) {
        return object[attr];
    } else {
        return value;
    }
}

function Options(element) {
    this.options = toArray((element || document).querySelectorAll('option'));
    Options.prototype.getIndexByText = function (text) {
        var result = -1;
        this.options.forEach(function (option, index) {
            if (option.innerText == text) {
                result = index;
            }
        });
        return result;
    };
}

function toArray(nodeList) {
    var result = [];
        for (var i = 0; i < nodeList.length; i++) {
            result.push(nodeList[i]);
        }
    return result;
}

function collectNthChild(elements, selector, nth) {
    var result = [];
    if (nth == 0) {
        toArray(elements).forEach(function (element) {
            result.push(element.querySelector(selector));
        });
    } else {
        toArray(elements).forEach(function (element) {
            result.push(element.querySelectorAll(selector)[nth]);
        });
    }
    return result;
}

function ElementReader(readerCreator) {
    var self = this;
    self.readerCreator = readerCreator;
    self.functionObject = {};

    ElementReader.prototype.create = function (exchange) {
        var reader = new ElementReader(self.readerCreator); //違和感
        reader.functionObject = self.readerCreator(exchange);
        reader.exchange = exchange;
        return reader;
    };
    ElementReader.prototype.read = function (key) {
        return self.functionObject[key]();
    };
    ElementReader.prototype.updateExchange = function (keys) {
        if (!keys) {
            keys = Object.keys(self.functionObject);
        } else if (!Array.isArray(keys)) {
            keys = [keys];
        }
        keys.forEach(function (key) {
            self.exchange.setHeader(key, self.read(key));
        });
    };
}

function writeNestedProperty(toWriteValue, keys, value) {
    if (keys.length === 1) {
        toWriteValue[keys[0]] = value;
        return toWriteValue;
    } else {
        var key = keys.shift();
        if (key in toWriteValue) {
            var nextValue = toWriteValue[key];
        } else {
            var nextValue = {};
        }
        toWriteValue[key] = writeNestedProperty(nextValue, keys, value);
        return toWriteValue;
    }
}