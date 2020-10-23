/*
    ************************************************************
    |  Serveur WebSocket ecrit par Dymko frédéric, Cazaux Axel |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

package com.franckbarbier.websockets.tyrus.java;

import org.json.JSONObject;

import javax.websocket.OnMessage;
import javax.websocket.Session;

//https://tyrus-project.github.io/documentation/1.12/user-guide.html#getting-started
public class WebSockets_illustration {

    /**
     * Danger : il faut que le constructeur de 'My_ServerEndpoint' soit bien
     * accessible par le serveur WebSockets. Ne pas oublier 'static'!
     */
    @javax.websocket.server.ServerEndpoint(value = "/WebSockets_illustration")
    public static class My_ServerEndpoint {

        @javax.websocket.OnClose
        public void onClose(javax.websocket.Session session, javax.websocket.CloseReason close_reason) {
            // TODO Faire une fermeture propre de la connexion
            System.out.println("onClose: " + close_reason.getReasonPhrase());
        }

        @javax.websocket.OnError
        public void onError(javax.websocket.Session session, Throwable throwable) {
            // TODO Gestion des erreurs
            System.out.println("onError: " + throwable.getMessage());
        }

        @OnMessage
        public void onMessage(Session session, String message) throws java.io.IOException {

            // Version nul
            /*
            JsonReader jsonReader = Json.createReader(new StringReader(message));
            JsonObject JSONRequestMessage = jsonReader.readObject();
            jsonReader.close();
            */

            JSONObject JSONRequestMessage = new JSONObject(message);
            String JSONReplyMessage = "";

            // Fonction de test a suppr une fois la partie fini
            System.out.println("From JSONObject : " + JSONRequestMessage.get("type"));
            System.out.println("Message from JavaScript: " + message);

            switch (JSONRequestMessage.getString("type")) {
                case "Handshake":

                    // TODO Envois des suffixe url (con, fr, ...) au client
                    String RequestMessageData = JSONRequestMessage.getString("data");
                    JSONRequestMessage.put("data", RequestMessageData.substring(0, RequestMessageData.length() - 1));
                    JSONReplyMessage = JSONRequestMessage.toString();

                    break;
                case "Request":
                    new JNDI_DNS(JSONRequestMessage.getString("data"));

                    // TODO Recupère les info du jndi et envois au client les infos demander (A, AAAA, NS, ..., ALL)

                    break;
            }
            session.getBasicRemote().sendText(JSONReplyMessage);
        }

        @javax.websocket.OnOpen
        public void onOpen(javax.websocket.Session session, javax.websocket.EndpointConfig ec) throws java.io.IOException {
            System.out.println("OnOpen... " + ec.getUserProperties().get("Author"));
            session.getBasicRemote().sendText("{ \"type\": \"Handshake\", \"data\": \"check\"}");

            // TODO Recupération de tout les suffixe et envois au client
        }

        // TODO Ajout des methodes d'envois d'information/reponce
        
    }

    public static void main(String[] args) {

        java.util.Map<String, Object> user_properties = new java.util.HashMap();
        user_properties.put("Author", "Dymko Frédéric, Cazaux Axel");

        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("localhost", 1963, "/FranckBarbier", user_properties /* or 'null' */, My_ServerEndpoint.class);
        try {
            server.start();
            // The Web page is launched from Java:
            java.awt.Desktop.getDesktop().browse(java.nio.file.FileSystems.getDefault().getPath("web" + java.io.File.separatorChar + "index.html").toUri());

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            System.out.println("Please press a key to stop the server...");
            reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
