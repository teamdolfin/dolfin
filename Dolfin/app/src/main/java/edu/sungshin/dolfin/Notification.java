package edu.sungshin.dolfin;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Notification extends Fragment implements  onBackPressedListener{
    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    private View view;

    Button btnEnd; // 예약완료 button
    RadioButton rdoCal, rdoTime; // 날짜설정, 시간설정 radiobutton
    CalendarView calView; // 캘린더(날짜선택)
    TimePicker tPicker; // 시간선택
    String tvYear, tvMonth, tvDay, tvHour, tvMinute; // 연,월,일,시,분
    int selectYear, selectMonth, selectDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification,container,false);
        //메뉴 활성화
        setHasOptionsMenu(true);

        notificationManager = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        mCalender = new GregorianCalendar();

        Log.v("HelloAlarmActivity", mCalender.getTime().toString());

        // 버튼
        btnEnd = (Button) view.findViewById(R.id.btnEnd);

        // 라디오버튼 2개
        rdoCal = (RadioButton) view.findViewById(R.id.rdoCal);
        rdoTime = (RadioButton) view.findViewById(R.id.rdoTime);

        // FrameLayout의 2개 위젯
        tPicker = (TimePicker) view.findViewById(R.id.timePicker1);
        calView = (CalendarView) view.findViewById(R.id.calendarView1);


        // 처음에는 2개를 안보이게 설정
        tPicker.setVisibility(View.INVISIBLE);
        calView.setVisibility(View.INVISIBLE);

        rdoCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 날짜설정 버튼 누르면 캘린더 표시
                tPicker.setVisibility(View.INVISIBLE);
                calView.setVisibility(View.VISIBLE);
            }
        });

        rdoTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 시간설정 버튼 누르면 TimePicker 표시
                tPicker.setVisibility(View.VISIBLE);
                calView.setVisibility(View.INVISIBLE);
            }
        });



        // 버튼을 클릭하면 예약 날짜와 시간을 토스트 메시지로 알려준다
        btnEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tvYear=(Integer.toString(selectYear));
                tvMonth=(Integer.toString(selectMonth));
                tvDay=(Integer.toString(selectDay));
                tvHour=(Integer.toString(tPicker.getCurrentHour()));
                tvMinute=(Integer.toString(tPicker.getCurrentMinute()));

                String a = (tvYear+"년"+tvMonth+"월"+tvDay+
                        "일 "+tvHour+"시"+tvMinute+"분 예약완료");


                Toast.makeText(getActivity(), a ,Toast.LENGTH_SHORT).show();
                setAlarm();

                Fragment fragment = new FragMyPage();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_frame ,fragment).commit();
            }
        });

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectYear =  year;
                selectMonth = month + 1;
                selectDay = dayOfMonth;
            }
        });

        //액션바
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("알림설정");
        actionBar.setDisplayHomeAsUpEnabled(false);




        return view;
    }

    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(getContext(), AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),1, receiverIntent, PendingIntent.FLAG_IMMUTABLE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 101, receiverIntent, PendingIntent.FLAG_IMMUTABLE);

        //String from = "2022-11-19 12:45"; //임의로 날짜와 시간을 지정
        String from = tvYear+"-"+tvMonth+"-"+tvDay+" "+tvHour+":"+tvMinute;

        //날짜 포맷을 바꿔주는 소스코드
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.util.Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);


    }

    // (추가) 뒤로가기 기능 - FragMypage으로 이동
    @Override
    public void onBackPressed() {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//        getActivity().getSupportFragmentManager().popBackStack();
        Fragment fragment = new FragMyPage();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_frame ,fragment).commit();
    }



}
