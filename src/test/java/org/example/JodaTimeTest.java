package org.example;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class JodaTimeTest {
    public static void main(String[] args) {
        /**
         * 获取 DateTime
         */
        // 构造函数 现在时间
        DateTime dt1 = new DateTime();
        System.out.println(dt1);
        // 2020-06-21T10:54:11.378+08:00

        // 静态方法 现在时间
        DateTime dt2 = DateTime.now();
        System.out.println(dt2);
        // 2020-06-21T10:54:11.444+08:00

        // long -> DateTime
        DateTime dt3 = new DateTime(1609294356000L);
        System.out.println(dt3);
        // 2020-12-30T10:12:36.000+08:00

        // int -> DateTime
        DateTime dt4 = new DateTime(2020, 1, 12, 12, 30, 53);
        System.out.println(dt4);
        // 2020-01-12T12:30:53.000+08:00

        // Date -> DateTime
        DateTime dt5 = new DateTime(new Date());
        System.out.println(dt5);
        // 2020-06-21T10:47:01.461+08:00

        // 解析时间 String -> DateTime
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt6 = DateTime.parse("2020-12-30 10:12:36", formatter);
        System.out.println(dt6);
        // 2020-12-30T10:12:36.000+08:00

        /**
         * DateTime 转为其他对象
         */
        // 格式化时间 DateTime -> String
        System.out.println(dt6.toString("yyyy-MM-dd HH:mm:ss"));
        // 2020-12-30 10:12:36

        // 获取13位时间戳 DateTime -> Long
        System.out.println(dt6.getMillis());
        // 1609294356000

        // DateTime -> int
        System.out.println(dt6.getYear());
        System.out.println(dt6.getMonthOfYear());
        System.out.println(dt6.getDayOfMonth());
        System.out.println(dt6.getHourOfDay());
        System.out.println(dt6.getMinuteOfHour());
        System.out.println(dt6.getSecondOfMinute());
        System.out.println(dt6.getMillisOfSecond());
        // 2020 12 30 10 12 36 0

        // DateTime -> Date
        System.out.println(dt6.toDate());
        // Wed Dec 30 10:12:36 CST 2020

        /**
         * 时间修改 with开头
         */
        DateTime dt7 = dt6.withYear(1)
                .withMonthOfYear(2)
                .withDayOfMonth(3)
                .withHourOfDay(4)
                .withMinuteOfHour(5)
                .withSecondOfMinute(6)
                .withMillisOfSecond(7);

        System.out.println(dt7);
        // 0001-02-03T04:05:06.007+08:05:43

        /**
         * 时间计算 plus(+) 或者 minus(-)开头
         */

        DateTime dt8 = dt7.plusYears(7)
                .plusMonths(6)
                .plusDays(5)
                .plusHours(4)
                .plusMinutes(3)
                .plusSeconds(2)
                .plusMillis(1);

        System.out.println(dt8);
        // 0008-08-08T08:08:08.008+08:05:43

        /**
         * 计算两个时间间隔天数
         */

        DateTime dt9 = new DateTime(2020, 10, 20, 10, 30, 12);
        DateTime dt10 = new DateTime(2020, 10, 30, 20, 30, 12);
        System.out.println(Days.daysBetween(dt9, dt10).getDays());
        //    10
    }
}
