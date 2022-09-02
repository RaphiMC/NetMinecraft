package net.raphimc.netminecraft.constants;

public enum ConnectionState {

    HANDSHAKING(-1),
    PLAY(0),
    STATUS(1),
    LOGIN(2);

    private final int id;

    ConnectionState(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }


    public static ConnectionState getById(final int id) {
        for (ConnectionState state : ConnectionState.values()) {
            if (state.getId() == id) return state;
        }
        return null;
    }

}
