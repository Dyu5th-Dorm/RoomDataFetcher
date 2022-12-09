package org.dyu5thdorm.RoomDataFetcher.models;

import java.util.Objects;

public record Room(String roomId, Student student) {

    @Override
    public String toString() {
        return "Room{" +
                "roomTag='" + roomId + '\'' +
                ", student=" + student +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Room) obj;
        return Objects.equals(this.roomId, that.roomId) &&
                Objects.equals(this.student, that.student);
    }

}
