package com.pocketuniversity.https;

import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebService2 {
	private String NAMESPACE = "http://www.webxml.com.cn/zh_cn/index.aspx";
	private String METHODNAME;
	private String ENDPOINT = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";
	private Map<String, String> param;
	String result;
	private static String TAG = "WebService";

	public WebService2(String mETHODNAME, Map<String, String> param) {
		super();
		METHODNAME = mETHODNAME;
		this.param = param;
	}

	public String getReturnInfo() {
		final String methodName = METHODNAME;
		Log.i(TAG, methodName);
		SoapObject rpc = new SoapObject(NAMESPACE, methodName);
		if(param.size()>0){
			for (Map.Entry<String, String> entry : param.entrySet()) {
				rpc.addProperty(entry.getKey(), entry.getValue());
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE transport = new HttpTransportSE(ENDPOINT);
		transport.debug = true;
		SoapObject object = null;

		int num = 1;
		while (num < 4 && object == null) {
			try {
				transport.call("http://WebXml.com.cn/getWeather", envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (envelope.bodyIn instanceof SoapFault){
				 String str= ((SoapFault) envelope.bodyIn).faultstring;
				 Log.i(TAG, str);
			}else{
			object = (SoapObject) envelope.bodyIn;}
			Log.i(TAG, "调用num=" + num);
			num++;
		}
		
		if (object == null) {
			result = "{'ret':'error','errorMsg':'Service is close'}";
		} else {
			result = object.getProperty(0).toString();
		}
		Log.i(TAG, result);
		return result;

	}
}
