package noman.weekcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import noman.weekcalendar.eventbus.BusProvider;
import noman.weekcalendar.eventbus.Event;
import noman.weekcalendar.listener.OnDateClickListener;
import noman.weekcalendar.view.WeekPager;

/**
 * Created by Administrator on 2016/11/29 0029.
 */

public class WeekCalendarTeach extends LinearLayout {
    private static final String TAG = "WeekCalendarTAG";
    private OnDateClickListener listener;
    private TypedArray typedArray;
    private GridView daysName;
    public WeekPager weekPager;

    //返回时间的接口
    private WeekDateChaListener weekDateChaListener;

    //设置返回时间的接口
    public void setWeekDateChaListener(WeekDateChaListener weekDateChaListener) {
        weekPager.setWeekDateChaListener(weekDateChaListener);
        this.weekDateChaListener = weekDateChaListener;
    }


    public WeekCalendarTeach(Context context) {
        super(context);
        init(null);
    }

    public WeekCalendarTeach(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public WeekCalendarTeach(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
        }
        setOrientation(VERTICAL);

        if (!typedArray.getBoolean(R.styleable.WeekCalendar_hideNames, false)) {
            daysName = getDaysNames();
            addView(daysName, 0);
        }
        weekPager = new WeekPager(getContext(), attrs);
        addView(weekPager);
        BusProvider.getInstance().register(this);

    }

    /***
     * Do not use this method
     * this is for receiving date,
     * use "setOndateClick" instead.
     */
    @Subscribe
    public void onDateClick(Event.OnDateClickEvent event) {
        if (listener != null)
            listener.onDateClick(event.getDateTime());
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }


    private GridView getDaysNames() {
        daysName = new GridView(getContext());
        daysName.setSelector(new StateListDrawable());
        daysName.setNumColumns(7);

        daysName.setAdapter(new BaseAdapter() {
            private String[] days = getWeekDayNames();

            public int getCount() {
                return days.length;
            }

            @Override
            public String getItem(int position) {
                return days[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.week_day_grid_item, null);
                }
                TextView day = (TextView) convertView.findViewById(R.id.daytext);
                day.setText(days[position]);

                if (typedArray != null) {
                    day.setTextColor(typedArray.getColor(R.styleable.WeekCalendar_weekTextColor,
                            Color.WHITE));
                    day.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable
                            .WeekCalendar_weekTextSize, day.getTextSize()));
                }
                //day.setTextSize(12);
                return convertView;
            }

            private String[] getWeekDayNames() {
                String[] names = DateFormatSymbols.getInstance().getShortWeekdays();
                List<String> daysName = new ArrayList<>(Arrays.asList(names));
                daysName.remove(0);
                daysName.add(daysName.remove(0));

                if (typedArray.getInt(R.styleable.WeekCalendar_dayNameLength, 0) == 0) {
                    for (int i = 0; i < daysName.size(); i++) {
                        String dayName = daysName.get(i);
                        Log.e(TAG, "getWeekDayNames: " + dayName);
                        daysName.set(i, dayName);
                        //将中文星期变为英文
                       // setDayEnglish(daysName, i, dayName);


//                        if (daysName.get(i).contains("周")) {
//                            String weekDay = daysName.get(i).replace("周", "星期");
//                            daysName.set(i, weekDay);
//                        } else {
//                            break;
//                        }
                    }
                }
                names = new String[daysName.size()];
                daysName.toArray(names);
                return names;

            }
        });
        if (typedArray != null)
            daysName.setBackgroundColor(typedArray.getColor(R.styleable
                    .WeekCalendar_weekBackgroundColor, ContextCompat.getColor(getContext(), R
                    .color.colorPrimary)));
        return daysName;
    }

    private void setDayEnglish(List<String> daysName, int i, String dayName) {
        switch (dayName) {
            case "周一":
                String weekDay = daysName.get(i).replace("周一", "MON");
                daysName.set(i, weekDay);
                break;
            case "周二":
                weekDay = daysName.get(i).replace("周二", "TUE");
                daysName.set(i, weekDay);
                break;
            case "周三":
                weekDay = daysName.get(i).replace("周三", "WED");
                daysName.set(i, weekDay);
                break;
            case "周四":
                weekDay = daysName.get(i).replace("周四", "THU");
                daysName.set(i, weekDay);
                break;
            case "周五":
                weekDay = daysName.get(i).replace("周五", "FRI");
                daysName.set(i, weekDay);

                break;
            case "周六":
                weekDay = daysName.get(i).replace("周六", "SAT");
                daysName.set(i, weekDay);
                break;
            case "周日":
                weekDay = daysName.get(i).replace("周日", "SUN");
                daysName.set(i, weekDay);
                break;
            default:
                break;

        }
    }

    public void moveToPrevious() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(-1));
    }

    public void moveToNext() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(1));
    }

    public void reset() {
        BusProvider.getInstance().post(new Event.ResetEvent());
    }

    public void setSelectedDate(DateTime selectedDate) {
        BusProvider.getInstance().post(new Event.SetSelectedDateEvent(selectedDate));
        //返回时间
        weekDateChaListener.getDate(selectedDate);
    }

    public void setStartDate(DateTime startDate) {
        BusProvider.getInstance().post(new Event.SetStartDateEvent(startDate));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }
}
