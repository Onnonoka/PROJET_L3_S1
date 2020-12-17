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
import java.io.IOException;

// For GlassfishServer
// For read/write file



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
            System.out.println("onClose: " + close_reason.getReasonPhrase() + "\n id: " + session.getId());
        }

        @javax.websocket.OnError
        public void onError(javax.websocket.Session session, Throwable err) throws IOException {
            System.err.println("onError: " + err.getMessage());
            JSONObject JSONReplyMessage = new JSONObject();
            JSONObject JSONError = new JSONObject();
            JSONError.put("error", err.toString());
            JSONError.put("details", err.getMessage());
            JSONReplyMessage.put("data", JSONError);
            JSONReplyMessage.put("succeed", false);

            session.getBasicRemote().sendText(JSONReplyMessage.toString());

            System.err.println(JSONReplyMessage.toString());
        }

        @OnMessage
        public void onMessage(Session session, String message) throws java.io.IOException, NamingException {

            JSONObject JSONMessage = new JSONObject(message);
            JSONObject JSONMessageData = JSONMessage.getJSONObject("data");
            JSONObject JSONReplyMessage = new JSONObject();

            if (JSONMessage.getString("type").equals("request")) {
                // Reply a JNDI request
                JNDI_DNS domainInfo;
                try {
                    domainInfo = new JNDI_DNS(JSONMessageData.getString("url"), JSONMessageData.getString("dns"));
                } catch (NamingException err) {
                    throw (new NamingException("DNS name not found!"));
                }
                if (!domainInfo.isEmpty()) {
                    JSONReplyMessage.put("data", domainInfo.toJSONObject());
                } else {
                    JSONReplyMessage.put("data", new JSONObject().toString());
                }
                JSONReplyMessage.put("url", JSONMessageData.getString("url"));
                JSONReplyMessage.put("succeed", true);
                JSONReplyMessage.put("type", "reply");
            } else {
                throw (new IOException("Unknown message format!"));
            }

            System.out.println("Message send to " + session.getId());
            session.getBasicRemote().sendText(JSONReplyMessage.toString());
        }

        @javax.websocket.OnOpen
        public void onOpen(javax.websocket.Session session, javax.websocket.EndpointConfig ec) throws IOException, NamingException {
            JSONObject JSONMessage = new JSONObject();
            JSONMessage.put("type", "info");
            try {
                JSONMessage.put("data", new JSONArray(JNDI_DNS.getSuffix()));
            } catch (NamingException err) {
                throw (new NamingException("Unable to get suffixes!"));
            }
            JSONMessage.put("succeed", true);
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
