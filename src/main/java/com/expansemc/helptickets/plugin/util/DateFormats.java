package com.expansemc.helptickets.plugin.util;

import java.time.format.DateTimeFormatter;

public final class DateFormats {

    public static final DateTimeFormatter PRIMARY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");

    private DateFormats() {
    }
}