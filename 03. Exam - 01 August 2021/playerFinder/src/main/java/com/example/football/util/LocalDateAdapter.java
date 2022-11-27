package com.example.football.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @Override
    public LocalDate unmarshal(String date) throws Exception {

        return LocalDate.parse(date, dateFormat);
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return date.format(dateFormat);
    }
}
