package com.e.moodkeeper.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.e.moodkeeper.MonthPicker;
import com.e.moodkeeper.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;

public class ShowChartFragment extends Fragment {

    private String monthBeginDate;
    private String monthEndDate;
    private int maxDate;

    //时间选择器
    protected String days;
    protected int mYear;
    protected int mMonth;
    protected int mDays;

    private DatePickerDialog.OnDateSetListener dateListener;

    private TextView monthTv;
    private View mView;
    private LineChart lineChart;
    private BarChart barChart;

    private XAxis xAxis;

    List<Entry> lineChartList = new ArrayList<>();
    List<BarEntry> barChartList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDays = calendar.get(Calendar.DAY_OF_MONTH);

        monthTv = (TextView) view.findViewById(R.id.month_choose);
        days = new StringBuffer().append(mYear).append("年").append(mMonth + 1).append("月").toString();
        monthTv.setText(days);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        monthBeginDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthEndDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        maxDate = getDaysByYearMonth(mYear, mMonth + 1);

        //年月选择器
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDays = dayOfMonth;
                if (mMonth > 9) {
//                    days = new StringBuffer().append(mYear).append("-").append(mMonth + 1).toString();
                    days = new StringBuffer().append(mYear).append("年").append(mMonth + 1).append("月").toString();
                    monthTv.setText(days);

                    monthBeginDate = new SimpleDateFormat("yyyy-MM-dd").format(getSupportBeginDayOfMonth(mYear, mMonth + 1));
                    monthEndDate = new SimpleDateFormat("yyyy-MM-dd").format(getSupportEndDayOfMonth(mYear, mMonth + 1));
                    maxDate = getDaysByYearMonth(mYear, mMonth + 1);

                    refreshData(monthBeginDate, monthEndDate);
                } else {
//                    days = new StringBuffer().append(mYear).append("-0").append(mMonth + 1).toString();
                    days = new StringBuffer().append(mYear).append("年").append(mMonth + 1).append("月").toString();
                    monthTv.setText(days);

                    monthBeginDate = new SimpleDateFormat("yyyy-MM-dd").format(getSupportBeginDayOfMonth(mYear, mMonth + 1));
                    monthEndDate = new SimpleDateFormat("yyyy-MM-dd").format(getSupportEndDayOfMonth(mYear, mMonth + 1));
                    maxDate = getDaysByYearMonth(mYear, mMonth + 1);
                    refreshData(monthBeginDate, monthEndDate);
                }
            }
        };

        monthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker monthPicker = new MonthPicker(getActivity(), 3, dateListener, mYear, mMonth, mDays);
                monthPicker.show();
            }
        });

        lineChart = (LineChart) view.findViewById(R.id.line_chart);
        //初始化折线图数据
        initLineChart(lineChart);
        initLineChartData(monthBeginDate, monthEndDate);
        LineDataSet lineDataSet = new LineDataSet(lineChartList,"心情");
        initLineDataSet(lineDataSet, Color.rgb(187,59,14), LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "";
            }
        });

        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        //初始化柱状图数据
        initBarChart(barChart);
        initBarChartData(monthBeginDate, monthEndDate);
        BarDataSet barDataSet = new BarDataSet(barChartList,"心情");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
//        barDataSet.setHighlightEnabled(false); //选中柱子是否高亮显示
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "";
            }
        });

        return view;
    }

    public void initLineChart(LineChart lineChart1) {
        lineChart1.setNoDataText("没有数据");  //没有数据时显示的文字
        YAxis leftYAxis;            //左侧Y轴
        YAxis rightYaxis;           //右侧Y轴
        /*图表设置*/
        lineChart1.setScaleEnabled(true);
        //是否展示网格线
        lineChart1.setDrawGridBackground(false);
        //是否显示边界
        lineChart1.setDrawBorders(false);
        //是否可以拖动
        lineChart1.setDragEnabled(false);
        //是否有触摸事件
        lineChart1.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart1.animateY(2500);
        lineChart1.animateX(1500);
        // 设置描述
        Description description = new Description();
        description.setEnabled(false);
        lineChart1.setDescription(description);

        /***XY轴的设置***/
        xAxis = lineChart1.getXAxis();
        leftYAxis = lineChart1.getAxisLeft();
        rightYaxis = lineChart1.getAxisRight();
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(1f);
        xAxis.setGranularity(1f);
        //X轴分成10份
        xAxis.setLabelCount(10,false);

        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        //rightYaxis.setAxisMinimum(0f);
        rightYaxis.setEnabled(false);
//        leftYAxis.setEnabled(false);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        leftYAxis.setLabelCount(9,false);
        leftYAxis.setGranularity(1f);
        leftYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 1) {
                    return "其他";
                }
                if (value == 2) {
                    return "生气";
                }
                if (value == 3) {
                    return "伤心";
                }
                if (value == 4) {
                    return "疲惫";
                }
                if (value == 5) {
                    return "平静";
                }
                if (value == 6) {
                    return "努力";
                }
                if (value == 7) {
                    return "充实";
                }
                if (value == 8) {
                    return "得意";
                }
                if (value == 9) {
                    return "开心";
                }
                return "";
            }
        });

        /***折线图例 标签 设置***/

        Legend legend = lineChart1.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

    }

    public void initBarChart(BarChart barChart1) {
        barChart1.setNoDataText("没有数据");  //没有数据时显示的文字
        barChart1.setScaleEnabled(true);
        //X轴
        XAxis xAxis = barChart1.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上网格线
        barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);  //X轴的位置，默认为上面

        xAxis.setLabelCount(9, false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 1) {
                    return "开心";
                }
                if (value == 2) {
                    return "得意";
                }
                if (value == 3) {
                    return "充实";
                }
                if (value == 4) {
                    return "努力";
                }
                if (value == 5) {
                    return "平静";
                }
                if (value == 6) {
                    return "疲惫";
                }
                if (value == 7) {
                    return "伤心";
                }
                if (value == 8) {
                    return "生气";
                }
                if (value == 9) {
                    return "其他";
                }
                return "";
            }
        });

        //Y轴
        YAxis yAxisLeft = barChart1.getAxisLeft();
//        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线
        yAxisLeft.setGranularity(1f);

        barChart1.getAxisRight().setEnabled(false);

        //设置XY轴动画效果
        barChart1.animateY(2500);
        barChart1.animateX(1500);
        // 设置描述
        Description description = new Description();
        description.setEnabled(false);
        barChart1.setDescription(description);

        //保证Y轴从0开始，不然会上移一点
        yAxisLeft.setAxisMinimum(0f);

        barChart1.getDescription().setEnabled(false);  //隐藏右下角英文

        barChart1.getAxisRight().setEnabled(false);  //隐藏右侧Y轴，默认两侧都有Y轴

        barChart1.setDrawBarShadow(false);

    }

    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawValues(false);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircles(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        }
        else {
            lineDataSet.setMode(mode);
        }
    }


    public List<Entry> initLineChartData(String beginDate, String endDate) {
        Date begin = new Date();
        Date end = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            begin = df.parse(beginDate);
            end = df.parse(endDate);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }

        DiaryDAO diaryDAO = new DiaryDAOImpl();
        int i = 0;
        List<Integer> moodChange = diaryDAO.moodChange(begin, end);
        lineChartList.add(new Entry(0,moodChange.get(0)));
        for (i = 0; i <= maxDate; i++) {
            lineChartList.add(new Entry(i, moodChange.get(i)));
        }
        return lineChartList;
    }

    public List<BarEntry> initBarChartData(String beginDate, String endDate) {
        Date begin = new Date();
        Date end = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            begin = df.parse(beginDate);
            end = df.parse(endDate);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }

        DiaryDAO diaryDAO = new DiaryDAOImpl();
        int i = 0;
        List<Integer> monthlyMoodCount = diaryDAO.monthlyMoodCount(begin, end);
        barChartList.add(new BarEntry(1,monthlyMoodCount.get(0)));
        for (int c:monthlyMoodCount) {
            barChartList.add(new BarEntry(i + 1,c));
            i++;
        }
        return barChartList;
    }

    public void refreshData(String beginDate1, String endDate1) {
        lineChartList.clear();
        barChartList.clear();

        lineChartList.addAll(initLineChartData(beginDate1,endDate1));
        LineDataSet lineDataSet = new LineDataSet(lineChartList,"心情");
        initLineDataSet(lineDataSet, Color.rgb(187,59,14), LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        barChartList.addAll(initBarChartData(beginDate1, endDate1));
        BarDataSet barDataSet = new BarDataSet(barChartList,"心情");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setHighlightEnabled(false); //选中柱子是否高亮显示
        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }

    public static Date getSupportBeginDayOfMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND, 0);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDay = cal.getTime();
        return firstDay;
    }

    public static Date getSupportEndDayOfMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND, 59);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDay = cal.getTime();
        return lastDate;
    }

    public static int getDaysByYearMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.DATE, -1);
        int maxDate = cal.get(Calendar.DATE) + 1;
        return  maxDate;
    }

}
