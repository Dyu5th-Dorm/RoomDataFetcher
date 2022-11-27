package org.dyu5thdorm.models;

import java.util.Objects;

public record Room(String roomTag, Student student) {

    @Override
    public String toString() {
        return "Room{" +
                "roomTag='" + roomTag + '\'' +
                ", student=" + student +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Room) obj;
        return Objects.equals(this.roomTag, that.roomTag) &&
                Objects.equals(this.student, that.student);
    }

}
