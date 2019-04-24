package com.qqchatserver.controller;

import java.io.*;
import java.net.*;
import java.util.*;

import com.yychat.model.Message;
import com.yychat.model.user;

public class StertServer {
	 public static HashMap hmSocket=new HashMap<String,Socket>();
	String userName;
	Socket s;
	String passWord;
	ServerSocket ss;
	ObjectOutputStream oos;
	public StertServer(){
	try {//捕获异常
		//double 
		ss= new ServerSocket(3456);
		System.out.println("服务器已经启动，监听3456端口");
		while(true){
	    s= ss.accept();//接受连接请求
		System.out .println("连接成功:"+s);
		
		//接受uear
		ObjectInputStream ois=new ObjectInputStream (s.getInputStream());
		 user user=(user)ois.readObject();
		 userName=user.getUserName();
		 passWord=user.getPassName();
		 System.out.println(userName);
		 System.out.println(passWord);
		//密码验证
		 Message mess=new Message();
		 mess.setSender("Senver");
		 mess.setReceiver(passWord);
		
		 if(passWord.equals("123456")){//对象比较
			 mess.setMessageType(Message.message_LoginSuccess);//1为验证通过
		 }else{
			 mess.setMessageType(Message.message_LoginFailure);//0为验证 不通过
		 }
		 oos=new ObjectOutputStream(s.getOutputStream());
		 oos.writeObject(mess);
			//接受聊天
		 if(passWord.equals("123456")){
			 mess.setMessageType(Message.message_NewOnlineFriend);
			 mess.setSender("Server");
			 mess.setContent(this.userName);
			 Set friendSet=hmSocket.keySet();
			 Iterator it=friendSet.iterator();
			 String friendName;
			 while(it.hasNext()){
				 friendName=(String)it.next();
				 mess.setReceiver(friendName);
				 Socket s1=(Socket)hmSocket.get(friendName);
				 oos=new ObjectOutputStream(s1.getOutputStream());
				 oos.writeObject(mess);
			 }
			 
			 hmSocket.put(userName, s);
			 new ServerReceiverThread(s).start();
		 }	
		}
	} catch (IOException e) {
		
		e.printStackTrace();//处理异常
	} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
	}
		
	}
	private void sendMessage(Message mess, Socket s2) {
		// TODO Auto-generated method stub
		
	}
	public void sendMessage(Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
		 oos.writeObject(mess);
	}
}
