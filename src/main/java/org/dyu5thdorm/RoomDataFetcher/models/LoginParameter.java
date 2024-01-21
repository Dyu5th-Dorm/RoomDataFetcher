package org.dyu5thdorm.RoomDataFetcher.models;

/**
 * DataFetchingParameter.
 * @param id id.
 * @param password password.
 * @param s_smye semester year.
 * @param s_smty semester.
 */
public record LoginParameter(String id, String password, String s_smye, String s_smty) {}
