package net.raphimc.netminecraft.constants;

import java.util.HashMap;
import java.util.Map;

import static net.raphimc.netminecraft.constants.ConnectionState.*;
import static net.raphimc.netminecraft.constants.PacketDirection.CLIENTBOUND;
import static net.raphimc.netminecraft.constants.PacketDirection.SERVERBOUND;

public enum MCPackets {

    //Handshake
    //Client -> Server
    C2S_HANDSHAKE(HANDSHAKING, SERVERBOUND, 0x00),


    //Status
    //Client -> Server
    C2S_STATUS_REQUEST(STATUS, SERVERBOUND, 0x00),
    C2S_STATUS_PING(STATUS, SERVERBOUND, 0x01),

    //Server -> Client
    S2C_STATUS_RESPONSE(STATUS, CLIENTBOUND, 0x00),
    S2C_STATUS_PONG(STATUS, CLIENTBOUND, 0x01),


    //Login
    //Client -> Server
    C2S_LOGIN_HELLO(LOGIN, SERVERBOUND, 0x00),
    C2S_LOGIN_ENCRYPTION_RESPONSE(LOGIN, SERVERBOUND, 0x01),
    C2S_LOGIN_QUERY_RESPONSE(LOGIN, SERVERBOUND, MCVersion.v1_13, 0x02),

    //Server -> Client
    S2C_LOGIN_DISCONNECT(LOGIN, CLIENTBOUND, 0x00),
    S2C_LOGIN_ENCRYPTION_REQUEST(LOGIN, CLIENTBOUND, 0x01),
    S2C_LOGIN_SUCCESS(LOGIN, CLIENTBOUND, 0x02),
    S2C_LOGIN_COMPRESSION(LOGIN, CLIENTBOUND, MCVersion.v1_8, 0x03),
    S2C_LOGIN_QUERY_REQUEST(LOGIN, CLIENTBOUND, MCVersion.v1_13, 0x04),


    //Play
    //Client -> Server
    C2S_ENTITY_NBT_REQUEST(PLAY, SERVERBOUND, MCVersion.v1_13, 0x0C, MCVersion.v1_14, 0x0D, MCVersion.v1_17, 0x0C, MCVersion.v1_19, 0x0E, MCVersion.v1_19_1, 0x0F, MCVersion.v1_19_3, 0x0E),
    C2S_CRAFT_RECIPE_REQUEST(PLAY, SERVERBOUND, MCVersion.v1_12_1, 0x12, MCVersion.v1_13, 0x16, MCVersion.v1_14, 0x18, MCVersion.v1_16, 0x19, MCVersion.v1_17, 0x18, MCVersion.v1_19, 0x1A, MCVersion.v1_19_1, 0x1B, MCVersion.v1_19_3, 0x1A),
    C2S_CHAT_PREVIEW(PLAY, SERVERBOUND, MCVersion.v1_19, 0x05, MCVersion.v1_19_1, 0x06, MCVersion.v1_19_3, -1),
    C2S_SET_DIFFICULTY(PLAY, SERVERBOUND, MCVersion.v1_14, 0x02),
    C2S_UPDATE_SIGN(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x12, MCVersion.v1_9, 0x19, MCVersion.v1_12, 0x1C, MCVersion.v1_13, 0x26, MCVersion.v1_14, 0x29, MCVersion.v1_16, 0x2A, MCVersion.v1_16_2, 0x2B, MCVersion.v1_19, 0x2D, MCVersion.v1_19_1, 0x2E, MCVersion.v1_19_3, 0x2D),
    C2S_SELECT_TRADE(PLAY, SERVERBOUND, MCVersion.v1_13, 0x1F, MCVersion.v1_14, 0x21, MCVersion.v1_16, 0x22, MCVersion.v1_16_2, 0x23, MCVersion.v1_19, 0x25, MCVersion.v1_19_1, 0x26, MCVersion.v1_19_3, 0x25),
    C2S_SEEN_RECIPE(PLAY, SERVERBOUND, MCVersion.v1_16_2, 0x1F, MCVersion.v1_19, 0x21, MCVersion.v1_19_1, 0x22, MCVersion.v1_19_3, 0x21),
    C2S_RENAME_ITEM(PLAY, SERVERBOUND, MCVersion.v1_13, 0x1C, MCVersion.v1_14, 0x1E, MCVersion.v1_16, 0x1F, MCVersion.v1_16_2, 0x20, MCVersion.v1_19, 0x22, MCVersion.v1_19_1, 0x23, MCVersion.v1_19_3, 0x22),
    C2S_KEEP_ALIVE(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x00, MCVersion.v1_9, 0x0B, MCVersion.v1_12, 0x0C, MCVersion.v1_12_1, 0x0B, MCVersion.v1_13, 0x0E, MCVersion.v1_14, 0x0F, MCVersion.v1_16, 0x10, MCVersion.v1_17, 0x0F, MCVersion.v1_19, 0x11, MCVersion.v1_19_1, 0x12, MCVersion.v1_19_3, 0x11),
    C2S_STEER_VEHICLE(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x0C, MCVersion.v1_9, 0x15, MCVersion.v1_12, 0x16, MCVersion.v1_13, 0x1A, MCVersion.v1_14, 0x1C, MCVersion.v1_16, 0x1D, MCVersion.v1_17, 0x1C, MCVersion.v1_19, 0x1E, MCVersion.v1_19_1, 0x1F, MCVersion.v1_19_3, 0x1E),
    C2S_PLAYER_ABILITIES(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x13, MCVersion.v1_9, 0x12, MCVersion.v1_12, 0x13, MCVersion.v1_13, 0x17, MCVersion.v1_14, 0x19, MCVersion.v1_16, 0x1A, MCVersion.v1_17, 0x19, MCVersion.v1_19, 0x1B, MCVersion.v1_19_1, 0x1C, MCVersion.v1_19_3, 0x1B),
    C2S_PLAYER_ROTATION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x05, MCVersion.v1_9, 0x0E, MCVersion.v1_12, 0x10, MCVersion.v1_12_1, 0x0F, MCVersion.v1_13, 0x12, MCVersion.v1_14, 0x13, MCVersion.v1_16, 0x14, MCVersion.v1_17, 0x13, MCVersion.v1_19, 0x15, MCVersion.v1_19_1, 0x16, MCVersion.v1_19_3, 0x15),
    C2S_ANIMATION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x0A, MCVersion.v1_9, 0x1A, MCVersion.v1_12, 0x1D, MCVersion.v1_13, 0x27, MCVersion.v1_14, 0x2A, MCVersion.v1_16, 0x2B, MCVersion.v1_16_2, 0x2C, MCVersion.v1_19, 0x2E, MCVersion.v1_19_1, 0x2F, MCVersion.v1_19_3, 0x2E),
    C2S_SET_BEACON_EFFECT(PLAY, SERVERBOUND, MCVersion.v1_13, 0x20, MCVersion.v1_14, 0x22, MCVersion.v1_16, 0x23, MCVersion.v1_16_2, 0x24, MCVersion.v1_19, 0x26, MCVersion.v1_19_1, 0x27, MCVersion.v1_19_3, 0x26),
    C2S_UPDATE_COMMAND_BLOCK_MINECART(PLAY, SERVERBOUND, MCVersion.v1_13, 0x23, MCVersion.v1_14, 0x25, MCVersion.v1_16, 0x26, MCVersion.v1_16_2, 0x27, MCVersion.v1_19, 0x29, MCVersion.v1_19_1, 0x2A, MCVersion.v1_19_3, 0x29),
    C2S_CHAT_COMMAND(PLAY, SERVERBOUND, MCVersion.v1_19, 0x03, MCVersion.v1_19_1, 0x04),
    C2S_ADVANCEMENT_TAB(PLAY, SERVERBOUND, MCVersion.v1_12, 0x19, MCVersion.v1_13, 0x1E, MCVersion.v1_14, 0x20, MCVersion.v1_16, 0x21, MCVersion.v1_16_2, 0x22, MCVersion.v1_19, 0x24, MCVersion.v1_19_1, 0x25, MCVersion.v1_19_3, 0x24),
    C2S_CHAT_MESSAGE(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x01, MCVersion.v1_9, 0x02, MCVersion.v1_12, 0x03, MCVersion.v1_12_1, 0x02, MCVersion.v1_14, 0x03, MCVersion.v1_19, 0x04, MCVersion.v1_19_1, 0x05),
    C2S_GENERATE_JIGSAW(PLAY, SERVERBOUND, MCVersion.v1_16, 0x0F, MCVersion.v1_17, 0x0E, MCVersion.v1_19, 0x10, MCVersion.v1_19_1, 0x11, MCVersion.v1_19_3, 0x10),
    C2S_PLAYER_MOVEMENT(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x03, MCVersion.v1_9, 0x0F, MCVersion.v1_12, 0x0D, MCVersion.v1_12_1, 0x0C, MCVersion.v1_13, 0x0F, MCVersion.v1_14, 0x14, MCVersion.v1_16, 0x15, MCVersion.v1_17, 0x14, MCVersion.v1_19, 0x16, MCVersion.v1_19_1, 0x17, MCVersion.v1_19_3, 0x16),
    C2S_QUERY_BLOCK_NBT(PLAY, SERVERBOUND, MCVersion.v1_13, 0x01),
    C2S_HELD_ITEM_CHANGE(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x09, MCVersion.v1_9, 0x17, MCVersion.v1_12, 0x1A, MCVersion.v1_13, 0x21, MCVersion.v1_14, 0x23, MCVersion.v1_16, 0x24, MCVersion.v1_16_2, 0x25, MCVersion.v1_19, 0x27, MCVersion.v1_19_1, 0x28, MCVersion.v1_19_3, 0x27),
    C2S_PICK_ITEM(PLAY, SERVERBOUND, MCVersion.v1_13, 0x15, MCVersion.v1_14, 0x17, MCVersion.v1_16, 0x18, MCVersion.v1_17, 0x17, MCVersion.v1_19, 0x19, MCVersion.v1_19_1, 0x1A, MCVersion.v1_19_3, 0x19),
    C2S_PLAYER_DIGGING(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x07, MCVersion.v1_9, 0x13, MCVersion.v1_12, 0x14, MCVersion.v1_13, 0x18, MCVersion.v1_14, 0x1A, MCVersion.v1_16, 0x1B, MCVersion.v1_17, 0x1A, MCVersion.v1_19, 0x1C, MCVersion.v1_19_1, 0x1D, MCVersion.v1_19_3, 0x1C),
    C2S_STEER_BOAT(PLAY, SERVERBOUND, MCVersion.v1_9, 0x11, MCVersion.v1_12, 0x12, MCVersion.v1_12_1, 0x11, MCVersion.v1_13, 0x14, MCVersion.v1_14, 0x16, MCVersion.v1_16, 0x17, MCVersion.v1_17, 0x16, MCVersion.v1_19, 0x18, MCVersion.v1_19_1, 0x19, MCVersion.v1_19_3, 0x18),
    C2S_VEHICLE_MOVE(PLAY, SERVERBOUND, MCVersion.v1_9, 0x10, MCVersion.v1_12, 0x11, MCVersion.v1_12_1, 0x10, MCVersion.v1_13, 0x13, MCVersion.v1_14, 0x15, MCVersion.v1_16, 0x16, MCVersion.v1_17, 0x15, MCVersion.v1_19, 0x17, MCVersion.v1_19_1, 0x18, MCVersion.v1_19_3, 0x17),
    C2S_CLICK_WINDOW_BUTTON(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x11, MCVersion.v1_9, 0x06, MCVersion.v1_12, 0x07, MCVersion.v1_12_1, 0x06, MCVersion.v1_13, 0x07, MCVersion.v1_14, 0x08, MCVersion.v1_17, 0x07, MCVersion.v1_19, 0x09, MCVersion.v1_19_1, 0x0A, MCVersion.v1_19_3, 0x09),
    C2S_CLOSE_WINDOW(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x0D, MCVersion.v1_9, 0x08, MCVersion.v1_12, 0x09, MCVersion.v1_12_1, 0x08, MCVersion.v1_13, 0x09, MCVersion.v1_14, 0x0A, MCVersion.v1_17, 0x09, MCVersion.v1_19, 0x0B, MCVersion.v1_19_1, 0x0C, MCVersion.v1_19_3, 0x0B),
    C2S_TELEPORT_CONFIRM(PLAY, SERVERBOUND, MCVersion.v1_9, 0x00),
    C2S_UPDATE_COMMAND_BLOCK(PLAY, SERVERBOUND, MCVersion.v1_13, 0x22, MCVersion.v1_14, 0x24, MCVersion.v1_16, 0x25, MCVersion.v1_16_2, 0x26, MCVersion.v1_19, 0x28, MCVersion.v1_19_1, 0x29, MCVersion.v1_19_3, 0x28),
    C2S_ENTITY_ACTION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x0B, MCVersion.v1_9, 0x14, MCVersion.v1_12, 0x15, MCVersion.v1_13, 0x19, MCVersion.v1_14, 0x1B, MCVersion.v1_16, 0x1C, MCVersion.v1_17, 0x1B, MCVersion.v1_19, 0x1D, MCVersion.v1_19_1, 0x1E, MCVersion.v1_19_3, 0x1D),
    C2S_CREATIVE_INVENTORY_ACTION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x10, MCVersion.v1_9, 0x18, MCVersion.v1_12, 0x1B, MCVersion.v1_13, 0x24, MCVersion.v1_14, 0x26, MCVersion.v1_16, 0x27, MCVersion.v1_16_2, 0x28, MCVersion.v1_19, 0x2A, MCVersion.v1_19_1, 0x2B, MCVersion.v1_19_3, 0x2A),
    C2S_CLIENT_SETTINGS(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x15, MCVersion.v1_9, 0x04, MCVersion.v1_12, 0x05, MCVersion.v1_12_1, 0x04, MCVersion.v1_14, 0x05, MCVersion.v1_19, 0x07, MCVersion.v1_19_1, 0x08, MCVersion.v1_19_3, 0x07),
    C2S_SPECTATE(PLAY, SERVERBOUND, MCVersion.v1_8, 0x18, MCVersion.v1_9, 0x1B, MCVersion.v1_12, 0x1E, MCVersion.v1_13, 0x28, MCVersion.v1_14, 0x2B, MCVersion.v1_16, 0x2C, MCVersion.v1_16_2, 0x2D, MCVersion.v1_19, 0x2F, MCVersion.v1_19_1, 0x30, MCVersion.v1_19_3, 0x2F),
    C2S_INTERACT_ENTITY(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x02, MCVersion.v1_9, 0x0A, MCVersion.v1_12, 0x0B, MCVersion.v1_12_1, 0x0A, MCVersion.v1_13, 0x0D, MCVersion.v1_14, 0x0E, MCVersion.v1_17, 0x0D, MCVersion.v1_19, 0x0F, MCVersion.v1_19_1, 0x10, MCVersion.v1_19_3, 0x0F),
    C2S_PREPARE_CRAFTING_GRID(PLAY, SERVERBOUND, MCVersion.v1_12, 0x01, MCVersion.v1_12_1, -1),
    C2S_WINDOW_CONFIRMATION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x0F, MCVersion.v1_9, 0x05, MCVersion.v1_12, 0x06, MCVersion.v1_12_1, 0x05, MCVersion.v1_13, 0x06, MCVersion.v1_14, 0x07, MCVersion.v1_17, -1),
    C2S_TAB_COMPLETE(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x14, MCVersion.v1_9, 0x01, MCVersion.v1_12, 0x02, MCVersion.v1_12_1, 0x01, MCVersion.v1_13, 0x05, MCVersion.v1_14, 0x06, MCVersion.v1_19, 0x08, MCVersion.v1_19_1, 0x09, MCVersion.v1_19_3, 0x08),
    C2S_PONG(PLAY, SERVERBOUND, MCVersion.v1_17, 0x1D, MCVersion.v1_19, 0x1F, MCVersion.v1_19_1, 0x20, MCVersion.v1_19_3, 0x1F),
    C2S_UPDATE_STRUCTURE_BLOCK(PLAY, SERVERBOUND, MCVersion.v1_13, 0x25, MCVersion.v1_14, 0x28, MCVersion.v1_16, 0x29, MCVersion.v1_16_2, 0x2A, MCVersion.v1_19, 0x2C, MCVersion.v1_19_1, 0x2D, MCVersion.v1_19_3, 0x2C),
    C2S_CLICK_WINDOW(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x0E, MCVersion.v1_9, 0x07, MCVersion.v1_12, 0x08, MCVersion.v1_12_1, 0x07, MCVersion.v1_13, 0x08, MCVersion.v1_14, 0x09, MCVersion.v1_17, 0x08, MCVersion.v1_19, 0x0A, MCVersion.v1_19_1, 0x0B, MCVersion.v1_19_3, 0x0A),
    C2S_CHAT_ACK(PLAY, SERVERBOUND, MCVersion.v1_19_1, 0x03),
    C2S_PLAYER_BLOCK_PLACEMENT(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x08, MCVersion.v1_9, 0x1C, MCVersion.v1_12, 0x1F, MCVersion.v1_13, 0x29, MCVersion.v1_14, 0x2C, MCVersion.v1_16, 0x2D, MCVersion.v1_16_2, 0x2E, MCVersion.v1_19, 0x30, MCVersion.v1_19_1, 0x31, MCVersion.v1_19_3, 0x30),
    C2S_UPDATE_JIGSAW_BLOCK(PLAY, SERVERBOUND, MCVersion.v1_14, 0x27, MCVersion.v1_16, 0x28, MCVersion.v1_16_2, 0x29, MCVersion.v1_19, 0x2B, MCVersion.v1_19_1, 0x2C, MCVersion.v1_19_3, 0x2B),
    C2S_EDIT_BOOK(PLAY, SERVERBOUND, MCVersion.v1_13, 0x0B, MCVersion.v1_14, 0x0C, MCVersion.v1_17, 0x0B, MCVersion.v1_19, 0x0D, MCVersion.v1_19_1, 0x0E, MCVersion.v1_19_3, 0x0D),
    C2S_RESOURCE_PACK_STATUS(PLAY, SERVERBOUND, MCVersion.v1_8, 0x19, MCVersion.v1_9, 0x16, MCVersion.v1_12, 0x18, MCVersion.v1_13, 0x1D, MCVersion.v1_14, 0x1F, MCVersion.v1_16, 0x20, MCVersion.v1_16_2, 0x21, MCVersion.v1_19, 0x23, MCVersion.v1_19_1, 0x24, MCVersion.v1_19_3, 0x23),
    C2S_USE_ITEM(PLAY, SERVERBOUND, MCVersion.v1_9, 0x1D, MCVersion.v1_12, 0x20, MCVersion.v1_13, 0x2A, MCVersion.v1_14, 0x2D, MCVersion.v1_16, 0x2E, MCVersion.v1_16_2, 0x2F, MCVersion.v1_19, 0x31, MCVersion.v1_19_1, 0x32, MCVersion.v1_19_3, 0x31),
    C2S_LOCK_DIFFICULTY(PLAY, SERVERBOUND, MCVersion.v1_14, 0x10, MCVersion.v1_16, 0x11, MCVersion.v1_17, 0x10, MCVersion.v1_19, 0x12, MCVersion.v1_19_1, 0x13, MCVersion.v1_19_3, 0x12),
    C2S_CLIENT_STATUS(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x16, MCVersion.v1_9, 0x03, MCVersion.v1_12, 0x04, MCVersion.v1_12_1, 0x03, MCVersion.v1_14, 0x04, MCVersion.v1_19, 0x06, MCVersion.v1_19_1, 0x07, MCVersion.v1_19_3, 0x06),
    C2S_PLAYER_POSITION_AND_ROTATION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x06, MCVersion.v1_9, 0x0D, MCVersion.v1_12, 0x0F, MCVersion.v1_12_1, 0x0E, MCVersion.v1_13, 0x11, MCVersion.v1_14, 0x12, MCVersion.v1_16, 0x13, MCVersion.v1_17, 0x12, MCVersion.v1_19, 0x14, MCVersion.v1_19_1, 0x15, MCVersion.v1_19_3, 0x14),
    C2S_PLUGIN_MESSAGE(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x17, MCVersion.v1_9, 0x09, MCVersion.v1_12, 0x0A, MCVersion.v1_12_1, 0x09, MCVersion.v1_13, 0x0A, MCVersion.v1_14, 0x0B, MCVersion.v1_17, 0x0A, MCVersion.v1_19, 0x0C, MCVersion.v1_19_1, 0x0D, MCVersion.v1_19_3, 0x0C),
    C2S_RECIPE_BOOK_DATA(PLAY, SERVERBOUND, MCVersion.v1_12, 0x17, MCVersion.v1_13, 0x1B, MCVersion.v1_14, 0x1D, MCVersion.v1_16, 0x1E, MCVersion.v1_19, 0x20, MCVersion.v1_19_1, 0x21, MCVersion.v1_19_3, 0x20),
    C2S_PLAYER_POSITION(PLAY, SERVERBOUND, MCVersion.v1_7_2, 0x04, MCVersion.v1_9, 0x0C, MCVersion.v1_12, 0x0E, MCVersion.v1_12_1, 0x0D, MCVersion.v1_13, 0x10, MCVersion.v1_14, 0x11, MCVersion.v1_16, 0x12, MCVersion.v1_17, 0x11, MCVersion.v1_19, 0x13, MCVersion.v1_19_1, 0x14, MCVersion.v1_19_3, 0x13),

    //Server -> Client
    S2C_ATTACH_ENTITY(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x1B, MCVersion.v1_9, 0x3A, MCVersion.v1_12, 0x3C, MCVersion.v1_12_1, 0x3D, MCVersion.v1_13, 0x40, MCVersion.v1_14, 0x44, MCVersion.v1_15, 0x45, MCVersion.v1_17, 0x4E, MCVersion.v1_19_1, 0x51, MCVersion.v1_19_3, 0x4F),
    S2C_PLAYER_CHAT_HEADER(PLAY, CLIENTBOUND, MCVersion.v1_19_1, 0x32, MCVersion.v1_19_3, -1),
    S2C_FACE_PLAYER(PLAY, CLIENTBOUND, MCVersion.v1_13, 0x31, MCVersion.v1_14, 0x34, MCVersion.v1_15, 0x35, MCVersion.v1_16, 0x34, MCVersion.v1_16_2, 0x33, MCVersion.v1_17, 0x37, MCVersion.v1_19, 0x35, MCVersion.v1_19_1, 0x38, MCVersion.v1_19_3, 0x37),
    S2C_WINDOW_PROPERTY(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x31, MCVersion.v1_9, 0x15, MCVersion.v1_13, 0x16, MCVersion.v1_14, 0x15, MCVersion.v1_15, 0x16, MCVersion.v1_16, 0x15, MCVersion.v1_16_2, 0x14, MCVersion.v1_17, 0x15, MCVersion.v1_19, 0x12, MCVersion.v1_19_3, 0x11),
    S2C_EXPLOSION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x27, MCVersion.v1_9, 0x1C, MCVersion.v1_13, 0x1E, MCVersion.v1_14, 0x1C, MCVersion.v1_15, 0x1D, MCVersion.v1_16, 0x1C, MCVersion.v1_16_2, 0x1B, MCVersion.v1_17, 0x1C, MCVersion.v1_19, 0x19, MCVersion.v1_19_1, 0x1B, MCVersion.v1_19_3, 0x1A),
    S2C_PLAYER_INFO(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x38, MCVersion.v1_9, 0x2D, MCVersion.v1_12_1, 0x2E, MCVersion.v1_13, 0x30, MCVersion.v1_14, 0x33, MCVersion.v1_15, 0x34, MCVersion.v1_16, 0x33, MCVersion.v1_16_2, 0x32, MCVersion.v1_17, 0x36, MCVersion.v1_19, 0x34, MCVersion.v1_19_1, 0x37, MCVersion.v1_19_3, -1),
    S2C_DESTROY_ENTITIES(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x13, MCVersion.v1_9, 0x30, MCVersion.v1_12, 0x31, MCVersion.v1_12_1, 0x32, MCVersion.v1_13, 0x35, MCVersion.v1_14, 0x37, MCVersion.v1_15, 0x38, MCVersion.v1_16, 0x37, MCVersion.v1_16_2, 0x36, MCVersion.v1_17, -1),
    S2C_SPAWN_MOB(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x0F, MCVersion.v1_9, 0x03, MCVersion.v1_16, 0x02, MCVersion.v1_19, -1),
    S2C_SERVER_DIFFICULTY(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x41, MCVersion.v1_9, 0x0D, MCVersion.v1_15, 0x0E, MCVersion.v1_16, 0x0D, MCVersion.v1_17, 0x0E, MCVersion.v1_19, 0x0B),
    S2C_UPDATE_ENTITY_NBT(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x49, MCVersion.v1_9, -1),
    S2C_DISPLAY_SCOREBOARD(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x3D, MCVersion.v1_9, 0x38, MCVersion.v1_12, 0x3A, MCVersion.v1_12_1, 0x3B, MCVersion.v1_13, 0x3E, MCVersion.v1_14, 0x42, MCVersion.v1_15, 0x43, MCVersion.v1_17, 0x4C, MCVersion.v1_19_1, 0x4F, MCVersion.v1_19_3, 0x4D),
    S2C_SET_SLOT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x2F, MCVersion.v1_9, 0x16, MCVersion.v1_13, 0x17, MCVersion.v1_14, 0x16, MCVersion.v1_15, 0x17, MCVersion.v1_16, 0x16, MCVersion.v1_16_2, 0x15, MCVersion.v1_17, 0x16, MCVersion.v1_19, 0x13, MCVersion.v1_19_3, 0x12),
    S2C_ACTIONBAR(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x41, MCVersion.v1_19, 0x40, MCVersion.v1_19_1, 0x43, MCVersion.v1_19_3, 0x42),
    S2C_ENTITY_ANIMATION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x0B, MCVersion.v1_9, 0x06, MCVersion.v1_16, 0x05, MCVersion.v1_17, 0x06, MCVersion.v1_19, 0x03),
    S2C_DISCONNECT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x40, MCVersion.v1_9, 0x1A, MCVersion.v1_13, 0x1B, MCVersion.v1_14, 0x1A, MCVersion.v1_15, 0x1B, MCVersion.v1_16, 0x1A, MCVersion.v1_16_2, 0x19, MCVersion.v1_17, 0x1A, MCVersion.v1_19, 0x17, MCVersion.v1_19_1, 0x19, MCVersion.v1_19_3, 0x17),
    S2C_SPAWN_PARTICLE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x2A, MCVersion.v1_9, 0x22, MCVersion.v1_13, 0x24, MCVersion.v1_14, 0x23, MCVersion.v1_15, 0x24, MCVersion.v1_16, 0x23, MCVersion.v1_16_2, 0x22, MCVersion.v1_17, 0x24, MCVersion.v1_19, 0x21, MCVersion.v1_19_1, 0x23, MCVersion.v1_19_3, 0x22),
    S2C_BLOCK_BREAK_ANIMATION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x25, MCVersion.v1_9, 0x08, MCVersion.v1_15, 0x09, MCVersion.v1_16, 0x08, MCVersion.v1_17, 0x09, MCVersion.v1_19, 0x06),
    S2C_KEEP_ALIVE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x00, MCVersion.v1_9, 0x1F, MCVersion.v1_13, 0x21, MCVersion.v1_14, 0x20, MCVersion.v1_15, 0x21, MCVersion.v1_16, 0x20, MCVersion.v1_16_2, 0x1F, MCVersion.v1_17, 0x21, MCVersion.v1_19, 0x1E, MCVersion.v1_19_1, 0x20, MCVersion.v1_19_3, 0x1F),
    S2C_SPAWN_POSITION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x05, MCVersion.v1_9, 0x43, MCVersion.v1_12, 0x45, MCVersion.v1_12_1, 0x46, MCVersion.v1_13, 0x49, MCVersion.v1_14, 0x4D, MCVersion.v1_15, 0x4E, MCVersion.v1_16, 0x42, MCVersion.v1_17, 0x4B, MCVersion.v1_19, 0x4A, MCVersion.v1_19_1, 0x4D, MCVersion.v1_19_3, 0x4C),
    S2C_ENTITY_METADATA(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x1C, MCVersion.v1_9, 0x39, MCVersion.v1_12, 0x3B, MCVersion.v1_12_1, 0x3C, MCVersion.v1_13, 0x3F, MCVersion.v1_14, 0x43, MCVersion.v1_15, 0x44, MCVersion.v1_17, 0x4D, MCVersion.v1_19_1, 0x50, MCVersion.v1_19_3, 0x4E),
    S2C_OPEN_WINDOW(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x2D, MCVersion.v1_9, 0x13, MCVersion.v1_13, 0x14, MCVersion.v1_14, 0x2E, MCVersion.v1_15, 0x2F, MCVersion.v1_16, 0x2E, MCVersion.v1_16_2, 0x2D, MCVersion.v1_17, 0x2E, MCVersion.v1_19, 0x2B, MCVersion.v1_19_1, 0x2D, MCVersion.v1_19_3, 0x2C),
    S2C_BLOCK_ACTION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x24, MCVersion.v1_9, 0x0A, MCVersion.v1_15, 0x0B, MCVersion.v1_16, 0x0A, MCVersion.v1_17, 0x0B, MCVersion.v1_19, 0x08),
    S2C_REMOVE_ENTITY_EFFECT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x1E, MCVersion.v1_9, 0x31, MCVersion.v1_12, 0x32, MCVersion.v1_12_1, 0x33, MCVersion.v1_13, 0x36, MCVersion.v1_14, 0x38, MCVersion.v1_15, 0x39, MCVersion.v1_16, 0x38, MCVersion.v1_16_2, 0x37, MCVersion.v1_17, 0x3B, MCVersion.v1_19, 0x39, MCVersion.v1_19_1, 0x3C, MCVersion.v1_19_3, 0x3B),
    S2C_EFFECT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x28, MCVersion.v1_9, 0x21, MCVersion.v1_13, 0x23, MCVersion.v1_14, 0x22, MCVersion.v1_15, 0x23, MCVersion.v1_16, 0x22, MCVersion.v1_16_2, 0x21, MCVersion.v1_17, 0x23, MCVersion.v1_19, 0x20, MCVersion.v1_19_1, 0x22, MCVersion.v1_19_3, 0x21),
    S2C_CAMERA(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x43, MCVersion.v1_9, 0x36, MCVersion.v1_12, 0x38, MCVersion.v1_12_1, 0x39, MCVersion.v1_13, 0x3C, MCVersion.v1_14, 0x3E, MCVersion.v1_15, 0x3F, MCVersion.v1_16, 0x3E, MCVersion.v1_17, 0x47, MCVersion.v1_19, 0x46, MCVersion.v1_19_1, 0x49, MCVersion.v1_19_3, 0x48),
    S2C_BLOCK_ENTITY_DATA(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x35, MCVersion.v1_9, 0x09, MCVersion.v1_15, 0x0A, MCVersion.v1_16, 0x09, MCVersion.v1_17, 0x0A, MCVersion.v1_19, 0x07),
    S2C_TITLE_TEXT(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x59, MCVersion.v1_18, 0x5A, MCVersion.v1_19_1, 0x5D, MCVersion.v1_19_3, 0x5B),
    S2C_CHAT_MESSAGE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x02, MCVersion.v1_9, 0x0F, MCVersion.v1_13, 0x0E, MCVersion.v1_15, 0x0F, MCVersion.v1_16, 0x0E, MCVersion.v1_17, 0x0F, MCVersion.v1_19, -1),
    S2C_WORLD_BORDER_WARNING_DELAY(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x45, MCVersion.v1_19, 0x44, MCVersion.v1_19_1, 0x47, MCVersion.v1_19_3, 0x46),
    S2C_ENTITY_STATUS(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x1A, MCVersion.v1_9, 0x1B, MCVersion.v1_13, 0x1C, MCVersion.v1_14, 0x1B, MCVersion.v1_15, 0x1C, MCVersion.v1_16, 0x1B, MCVersion.v1_16_2, 0x1A, MCVersion.v1_17, 0x1B, MCVersion.v1_19, 0x18, MCVersion.v1_19_1, 0x1A, MCVersion.v1_19_3, 0x19),
    S2C_SPAWN_GLOBAL_ENTITY(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x2C, MCVersion.v1_9, 0x02, MCVersion.v1_16, -1),
    S2C_RESPAWN(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x07, MCVersion.v1_9, 0x33, MCVersion.v1_12, 0x34, MCVersion.v1_12_1, 0x35, MCVersion.v1_13, 0x38, MCVersion.v1_14, 0x3A, MCVersion.v1_15, 0x3B, MCVersion.v1_16, 0x3A, MCVersion.v1_16_2, 0x39, MCVersion.v1_17, 0x3D, MCVersion.v1_19, 0x3B, MCVersion.v1_19_1, 0x3E, MCVersion.v1_19_3, 0x3D),
    S2C_TAGS(PLAY, CLIENTBOUND, MCVersion.v1_13, 0x55, MCVersion.v1_14, 0x5B, MCVersion.v1_15, 0x5C, MCVersion.v1_16, 0x5B, MCVersion.v1_17, 0x66, MCVersion.v1_18, 0x67, MCVersion.v1_19, 0x68, MCVersion.v1_19_1, 0x6B, MCVersion.v1_19_3, 0x6A),
    S2C_ENTITY_EFFECT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x1D, MCVersion.v1_9, 0x4C, MCVersion.v1_9_3, 0x4B, MCVersion.v1_12, 0x4E, MCVersion.v1_12_1, 0x4F, MCVersion.v1_13, 0x53, MCVersion.v1_14, 0x59, MCVersion.v1_15, 0x5A, MCVersion.v1_16, 0x59, MCVersion.v1_17, 0x64, MCVersion.v1_18, 0x65, MCVersion.v1_19, 0x66, MCVersion.v1_19_1, 0x69, MCVersion.v1_19_3, 0x68),
    S2C_TIME_UPDATE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x03, MCVersion.v1_9, 0x44, MCVersion.v1_12, 0x46, MCVersion.v1_12_1, 0x47, MCVersion.v1_13, 0x4A, MCVersion.v1_14, 0x4E, MCVersion.v1_15, 0x4F, MCVersion.v1_16, 0x4E, MCVersion.v1_17, 0x58, MCVersion.v1_18, 0x59, MCVersion.v1_19_1, 0x5C, MCVersion.v1_19_3, 0x5A),
    S2C_COMBAT_ENTER(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x34, MCVersion.v1_19, 0x32, MCVersion.v1_19_1, 0x35, MCVersion.v1_19_3, 0x33),
    S2C_OPEN_BOOK(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x2D, MCVersion.v1_15, 0x2E, MCVersion.v1_16, 0x2D, MCVersion.v1_16_2, 0x2C, MCVersion.v1_17, 0x2D, MCVersion.v1_19, 0x2A, MCVersion.v1_19_1, 0x2C, MCVersion.v1_19_3, 0x2B),
    S2C_HELD_ITEM_CHANGE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x09, MCVersion.v1_9, 0x37, MCVersion.v1_12, 0x39, MCVersion.v1_12_1, 0x3A, MCVersion.v1_13, 0x3D, MCVersion.v1_14, 0x3F, MCVersion.v1_15, 0x40, MCVersion.v1_16, 0x3F, MCVersion.v1_17, 0x48, MCVersion.v1_19, 0x47, MCVersion.v1_19_1, 0x4A, MCVersion.v1_19_3, 0x49),
    S2C_ENTITY_POSITION_AND_ROTATION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x17, MCVersion.v1_9, 0x26, MCVersion.v1_12, 0x27, MCVersion.v1_13, 0x29, MCVersion.v1_15, 0x2A, MCVersion.v1_16, 0x29, MCVersion.v1_16_2, 0x28, MCVersion.v1_17, 0x2A, MCVersion.v1_19, 0x27, MCVersion.v1_19_1, 0x29, MCVersion.v1_19_3, 0x28),
    S2C_ENTITY_MOVEMENT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x14, MCVersion.v1_9, 0x28, MCVersion.v1_12, 0x25, MCVersion.v1_13, 0x27, MCVersion.v1_14, 0x2B, MCVersion.v1_15, 0x2C, MCVersion.v1_16, 0x2B, MCVersion.v1_16_2, 0x2A, MCVersion.v1_17, -1),
    S2C_BLOCK_CHANGED_ACK(PLAY, CLIENTBOUND, MCVersion.v1_19, 0x05),
    S2C_WORLD_BORDER_WARNING_DISTANCE(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x46, MCVersion.v1_19, 0x45, MCVersion.v1_19_1, 0x48, MCVersion.v1_19_3, 0x47),
    S2C_RESOURCE_PACK(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x48, MCVersion.v1_9, 0x32, MCVersion.v1_12, 0x33, MCVersion.v1_12_1, 0x34, MCVersion.v1_13, 0x37, MCVersion.v1_14, 0x39, MCVersion.v1_15, 0x3A, MCVersion.v1_16, 0x39, MCVersion.v1_16_2, 0x38, MCVersion.v1_17, 0x3C, MCVersion.v1_19, 0x3A, MCVersion.v1_19_1, 0x3D, MCVersion.v1_19_3, 0x3C),
    S2C_SET_COMPRESSION(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x46, MCVersion.v1_9, -1),
    S2C_OPEN_SIGN_EDITOR(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x36, MCVersion.v1_9, 0x2A, MCVersion.v1_13, 0x2C, MCVersion.v1_14, 0x2F, MCVersion.v1_15, 0x30, MCVersion.v1_16, 0x2F, MCVersion.v1_16_2, 0x2E, MCVersion.v1_17, 0x2F, MCVersion.v1_19, 0x2C, MCVersion.v1_19_1, 0x2E, MCVersion.v1_19_3, 0x2D),
    S2C_ADVANCEMENTS(PLAY, CLIENTBOUND, MCVersion.v1_12, 0x4C, MCVersion.v1_12_1, 0x4D, MCVersion.v1_13, 0x51, MCVersion.v1_14, 0x57, MCVersion.v1_15, 0x58, MCVersion.v1_16, 0x57, MCVersion.v1_17, 0x62, MCVersion.v1_18, 0x63, MCVersion.v1_19, 0x64, MCVersion.v1_19_1, 0x67, MCVersion.v1_19_3, 0x65),
    S2C_UPDATE_ENABLED_FEATURES(PLAY, CLIENTBOUND, MCVersion.v1_19_3, 0x67),
    S2C_SPAWN_EXPERIENCE_ORB(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x11, MCVersion.v1_9, 0x01),
    S2C_ACKNOWLEDGE_PLAYER_DIGGING(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x5C, MCVersion.v1_15, 0x08, MCVersion.v1_16, 0x07, MCVersion.v1_17, 0x08, MCVersion.v1_19, -1),
    S2C_UPDATE_HEALTH(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x06, MCVersion.v1_9, 0x3E, MCVersion.v1_12, 0x40, MCVersion.v1_12_1, 0x41, MCVersion.v1_13, 0x44, MCVersion.v1_14, 0x48, MCVersion.v1_15, 0x49, MCVersion.v1_17, 0x52, MCVersion.v1_19_1, 0x55, MCVersion.v1_19_3, 0x53),
    S2C_TEAMS(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x3E, MCVersion.v1_9, 0x41, MCVersion.v1_12, 0x43, MCVersion.v1_12_1, 0x44, MCVersion.v1_13, 0x47, MCVersion.v1_14, 0x4B, MCVersion.v1_15, 0x4C, MCVersion.v1_17, 0x55, MCVersion.v1_19_1, 0x58, MCVersion.v1_19_3, 0x56),
    S2C_PING(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x30, MCVersion.v1_19, 0x2D, MCVersion.v1_19_1, 0x2F, MCVersion.v1_19_3, 0x2E),
    S2C_TAB_COMPLETE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x3A, MCVersion.v1_9, 0x0E, MCVersion.v1_13, 0x10, MCVersion.v1_15, 0x11, MCVersion.v1_16, 0x10, MCVersion.v1_16_2, 0x0F, MCVersion.v1_17, 0x11, MCVersion.v1_19, 0x0E, MCVersion.v1_19_3, 0x0D),
    S2C_SOUND(PLAY, CLIENTBOUND, MCVersion.v1_9, 0x47, MCVersion.v1_9_3, 0x46, MCVersion.v1_12, 0x48, MCVersion.v1_12_1, 0x49, MCVersion.v1_13, 0x4D, MCVersion.v1_14, 0x51, MCVersion.v1_15, 0x52, MCVersion.v1_16, 0x51, MCVersion.v1_17, 0x5C, MCVersion.v1_18, 0x5D, MCVersion.v1_19_1, 0x60, MCVersion.v1_19_3, 0x5E),
    S2C_REMOVE_ENTITY(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x3A, MCVersion.v1_17_1, -1),
    S2C_SCOREBOARD_OBJECTIVE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x3B, MCVersion.v1_9, 0x3F, MCVersion.v1_12, 0x41, MCVersion.v1_12_1, 0x42, MCVersion.v1_13, 0x45, MCVersion.v1_14, 0x49, MCVersion.v1_15, 0x4A, MCVersion.v1_17, 0x53, MCVersion.v1_19_1, 0x56, MCVersion.v1_19_3, 0x54),
    S2C_SPAWN_PAINTING(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x10, MCVersion.v1_9, 0x04, MCVersion.v1_16, 0x03, MCVersion.v1_19, -1),
    S2C_WORLD_BORDER_CENTER(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x42, MCVersion.v1_19, 0x41, MCVersion.v1_19_1, 0x44, MCVersion.v1_19_3, 0x43),
    S2C_STATISTICS(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x37, MCVersion.v1_9, 0x07, MCVersion.v1_16, 0x06, MCVersion.v1_17, 0x07, MCVersion.v1_19, 0x04),
    S2C_TITLE_SUBTITLE(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x57, MCVersion.v1_18, 0x58, MCVersion.v1_19_1, 0x5B, MCVersion.v1_19_3, 0x59),
    S2C_UPDATE_SCORE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x3C, MCVersion.v1_9, 0x42, MCVersion.v1_12, 0x44, MCVersion.v1_12_1, 0x45, MCVersion.v1_13, 0x48, MCVersion.v1_14, 0x4C, MCVersion.v1_15, 0x4D, MCVersion.v1_17, 0x56, MCVersion.v1_19_1, 0x59, MCVersion.v1_19_3, 0x57),
    S2C_TITLE_TIMES(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x5A, MCVersion.v1_18, 0x5B, MCVersion.v1_19_1, 0x5E, MCVersion.v1_19_3, 0x5C),
    S2C_ENTITY_HEAD_LOOK(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x19, MCVersion.v1_9, 0x34, MCVersion.v1_12, 0x35, MCVersion.v1_12_1, 0x36, MCVersion.v1_13, 0x39, MCVersion.v1_14, 0x3B, MCVersion.v1_15, 0x3C, MCVersion.v1_16, 0x3B, MCVersion.v1_16_2, 0x3A, MCVersion.v1_17, 0x3E, MCVersion.v1_19, 0x3C, MCVersion.v1_19_1, 0x3F, MCVersion.v1_19_3, 0x3E),
    S2C_COOLDOWN(PLAY, CLIENTBOUND, MCVersion.v1_9, 0x17, MCVersion.v1_13, 0x18, MCVersion.v1_14, 0x17, MCVersion.v1_15, 0x18, MCVersion.v1_16, 0x17, MCVersion.v1_16_2, 0x16, MCVersion.v1_17, 0x17, MCVersion.v1_19, 0x14, MCVersion.v1_19_3, 0x13),
    S2C_SPAWN_ENTITY(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x0E, MCVersion.v1_9, 0x00),
    S2C_PLUGIN_MESSAGE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x3F, MCVersion.v1_9, 0x18, MCVersion.v1_13, 0x19, MCVersion.v1_14, 0x18, MCVersion.v1_15, 0x19, MCVersion.v1_16, 0x18, MCVersion.v1_16_2, 0x17, MCVersion.v1_17, 0x18, MCVersion.v1_19, 0x15, MCVersion.v1_19_1, 0x16, MCVersion.v1_19_3, 0x15),
    S2C_CRAFT_RECIPE_RESPONSE(PLAY, CLIENTBOUND, MCVersion.v1_12_1, 0x2B, MCVersion.v1_13, 0x2D, MCVersion.v1_14, 0x30, MCVersion.v1_15, 0x31, MCVersion.v1_16, 0x30, MCVersion.v1_16_2, 0x2F, MCVersion.v1_17, 0x31, MCVersion.v1_19, 0x2E, MCVersion.v1_19_1, 0x30, MCVersion.v1_19_3, 0x2F),
    S2C_GAME_EVENT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x2B, MCVersion.v1_9, 0x1E, MCVersion.v1_13, 0x20, MCVersion.v1_14, 0x1E, MCVersion.v1_15, 0x1F, MCVersion.v1_16, 0x1E, MCVersion.v1_16_2, 0x1D, MCVersion.v1_17, 0x1E, MCVersion.v1_19, 0x1B, MCVersion.v1_19_1, 0x1D, MCVersion.v1_19_3, 0x1C),
    S2C_TAB_LIST(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x47, MCVersion.v1_9, 0x48, MCVersion.v1_9_3, 0x47, MCVersion.v1_12, 0x49, MCVersion.v1_12_1, 0x4A, MCVersion.v1_13, 0x4E, MCVersion.v1_14, 0x53, MCVersion.v1_15, 0x54, MCVersion.v1_16, 0x53, MCVersion.v1_17, 0x5E, MCVersion.v1_18, 0x5F, MCVersion.v1_19, 0x60, MCVersion.v1_19_1, 0x63, MCVersion.v1_19_3, 0x61),
    S2C_ENTITY_SOUND(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x50, MCVersion.v1_15, 0x51, MCVersion.v1_16, 0x50, MCVersion.v1_17, 0x5B, MCVersion.v1_18, 0x5C, MCVersion.v1_19_1, 0x5F, MCVersion.v1_19_3, 0x5D),
    S2C_UPDATE_VIEW_POSITION(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x40, MCVersion.v1_15, 0x41, MCVersion.v1_16, 0x40, MCVersion.v1_17, 0x49, MCVersion.v1_19, 0x48, MCVersion.v1_19_1, 0x4B, MCVersion.v1_19_3, 0x4A),
    S2C_BOSSBAR(PLAY, CLIENTBOUND, MCVersion.v1_9, 0x0C, MCVersion.v1_15, 0x0D, MCVersion.v1_16, 0x0C, MCVersion.v1_17, 0x0D, MCVersion.v1_19, 0x0A),
    S2C_USE_BED(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x0A, MCVersion.v1_9, 0x2F, MCVersion.v1_12_1, 0x30, MCVersion.v1_13, 0x33, MCVersion.v1_14, -1),
    S2C_JOIN_GAME(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x01, MCVersion.v1_9, 0x23, MCVersion.v1_13, 0x25, MCVersion.v1_15, 0x26, MCVersion.v1_16, 0x25, MCVersion.v1_16_2, 0x24, MCVersion.v1_17, 0x26, MCVersion.v1_19, 0x23, MCVersion.v1_19_1, 0x25, MCVersion.v1_19_3, 0x24),
    S2C_ENTITY_VELOCITY(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x12, MCVersion.v1_9, 0x3B, MCVersion.v1_12, 0x3D, MCVersion.v1_12_1, 0x3E, MCVersion.v1_13, 0x41, MCVersion.v1_14, 0x45, MCVersion.v1_15, 0x46, MCVersion.v1_17, 0x4F, MCVersion.v1_19_1, 0x52, MCVersion.v1_19_3, 0x50),
    S2C_ENTITY_EQUIPMENT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x04, MCVersion.v1_9, 0x3C, MCVersion.v1_12, 0x3E, MCVersion.v1_12_1, 0x3F, MCVersion.v1_13, 0x42, MCVersion.v1_14, 0x46, MCVersion.v1_15, 0x47, MCVersion.v1_17, 0x50, MCVersion.v1_19_1, 0x53, MCVersion.v1_19_3, 0x51),
    S2C_ENTITY_POSITION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x15, MCVersion.v1_9, 0x25, MCVersion.v1_12, 0x26, MCVersion.v1_13, 0x28, MCVersion.v1_15, 0x29, MCVersion.v1_16, 0x28, MCVersion.v1_16_2, 0x27, MCVersion.v1_17, 0x29, MCVersion.v1_19, 0x26, MCVersion.v1_19_1, 0x28, MCVersion.v1_19_3, 0x27),
    S2C_UNLOCK_RECIPES(PLAY, CLIENTBOUND, MCVersion.v1_12, 0x30, MCVersion.v1_12_1, 0x31, MCVersion.v1_13, 0x34, MCVersion.v1_14, 0x36, MCVersion.v1_15, 0x37, MCVersion.v1_16, 0x36, MCVersion.v1_16_2, 0x35, MCVersion.v1_17, 0x39, MCVersion.v1_19, 0x37, MCVersion.v1_19_1, 0x3A, MCVersion.v1_19_3, 0x39),
    S2C_CHAT_PREVIEW(PLAY, CLIENTBOUND, MCVersion.v1_19, 0x0C, MCVersion.v1_19_3, -1),
    S2C_DECLARE_RECIPES(PLAY, CLIENTBOUND, MCVersion.v1_13, 0x54, MCVersion.v1_14, 0x5A, MCVersion.v1_15, 0x5B, MCVersion.v1_16, 0x5A, MCVersion.v1_17, 0x65, MCVersion.v1_18, 0x66, MCVersion.v1_19, 0x67, MCVersion.v1_19_1, 0x6A, MCVersion.v1_19_3, 0x69),
    S2C_SPAWN_PLAYER(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x0C, MCVersion.v1_9, 0x05, MCVersion.v1_16, 0x04, MCVersion.v1_19, 0x02),
    S2C_DISGUISED_CHAT(PLAY, CLIENTBOUND, MCVersion.v1_19_3, 0x18),
    S2C_UPDATE_SIGN(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x33, MCVersion.v1_9, 0x46, MCVersion.v1_9_3, -1),
    S2C_COMBAT_KILL(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x35, MCVersion.v1_19, 0x33, MCVersion.v1_19_1, 0x36, MCVersion.v1_19_3, 0x34),
    S2C_CUSTOM_CHAT_COMPLETIONS(PLAY, CLIENTBOUND, MCVersion.v1_19_1, 0x15, MCVersion.v1_19_3, 0x14),
    S2C_SET_DISPLAY_CHAT_PREVIEW(PLAY, CLIENTBOUND, MCVersion.v1_19, 0x4B, MCVersion.v1_19_1, 0x4E, MCVersion.v1_19_3, -1),
    S2C_PLAYER_ABILITIES(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x39, MCVersion.v1_9, 0x2B, MCVersion.v1_12_1, 0x2C, MCVersion.v1_13, 0x2E, MCVersion.v1_14, 0x31, MCVersion.v1_15, 0x32, MCVersion.v1_16, 0x31, MCVersion.v1_16_2, 0x30, MCVersion.v1_17, 0x32, MCVersion.v1_19, 0x2F, MCVersion.v1_19_1, 0x31, MCVersion.v1_19_3, 0x30),
    S2C_MULTI_BLOCK_CHANGE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x22, MCVersion.v1_9, 0x10, MCVersion.v1_13, 0x0F, MCVersion.v1_15, 0x10, MCVersion.v1_16, 0x0F, MCVersion.v1_16_2, 0x3B, MCVersion.v1_17, 0x3F, MCVersion.v1_19, 0x3D, MCVersion.v1_19_1, 0x40, MCVersion.v1_19_3, 0x3F),
    S2C_SELECT_ADVANCEMENTS_TAB(PLAY, CLIENTBOUND, MCVersion.v1_12, 0x36, MCVersion.v1_12_1, 0x37, MCVersion.v1_13, 0x3A, MCVersion.v1_14, 0x3C, MCVersion.v1_15, 0x3D, MCVersion.v1_16, 0x3C, MCVersion.v1_17, 0x40, MCVersion.v1_19, 0x3E, MCVersion.v1_19_1, 0x41, MCVersion.v1_19_3, 0x40),
    S2C_WORLD_BORDER_LERP_SIZE(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x43, MCVersion.v1_19, 0x42, MCVersion.v1_19_1, 0x45, MCVersion.v1_19_3, 0x44),
    S2C_SET_PASSENGERS(PLAY, CLIENTBOUND, MCVersion.v1_9, 0x40, MCVersion.v1_12, 0x42, MCVersion.v1_12_1, 0x43, MCVersion.v1_13, 0x46, MCVersion.v1_14, 0x4A, MCVersion.v1_15, 0x4B, MCVersion.v1_17, 0x54, MCVersion.v1_19_1, 0x57, MCVersion.v1_19_3, 0x55),
    S2C_WORLD_BORDER(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x44, MCVersion.v1_9, 0x35, MCVersion.v1_12, 0x37, MCVersion.v1_12_1, 0x38, MCVersion.v1_13, 0x3B, MCVersion.v1_14, 0x3D, MCVersion.v1_15, 0x3E, MCVersion.v1_16, 0x3D, MCVersion.v1_17, -1),
    S2C_UPDATE_LIGHT(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x24, MCVersion.v1_15, 0x25, MCVersion.v1_16, 0x24, MCVersion.v1_16_2, 0x23, MCVersion.v1_17, 0x25, MCVersion.v1_19, 0x22, MCVersion.v1_19_1, 0x24, MCVersion.v1_19_3, 0x23),
    S2C_PLAYER_CHAT(PLAY, CLIENTBOUND, MCVersion.v1_19, 0x30, MCVersion.v1_19_1, 0x33, MCVersion.v1_19_3, 0x31),
    S2C_REMOVE_ENTITIES(PLAY, CLIENTBOUND, MCVersion.v1_17_1, 0x3A, MCVersion.v1_19, 0x38, MCVersion.v1_19_1, 0x3B, MCVersion.v1_19_3, 0x3A),
    S2C_WINDOW_ITEMS(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x30, MCVersion.v1_9, 0x14, MCVersion.v1_13, 0x15, MCVersion.v1_14, 0x14, MCVersion.v1_15, 0x15, MCVersion.v1_16, 0x14, MCVersion.v1_16_2, 0x13, MCVersion.v1_17, 0x14, MCVersion.v1_19, 0x11, MCVersion.v1_19_3, 0x10),
    S2C_SET_EXPERIENCE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x1F, MCVersion.v1_9, 0x3D, MCVersion.v1_12, 0x3F, MCVersion.v1_12_1, 0x40, MCVersion.v1_13, 0x43, MCVersion.v1_14, 0x47, MCVersion.v1_15, 0x48, MCVersion.v1_17, 0x51, MCVersion.v1_19_1, 0x54, MCVersion.v1_19_3, 0x52),
    S2C_SET_SIMULATION_DISTANCE(PLAY, CLIENTBOUND, MCVersion.v1_18, 0x57, MCVersion.v1_19_1, 0x5A, MCVersion.v1_19_3, 0x58),
    S2C_WORLD_BORDER_SIZE(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x44, MCVersion.v1_19, 0x43, MCVersion.v1_19_1, 0x46, MCVersion.v1_19_3, 0x45),
    S2C_WORLD_BORDER_INIT(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x20, MCVersion.v1_19, 0x1D, MCVersion.v1_19_1, 0x1F, MCVersion.v1_19_3, 0x1E),
    S2C_DECLARE_COMMANDS(PLAY, CLIENTBOUND, MCVersion.v1_13, 0x11, MCVersion.v1_15, 0x12, MCVersion.v1_16, 0x11, MCVersion.v1_16_2, 0x10, MCVersion.v1_17, 0x12, MCVersion.v1_19, 0x0F, MCVersion.v1_19_3, 0x0E),
    S2C_MAP_DATA(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x34, MCVersion.v1_9, 0x24, MCVersion.v1_13, 0x26, MCVersion.v1_15, 0x27, MCVersion.v1_16, 0x26, MCVersion.v1_16_2, 0x25, MCVersion.v1_17, 0x27, MCVersion.v1_19, 0x24, MCVersion.v1_19_1, 0x26, MCVersion.v1_19_3, 0x25),
    S2C_ENTITY_PROPERTIES(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x20, MCVersion.v1_9, 0x4B, MCVersion.v1_9_3, 0x4A, MCVersion.v1_12, 0x4D, MCVersion.v1_12_1, 0x4E, MCVersion.v1_13, 0x52, MCVersion.v1_14, 0x58, MCVersion.v1_15, 0x59, MCVersion.v1_16, 0x58, MCVersion.v1_17, 0x63, MCVersion.v1_18, 0x64, MCVersion.v1_19, 0x65, MCVersion.v1_19_1, 0x68, MCVersion.v1_19_3, 0x66),
    S2C_VEHICLE_MOVE(PLAY, CLIENTBOUND, MCVersion.v1_9, 0x29, MCVersion.v1_13, 0x2B, MCVersion.v1_14, 0x2C, MCVersion.v1_15, 0x2D, MCVersion.v1_16, 0x2C, MCVersion.v1_16_2, 0x2B, MCVersion.v1_17, 0x2C, MCVersion.v1_19, 0x29, MCVersion.v1_19_1, 0x2B, MCVersion.v1_19_3, 0x2A),
    S2C_CLOSE_WINDOW(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x2E, MCVersion.v1_9, 0x12, MCVersion.v1_13, 0x13, MCVersion.v1_15, 0x14, MCVersion.v1_16, 0x13, MCVersion.v1_16_2, 0x12, MCVersion.v1_17, 0x13, MCVersion.v1_19, 0x10, MCVersion.v1_19_3, 0x0F),
    S2C_TRADE_LIST(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x27, MCVersion.v1_15, 0x28, MCVersion.v1_16, 0x27, MCVersion.v1_16_2, 0x26, MCVersion.v1_17, 0x28, MCVersion.v1_19, 0x25, MCVersion.v1_19_1, 0x27, MCVersion.v1_19_3, 0x26),
    S2C_PLAYER_INFO_REMOVE(PLAY, CLIENTBOUND, MCVersion.v1_19_3, 0x35),
    S2C_ENTITY_ROTATION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x16, MCVersion.v1_9, 0x27, MCVersion.v1_12, 0x28, MCVersion.v1_13, 0x2A, MCVersion.v1_15, 0x2B, MCVersion.v1_16, 0x2A, MCVersion.v1_16_2, 0x29, MCVersion.v1_17, 0x2B, MCVersion.v1_19, 0x28, MCVersion.v1_19_1, 0x2A, MCVersion.v1_19_3, 0x29),
    S2C_CLEAR_TITLES(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x10, MCVersion.v1_19, 0x0D, MCVersion.v1_19_3, 0x0C),
    S2C_OPEN_HORSE_WINDOW(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x1F, MCVersion.v1_15, 0x20, MCVersion.v1_16, 0x1F, MCVersion.v1_16_2, 0x1E, MCVersion.v1_17, 0x1F, MCVersion.v1_19, 0x1C, MCVersion.v1_19_1, 0x1E, MCVersion.v1_19_3, 0x1D),
    S2C_PLAYER_INFO_UPDATE(PLAY, CLIENTBOUND, MCVersion.v1_19_3, 0x36),
    S2C_COMBAT_END(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x33, MCVersion.v1_19, 0x31, MCVersion.v1_19_1, 0x34, MCVersion.v1_19_3, 0x32),
    S2C_BLOCK_CHANGE(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x23, MCVersion.v1_9, 0x0B, MCVersion.v1_15, 0x0C, MCVersion.v1_16, 0x0B, MCVersion.v1_17, 0x0C, MCVersion.v1_19, 0x09),
    S2C_UNLOAD_CHUNK(PLAY, CLIENTBOUND, MCVersion.v1_9, 0x1D, MCVersion.v1_13, 0x1F, MCVersion.v1_14, 0x1D, MCVersion.v1_15, 0x1E, MCVersion.v1_16, 0x1D, MCVersion.v1_16_2, 0x1C, MCVersion.v1_17, 0x1D, MCVersion.v1_19, 0x1A, MCVersion.v1_19_1, 0x1C, MCVersion.v1_19_3, 0x1B),
    S2C_WINDOW_CONFIRMATION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x32, MCVersion.v1_9, 0x11, MCVersion.v1_13, 0x12, MCVersion.v1_15, 0x13, MCVersion.v1_16, 0x12, MCVersion.v1_16_2, 0x11, MCVersion.v1_17, -1),
    S2C_NAMED_SOUND(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x29, MCVersion.v1_9, 0x19, MCVersion.v1_13, 0x1A, MCVersion.v1_14, 0x19, MCVersion.v1_15, 0x1A, MCVersion.v1_16, 0x19, MCVersion.v1_16_2, 0x18, MCVersion.v1_17, 0x19, MCVersion.v1_19, 0x16, MCVersion.v1_19_1, 0x17, MCVersion.v1_19_3, -1),
    S2C_NBT_QUERY(PLAY, CLIENTBOUND, MCVersion.v1_13, 0x1D, MCVersion.v1_14, 0x54, MCVersion.v1_15, 0x55, MCVersion.v1_16, 0x54, MCVersion.v1_17, 0x5F, MCVersion.v1_18, 0x60, MCVersion.v1_19, 0x61, MCVersion.v1_19_1, 0x64, MCVersion.v1_19_3, 0x62),
    S2C_SYSTEM_CHAT(PLAY, CLIENTBOUND, MCVersion.v1_19, 0x5F, MCVersion.v1_19_1, 0x62, MCVersion.v1_19_3, 0x60),
    S2C_CHUNK_DATA(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x21, MCVersion.v1_9, 0x20, MCVersion.v1_13, 0x22, MCVersion.v1_14, 0x21, MCVersion.v1_15, 0x22, MCVersion.v1_16, 0x21, MCVersion.v1_16_2, 0x20, MCVersion.v1_17, 0x22, MCVersion.v1_19, 0x1F, MCVersion.v1_19_1, 0x21, MCVersion.v1_19_3, 0x20),
    S2C_ENTITY_TELEPORT(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x18, MCVersion.v1_9, 0x4A, MCVersion.v1_9_3, 0x49, MCVersion.v1_12, 0x4B, MCVersion.v1_12_1, 0x4C, MCVersion.v1_13, 0x50, MCVersion.v1_14, 0x56, MCVersion.v1_15, 0x57, MCVersion.v1_16, 0x56, MCVersion.v1_17, 0x61, MCVersion.v1_18, 0x62, MCVersion.v1_19, 0x63, MCVersion.v1_19_1, 0x66, MCVersion.v1_19_3, 0x64),
    S2C_ADD_VIBRATION_SIGNAL(PLAY, CLIENTBOUND, MCVersion.v1_17, 0x05, MCVersion.v1_19, -1),
    S2C_UPDATE_VIEW_DISTANCE(PLAY, CLIENTBOUND, MCVersion.v1_14, 0x41, MCVersion.v1_15, 0x42, MCVersion.v1_16, 0x41, MCVersion.v1_17, 0x4A, MCVersion.v1_19, 0x49, MCVersion.v1_19_1, 0x4C, MCVersion.v1_19_3, 0x4B),
    S2C_STOP_SOUND(PLAY, CLIENTBOUND, MCVersion.v1_13, 0x4C, MCVersion.v1_14, 0x52, MCVersion.v1_15, 0x53, MCVersion.v1_16, 0x52, MCVersion.v1_17, 0x5D, MCVersion.v1_18, 0x5E, MCVersion.v1_19_1, 0x61, MCVersion.v1_19_3, 0x5F),
    S2C_TITLE(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x45, MCVersion.v1_12, 0x47, MCVersion.v1_12_1, 0x48, MCVersion.v1_13, 0x4B, MCVersion.v1_14, 0x4F, MCVersion.v1_15, 0x50, MCVersion.v1_16, 0x4F, MCVersion.v1_17, -1),
    S2C_COMBAT_EVENT(PLAY, CLIENTBOUND, MCVersion.v1_8, 0x42, MCVersion.v1_9, 0x2C, MCVersion.v1_12_1, 0x2D, MCVersion.v1_13, 0x2F, MCVersion.v1_14, 0x32, MCVersion.v1_15, 0x33, MCVersion.v1_16, 0x32, MCVersion.v1_16_2, 0x31, MCVersion.v1_17, -1),
    S2C_MAP_BULK_CHUNK(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x26, MCVersion.v1_9, -1),
    S2C_DELETE_CHAT_MESSAGE(PLAY, CLIENTBOUND, MCVersion.v1_19_1, 0x18, MCVersion.v1_19_3, 0x16),
    S2C_SERVER_DATA(PLAY, CLIENTBOUND, MCVersion.v1_19, 0x3F, MCVersion.v1_19_1, 0x42, MCVersion.v1_19_3, 0x41),
    S2C_COLLECT_ITEM(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x0D, MCVersion.v1_9, 0x49, MCVersion.v1_9_3, 0x48, MCVersion.v1_12, 0x4A, MCVersion.v1_12_1, 0x4B, MCVersion.v1_13, 0x4F, MCVersion.v1_14, 0x55, MCVersion.v1_15, 0x56, MCVersion.v1_16, 0x55, MCVersion.v1_17, 0x60, MCVersion.v1_18, 0x61, MCVersion.v1_19, 0x62, MCVersion.v1_19_1, 0x65, MCVersion.v1_19_3, 0x63),
    S2C_PLAYER_POSITION(PLAY, CLIENTBOUND, MCVersion.v1_7_2, 0x08, MCVersion.v1_9, 0x2E, MCVersion.v1_12_1, 0x2F, MCVersion.v1_13, 0x32, MCVersion.v1_14, 0x35, MCVersion.v1_15, 0x36, MCVersion.v1_16, 0x35, MCVersion.v1_16_2, 0x34, MCVersion.v1_17, 0x38, MCVersion.v1_19, 0x36, MCVersion.v1_19_1, 0x39, MCVersion.v1_19_3, 0x38),
    ;


    private final ConnectionState state;
    private final PacketDirection direction;
    private final Map<Integer, Integer> packetIds;
    private final Integer staticId;

    MCPackets(final ConnectionState state, final PacketDirection direction, final int... packetIds) {
        if (packetIds.length == 1) {
            this.staticId = packetIds[0];
        } else {
            this.staticId = null;
            if (packetIds.length % 2 != 0) throw new IllegalStateException("Packet id count has to be a multiple of 2");
        }

        this.state = state;
        this.direction = direction;
        this.packetIds = new HashMap<>();

        if (packetIds.length != 1) this.putVersions(packetIds);
    }

    public ConnectionState getState() {
        return this.state;
    }

    public PacketDirection getDirection() {
        return this.direction;
    }

    public int getId(final int protocolVersion) {
        if (this.staticId != null) return this.staticId;
        return this.packetIds.getOrDefault(protocolVersion, -1);
    }

    private void putVersions(final int[] packetIds) {
        Map<Integer, Integer> ids = new HashMap<>();
        for (int i = 0; i < packetIds.length; i += 2) ids.put(packetIds[i], packetIds[i + 1]);
        int lastId = -1;
        for (Integer version : MCVersion.ALL_VERSIONS.keySet()) {
            Integer packetId = ids.get(version);
            if (packetId != null) lastId = packetId;
            this.packetIds.put(version, lastId);
        }
    }

    public static MCPackets getPacket(final ConnectionState state, final PacketDirection direction, final int protocolVersion, final int packetId) {
        for (MCPackets packet : MCPackets.values()) {
            if (packet.getState() != state) continue;
            if (packet.getDirection() != direction) continue;
            if (packet.getId(protocolVersion) == packetId) return packet;
        }
        return null;
    }

}
