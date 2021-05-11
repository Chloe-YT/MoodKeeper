package util;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import pojo.Diary;


public class SyncUtil {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final static OkHttpClient client = new OkHttpClient();

    public static String CLOUD_HOST_IP="39.100.48.69";

    public static String LOCAL_HOST_IP="192.168.1.11";   //这里应填入使用ipconfig命令查看到的本机ip,可能随网络环境变化

    public static String HOST_IP=CLOUD_HOST_IP;

    /**
     * 构造存有Diary表待上传记录的jsonObject
     * @param needSync
     * @param diaryList
     * @return
     */
    public static JSONObject getDiarySyncRecordsJson(boolean needSync, List<Diary> diaryList){
        JSONObject diarySyncRecords=new JSONObject();

        //构建符合格式的json记录数组
        JSONArray diaryArray=new JSONArray();

        if(diaryList!=null) {
            for (Diary diary : diaryList) {
                //创建对应每个Diary记录的json对象
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("localId", diary.getDiary_id());
                jsonObject.put("userId", diary.getUser_id());
                jsonObject.put("moodId", diary.getMood_id());
                jsonObject.put("weatherId", diary.getWeather_id());
                jsonObject.put("categoryId", diary.getCategory_id());
                jsonObject.put("diaryName", diary.getDiary_name());
                jsonObject.put("diaryContent", diary.getDiary_content());
                jsonObject.put("diaryDate", diary.getDiary_date());
                jsonObject.put("state", diary.getState());
                jsonObject.put("anchor", diary.getAnchor());

                //添加到jsonArray中
                diaryArray.add(jsonObject);
            }
        }

        diarySyncRecords.put("needSync",needSync);
        diarySyncRecords.put("tableName","Diary");
        diarySyncRecords.put("recordList",diaryArray);

        return diarySyncRecords;
    }


    /**
     * 获取所有需要同步的记录
     * @return   一个存有各表待上传记录的JSONObject
     */
    public static JSONObject getAllSyncRecords(){
        JSONObject diaryRecordsJson;
        JSONObject finalResult=new JSONObject();

        DiaryDAO diaryDAO = new DiaryDAOImpl();

        List<Diary> diaryList = diaryDAO.getSuncDiary();

        if(diaryList.size()>0) {
            diaryRecordsJson = SyncUtil.getDiarySyncRecordsJson(true, diaryList);
        }
        else{
            diaryRecordsJson= SyncUtil.getDiarySyncRecordsJson(false,null);
        }

        finalResult.put("Diary",diaryRecordsJson);

        return finalResult;
    }

    /**
     * 上传成功后，根据服务器返回结果对本地记录同步状态进行更新
     * @param dataJson
     */
    public static void processUploadResult(JSONObject dataJson){
        JSONObject diarySyncRecords=dataJson.getJSONObject("Diary");

        DiaryDAO diaryDAO = new DiaryDAOImpl();

        if(diarySyncRecords.getBoolean("needSync")==true){
            JSONArray diaries=diarySyncRecords.getJSONArray("recordList");
            for(int i=0;i<diaries.size();i++){
                JSONObject diary=diaries.getJSONObject(i);
                int state=diary.getInteger("state");
                int id=diary.getInteger("localId");

                //对于新增或更新的记录，更新同步状态和上次与服务器同步时间
                if(state==0 || state==1) {
                    diaryDAO.setStateAndAnchor(id,9, diary.getDate("modified"));
                }
                //对于要删除的记录，进行物理删除
                else{
                    diaryDAO.deleteDiary(id);
                }
            }
            Log.d("处理上传响应","更新了"+diaries.size()+"条Account记录的同步状态");
        }
        System.out.println("处理上传结果响应成功");
    }

    /**
     * 获取各表与上一次与服务器同步的时间
     * @return
     */
    public static JSONObject getTableLastUpdateTime(){
        DiaryDAO diaryDAO=new DiaryDAOImpl();

        JSONObject resultObject=new JSONObject();

        //注意：根据getMaxAnchor（）函数的实现可知，表中无记录时，获得到的anchor为Date(0）,刚好可以让服务器返回
        // 所有记录
        resultObject.put("Diary",diaryDAO.getMaxAnchor());

        Log.d("发送下载请求","——————发送的本地最大anchor分别为：\nDiary:"+diaryDAO.getMaxAnchor());
        return resultObject;
    }

    /**
     * 根据服务器对下载请求的响应结果 更新本地数据库(调用处理各表下载结果的子函数)
     * @param dataJson
     */
    public static void processDownloadResult(JSONObject dataJson){
        //取出各表数据
        JSONObject diarySyncRecords=dataJson.getJSONObject("Diary");
        //调用处理各表下载结果的子函数
        processDiaryDownload(diarySyncRecords);

    }

    /**
     * 处理Account表下载结果 (！注意：当前版本只适用于处理没有离线添加数据的情况！）
     * @param diarySyncRecords
     */
    public static void processDiaryDownload(JSONObject diarySyncRecords){
        DiaryDAO diaryDAO=new DiaryDAOImpl();
        JSONArray recordsJSONArray=diarySyncRecords.getJSONArray("recordList");

        if(recordsJSONArray==null){
            return;
        }
        for(int i=0;i<recordsJSONArray.size();i++){
            JSONObject recordObject=recordsJSONArray.getJSONObject(i);
            int id=recordObject.getInteger("localId");
            int userId=recordObject.getInteger("userId");
            int moodId=recordObject.getInteger("moodId");
            int weatherId=recordObject.getInteger("weatherId");
            int categoryId=recordObject.getInteger("categoryId");
            String name=recordObject.getString("name");
            String content=recordObject.getString("content");
            Date diaryDate = recordObject.getDate("diaryDate");
            Date anchor=recordObject.getDate("modified");

            Diary diary=new Diary(id, userId, moodId, weatherId, categoryId, name, content, diaryDate,9, anchor);

            if(diaryDAO.getDiaryById(id)==null){
                //本地找不到该localId的记录————说明需要在本地添加该记录
                diaryDAO.insertDiary(diary);
                Log.d("处理下载请求","从服务器获取新Account记录："+diary.toString());
            }
            else{
                //找得到对应记录————根据从服务器那获取的数据修改
                diaryDAO.updateDiary(diary);
                Log.d("处理下载请求","从服务器更新Account记录："+diary.toString());
            }
        }
    }


    /**
     * 删除各表全部记录
     */
    public static void deleteAllRecords(){
        DiaryDAO diaryDAO=new DiaryDAOImpl();
        diaryDAO.deleteAll();
    }
}

