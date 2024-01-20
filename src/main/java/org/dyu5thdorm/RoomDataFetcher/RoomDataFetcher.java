package org.dyu5thdorm.RoomDataFetcher;

import org.dyu5thdorm.RoomDataFetcher.models.Dormitory;
import org.dyu5thdorm.RoomDataFetcher.models.DataFetchingParameter;
import org.dyu5thdorm.RoomDataFetcher.models.Room;
import org.dyu5thdorm.RoomDataFetcher.models.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A web crawler library for obtaining Dormitory of DaYeh University room and student information.
 */
public final class RoomDataFetcher {
    private static String cookie;
    private static final String LOGIN_LINK = "http://163.23.1.52/dorm_muster/chk_login.php";
    private static final String ROOM_DATA_LINK = "http://163.23.1.52/dorm_muster/view_free_bad.php";
    private static LocalDateTime loginTime = LocalDateTime.now();


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
        Document document;
        try {
            document = getAllRoomsData(d);
        } catch (IOException e) {
            // If login fails, rethrow the exception to indicate a failure
            throw new IOException("Failed to login or fetch data", e);
        }

        Elements tdField = document.getElementsByTag("td");
        if (tdField.isEmpty()) {
            // Handle the case where no data is returned
            throw new IOException("No data returned from the server");
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
        // Generate a new session cookie for each login attempt
        cookie = "PHPSESSID=" + UUID.randomUUID();
        Document loginResponse = Jsoup.connect(LOGIN_LINK)
                .header("Cookie", cookie)
                .data("login_id", id)
                .data("login_passwd", password)
                .post();


         if (loginResponse.text().length() > 9) {
             throw new IOException("Login failed");
         }

        loginTime = LocalDateTime.now();
    }

    /**
     * Get all dormitories data.
     * @param d Login parameter and semester to be fetched.
     * @return All dormitories data.
     * @throws IOException Login failed.
     */
    private static Document getAllRoomsData(DataFetchingParameter d) throws IOException {
        LocalDateTime currentTime = LocalDateTime.now();
        long differenceInMinutes = Duration.between(loginTime, currentTime).toMinutes();
        if (differenceInMinutes >= 30) login(d.id(), d.password());

        return Jsoup.connect(ROOM_DATA_LINK)
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
