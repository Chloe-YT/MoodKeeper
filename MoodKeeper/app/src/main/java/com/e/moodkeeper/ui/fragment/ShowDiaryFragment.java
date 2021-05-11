package com.e.moodkeeper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.activity.ActivityUpdateDiary;
import com.e.moodkeeper.R;
import com.e.moodkeeper.adapter.ShowDiaryAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;
import pojo.Diary;

public class ShowDiaryFragment extends Fragment {

    private List<Diary> diaryList = new ArrayList<>();

    private ShowDiaryAdapter adapter;

    private SearchView search_diary_sv;
    private MaterialSpinner category_spinner;
    private RecyclerView show_diary_rv;

    private int diary_id;
    private int user_id  = 1;
    private int category_id = 1;
    private Date anchor = new Date(0);
    //状态
    private int addState = 0;  //本地新增
    private int deleteState = -1;  //标记删除
    private int updateState = 1;  //本地更新

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        initDiary();

        show_diary_rv = (RecyclerView) view.findViewById(R.id.show_diary_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        show_diary_rv.setLayoutManager(layoutManager);

        adapter = new ShowDiaryAdapter(diaryList);
        show_diary_rv.setAdapter(adapter);
        setRecyclerViewClickListener();
//        adapter.setOnItemClickListener(new ShowDiaryAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getActivity(), "click" + position, Toast.LENGTH_SHORT).show();
//                Diary tempDiary = diaryList.get(position);
//                diary_id = tempDiary.getDiary_id();
//                Intent intent = new Intent(getActivity(), ActivityUpdateDiary.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("diary_id", diary_id);
//                intent.putExtra("bundle",bundle);
//                startActivity(intent);
//            }
//        });

        search_diary_sv = (SearchView) view.findViewById(R.id.search_diary);
        search_diary_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter = new ShowDiaryAdapter(diaryList);
                    adapter.notifyDataSetChanged();
                    show_diary_rv.setAdapter(adapter);
                    setRecyclerViewClickListener();
                } else {
                    adapter = new ShowDiaryAdapter(searchDiary(newText));
                    adapter.notifyDataSetChanged();
                    show_diary_rv.setAdapter(adapter);
                    setChangeRecyclerViewClickListener(searchDiary(newText));
                }
                return false;
            }
        });

        category_spinner = (MaterialSpinner)view.findViewById(R.id.category_spinner);
        category_spinner.setItems("所有类别", "生 活", "学 习", "办 公", "娱 乐", "其他");
        category_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                Snackbar.make(view, "Clicked" + item, Snackbar.LENGTH_LONG).show();
                category_id = position;

                if (position == 0) {
                    adapter = new ShowDiaryAdapter(diaryList);
                    adapter.notifyDataSetChanged();
                    show_diary_rv.setAdapter(adapter);
                    setRecyclerViewClickListener();
                } else {
                    adapter = new ShowDiaryAdapter(categoryDiary(position));
                    adapter.notifyDataSetChanged();
                    show_diary_rv.setAdapter(adapter);
                    setChangeRecyclerViewClickListener(categoryDiary(position));

                }

            }
        });

        return view;
    }

    private void initDiary() {
        DiaryDAO diaryDAO = new DiaryDAOImpl();

        List<Diary> diaryList1 = diaryDAO.listDiaryByDate();
        List<Diary> newData = new ArrayList<>();
        for (Diary item:diaryList1) {
            if (item.getState() != -1) {
                newData.add(item);
            }
        }
        diaryList = newData;

//        diaryList = diaryDAO.listDiary();
//        List<Integer> diaryIdList = new ArrayList<>();
//        List<String> diaryNameList = new ArrayList<>();
//        List<Date> diaryDateList = new ArrayList<>();
//        List<String> diaryContentList = new ArrayList<>();
//        List<Integer> weatherIdList = new ArrayList<>();
//        List<Integer> moodIdList = new ArrayList<>();
//        List<Integer> categoryIdList = new ArrayList<>();
//
//        if (diaryIdList != null) {
//            diaryIdList.clear();
//        }
//
//        for (int i = 0; i < diaryList.size(); i++) {
//            Diary diary = (Diary) diaryList.get(i);
//            if (diary.getState() != -1) {
//                diaryIdList.add(diary.getDiary_id());
//                diaryNameList.add(diary.getDiary_name());
//                diaryContentList.add(diary.getDiary_content());
//                diaryDateList.add(diary.getDiary_date());
//                weatherIdList.add(diary.getWeather_id());
//                moodIdList.add(diary.getMood_id());
//                categoryIdList.add(diary.getCategory_id());
//            }
//        }
//
//        if (diaryList != null) {
//            diaryList.clear();
//            for (int j = 0; j < diaryIdList.size(); j++) {
//                diary_id = diaryIdList.get(j);
//                Diary diary = new Diary(diary_id, user_id, moodIdList.get(j), weatherIdList.get(j), categoryIdList.get(j), diaryNameList.get(j), diaryContentList.get(j),diaryDateList.get(j),updateState, anchor);
//                diaryList.add(diary);
//            }
//        } else {
//            for (int j = 0; j < diaryIdList.size(); j++) {
//                diary_id = diaryIdList.get(j);
//                Diary diary = new Diary(diary_id, user_id, moodIdList.get(j), weatherIdList.get(j), categoryIdList.get(j), diaryNameList.get(j), diaryContentList.get(j),diaryDateList.get(j),updateState, anchor);
//                diaryList.add(diary);
//            }
//        }


//        Date date0=new Date(0);
//        Diary diary1 = new Diary(1, 1, 1, 1, 1,"哈哈哈哈", "111", new Date(2020-1900,1-1,10), 1, date0);
//        diaryList.add(diary1);
//        Diary diary2 = new Diary(2, 1, 1, 1, 1,"日记", "111222", new Date(2020-1900,1-1,10), 1, date0);
//        diaryList.add(diary2);
//        Diary diary3 = new Diary(3, 1, 1, 1, 1,"武汉去", "111222", new Date(2020-1900,1-1,10), 1, date0);
//        diaryList.add(diary3);

    }

    public void setRecyclerViewClickListener() {
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
    }

    public void setChangeRecyclerViewClickListener(final List<Diary> changeDiaryList) {
        adapter.setOnItemClickListener(new ShowDiaryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "click" + position, Toast.LENGTH_SHORT).show();
                Diary tempDiary = changeDiaryList.get(position);
                diary_id = tempDiary.getDiary_id();
                Intent intent = new Intent(getActivity(), ActivityUpdateDiary.class);
                Bundle bundle = new Bundle();
                bundle.putInt("diary_id", diary_id);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }

    public List<Diary> searchDiary(String query) {
        List<Diary> filterDatas = new ArrayList<>();
        for (Diary source : diaryList) {
            if (source.getDiary_content().contains(query) || source.getDiary_name().contains(query)) {
                filterDatas.add(source);
            }
        }
        return filterDatas;
    }

    public List<Diary> categoryDiary(int position) {
        List<Diary> filterDatas = new ArrayList<>();
        for (Diary source : diaryList) {
            if (source.getCategory_id() == position) {
                filterDatas.add(source);
            }
        }
        return filterDatas;
    }

}
