package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络操作类，会开启一线程访问网络资源
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
					// 设置提交请求的方式
					connection.setRequestMethod("GET");
					// 设置超时响应
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					// 获得字节输入流
					InputStream in = connection.getInputStream();
					// 将字节流包装成字符流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					// 读取数据
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
					// 关掉连接
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

}
