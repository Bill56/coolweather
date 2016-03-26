package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ��������࣬�Ὺ��һ�̷߳���������Դ
 * 
 * @author Bill56
 * 
 */
public class HttpUtil {

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					// �����ύ����ķ�ʽ
					connection.setRequestMethod("GET");
					// ���ó�ʱ��Ӧ
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					// ����ֽ�������
					InputStream in = connection.getInputStream();
					// ���ֽ�����װ���ַ���
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					// ��ȡ����
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (MalformedURLException e) {
					if (listener != null) {
						listener.onError(e);
					}
				} catch (IOException e) {
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					// �ص�����
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

}
