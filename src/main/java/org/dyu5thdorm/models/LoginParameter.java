package org.dyu5thdorm.models;

public record LoginParameter(String id, String password, String s_smye, String s_smty) {
    @Override
    public String toString() {
        return "LoginParameter{" +
                "s_smye='" + s_smye + '\'' +
                ", s_smty='" + s_smty + '\'' +
                '}';
    }
}
