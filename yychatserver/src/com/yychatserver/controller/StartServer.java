package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;
import com.yychat.model.User;

public class StartServer {
	public static HashMap hmSocket=new HashMap<String,Socket>();
	
	ServerSocket ss;
	Socket s;
	String userName;
	String passWord;
	public StartServer(){
		try {//捕获异常
			ss= new ServerSocket(3456);
			System.out.println("服务器已经启动，监听3456端口");
			while(true){//?Thread多线程
				s= ss.accept();//接收客户端连接请求
				System.out.println("连接成功:"+s);
				
				//接收User对象
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User user=(User)ois.readObject();
				userName=user.getUserName();
				passWord=user.getPassWord();
				System.out.println(userName);
				System.out.println(passWord);
				
				//实现密码验证功能
				Message mess=new Message();
				mess.setSender("Server");
				mess.setReceiver(userName);
				if(passWord.equals("123456")){//对象比较
					//告诉客户端密码验证通过的消息，可以创建Message类				
					mess.setMessageType(Message.message_LoginSuccess);//"1"为验证通过				
				}else {
					mess.setMessageType(Message.message_LoginFailure);//"0"为验证不通过		
				}
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(mess);
				
				//在这里接收聊天信息，可不可以？不可以，应该新建一个接收线程
				if(passWord.equals("123456")){
					hmSocket.put(userName, s);
					new ServerReceiverThread(s).start();//就绪
				}				
			}
			
		} catch (IOException e) {
			e.printStackTrace();//处理异常
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
