package org.dyu5thdorm.models;

public record Student(String studentId,
                      String name,
                      String sex,
                      String major,
                      String citizenship) {
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", major='" + major + '\'' +
                ", citizenship='" + citizenship + '\'' +
                '}';
    }
}