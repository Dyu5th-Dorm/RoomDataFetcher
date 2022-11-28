package org.dyu5thdorm.RoomDataFetcher.models;

import java.util.Objects;

/**
 * DataFetchingParameter.
 * @param id id.
 * @param password password.
 * @param s_smye semester year.
 * @param s_smty semester.
 */
public record DataFetchingParameter(String id, String password, String s_smye, String s_smty) {

    @Override
    public String toString() {
        return "DataFetchingParameter{" +
                "s_smye='" + s_smye + '\'' +
                ", s_smty='" + s_smty + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DataFetchingParameter) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.s_smye, that.s_smye) &&
                Objects.equals(this.s_smty, that.s_smty);
    }

}
