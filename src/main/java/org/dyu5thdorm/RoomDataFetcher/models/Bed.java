package org.dyu5thdorm.RoomDataFetcher.models;

import java.util.Objects;

public record Bed(String bedId, Student student, String dataTime) {
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + bedId + '\'' +
                ", student=" + student +
                ", dataTime='" + dataTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bed room = (Bed) o;

        if (!Objects.equals(bedId, room.bedId)) return false;
        if (!Objects.equals(student, room.student)) return false;
        return Objects.equals(dataTime, room.dataTime);
    }
}
