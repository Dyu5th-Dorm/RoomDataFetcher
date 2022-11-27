package org.dyu5thdorm.models;

import java.util.Objects;

/**
 * Student Models
 * @param studentId Student id.
 * @param name Student name.
 * @param sex Sex of student(M or F).
 * @param major Major of student.
 * @param citizenship Citizenship of student.
 */
public record Student(String studentId, String name, String sex, String major, String citizenship) {

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Student) obj;
        return Objects.equals(this.studentId, that.studentId) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.sex, that.sex) &&
                Objects.equals(this.major, that.major) &&
                Objects.equals(this.citizenship, that.citizenship);
    }

}