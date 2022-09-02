package net.raphimc.netminecraft.util;

import java.util.UUID;

public class UUIDAdapter {

    public static String fromUUID(final UUID value) {
        return value.toString().replace("-", "");
    }

    public static UUID fromString(final String input) {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

}
