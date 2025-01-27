package su.panfilov.bogoban.models;

import android.content.Context;
import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import su.panfilov.bogoban.components.FileCache;


public class SetItem {
    public SetItemInfo info;
    public Date timeStamp;
    public List<Stone> stoneList;

    public SetItem(SetItemInfo info, List<Stone> stoneList) {
        this.info = info;
        this.timeStamp = new Date();
        this.stoneList = stoneList;
    }

    public SetItem(SetItemInfo info, Date timeStamp, List<Stone> stoneList) {
        this.info = info;
        this.timeStamp = timeStamp;
        this.stoneList = stoneList;
    }

    public SetItem() {
        this.timeStamp = new Date();
    }

    public void saveState(Context context) {
        Map<String, SetItemInfo> setsTitles = readSetsTitlesFromCache(context);
        setsTitles.put(getId(), info);
        saveSetsTitlesToCache(context, setsTitles);
        saveSetToCache(context,this);
    }

    public void delete(Context context) {
        Map<String, SetItemInfo> setsTitles = readSetsTitlesFromCache(context);
        setsTitles.remove(getId());
        saveSetsTitlesToCache(context, setsTitles);
        deleteSetFromCache(context, getId());
    }

    public static Map<String, SetItemInfo> readSetsTitlesFromCache(Context context) {
        Map<String, SetItemInfo> setsTitles;

        FileCache fileCache = new FileCache(context);
        String setsTitlesJson = fileCache.getStringFromFile("sets_titles");
        if (setsTitlesJson.equals("")) {
            setsTitlesJson = "{}";
        }
        Gson gson = new Gson();
        setsTitles = gson.fromJson(setsTitlesJson, new TypeToken<TreeMap<String, SetItemInfo>>() {}.getType());

        return setsTitles;
    }

    private void saveSetsTitlesToCache(Context context, Map<String, SetItemInfo> setsTitles) {
        FileCache fileCache = new FileCache(context);
        Gson gson = new Gson();
        String setsTitlesJson = gson.toJson(setsTitles, new TypeToken<TreeMap<String, SetItemInfo>>() {}.getType());
        fileCache.putStringToFile("sets_titles", setsTitlesJson);
    }

    public static SetItem readSetFromCache(Context context, String id) {
        FileCache fileCache = new FileCache(context);
        String setDataJson = fileCache.getStringFromFile("set_data_" + id);
        if (setDataJson.equals("")) {
            setDataJson = "{}";
        }
        Gson gson = new Gson();
        SetItem setItem = gson.fromJson(setDataJson, SetItem.class);

        return setItem;
    }

    private void saveSetToCache(Context context, SetItem setItem) {
        FileCache fileCache = new FileCache(context);
        Gson gson = new Gson();
        String setDataJson = gson.toJson(setItem, SetItem.class);
        fileCache.putStringToFile("set_data_" + setItem.getId(), setDataJson);
    }

    private void deleteSetFromCache(Context context, String id) {
        FileCache fileCache = new FileCache(context);
        fileCache.deleteFile("set_data_" + id);
    }

    public String getId() {
        return DateFormat.format("yyyyMMddHHmmss", timeStamp).toString();
    }
}
