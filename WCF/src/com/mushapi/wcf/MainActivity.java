package com.mushapi.wcf;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	public EditText ipEdit;
	public TextView msgView, jsonView;
	private Handler mainMapActivityHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
			{
				String str = (String) msg.obj;
				// msgView.setText(str);
				msgView.append(str);
				break;
			}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ipEdit = (EditText) this.findViewById(R.id.IPText);
		msgView = (TextView) this.findViewById(R.id.MSGView);
		jsonView = (TextView) this.findViewById(R.id.JSONView);
		ipEdit.setText("http://192.168.1.110:8732");
		new Thread(connectRunnable).start();
	}

	Runnable connectRunnable=new Runnable()
	{
		
		@Override
		public void run()
		{
			// TODO �Զ����ɵķ������
			WebClient client = new WebClient();
			JSONObject json = new JSONObject();
			String result = "ë�߶�ľ�а�!!!";
			try
			{
				// json.put("message", "form shouji");
				result = client.doPost(ipEdit.getText().toString()
						+ C.service.Service_getMsg, null);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// json.put("Name", name);
			Log.v("setMsg", result);

			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			try
			{
				result = client.doPost(ipEdit.getText().toString()
						+ C.service.Service_getData);
				Log.v("getData", result);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// {"d":{"__type":"Data:#Host","Name":"Mush Service","age":20,"array":"","msg":"This is a data form service_HelloData"}}
			//
			// {"d":{"Name":"Mush Moble","__type":"Data:#Host","msg":"This is form cell phone","array":"","age":"20"}}
			result = "�Ҳ�";
			try
			{

				result = client.doPost(ipEdit.getText().toString()
						+ C.service.Service_gettext, "���ү", "application/json");
				// Log.v("Json: ", json.toString());
			}
			catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
			Log.v("gettext", result);

			result = "hehe";
			try
			{
				Data data = new Data();
				data.setName("Mush C");
				data.setAge(21);
				data.setMsg("is form c");
				data.setArray(null);
				result = client.doPost(ipEdit.getText().toString()
						+ C.service.Service_setData, data, "application/json");
				Log.v("Json: ", json.toString());
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// json.put("Name", name);
			Log.v("setData", result);


			// /////////////////////////////////////////////////////////
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void startButton(View view)
	{
		// Toast.makeText(this, "Ĭ��Toast��ʽ", Toast.LENGTH_SHORT).show();
		// Thread mThread = new WebClient("WebClient",
		// ipEdit.getText().toString(), mainMapActivityHandler);
		// mThread.start();
		// �����ݴ���WCF��
		/*
		WebClient client = new WebClient();
		JSONObject json = new JSONObject();
		String result = "ë�߶�ľ�а�!!!";
		try
		{
			// json.put("message", "form shouji");
			result = client.doPost(ipEdit.getText().toString()
					+ C.service.Service_getMsg, null);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// json.put("Name", name);
		Log.v("setMsg", result);
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		try
		{
			result = client.doPost(ipEdit.getText().toString()
					+ C.service.Service_getData);
			Log.v("getData", result);
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// {"d":{"__type":"Data:#Host","Name":"Mush Service","age":20,"array":"","msg":"This is a data form service_HelloData"}}
		//
		// {"d":{"Name":"Mush Moble","__type":"Data:#Host","msg":"This is form cell phone","array":"","age":"20"}}
		result = "�Ҳ�";
		try
		{

			result = client.doPost(ipEdit.getText().toString()
					+ C.service.Service_gettext, "���ү", "application/json");
			// Log.v("Json: ", json.toString());
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		Log.v("gettext", result);
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

		result = "hehe";
		try
		{
			Data data = new Data();
			data.setName("Mush C");
			data.setAge(21);
			data.setMsg("is form c");
			data.setArray(null);
			result = client.doPost(ipEdit.getText().toString()
					+ C.service.Service_setData, data, "application/json");
			Log.v("Json: ", json.toString());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// json.put("Name", name);
		Log.v("setData", result);

		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

		// /////////////////////////////////////////////////////////
*/
		msgView.setText("�Ҳ����");
	}

}
