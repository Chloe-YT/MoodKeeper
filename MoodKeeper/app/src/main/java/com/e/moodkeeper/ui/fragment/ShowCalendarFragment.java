package com.e.moodkeeper.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.activity.ActivityUpdateDiary;
import com.e.moodkeeper.R;
import com.e.moodkeeper.adapter.ShowDiaryAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;
import pojo.Diary;

public class ShowCalendarFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener{

    private ShowDiaryAdapter adapter;

    private TextView mTextMonthDay;
    private TextView mTextYear;
    private TextView mTextLunar;
    private TextView mTextCurrentDay;
    private CalendarView mCalendarView;
    private RelativeLayout mRelativeTool;
    private CalendarLayout mCalendarLayout;
    private RecyclerView mRecyclerView;

    private int diary_id;

    private int mYear;
    private String days;

    private List<Diary> diaryList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mTextMonthDay = view.findViewById(R.id.tv_month_day);
        mTextYear = view.findViewById(R.id.tv_year);
        mTextLunar = view.findViewById(R.id.tv_lunar);
        mRelativeTool = view.findViewById(R.id.rl_tool);
        mCalendarView = view.findViewById(R.id.calendarView);
        mTextCurrentDay = view.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });

        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mCalendarLayout = view.findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this,false);

        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        days = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        initDiaryData(days);
        initData();

        mRecyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new ShowDiaryAdapter(diaryList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShowDiaryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "click" + position, Toast.LENGTH_SHORT).show();
                Diary tempDiary = diaryList.get(position);
                diary_id = tempDiary.getDiary_id();
                Intent intent = new Intent(getActivity(), ActivityUpdateDiary.class);
                Bundle bundle = new Bundle();
                bundle.putInt("diary_id", diary_id);
                intent.putExtra("bundle",bundle);
                startActivity(intent);

            }
        });

        return view;
    }

//    protected void initData() {
//        int year = mCalendarView.getCurYear();
//        int month = mCalendarView.getCurMonth();
//
//        Map<String, Calendar> map = new HashMap<>();
//        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
//                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
//        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
//                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
//        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
//                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
//        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
//                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
//        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
//                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
//        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
//                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
//        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
//                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
//        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
//                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
//        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
//                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
//        //此方法在巨大的数据量上不影响遍历性能，推荐使用
//        mCalendarView.setSchemeDate(map);
//
////        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
////        mRecyclerView.setAdapter(new ArticleAdapter(this));
////        mRecyclerView.notifyDataSetChanged();
//    }

    protected void initData() {
        Map<String, Calendar> map = new HashMap<>();
        DiaryDAO diaryDAO = new DiaryDAOImpl();

        List<Date> dateList = diaryDAO.markDiaryDate();

        for (int i = 0; i < dateList.size(); i++) {
            String[] strNow1 = new SimpleDateFormat("yyyy-MM-dd").format(dateList.get(i)).toString().split("-");

            map.put(getSchemeCalendar(Integer.parseInt(strNow1[0]), Integer.parseInt(strNow1[1]), Integer.parseInt(strNow1[2])).toString(),
                    getSchemeCalendar(Integer.parseInt(strNow1[0]), Integer.parseInt(strNow1[1]), Integer.parseInt(strNow1[2])));

        }
        mCalendarView.setSchemeDate(map);
    }

//    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
//        Calendar calendar = new Calendar();
//        calendar.setYear(year);
//        calendar.setMonth(month);
//        calendar.setDay(day);
//        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
//        calendar.setScheme(text);
//        return calendar;
//    }

    private Calendar getSchemeCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(0xFF13acf0);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (isClick) {

            days = new StringBuffer().append(mYear).append("-").append(calendar.getMonth()).append("-").append(calendar.getDay()).toString();
            initDiaryData(days);

            adapter = new ShowDiaryAdapter(diaryList);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ShowDiaryAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Toast.makeText(getActivity(), "click" + position, Toast.LENGTH_SHORT).show();
                    Diary tempDiary = diaryList.get(position);
                    diary_id = tempDiary.getDiary_id();
                    Intent intent = new Intent(getActivity(), ActivityUpdateDiary.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("diary_id", diary_id);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);

                }
            });

            Toast.makeText(getActivity(), "You click " + calendar.getMonth() + "月" + calendar.getDay() + "日", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Log.e("onDateLongClick", "  -- " + calendar.getDay() + "  --  " + calendar.getMonth());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onClick(View v) {

    }

    public void initDiary() {

        DiaryDAO diaryDAO = new DiaryDAOImpl();

        List<Diary> diaryList1 = diaryDAO.listDiaryByDate();
        List<Diary> newData = new ArrayList<>();
        for (Diary item:diaryList1) {
            if (item.getState() != -1) {
                newData.add(item);
            }
        }
        diaryList = newData;
    }

    public void initDiaryData(String days) {

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(days);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }

        DiaryDAO diaryDAO = new DiaryDAOImpl();

        List<Diary> diaryList1 = diaryDAO.listDiaryForDate(date);
        List<Diary> newData = new ArrayList<>();
        for (Diary item:diaryList1) {
            if (item.getState() != -1) {
                newData.add(item);
            }
        }
        diaryList = newData;
    }

}
