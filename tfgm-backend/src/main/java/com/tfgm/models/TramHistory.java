package com.tfgm.models;

import java.util.UUID;

public record TramHistory(
    UUID tramHistoryId,
    UUID tramId,
    String origin,
    String destination,
    long timeAtOrigin,
    long timeAtDestination,
    String status) {
}
