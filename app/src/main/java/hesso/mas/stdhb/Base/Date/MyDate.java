package hesso.mas.stdhb.Base.Date;

import com.hp.hpl.jena.sparql.function.library.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author chf
 * @version Version 1.0
 * @since 13.09.2016
 */
public final class MyDate {

    private date mDateTime;

    private MyDate() {}

    public static MyDate Today() { return new MyDate();}

    public String GetDateNow() {

        DateFormat lDateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String lCurrentDate = lDateFormat.format(Calendar.getInstance().getTime());

        return lCurrentDate;
    }

    public Integer Year() { return 2016; }

}
