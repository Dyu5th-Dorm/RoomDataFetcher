package org.dyu5thdorm.RoomDataFetcher;

import org.dyu5thdorm.RoomDataFetcher.models.Dormitory;
import org.dyu5thdorm.RoomDataFetcher.models.DataFetchingParameter;
import org.dyu5thdorm.RoomDataFetcher.models.Room;
import org.dyu5thdorm.RoomDataFetcher.models.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * A web crawler library for obtaining Dormitory of DaYeh University room and student information.
 */
public final class RoomDataFetcher {
    private static final String cookie;
    private static final String loginLink;
    private static final String roomDataLink;
    private static boolean loginStatus;

    static {
        cookie = "PHPSESSID=ahpm4amhmd69e7vasddo11mbf0";
        loginLink = "http://163.23.1.52/dorm_muster/chk_login.php";
        roomDataLink = "http://163.23.1.52/dorm_muster/view_free_bad.php";
    }

    /**
     * To obtain diligent dormitory room and student information.
     * @param d Login parameter and semester to be fetched.
     * @return Diligent Dormitory room information (contains student information).
     * @throws IOException Login failed.
     */
    public static List<Room> getData(DataFetchingParameter d) throws IOException {
        return getData(d, Dormitory.DILIGENT);
    }

    /**
     * Same with <b>getData(DataFetchingParameter d)</b>, but this method can
     * specify the desired dormitory information.
     * @param d Login parameter and semester to be fetched.
     * @param dormId The dormitory information to be fetched.
     * @return Specify Dormitory room information (contains student information).
     * @throws IOException Login Failed.
     */
    public static List<Room> getData(DataFetchingParameter d, char dormId) throws IOException {
        Document document = getAllRoomsData(d);
        Elements tdField = document.getElementsByTag("td");

        if (tdField.isEmpty()) {
            loginStatus = false;
            document = getAllRoomsData(d);
            tdField = document.getElementsByTag("td");
        }

        return roomDataGenerator(tdField, dormId);
    }

    /**
     * To login to dormitory data web.
     * @param id id of data web.
     * @param password id of data web.
     * @throws IOException Login failed.
     */
    private static void login(String id, String password) throws IOException {
        Jsoup.connect(loginLink)
                .header("Cookie", cookie)
                .data("login_id", id)
                .data("login_passwd", password)
                .post();

        if (!loginStatus) {
            loginStatus = true;
        }
    }

    /**
     * Get all dormitories data.
     * @param d Login parameter and semester to be fetched.
     * @return All dormitories data.
     * @throws IOException Login failed.
     */
    private static Document getAllRoomsData(DataFetchingParameter d) throws IOException {
        if (!loginStatus) login(d.id(), d.password());

        return Jsoup.connect(roomDataLink)
                .header("Cookie", cookie)
                .data("s_smye", d.s_smye())
                .data("s_smty", d.s_smty())
                .post();
    }

    /**
     * Same with <b>roomDataGenerator(Elements tdField)</b>, but this method can
     * specify the desired dormitory information.
     * @param tdField Html element.
     * @param dormId The dormitory information to be fetched.
     * @return Filtered data.
     */
    private static List<Room> roomDataGenerator(Elements tdField, char dormId) {
        List<Room> rooms = new ArrayList<>();

        for (int i = 11; i < tdField.size(); i+= 10) {
            String roomTag = tdField.get(i).text();

            if (roomTag.charAt(0) != dormId) continue;

            String major = tdField.get(i+2).text();
            String studentID = tdField.get(i+3).text();
            String name = tdField.get(i+4).text();
            String sex = tdField.get(i+5).text().equals("1") ? "M" : "F";
            String citizenship = tdField.get(i+6).text();
            String changeTime = tdField.get(i+9).text();

            Room room;

            if (studentID.isEmpty() || name.isEmpty()) { // empty room
                room = new Room(roomTag, null, changeTime);
                rooms.add(room);
                continue;
            }

            room = new Room(
                    roomTag,
                    new Student(studentID, name, sex, major, citizenship),
                    changeTime
            );

            rooms.add(room);
        }

        return rooms;
    }
}
