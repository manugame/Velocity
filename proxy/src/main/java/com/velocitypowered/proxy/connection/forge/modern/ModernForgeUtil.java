package com.velocitypowered.proxy.connection.forge.modern;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.util.ModInfo;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.StateRegistry;
import com.velocitypowered.proxy.protocol.packet.LoginPluginMessage;
import com.velocitypowered.proxy.protocol.packet.LoginPluginResponse;
import com.velocitypowered.proxy.protocol.packet.PluginMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

public class ModernForgeUtil {
    private ModernForgeUtil() {
        throw new AssertionError();
    }

    static List<ModInfo.Mod> readModList(LoginPluginResponse message) {
        Preconditions.checkNotNull(message, "message");
        ByteBuf buf = message.content().slice();

        String channel = ProtocolUtils.readString(buf);
        if (!channel.equals(ModernForgeConstants.HANDSHAKE_CHANNEL)) {
            return null;
        }

        int payloadLength = ProtocolUtils.readVarInt(buf);
        if (payloadLength < 1) {
            return null;
        }

        int discriminator = buf.readUnsignedByte();
        if (discriminator != ModernForgeConstants.MOD_LIST_DISCRIMINATOR) {
            return null;
        }

        ImmutableList.Builder<ModInfo.Mod> mods = ImmutableList.builder();
        int modLength = ProtocolUtils.readVarInt(buf);
        for (int index = 0; index < modLength; index++) {
            String id = ProtocolUtils.readString(buf, 256);
            mods.add(new ModInfo.Mod(id));
        }

        return mods.build();
    }

    static MinecraftPacket resetPacket(StateRegistry state) {
        ByteBuf buf = Unpooled.buffer();
        // Channel
        ProtocolUtils.writeString(buf, ModernForgeConstants.HANDSHAKE_CHANNEL);
        // Payload Length
        ProtocolUtils.writeVarInt(buf, 1);
        // Discriminator
        buf.writeByte(ModernForgeConstants.RESET_DISCRIMINATOR & 0xFF);

        switch (state) {
            case LOGIN: {
                return new LoginPluginMessage(
                        ModernForgeConstants.RESET_DISCRIMINATOR,
                        ModernForgeConstants.LOGIN_WRAPPER_CHANNEL,
                        buf);
            }
            case PLAY: {
                return new PluginMessage(
                        ModernForgeConstants.LOGIN_WRAPPER_CHANNEL,
                        buf);
            }
            default: {
                throw new UnsupportedOperationException("Unsupported state " + state);
            }
        }
    }
}
