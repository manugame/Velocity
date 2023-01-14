package com.velocitypowered.proxy.connection.forge.modern;

public class ModernForgeConstants {

    /**
     * The version for forge handshakes.
     */
    public static final String VERSION = "FML2";

    /**
     * Clients attempting to connect to 1.13+ Forge servers will have
     * this token appended to the hostname in the initial handshake
     * packet.
     */
    public static final String HANDSHAKE_HOSTNAME_TOKEN = '\0' + VERSION + '\0';

    /**
     * The channel for forge handshakes.
     */
    public static final String HANDSHAKE_CHANNEL = "fml:handshake";

    /**
     * The channel for forge login wrapper.
     */
    public static final String LOGIN_WRAPPER_CHANNEL = "fml:loginwrapper";

    /**
     * The channel for forge play.
     */
    public static final String PLAY_CHANNEL = "fml:play";

    /**
     * The Mod List discriminator.
     */
    static final int MOD_LIST_DISCRIMINATOR = 2;

    /**
     * The Reset discriminator.
     */
    static final int RESET_DISCRIMINATOR = 98;

    private ModernForgeConstants() {
        throw new AssertionError();
    }
}
