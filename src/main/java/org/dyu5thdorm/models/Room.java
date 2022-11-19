package org.dyu5thdorm.models;

public record Room(String roomTag, Student student) {
    @Override
    public String toString() {
        return "Room{" +
                "roomTag='" + roomTag + '\'' +
                ", student=" + student +
                '}';
    }
}
