package org.dyu5thdorm.RoomDataFetcher.models;

import java.util.Objects;

public record Room(String roomId, Student student, String dataTime) {
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", student=" + student +
                ", dataTime='" + dataTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (!Objects.equals(roomId, room.roomId)) return false;
        if (!Objects.equals(student, room.student)) return false;
        return Objects.equals(dataTime, room.dataTime);
    }
}
