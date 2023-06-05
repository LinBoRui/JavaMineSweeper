package tw.edu.ncku.csie.javaminesweeper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RankItem implements Serializable {
    private int time;
    private String date;
    private String timeString;

    public RankItem(int time) {
        this.time = time;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        date = dtf.format(LocalDateTime.now());

        int sec = time % 60;
        int min = (time / 60) % 60;
        int hour = time / 3600;
        timeString = sec + "s";
        if (min > 0) {
            timeString = min + "m" + timeString;
        }
        if (hour > 0) {
            timeString = hour + "h" + timeString;
        }
    }

    public int getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getTimeString() {
        return timeString;
    }
}
