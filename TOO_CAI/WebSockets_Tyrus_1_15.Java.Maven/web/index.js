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
            case "Reply" :
                console.log(reply);
                break;
            default :
                console.log("Unknown reply type");
        }
    };
    service.onopen = () => {
        console.log("service.onopen...");
        let response = window.confirm(service.url + " just opened... Say 'Hi!'?");
        // TODO Ajouter la verification de la valeur de l'input (C'est c'est bien une url et que le suffixe est correct

        // Event listener for the input text field
        document.getElementById("button").addEventListener("click", service.getDNSInformation);
        document.getElementById("inputText").addEventListener("keypress", (event) => {
            console.log(event);
            if (event.code === "Enter")
                service.getDNSInformation();
        });
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

        // Verification of the url
        if(inputValue) {
            if (inputValue.match(/[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi)) {

                // Sending of the request
                service.send(JSON.stringify({
                    type: "Request",
                    data: {
                        url: inputValue,
                        dns: "dns://8.8.8.8"
                    }
                }));
                console.log("Request Send to the WebSocketServer");
            } else {
                window.confirm("It's not a url");
            }
        }
    };

    // TODO Fonction d'affichage des informations

};
