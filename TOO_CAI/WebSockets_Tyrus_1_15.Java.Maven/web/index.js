/*
    ************************************************************
    |  Script ecrit par Dymko frédéric, Cazaux Axel            |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

'use strict';

window.onload = () => {

    // Tested with Tyrus 1.15 WebSockets Java library
    let service = new WebSocket("ws://localhost:1963/FranckBarbier/WebSockets_illustration");
    console.log(service);
    service.onerror = () => {
        // TODO Traitement des erreurs
        window.alert("service.onerror...");
    };
    service.onmessage = (event) => {
        // TODO Traitement du message reçu
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
        // TODO Demande des suffixes url (com, fr, ...) au serveur webSocket
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
        // TODO Fermeture de la connexion
        console.log("service.onclose... " + event.code);
        window.alert("Bye! See you later...");
// '1011': the server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.
    };

    service.getDNSInformation = () => {
        // TODO Séparer la recupération de la valuer de l'input et la fonction d'envois de la requette

        // get the document input
        let input = document.getElementById('inputText');
        let inputValue = input.value;

        if(inputValue) {
            if (inputValue.match(/[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi)) {
                service.send(JSON.stringify({
                    type: "Request",
                    value: inputValue
                }));
                console.log("Request Send to the WebSocketServer");
            } else {
                window.confirm("It's not a url");
            }
        }
    };


    // Event listener for valid the input text field
    document.getElementById("button").addEventListener("click", service.getDNSInformation);
    document.getElementById("inputText").addEventListener("keypress", (event) => {
        console.log(event);
        if (event.code === "Enter")
            service.getDNSInformation();
    });

    // TODO Fonction d'affichage des informations

};
