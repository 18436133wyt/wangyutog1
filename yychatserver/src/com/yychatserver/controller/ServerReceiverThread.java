package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	public ServerReceiverThread(Socket s){//s是与发送者相对应的服务器Socket对象
		this.s=s;
	}
	
	public void run(){
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Message mess;
		while (true) {
			try {
				ois = new ObjectInputStream(s.getInputStream());
				mess=(Message)ois.readObject();//接收聊天信息
				System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getContent());
				
				Socket s1=(Socket)StartServer.hmSocket.get(mess.getReceiver());//得到了与接收者相对应的服务器Socket对象
				oos=new ObjectOutputStream(s1.getOutputStream());
				oos.writeObject(mess);//转发聊天信息
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
}
