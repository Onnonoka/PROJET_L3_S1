'use strict';

window.onload = () => {
    // Tested with Tyrus 1.15 WebSockets Java library
    let service = new WebSocket("ws://localhost:1963/FranckBarbier/WebSockets_illustration");
    console.log(service);
    service.onerror = () => {
        window.alert("service.onerror...");
    };
    service.onmessage = (event) => {
        console.log("Message from Java: ", event.data);

        // Ne pas enlever les commentaires tant que le handshake n'est pas fait sur le serveur

        let reply = JSON.parse(event.data);
        switch (reply.type) {
            case "Handshake" :
                if (reply.data !== "check")
                    service.onerror(undefined);
                break;
            case "reply" :
                console.log(reply.data);
                break;
            default :
                console.log("Unknown reply type");
        }
    };
    service.onopen = () => {
        console.log("service.onopen...");
        let response = window.confirm(service.url + " just opened... Say 'Hi!'?");
        if (response) {
            service.send(JSON.stringify({
                type: "Handshake",
                data: "check?"
            }));
        }
    };
    service.onclose = (event/*:CloseEvent*/) => {
        console.log("service.onclose... " + event.code);
        window.alert("Bye! See you later...");
// '1011': the server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.
    };

    webSocketService.service = service;
};
