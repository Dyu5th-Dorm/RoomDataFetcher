package org.dyu5thdorm.models;

public record Student(String roomTag, String studentID, String name) {
    @Override
    public String toString() {
        return "Student{" +
                "roomTag='" + roomTag + '\'' +
                ", studentID='" + studentID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
