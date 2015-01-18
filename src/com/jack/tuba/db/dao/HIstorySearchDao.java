package com.jack.tuba.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jack.tuba.db.HistorySearchHelper;
import com.jack.tuba.domain.HistorySearch;

public class HIstorySearchDao {

	private static final String TABLE_HISTORY = "history";

	private static HistorySearchHelper helper;

	private static HIstorySearchDao singleton;

	private HIstorySearchDao(Context context) {

		helper = new HistorySearchHelper(context);
	}

	public static HIstorySearchDao getInstance(Context context) {
		if (singleton == null) {
			synchronized (HIstorySearchDao.class) {
				if (singleton == null) {
					singleton = new HIstorySearchDao(context);
				}
			}
		}
		return singleton;
	}
   /**
    * 将搜索词加入到history表中
    * @param searchKey
    * @return
    */
	public long insert(HistorySearch hSearch) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("search_key", hSearch.getKey());
		values.put("hisroty_time", hSearch.getTime());
		long l = db.insert(TABLE_HISTORY, null, values);
		db.close();
		return l;
	}
	
	/**
	 * 从数据库中查询历史记录
	 * @param limit  String 形式："8,5"  从第8条开始，查5条  
	 * @return
	 */
	public List<HistorySearch> query(String limit) {
		List<HistorySearch> mHisList=new ArrayList<>();
		SQLiteDatabase db=helper.getWritableDatabase();
		String[] columns={"search_key","hisroty_time"};
		String orderBy="_id desc";
		Cursor cursor=db.query(TABLE_HISTORY, columns, null, null,null, null, orderBy, limit);
		while (cursor.moveToNext()) {
			HistorySearch hs=new HistorySearch();
			hs.setKey(cursor.getString(0));
			hs.setTime(cursor.getString(1));
			mHisList.add(hs);
		}
		db.close();
		return mHisList;
	}
	
	/**
	 * 
	 */
	public void deleteAll(){
		
	}
}
