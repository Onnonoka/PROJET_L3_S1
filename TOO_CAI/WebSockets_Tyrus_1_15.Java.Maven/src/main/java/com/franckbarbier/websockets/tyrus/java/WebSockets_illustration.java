/*
    ************************************************************
    |  Serveur WebSocket ecrit par Dymko frédéric, Cazaux Axel |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

package com.franckbarbier.websockets.tyrus.java;

import org.json.JSONObject;

import javax.naming.NamingException;
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

            JSONObject JSONMessage = new JSONObject(message);
            JSONObject JSONMessageData = JSONMessage.getJSONObject("data");
            JSONObject JSONReplyMessage = new JSONObject();

            // Fonction de test a suppr une fois la partie fini
            System.out.println("From JSONObject : " + JSONMessage.get("type"));
            System.out.println("Message from JavaScript: " + message);
            System.out.println("data : " +  JSONMessageData.toString());

            try {
                switch (JSONMessage.getString("type")) {
                    case "Handshake":

                        // TODO Envois des suffixe url (con, fr, ...) au client
                    /*String RequestMessageData = JSONMessage.getString("data");
                    JSONMessage.put("data", RequestMessageData.substring(0, RequestMessageData.length() - 1));
                    JSONReplyMessage = JSONMessage;*/

                        break;
                    case "Request":
                        JNDI_DNS domainInfo = new JNDI_DNS(JSONMessageData.getString("url"), JSONMessageData.getString("dns"));
                        if (!domainInfo.isEmpty()) {
                            JSONReplyMessage.put("data", domainInfo.toJSONObject());
                        } else {
                            JSONReplyMessage.put("data", new JSONObject().toString());
                        }
                        break;
                }
                JSONReplyMessage.put("succeed", true);
            } catch (NamingException err) {
                JSONObject JSONError = new JSONObject();
                JSONError.put("error", err.getMessage());
                JSONError.put("details", err.getExplanation());
                JSONReplyMessage.put("data", JSONError);
                JSONReplyMessage.put("succeed", false);
            } finally {
                JSONReplyMessage.put("type", "Reply");
                session.getBasicRemote().sendText(JSONReplyMessage.toString());
            }
        }

        @javax.websocket.OnOpen
        public void onOpen(javax.websocket.Session session, javax.websocket.EndpointConfig ec) throws java.io.IOException {
            System.out.println("OnOpen... " + ec.getUserProperties().get("Author"));
            session.getBasicRemote().sendText("{ \"type\": \"Handshake\", \"data\": \"check\"}");
            try {
                JNDI_DNS.getSuffix();
            } catch (NamingException e) {
                e.printStackTrace();
            }
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
