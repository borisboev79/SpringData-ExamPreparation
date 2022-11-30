package softuni.exam.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public LocalDateTime unmarshal(String date) throws Exception {

        return LocalDateTime.parse(date, dateFormat);
    }

    @Override
    public String marshal(LocalDateTime date) throws Exception {
        return date.format(dateFormat);
    }
}
