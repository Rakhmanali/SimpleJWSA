package org.wsa.sjwsa.services;

import org.wsa.sjwsa.EncodingType;
import org.wsa.sjwsa.PgsqlDbType;
import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertingServiceTest {
    ConvertingService convertingService = new ConvertingService();

    // https://www.dariawan.com/tutorials/java/java-datetimeformatter-tutorial-examples/

    // PgsqlDbType.Time

    @Test
    public void localTime_00_00_00_To_DbTimeString() {
        LocalTime localTime = LocalTime.of(0, 0, 0, 0);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Time, false, localTime, EncodingType.NONE);

        Assert.assertEquals("00:00:00", objects[0]);
    }

    @Test
    public void localTime_00_00_00_456_To_DbTimeString() {
        LocalTime localTime = LocalTime.of(0, 0, 0, 456000000);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Time, false, localTime, EncodingType.NONE);

        Assert.assertEquals("00:00:00.456", objects[0]);
    }

    @Test
    public void localTime_02_03_04_To_DbTimeString() {
        LocalTime localTime = LocalTime.of(2, 3, 4, 0);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Time, false, localTime, EncodingType.NONE);

        Assert.assertEquals("02:03:04", objects[0]);
    }

    @Test
    public void localTime_23_59_59_To_DbTimeString() {
        LocalTime localTime = LocalTime.of(23, 59, 59, 0);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Time, false, localTime, EncodingType.NONE);

        Assert.assertEquals("23:59:59", objects[0]);
    }

    @Test
    public void localTime_23_59_59_456_To_DbTimeString() {
        LocalTime localTime = LocalTime.of(23, 59, 59, 456000000);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Time, false, localTime, EncodingType.NONE);

        Assert.assertEquals("23:59:59.456", objects[0]);
    }

    // PgsqlDbType.TimeTZ

    @Test
    public void offsetTime_WithTimeAndOffset_To_DbTimeTzString()
    {
        OffsetTime offsetTime = OffsetTime.of(23, 59,59,0, ZoneOffset.of("+2"));
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.TimeTZ, false, offsetTime, EncodingType.NONE);

        Assert.assertEquals("23:59:59+02:00", objects[0]);
    }

    @Test
    public void offsetTime_WithTimeAndMsAndOffset_To_DbTimeTzString()
    {
        OffsetTime offsetTime = OffsetTime.of(23, 59,59,678000000, ZoneOffset.of("+02:32"));
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.TimeTZ, false, offsetTime, EncodingType.NONE);

        Assert.assertEquals("23:59:59.678+02:32", objects[0]);
    }

    // PgsqlDbType.Timestamp

    @Test
    public void localDateTime_Without_NanoOfSecond_To_DbTimestampString()
    {
        LocalDateTime dateTime = LocalDateTime.of(2021, 8, 26, 23, 51, 35);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Timestamp, false, dateTime, EncodingType.NONE);

        Assert.assertEquals("2021-08-26 23:51:35", objects[0]);
    }

    @Test
    public void localDateTime_With_NanoOfSecond_To_DbTimestampString()
    {
        LocalDateTime dateTime = LocalDateTime.of(2021, 8, 26, 23, 51, 35, 456000000);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Timestamp, false, dateTime, EncodingType.NONE);

        Assert.assertEquals("2021-08-26 23:51:35.456", objects[0]);
    }

    @Test
    public void localDateTimeArray_Without_NanoOfSecond_To_DbTimestampArray()
    {
        List<LocalDateTime> localDateTimeList=new ArrayList<>();

        LocalDateTime dateTime = LocalDateTime.of(2021, 8, 26, 23, 51, 35);
        localDateTimeList.add(dateTime);

        LocalDateTime dateTime1 = LocalDateTime.of(2020, 7, 25, 22, 50, 34);
        localDateTimeList.add(dateTime1);

        LocalDateTime dateTime2 = LocalDateTime.of(2019, 6, 24, 21, 49, 33);
        localDateTimeList.add(dateTime2);

        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Timestamp, true, localDateTimeList.toArray(), EncodingType.NONE);

        Object[] expecteds = Arrays.asList("2021-08-26 23:51:35", "2020-07-25 22:50:34", "2019-06-24 21:49:33").toArray();
        Assert.assertArrayEquals(expecteds, objects);
    }

    // PgsqlDbType.TimestampTZ

    @Test
    public void offsetDateTime_Without_NanoOfSecond_To_DbTimestampTzString()
    {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2021, 8, 26, 18, 51, 35, 0, ZoneOffset.of("+5"));
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.TimestampTZ, false, offsetDateTime, EncodingType.NONE);

        Assert.assertEquals("2021-08-26 18:51:35+05:00", objects[0]);
    }

    @Test
    public void offsetDateTime_With_NanoOfSecond_To_DbTimestampTzString()
    {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2021, 8, 26, 18, 51, 35, 456000000, ZoneOffset.of("+5"));
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.TimestampTZ, false, offsetDateTime, EncodingType.NONE);

        Assert.assertEquals("2021-08-26 18:51:35.456+05:00", objects[0]);
    }

    @Test
    public void offsetDateTimeArray_With_NanoOfSecond_To_DbTimestampTZArray()
    {
        List<OffsetDateTime> offsetDateTimeList=new ArrayList<>();

        OffsetDateTime offsetDateTime = OffsetDateTime.of(2021, 8, 26, 23, 51, 35,456000000, ZoneOffset.of("+5"));
        offsetDateTimeList.add(offsetDateTime);

        OffsetDateTime offsetDateTime1 = OffsetDateTime.of(2020, 7, 25, 22, 50, 34,455000000, ZoneOffset.of("+4"));
        offsetDateTimeList.add(offsetDateTime1);

        OffsetDateTime offsetDateTime2 = OffsetDateTime.of(2019, 6, 24, 21, 49, 33,454000000, ZoneOffset.of("+3"));
        offsetDateTimeList.add(offsetDateTime2);

        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.TimestampTZ, true, offsetDateTimeList.toArray(), EncodingType.NONE);

        Object[] expecteds = Arrays.asList("2021-08-26 23:51:35.456+05:00", "2020-07-25 22:50:34.455+04:00", "2019-06-24 21:49:33.454+03:00").toArray();
        Assert.assertArrayEquals(expecteds, objects);
    }

    // PgsqlDbType.Date
    @Test
    public void localDate_To_DbDateString()
    {
        LocalDate localDate = LocalDate.of(2021, 8, 26);
        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Date, false, localDate, EncodingType.NONE);

        Assert.assertEquals("2021-08-26", objects[0]);
    }

    @Test
    public void localDateArray_To_DbDateArray()
    {
        List<LocalDate> localDates=new ArrayList<>();

        LocalDate localDate = LocalDate.of(2021, 8, 26);
        localDates.add(localDate);

        LocalDate localDate1 = LocalDate.of(2020, 7, 25);
        localDates.add(localDate1);

        LocalDate localDate2 = LocalDate.of(2019, 6, 24);
        localDates.add(localDate2);

        Object[] objects = convertingService.convertObjectToDb(PgsqlDbType.Date, true, localDates.toArray(), EncodingType.NONE);

        Object[] expecteds = Arrays.asList("2021-08-26", "2020-07-25", "2019-06-24").toArray();
        Assert.assertArrayEquals(expecteds, objects);
    }
}