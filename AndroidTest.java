package com.tsis.tms3.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tobesoft.platform.data.Dataset;
import com.tsis.ntms.R;
import com.tsis.ntms.SmsTmsClient;
import com.tsis.tms3.common.CommonDialog;
import com.tsis.tms3.common.TmsPolicyVo;
import com.tsis.tms3.util.DanalAuthUtil;
import com.tsis.tms3.util.dialog.ManagedActivity;
import com.tsis.tms3.util.net.THandler;
import com.tsis.tms3.util.net.TPlatformData;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;


/**
 * 버터나이프 예제
 * 리싸이클뷰 예제
 */
public class AndroidTest extends ManagedActivity {
	
	
	
	// 화면변수
//	@Bind(R.id.work_search_btn)
//	Button workSearchBtn;

	private Button mCrashTerminateBtn;
	private Button mCrashLogoutBtn;
	private Button mListViewBtn;
	
	
	
	private Dataset dsCodeAuth = null;
	private Dataset dsAuth = null;
	private Dataset dsCond = null;
	private String callbackType = "";
	private List<Map<String, String>> errorHistrory = null;
	private Spinner spDong;
	private List<Item> items = null;
	private Item item = null;
	
	private THandler handler = new THandler(AndroidTest.this);
	private static String fgvBizBean = "com.opaleye.exbill.sms.inter.biz.NameCertifyBiz";//기간계 접속할 경우 사용할 Biz명시
	private CountDownTimer timer = null;
	
	private Button init_pwd_btn;
	private Button pwd_confirm_btn;
	private TextView codeDelayTextView;
	
	List<String> list = null;
	
	int width = 0;
	int height = 0;
	
	private boolean hasAuth = false;
	public static final String TAG = "GU>>";
	
	@Override
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.w_android_test);
		Log.i(TAG,"안드로이드 테스트 시작");
		ButterKnife.bind(this);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
		actionBar.setTitle("TEST");
		
		init_pwd_btn = (Button)findViewById(R.id.init_pwd_btn);
		init_pwd_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Log.i(TAG,"인증번호받기");
				initPwd();				
			}
		});
	
		
		pwd_confirm_btn = (Button)findViewById(R.id.pwd_confirm_btn);
		pwd_confirm_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Log.i(TAG,"인증번호확인");
				pwdChg();				
			}
		});
		
		//타이머시작
		codeDelayTextView = (TextView)findViewById(R.id.codeDelayTextView);
		
		Log.i(TAG,"안드로이드 에러시작");
		Log.i(TAG,"안드로이드 에러끝");

		errorHistrory = new ArrayList<Map<String, String>>();
		mCrashTerminateBtn = (Button)findViewById(R.id.crash_logout_btn);
		
		mCrashTerminateBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Crash 로그 테스트 - 앱 죽이기
				if(hasAuth){
					TextView view = (TextView)findViewById(R.id.addOK);
					view.setText("test");
				}
				else{
					Toast.makeText(AndroidTest.this, "테스트 권한이 없습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		mCrashLogoutBtn = (Button)findViewById(R.id.crash_logout_btn);
		mCrashLogoutBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Crash 로그 테스트 - 로그아웃
				if(hasAuth){
					try{
						
						TextView view = (TextView)findViewById(R.id.addOK);
						view.setText("test");
					} catch (Exception e) {
						CommonDialog.showException(AndroidTest.this, e);
					}
				}
				else{
					Toast.makeText(AndroidTest.this, "테스트 권한이 없습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		
		/*
		<Spinner
			android:id="@+id/sp_dong"
			style="@style/spinner_middle"
			android:layout_width="wrap_content"
			android:layout_marginLeft="5dip"
			android:layout_marginRight="5dip"
			android:layout_height="40dip"/>
		 
		*/
		/*
		final List<Item> items = new ArrayList<Item>();
		Spinner spDong = (Spinner)findViewById(R.id.sp_dong);
		Item item = new Item();
		ItemData itemData = new ItemData();
		
		item.setKey("KEY1");
		item.setValue("가");
		itemData.setName("일길동");
		itemData.setAge("10");
		item.setItemData(itemData);				
		items.add(item);
		
		item = new Item();
		item.setKey("KEY2");
		item.setValue("나");
		itemData = new ItemData();
		itemData.setName("이길동");
		itemData.setAge("20");
		item.setItemData(itemData);
		items.add(item);
		
		
		item = new Item();
		item.setKey("KEY3");
		item.setValue("다");	
		itemData = new ItemData();
		itemData.setName("삼길동");
		itemData.setAge("30");
		item.setItemData(itemData);
		items.add(item);


		//어뎁터에서 제너릭 사용법은 
		//ArrayAdapter<T> ArrayList<T> T부분이 같은 타입이면 에러가 안남 여기선 Item
		//R.layout.custom_simple_dropdown_item_1line 사용자정의 레이아웃
		ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_spinner_item, items) {
			@Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        TextView textView = (TextView) super.getView(position, convertView, parent);
		        textView.setTextColor(Color.parseColor("#000000"));   //선택값은 검정색 getResources().getColor(R.color.defaultBtnText)
		        
		        return textView;
		    }
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spDong.setAdapter(adapter);
		spDong.setSelection(2, false);		//디폴트값 선택시에는 이벤트가 일어나지 않도록 한다.
		spDong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view, int pos, long l) {
				
				//이벤트가 발생한 위치
				Item item = items.get(pos);
				
				//선택한키가 3번이라면 
				if("KEY3".equals(item.getKey())) {
					Toast.makeText(getApplicationContext(), item.getItemData().getName(), Toast.LENGTH_SHORT).show();
				}
			}
		
			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		*/
		
		
		/*
		ArrayAdapter adapter = new ArrayAdapter (
					this,
					android.R.layout.simple_spinner_dropdown_item, items);
		
		spDong.setAdapter(adapter);
		spDong.setSelection(2,false);		
		spDong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view, int i, long l) {
				Toast.makeText(getApplicationContext(),"11", Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
		
		mListViewBtn = (Button) findViewById(R.id.work_search_list);
		mListViewBtn.setOnClickListener(new ListViewTest());
		
		//다날공통모듈 테스트
		WebView webView = (WebView)findViewById(R.id.webview);
		DanalAuthUtil util = new DanalAuthUtil.Builder(this)
				.setWebView(webView)			//화면ID
				.setIdCust("4024445344")        
				.setIdRcv("999999999")				
				.setIdSo("1005")
				.setDanalAuthListener(new DanalAuthUtil.DanalAuthListener() {
					@Override
					public void DanalAuthResult(String val) {
						// TODO Auto-generated method stub
						Log.i("Gu>>", val);
						Log.i("Gu>>", val);
						Log.i("Gu>>", val);
					}
				});
		}
	
	
	
	
	
	
	
	public class ListViewTest implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			//Intent intent = new Intent(AndroidTest.this, Test.class);
			
			//startActivity(intent);
		}
	}

	/**
	 * @throws IOException 
	 * @category 클릭이벤트
	 */
//	@OnClick({ R.id.work_search_btn, R.id.work_init_btn, R.id.work_trans_btn })
//	public void sayHi(Button button){
//		switch (button.getId()) {
//			case R.id.work_search_btn:
//				Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_SHORT).show();
//				Intent intent = new Intent(AndroidTest.this, OpticalPortChangeRf.class);
//				
//				intent.putExtra("VIEW_GBN", "OpticalPortChangeRf");
//				intent.putExtra("ARG_ID_BLD", "4013661295");
//				intent.putExtra("ARG_DONG", "101");
//				intent.putExtra("ARG_HO", "101");
//				intent.putExtra("ARG_ID_CUST", "1000833345");
//				intent.putExtra("ARG_NO_BLD_DTL", "40136612950101000101");
//				
//				startActivity(intent);
//				break;
//			case R.id.work_init_btn:
//				Toast.makeText(getApplicationContext(), "카카오확인", Toast.LENGTH_SHORT).show();
//				kakaoReady();
//				break;
//			case R.id.work_trans_btn:
//				
//				Intent intent1 = new Intent();
//				
//				intent1.putExtra("title", "제목제목");
//				intent1.putExtra("msg", "푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시푸시");
//		}
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean callback(String requestId, int result, TPlatformData data) throws Exception {
		if (super.callback(requestId, result, data)) {
			return false;
		}

		if (requestId.equals("getResult")) { 
			try {
				String errorCode = data.getVariable("errorCode");
				String errorMsg = data.getVariable("errorMsg");
				Dataset dsNameCertify = data.getDataset("dsNameCertify");
					
				CommonDialog.showOk(AndroidTest.this, errorCode + "\n" + errorMsg);
			} catch(Exception e) {
				CommonDialog.showOk(AndroidTest.this, e.getMessage());
			}
		} else if(requestId.equals("kakaoReady")) {
			String msg = data.getVariable("kakaPayResult");
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		} else if(requestId.equals("initPwd")) {
			dsCond = data.getDataset("dsCond");
			final String rtCode = data.getVariable("rtCode");
			final String rtMsg = data.getVariable("rtMsg");
			
			Log.i(TAG,"rtCode=" + rtCode);
			Log.i(TAG,"rtMsg=" + rtMsg);
			
			if(rtCode.equals("OK")) {
				Timer timer = new Timer((Integer.parseInt(TmsPolicyVo.getInstance().getNO_CD13()) * 60) + 1);
				
				//타이머 시작
				timer.AuthCodeTimmer();
				
				init_pwd_btn.setText("인증번호재요청");
			}
			CommonDialog.showOk(AndroidTest.this, rtMsg);
		} else if(requestId.equals("chgPwd")) {
			final String rtCode = data.getVariable("rtCode");
			final String rtMsg = data.getVariable("rtMsg");
			
			Log.i(TAG,"rtCode=" + rtCode);
			Log.i(TAG,"rtMsg=" + rtMsg);
			
			CommonDialog.showOk(AndroidTest.this, rtMsg, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(rtCode.equals("OK")) {
						if(timer != null) {
							timer.cancel();
						}
						finish();
					}
				}
			});
		}
		
		return false;
	}
	
	private void initPwd() {
		
		handler.clear();
		
		handler.addVariable("CALL_DIV", "KeyCreate");
		handler.addVariable("ID_AGENT", "A740003859");
		handler.addVariable("NM_AGENT", "태광시스템즈_테스트");
		handler.addVariable("PDA_TEL", "010-4123-9453");
		handler.addVariable("TIME_MIN", TmsPolicyVo.getInstance().getNO_CD13());
		handler.addVariable("KEY_AUTH", "");
		
		handler.call("loginController", "initPwd"); //콜백, 서비스네임, 메서드명
	}
	
	private void pwdChg() {
		handler.clear();
		handler.addVariable("CALL_DIV", "KeyCompare");
		handler.addVariable("ID_AGENT", dsCond.getColumnAsString(0, "ID_AGENT"));
		handler.addVariable("NM_AGENT", dsCond.getColumnAsString(0, "NM_AGENT"));
		handler.addVariable("PDA_TEL", dsCond.getColumnAsString(0, "PDA_TEL"));
		handler.addVariable("KEY_AUTH", dsCond.getColumnAsString(0, "KEY_AUTH"));
		
		handler.call("chgPwd","loginController", "initPwd"); //콜백, 서비스네임, 메서드명
	}
	
	private void getResult() throws IOException, Exception {
		
		fgvBizBean = "com.opaleye.exbill.sms.inter.biz.NameCertifyBiz";//기간계 접속할 경우 사용할 Biz명시
		handler.clear();
		handler.addVariable("ID_CUST", "4024445344");
		handler.smsNewCall("searchCustInfo", fgvBizBean, "searchCustInfo"); //콜백, 서비스네임, 메서드명		
	}
	
	private void kakaoReady() {
//		String ID_SO = ctx.getInputVariable("ID_SO");
//		String UserId = ctx.getInputVariable("UserId");
//		String PhoneNo = ctx.getInputVariable("PhoneNo");
//		String Price = ctx.getInputVariable("Price"); 
//		String Birth = ctx.getInputVariable("Birth"); 
//		String cdItmGrp = ctx.getInputVariable("cdItmGrp"); 
//		String ID_INSERT = ctx.getInputVariable("ID_INSERT");
		
		
		
		
		try {
			
			getResult();
//			handler.clear();
//			handler = new THandler(AndroidTest.this);
//			handler.addVariable("ID_SO", "3200");
//			handler.addVariable("USER_ID", "0000126805");
//			handler.addVariable("PHONE_NO", "01071642496");
//			handler.addVariable("PRICE", "36436");
//			handler.addVariable("BIRTH", "471221");
//			handler.addVariable("CD_ITM_GRP", "00");		//전체
//			handler.addVariable("ID_INSERT", "A740003859");
//			handler.call("payController", "kakaoReady");
		} catch(Exception e) {
		}
	}
	
	/**
	 * @category 기간계 세션을 가져온다  
	 */
	private void getSmsSession() throws IOException {
		handler = new THandler(this);
		handler.smsCall(SmsTmsClient.reqID1,SmsTmsClient.sloginId,SmsTmsClient.spwd,SmsTmsClient.sname);	
	}
	
	/**
	 * @category 연동을 타다가 하나라도 에러가 존재시 에러내용 표시
	 */
	private void printErrorHist() {
		
		String errorCode = "";
		String errorMsg = "";
		
		//하나라도 에러내용이 있는경우 에러메시지 출력
		for(Map<String, String> m : errorHistrory) {
			errorCode = m.get("errorCode");
			errorMsg = m.get("errorMsg");
			
			if("OK".equals(errorCode)) {
				continue;								
			}
			
			//에러가 있는경우 에러메시지 출력 후 에러이력들 모두 삭제
			if(!"0".equals(errorCode)) {
				CommonDialog.showOk(AndroidTest.this, errorMsg);
				errorHistrory = new ArrayList<Map<String, String>>(); 
				break;
			}
		}
	}
	
	private boolean callBackError(String errorCode, String errorMsg) {
		
		String returnMsgTms = "";
		
		//에러코드를 모두 저장한다(모두호출후 에러가 있는경우 마지막에 에러내용을 출력하기 위함)
		Map<String, String> errMap = new HashMap<String, String>();
		errMap.put("errorCode", errorCode);
		errMap.put("errorMsg", errorMsg);
		errorHistrory.add(errMap);
		
		if(errorCode.equals("OK")) {
			return true;
		}
		
		if(errorCode.equals("ERROR")) {
			returnMsgTms = "전산 로그인 오류가 있습니다.\n 헬프데스크(070-8188-0864) 로 문의 부탁드립니다.\n(" + errorMsg + ")";
			CommonDialog.showOk(AndroidTest.this, returnMsgTms);
			
			return false;
		} else if(!errorCode.equals("0")) {
			if(errorCode.equals("-8888888")) {
				returnMsgTms = "세션이 종료되었습니다.\n 헬프데스크(070-8188-0864) 로 문의 부탁드립니다.";
				CommonDialog.showOk(AndroidTest.this, returnMsgTms);
				
				return false;
			} else {
				//기간계에서 던져준 에러표시
				return false;
			}
		}
		
		return true;
	}
	
	private class Timer {
		private static final int MIN = 60;
		private static final int HOUR = MIN * 60;
		private int value = 0;
		private int h = 0, m = 0, s = 0;
		private int mnMilliSecond = 1000;
		private int mnExitDelay = 0;
		private int delay;
		
		
		public Timer(int timerSetup) {
			
			//이전에 수행중이던 타이머는 종료처리
			if(timer != null) {
				timer.cancel();
			}
			
			//타이머(초) 설정
			this.value = timerSetup;
			this.mnExitDelay = timerSetup;
			this.delay = mnExitDelay * mnMilliSecond;
		}
		
		public void AuthCodeTimmer() {
			
			//1초마다 아래함수 호출
			timer = new CountDownTimer(delay, mnMilliSecond) {
				
				@Override
				public void onTick(long l) {
					// TODO Auto-generated method stub
					codeDelayTextView.setText(getTimeFormat(--value));
				}
				
				@Override
				public void onFinish() {
					timer.cancel();
					codeDelayTextView.setText("00:00");
					CommonDialog.showOk(AndroidTest.this, "입력시간이 종료 되었습니다 인증번호를 재요청 해주세요");
				}
			};
			
			//타이머 시작
			timer.start();
		}
		
		public String getTimeFormat(int value) {
			s = value % MIN;
			m = value > HOUR ? value / MIN / HOUR : value / MIN;
			h = value / HOUR;
			
			return String.format("%02d", m) + ":" + String.format("%02d", s);
		}
	}

	@Override
    public void onBackPressed() {
        
		if(timer != null) {
			timer.cancel();
		}
		
		finish();
    }
}

class Item {
	private String key;
	private String value;
	private ItemData itemData;
	
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}
	public ItemData getItemData() {
		return itemData;
	}
	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}
}

class ItemData {
	private String name;
	private String age;
	public String getName() {
		return name;
	}
	public String getAge() {
		return age;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(String age) {
		this.age = age;
	}
}



