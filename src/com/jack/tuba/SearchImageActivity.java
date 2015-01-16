package com.jack.tuba;

import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jack.tuba.adapter.HistorySearchAdapter;
import com.jack.tuba.adapter.SuggestSearchAdapter;
import com.jack.tuba.db.dao.HIstorySearchDao;
import com.jack.tuba.domain.HistorySearch;
import com.jack.tuba.utils.Constant;
import com.jack.tuba.utils.TubaUtils;
import com.jack.tuba.widget.LoadMoreListView;
import com.jack.tuba.widget.LoadMoreListView.OnLoadMoreListener;
/**
 * 分三部分
 * 上部：搜索框
 * 中部：推荐搜索关键字
 * 下部：查询数据库分页显示历史查询记录
 * 
 * 如果点了搜索，推荐搜索或者历史记录，将搜素关键字通过intent传回MainActivity,在调用异步任务查询；
 * 
 * @author Administrator
 *
 */
public class SearchImageActivity extends Activity implements OnClickListener, OnItemClickListener{

	private static final String TAG = SearchImageActivity.class.getName();
	
	private EditText searchEdText;

	private LoadHistoryTask mHistoryTask;
	
	private int start=0;
	
	private int size=20;

	private LoadMoreListView mListView;
	
	private List<HistorySearch> historys;

	private ProgressBar historyBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initGridView();
		initListView();
		initActionBar();
			
		
	}
	/**
	 * 初始化aciontbar，添加自定义的view,得到相应控件并添加了事件
	 */
	private void initActionBar(){
		ActionBar actionBar=getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,  
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME |  
                ActionBar.DISPLAY_SHOW_TITLE);
        LayoutInflater inflater=LayoutInflater.from(this);
		View view=inflater.inflate(R.layout.item_search_actionbar,new RelativeLayout(this),false);
		
		ImageButton backHomeBtn=(ImageButton) view.findViewById(R.id.search_back);
		backHomeBtn.setOnClickListener(this);
		
		searchEdText = (EditText) view.findViewById(R.id.editText);
		searchEdText.setHint("请输入搜索内容");
		ImageButton searchBtn=(ImageButton) view.findViewById(R.id.search);
		searchBtn.setOnClickListener(this);
		actionBar.setCustomView(view);
	};



	private void initListView() {
		mListView = (LoadMoreListView) findViewById(R.id.search_history_list);
		mListView.setOnLoadMoreListener(new MyLoadMoreListener());
		mListView.setOnItemClickListener(this);;
		
		historyBar = (ProgressBar) findViewById(R.id.history_bar);
		historyBar.setVisibility(View.VISIBLE);
		getHistory();	
	}
	
	/**
	 * 加载更多
	 * @author jack
	 *
	 */
	class MyLoadMoreListener implements OnLoadMoreListener{

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			getHistory();
		}
		
	}
	
	private void getHistory(){
		mHistoryTask = new LoadHistoryTask();	
		mHistoryTask.execute(getQueryParams(start, size));
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (mHistoryTask!=null||mHistoryTask.getStatus()==AsyncTask.Status.RUNNING) {
			mHistoryTask.cancel(true);
		}
		super.onStop();
	}
	/**
	 * 得到数据查询条件
	 * @return
	 */
	private String getQueryParams(int start,int size){
		return String.valueOf(start)+","+size;
	}
	
   /**
    *热门词条 
    */
	private void initGridView() {
		// TODO Auto-generated method stub
		GridView mGridView=(GridView) findViewById(R.id.search_suggest);
		SuggestSearchAdapter mGvAdapter=new SuggestSearchAdapter(Constant.suggestSearch,this);
		mGridView.setAdapter(mGvAdapter);
	}
	
	/**
	 * 加载历史查询信息
	 * @author jack
	 *
	 */
	class LoadHistoryTask extends AsyncTask<String, Void, List<HistorySearch>>{

		@Override
		protected List<HistorySearch> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String p=params[0];
			HIstorySearchDao dao;
			dao=HIstorySearchDao.getInstance(SearchImageActivity.this);
			return dao.query(p);
		}
		
		@Override
		protected void onPostExecute(List<HistorySearch> result) {
			// 更新listView
			if (result!=null&&result.size()>0) {
				if (historys==null) {
					historys=result;
					HistorySearchAdapter mHadapter=new HistorySearchAdapter(SearchImageActivity.this, historys);
					mListView.setAdapter(mHadapter);
					historyBar.setVisibility(View.GONE);
					start=start+size;
				}else {
					historys.addAll(result);
					start=start+size;
				}
			}else {
				if (historys==null) {
					historyBar.setVisibility(View.GONE);
				}
			}
			mListView.onLoadMoreComplete();
			super.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
		}
	}
	
	/**
	 * 加载热门查询词条
	 * @author jack
	 *
	 */
	class LoadHotSearch extends AsyncTask<String, Integer,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
   /**
    * 将查询信息写道数据库
    * @author jack
    *
    */
	class WriteToDbTask extends AsyncTask<HistorySearch,Integer, HistorySearch>{

		@Override
		protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		}
		
		@Override
		protected HistorySearch doInBackground(HistorySearch... params) {
			// TODO Auto-generated method stub
			HIstorySearchDao.getInstance(SearchImageActivity.this).insert(params[0]);
			return params[0];
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(HistorySearch result) {
		// TODO Auto-generated method stub
			backtiHome(result.getKey(),RESULT_OK);
		super.onPostExecute(result);
		}


	}
	/**
	 * 返回主页
	 * @param key
	 */
	private void backtiHome(String key,int resultCode) {
		// TODO Auto-generated method stub
		Intent i=new Intent();
		i.putExtra("key",key);
		setResult(resultCode,i);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_back:
			backtiHome("",RESULT_CANCELED);
			break;
		case R.id.search:
			writeToDbTask();
			break;

		default:
			break;
		}
	}
	/**
	 * 先写入数据库后返回查询
	 */
	private void writeToDbTask() {
		// TODO Auto-generated method stub
		String key=searchEdText.getText().toString().trim();
		if (key.equals("")) {
			searchEdText.setHint("搜索内容不能为空");;
			return;
		}
		HistorySearch hs=new HistorySearch();
		hs.setKey(key);
		hs.setTime(TubaUtils.getDate(System.currentTimeMillis()));
		new WriteToDbTask().execute(hs);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String key=historys.get(position).getKey();
		backtiHome(key, RESULT_OK);
	}
	

	
}
