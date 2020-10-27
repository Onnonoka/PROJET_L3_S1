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
import javax.naming.*;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JNDI_DNS {

    private final HashMap<String, List<String>> DNSAttributesValue = new HashMap<>();

    public JNDI_DNS(String url, String dns) throws NamingException {
        System.out.println(url + ", " + dns);
        // For Java 9 and more: https://mvnrepository.com/artifact/com.sun.jndi/dns:
        // _p.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        // Use another DNS server (platform config. otherwise: https://docs.oracle.com/javase/7/docs/technotes/guides/jndi/jndi-dns.html#URL):
        // _p.setProperty(Context.PROVIDER_URL, "dns://194.167.156.13"); // UPPA                         !!!! On as un accée refusé
        // _p.setProperty(Context.PROVIDER_URL, "dns://8.8.8.8"); // Google                              Donne plus de resultat que le dns de base
        // _p.setProperty(Context.PROVIDER_URL, "dns://193.146.78.14"); // University of Mondragon       !!!! On as un accée refusé

        // Set property for DirContext
        java.util.Properties DirContextEnv = new java.util.Properties();
        DirContextEnv.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        DirContextEnv.setProperty(Context.PROVIDER_URL, dns);
        DirContext dc = new InitialDirContext(DirContextEnv);

        // plus de resultat si l'url est sous la forme "google.com"
        // Store DNS attributes for the url given as argument
        Attributes attributes = dc.getAttributes(url, new String[]{"A", "AAAA", "CNAME", "MX", "NS", "PTR", "SRV", "SOA", "TXT", "HINFO", "NAPTR"}); // Stores the name server for a DNS entry...
        // Possible dns info => A, AAAA, CNAME, MX, NS, PTR, SRV, SOA, TXT, HINFO, NAPTR
        // CAA n'est pas reconue par DirContext
        if (attributes != null) {

            // Store as HashMap the value of the Attribute for the url given as argument
            NamingEnumeration<String> attributesIDsEnum = attributes.getIDs();
            while(attributesIDsEnum.hasMore()) {

                String ID = attributesIDsEnum.next();
                NamingEnumeration attributesValues = attributes.get(ID).getAll();
                List<String> attributeValues = new ArrayList<>();
                while(attributesValues.hasMore())
                    attributeValues.add(attributesValues.next().toString());
                this.DNSAttributesValue.put(ID, attributeValues);
            }

        }

        // Affichage de test a suppr avant de rendre le projet
        for (String key : this.DNSAttributesValue.keySet()) {
            for(String value : this.DNSAttributesValue.get(key))
                System.out.println(key + " " + value);
        }
    }

    /**
     * STATIC! Get and return a list of url suffix (con, fr, ...)
     * @return List<String>
     */
    public static List<String> getSuffix() throws NamingException {
        java.util.Properties dirContextEnv = new java.util.Properties();
        dirContextEnv.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        InitialContext _ic = new InitialContext(dirContextEnv);
        List<String> suffix = new ArrayList<>();

        System.out.println("\nInitial context: " + _ic.getNameInNamespace());  // Message de test
        System.out.println("1 ok");                                            // Message de test
        NamingEnumeration<NameClassPair> ne = _ic.list("");
        System.out.println("2 ok");                                            // Message de test
        while (ne.hasMore()) {
            suffix.add(ne.next().getName());
        }
        _ic.close();
        return suffix;
    }

    /**
     * return the Domain values in JSONObject
     * @return JSONObject
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        for (String key : this.DNSAttributesValue.keySet()) {
            jsonObject.put(key, new JSONArray(this.DNSAttributesValue.get(key)));
        }
        return jsonObject;
    }

    /**
     * check is value in this class
     * @return boolean
     */
    public boolean isEmpty() {
        return this.DNSAttributesValue.size() == 0;
    }

}
