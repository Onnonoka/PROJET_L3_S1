/*
    ************************************************************
    |  Class JNDI_DNS ecrit par Dymko frédéric, Cazaux Axel    |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

package com.franckbarbier.websockets.tyrus.java;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JNDI_DNS {

    private HashMap<String, List<String>> DNSAttributesValue = new HashMap<>();

    public JNDI_DNS(String url) {
        try {
            // For Java 9 and more: https://mvnrepository.com/artifact/com.sun.jndi/dns:
            // _p.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            // Use another DNS server (platform config. otherwise: https://docs.oracle.com/javase/7/docs/technotes/guides/jndi/jndi-dns.html#URL):
            // _p.setProperty(Context.PROVIDER_URL, "dns://194.167.156.13"); // UPPA
            // _p.setProperty(Context.PROVIDER_URL, "dns://8.8.8.8"); // Google
            // _p.setProperty(Context.PROVIDER_URL, "dns://193.146.78.14"); // University of Mondragon

            // Set property for DirContext
            java.util.Properties DirContextEnv = new java.util.Properties();
            DirContextEnv.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            // DirContextEnv.setProperty(Context.PROVIDER_URL, dns); TODO A ajouter quand le js enveras le dns a utilisé
            DirContext dc = new InitialDirContext(DirContextEnv);

            // plus de resultat si l'url est sous la forme "google.com"
            // Store DNS attributes for the url given as argument
            Attributes attributes = dc.getAttributes(url, new String[]{"A", "AAAA", "MX", "NS", "PTR", "SRV", "SOA", "TXT"}); // Stores the name server for a DNS entry...
            // Possible dns info => A, AAAA, CNAME, MX, NS, PTR, SRV, SOA, TXT (Il peux y en avoir plus mais je les ai pas trouvé)
            // CAA n'est pas reconue par DirContext
            if (attributes != null) {

                // Store as HashMap the value of the Attribute for the url given as argument
                NamingEnumeration<String> attributesIDsEnum = attributes.getIDs();
                while(attributesIDsEnum.hasMore()) {

                    String ID = attributesIDsEnum.next().toString();
                    NamingEnumeration attributesValues = attributes.get(ID).getAll();
                    List<String> attributeValues = new ArrayList<>();
                    while(attributesValues.hasMore())
                        attributeValues.add(attributesValues.next().toString());
                    this.DNSAttributesValue.put(ID, attributeValues);
                }

            }

            /*
            for (String key : this.DNSAttributesValue.keySet()) {
                for(String value : this.DNSAttributesValue.get(key))
                System.out.println(key + " " + value);
            }
            */
        } catch (NamingException err) {
            System.err.println(err.getMessage() + ": " + err.getExplanation());
        }
    }

    /**
     * return the Domain value in JSONArray
     * @return JSONObject
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        for (String key : this.DNSAttributesValue.keySet()) {
            jsonObject.put(key, new JSONArray(this.DNSAttributesValue.get(key)));
        }
        return jsonObject;
    }

}
