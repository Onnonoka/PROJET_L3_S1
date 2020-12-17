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
    MODEL.service = new WebSocket("ws://localhost:1963/FranckBarbier/WebSockets_illustration");
    MODEL.init()

};
