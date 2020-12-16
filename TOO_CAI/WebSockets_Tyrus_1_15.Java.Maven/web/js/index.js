/*
    ************************************************************
    |  Script ecrit par Dymko Frédéric, Cazaux Axel            |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

'use strict';

window.onload = () => {

    // Tested with Tyrus 1.15 WebSockets Java library
    let service = new WebSocket("ws://localhost:1963/FranckBarbier/WebSockets_illustration");
    console.log(service);

    MODEL.service = service;
    MODEL.init()
    console.log("fait", MODEL.service);

    // TODO Fonction d'affichage des informations

};
