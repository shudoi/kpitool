new Listener("onclick", byid("logout-link"))
        .to(function (exchange) {
            docCookies.removeItem("jwt");
            return exchange;
        })
        .document(redirect("/login"));
