package org.dyu5thdorm;

import org.dyu5thdorm.models.LoginParameter;
import org.dyu5thdorm.models.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomDataFetcher {
    private static final String cookie;
    private static final String loginLink;
    private static final String roomDataLink;
    private static boolean loginStatus;
    private static List<Student> studentsData;

    static {
        cookie = "PHPSESSID=ahpm4amhmd69e7vasddo11mbf0";
        loginLink = "http://163.23.1.52/dorm_muster/chk_login.php";
        roomDataLink = "http://163.23.1.52/dorm_muster/view_free_bad.php";
        studentsData = new ArrayList<>();
    }

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

    private static Document getRoomData(LoginParameter l) throws IOException {
        if (!loginStatus) login(l.id(), l.password());


        return Jsoup.connect(roomDataLink)
                .header("Cookie", cookie)
                .data("s_smye", l.s_smye())
                .data("s_smty", l.s_smty())
                .post();
    }

    public static List<Student> getStudentData(LoginParameter l) throws IOException {
        Document document = getRoomData(l);
        Elements tdField;

        tdField = document.getElementsByTag("td");

        for (int i = 11; i < tdField.size(); i+= 10) {
            String roomTag = tdField.get(i).text();
            if (roomTag.charAt(0) != '5') continue; // Diligence dorm filter

            String studentID = tdField.get(i+3).text();
            String name = tdField.get(i+4).text();

            if (studentID.isEmpty() || name.isEmpty()) continue; // check if is empty room

            Student student = new Student(roomTag, studentID, name);

            if (!studentsData.contains(student)) {
                studentsData.add(student);
            }
        }

        return studentsData;
    }
}
