package com.velocitypowered.proxy.connection.forge.modern;

import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.proxy.config.PlayerInfoForwarding;
import com.velocitypowered.proxy.connection.util.ConnectionTypeImpl;


public class ModernForgeConnectionType extends ConnectionTypeImpl {

    private static final GameProfile.Property FORGE_CLIENT_PROPERTY =
            new GameProfile.Property("forgeClient", ModernForgeConstants.VERSION, "");

    public ModernForgeConnectionType() {
        super(ModernForgeHandshakeClientPhase.NOT_STARTED,
                ModernForgeHandshakeBackendPhase.NOT_STARTED);
    }
    @Override
    public GameProfile addGameProfileTokensIfRequired(GameProfile original,
                                                      PlayerInfoForwarding forwardingType) {
        // We can't forward the FML token to the server when we are running in legacy forwarding mode,
        // since both use the "hostname" field in the handshake. We add a special property to the
        // profile instead, which will be ignored by non-Forge servers and can be intercepted by a
        // Forge coremod, such as SpongeForge.
        if (forwardingType == PlayerInfoForwarding.LEGACY) {
            return original.addProperty(FORGE_CLIENT_PROPERTY);
        }

        return original;
    }

}
