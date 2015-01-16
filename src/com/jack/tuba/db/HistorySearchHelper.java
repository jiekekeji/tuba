package com.jack.tuba.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistorySearchHelper extends SQLiteOpenHelper {

	public HistorySearchHelper(Context context) {
		super(context, "history.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 搜索的历史记录
		String sql = "create table history(_id integer primary key, search_key varchar(30),hisroty_time varchar(18));";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
