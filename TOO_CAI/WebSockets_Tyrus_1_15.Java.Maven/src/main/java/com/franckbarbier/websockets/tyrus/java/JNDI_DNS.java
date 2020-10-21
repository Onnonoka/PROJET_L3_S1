/*
    ************************************************************
    |  Class JNDI ecrit par Dymko frédéric, Cazaux Axel        |
    |  L3 informatique                                         |
    |  Projet TOO/CAI                                          |
    ************************************************************
*/

package com.franckbarbier.websockets.tyrus.java;

import javax.naming.*;
import javax.naming.directory.*;

public class JNDI_DNS {
    private String url;
    private String DNS;

    public JNDI_DNS(String url) {
        this.url = url;
        // TODO Récupération des infos d'une url
        try {
            java.util.Properties _p = new java.util.Properties();
            // For Java 9 and more: https://mvnrepository.com/artifact/com.sun.jndi/dns:
            _p.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            // Use another DNS server (platform config. otherwise: https://docs.oracle.com/javase/7/docs/technotes/guides/jndi/jndi-dns.html#URL):
            // _p.setProperty(Context.PROVIDER_URL, "dns://194.167.156.13"); // UPPA
            // _p.setProperty(Context.PROVIDER_URL, "dns://8.8.8.8"); // Google
            // _p.setProperty(Context.PROVIDER_URL, "dns://193.146.78.14"); // University of Mondragon
            DirContext dc = new InitialDirContext(_p);
            Attributes attributes = dc.getAttributes(url, new String[]{"MX"}); // Stores the name server for a DNS entry...
            // Possible dns info => A, AAAA, CNAME, MX, NS, PTR, SRV, SOA, TXT, CAA
            if (attributes != null) {
                NamingEnumeration ne = attributes.get("MX").getAll();
                while (ne.hasMoreElements()) {
                    System.out.println("[MX] entry: " + ne.next().toString());
                }
            }
        } catch (NamingException err) {
            System.err.println(err.getMessage() + ": " + err.getExplanation());
        }
    }
}
