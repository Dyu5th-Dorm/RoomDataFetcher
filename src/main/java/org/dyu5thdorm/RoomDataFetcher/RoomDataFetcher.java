package org.dyu5thdorm.RoomDataFetcher;

import org.dyu5thdorm.RoomDataFetcher.models.LoginParameter;
import org.dyu5thdorm.RoomDataFetcher.models.Room;
import org.dyu5thdorm.RoomDataFetcher.models.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.dyu5thdorm.RoomDataFetcher.RoomDataField.*;

/**
 * A web crawler library for obtaining Dormitory of DaYeh University room and student information.
 */
public final class RoomDataFetcher {
    private static String cookie;
    private static final String LOGIN_LINK = "http://163.23.1.52/dorm_muster/chk_login.php";
    private static final String ROOM_DATA_LINK = "http://163.23.1.52/dorm_muster/view_free_bad.php";
    private static LocalDateTime loginTime;

    /**
     * To obtain diligent dormitory room and student information.
     * @param param Login parameter and semester to be fetched.
     * @return Diligent Dormitory room information (contains student information).
     * @throws IOException Login failed.
     */
    public static List<Room> getData(LoginParameter param) throws IOException {
        return getData(param, Dormitory.DILIGENT);
    }

    /**
     * Same with <b>getData(DataFetchingParameter d)</b>, but this method can
     * specify the desired dormitory information.
     * @param param Login parameter and semester to be fetched.
     * @param dormitory The dormitory information to be fetched.
     * @return Specify Dormitory room information (contains student information).
     * @throws IOException Login Failed.
     */
    public static List<Room> getData(LoginParameter param, Dormitory dormitory) throws IOException {
        Document document;
        try {
            document = getAllRoomsData(param);
        } catch (IOException e) {
            // If login fails, rethrow the exception to indicate a failure
            throw new IOException("Failed to login or fetch data", e);
        }

        Elements tdField = document.getElementsByTag("td");
        if (tdField.isEmpty()) {
            // Handle the case where no data is returned
            throw new IOException("No data returned from the server");
        }

        return roomDataGenerator(tdField, dormitory);
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
     *
     * @param param Login parameter and semester to be fetched.
     * @return All dormitories data.
     * @throws IOException Login failed.
     */
    private static Document getAllRoomsData(LoginParameter param) throws IOException {
        if (loginTime == null || Duration.between(loginTime, LocalDateTime.now()).toMinutes() >= 30) {
            login(param.id(), param.password());
            loginTime = LocalDateTime.now();
        }

        return Jsoup.connect(ROOM_DATA_LINK)
                .header("Cookie", cookie)
                .data("s_smye", param.s_smye())
                .data("s_smty", param.s_smty())
                .post();
    }

    /**
     * Same with <b>roomDataGenerator(Elements tdField)</b>, but this method can
     * specify the desired dormitory information.
     * @param tdField Html element.
     * @param dormitory The dormitory information to be fetched.
     * @return Filtered data.
     */
    private static List<Room> roomDataGenerator(Elements tdField, Dormitory dormitory) {
        List<Room> rooms = new ArrayList<>();
        for (int i = ROOM_TAG_INDEX.getValue(); i < tdField.size(); i += STEP_SIZE.getValue()) {
            Element element = tdField.get(i);
            String roomTag = element.text();
            if (roomTag.isEmpty() || roomTag.charAt(0) != dormitory.getId()) continue;

            Optional<Student> student = createStudentIfNotEmpty(tdField, i);

            String changeTime = tdField.get(i + TIME_OFFSET.getValue()).text();
            Room room = student.map(
                    value -> new Room(roomTag, value, changeTime)
            ).orElseGet(
                    () -> new Room(roomTag, null, changeTime)
            );

            rooms.add(room);
        }
        return rooms;
    }

    private static Optional<Student> createStudentIfNotEmpty(Elements tdField, int index) {
        String studentID = tdField.get(index + STUDENT_ID_OFFSET.getValue()).text();
        String name = tdField.get(index + NAME_OFFSET.getValue()).text();

        if (studentID.isEmpty() || name.isEmpty()) {
            return Optional.empty();
        }

        String major = tdField.get(index + MAJOR_OFFSET.getValue()).text();
        String sex = tdField.get(index + SEX_OFFSET.getValue()).text().equals("1") ? "M" : "F";
        String citizenship = tdField.get(index + CITIZENSHIP_OFFSET.getValue()).text();

        return Optional.of(new Student(studentID, name, sex, major, citizenship));
    }
}
