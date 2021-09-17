module org.cybergarage.upnp.std {
    requires org.cybergarage.upnp.core;
    requires java.prefs;
    requires java.desktop;
    requires java.sql;

    exports org.cybergarage.upnp.std.av.controller;
    exports org.cybergarage.upnp.std.av.player;
    exports org.cybergarage.upnp.std.av.renderer;
    exports org.cybergarage.upnp.std.av.server;
}