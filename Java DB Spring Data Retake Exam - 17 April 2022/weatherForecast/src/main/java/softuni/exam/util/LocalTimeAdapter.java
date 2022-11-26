package softuni.exam.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Override
    public LocalTime unmarshal(String time) throws Exception {

        return LocalTime.parse(time, timeFormat);
    }

    @Override
    public String marshal(LocalTime time) throws Exception {
        return time.format(timeFormat);
    }
}
