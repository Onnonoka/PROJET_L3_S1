/*
    ************************************************************
    |  Serveur WebSocket ecrit par Dymko frédéric, Cazaux Axel |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

package com.franckbarbier.websockets.tyrus.java;

// For json
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.NamingException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.util.HashMap;

// For GlassfishServer
// For read/write file



//https://tyrus-project.github.io/documentation/1.12/user-guide.html#getting-started
public class WebSockets_illustration {

    static HashMap<String, client> clientList = new HashMap<>(); // list of client connected

    /**
     * Danger : il faut que le constructeur de 'My_ServerEndpoint' soit bien
     * accessible par le serveur WebSockets. Ne pas oublier 'static'!
     */
    @javax.websocket.server.ServerEndpoint(value = "/WebSockets_illustration")
    public static class My_ServerEndpoint {

        @javax.websocket.OnClose
        public void onClose(javax.websocket.Session session, javax.websocket.CloseReason close_reason) {
            // TODO Faire une fermeture propre de la connexion
            System.out.println("onClose: " + close_reason.getReasonPhrase() + "\n id: " + session.getId());
        }

        @javax.websocket.OnError
        public void onError(javax.websocket.Session session, Throwable err) {
            // TODO Gestion des erreurs
            System.err.println("onError: " + err.getMessage());
            JSONObject JSONReplyMessage = new JSONObject();
            JSONObject JSONError = new JSONObject();
            JSONError.put("error", err.toString());
            JSONError.put("details", err.getMessage());
            JSONReplyMessage.put("data", JSONError);
            JSONReplyMessage.put("succeed", false);
            JSONReplyMessage.put("type", "Reply");
            /*
            // Plus compliqué que prévus!
            try {
                session.getBasicRemote().sendText(JSONReplyMessage.toString());
            } catch (IOException e) {

            }*/
            System.err.println(JSONReplyMessage.toString());
        }

        @OnMessage
        public void onMessage(Session session, String message) throws java.io.IOException, NamingException {

            JSONObject JSONMessage = new JSONObject(message);
            JSONObject JSONMessageData = JSONMessage.getJSONObject("data");
            JSONObject JSONReplyMessage = new JSONObject();

            // Fonction de test a suppr une fois la partie fini
            System.out.println("From JSONObject : " + JSONMessage.get("type"));
            System.out.println("Message from JavaScript: " + message);
            System.out.println("data : " +  JSONMessageData.toString());

            switch(JSONMessage.getString("type")) {
                // Creating a new User
                case "newAuth":
                    client newUser = new client(JSONMessageData.getString("username"), JSONMessageData.getString("password"));
                    if (newUser.create()) {
                        JSONReplyMessage.put("succeed", true);
                        newUser.save(new JSONObject().toString());
                    } else {
                        JSONReplyMessage.put("succeed", false);
                    }
                    JSONReplyMessage.put("type", "newAuth");
                    JSONReplyMessage.put("data", new JSONObject());
                    break;

                // Oppening a User
                case "auth":
                    client user = new client(JSONMessageData.getString("username"), JSONMessageData.getString("password"));
                    JSONReplyMessage.put("type", "auth");
                    if (user.exist()) {
                        JSONReplyMessage.put("succeed", true);
                        JSONReplyMessage.put("data", new JSONObject(user.get()));
                        WebSockets_illustration.clientList.put(session.getId(), user);
                    } else {
                        JSONReplyMessage.put("succeed", false);
                        JSONReplyMessage.put("data", new JSONObject());
                    }
                    break;

                // Reply a JNDI request
                case "request":
                    JNDI_DNS domainInfo = new JNDI_DNS(JSONMessageData.getString("url"), JSONMessageData.getString("dns"));
                    if (!domainInfo.isEmpty()) {
                        JSONReplyMessage.put("data", domainInfo.toJSONObject());
                    } else {
                        JSONReplyMessage.put("data", new JSONObject().toString());
                    }
                    JSONReplyMessage.put("url", JSONMessageData.getString("url"));
                    JSONReplyMessage.put("succeed", true);
                    JSONReplyMessage.put("type", "reply");
                    break;

                // Save the User save in his file
                case "save":
                    client userSave = WebSockets_illustration.clientList.get(session.getId());
                    if (userSave.save(JSONMessageData.toString())) {
                        JSONReplyMessage.put("succeed", true);
                        JSONReplyMessage.put("type", "save");
                        JSONReplyMessage.put("data", new JSONObject());
                    } else {
                        JSONReplyMessage.put("succeed", false);
                        JSONReplyMessage.put("type", "save");
                        JSONReplyMessage.put("data", new JSONObject());
                    }
                    break;
            }

            System.out.println("Message send : " + JSONReplyMessage.toString());
            session.getBasicRemote().sendText(JSONReplyMessage.toString());
        }

        @javax.websocket.OnOpen
        public void onOpen(javax.websocket.Session session, javax.websocket.EndpointConfig ec) throws java.io.IOException, NamingException {
            new client("test1", "test2");
            JSONObject JSONMessage = new JSONObject();
            JSONMessage.put("type", "info");
            /*System.out.println("OnOpen... " + ec.getUserProperties().get("Author"));*/
            JSONMessage.put("data", new JSONArray(JNDI_DNS.getSuffix()));
            session.getBasicRemote().sendText(JSONMessage.toString());
        }

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
