package com.ppchatlient.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.ppchatcllient.view.ClientLogin;
import com.ppchatcllient.view.FriendChat1;
import com.ppchatcllient.view.FriendList;
import com.yychat.model.Message;

public class ClientReceiverTiread extends Thread {
	
	ObjectInputStream ois;
	  private Socket s;
      public ClientReceiverTiread(Socket s){
    	  this.s=s;
      }
	  public void run() {
			//ObjectInputStream ois;
			while(true){
				try {
				
				ois = new ObjectInputStream(ClientConnect.s.getInputStream());
				Message mess=(Message)ois.readObject();
				String showMessage=mess.getSender()+"对"+mess.getReceiver()+"说"+mess.getContent();
				System.out.println(showMessage);
				if(mess.getMessageType().equals(Message.message_Common)){
					//jta.append(showMessage+"\r\n");
					
					// 在好友界面上显示聊天信息
					//1.如何得到聊天信息
					FriendChat1 friendChat1=(FriendChat1)FriendList.hmfriendChat1.get(mess.getReceiver()+"to"+mess.getSender());
					//2.再显示信习
					friendChat1.appendJta(showMessage);
				}
				//第3步：客户端接受服务器发送来的在线好友信息，然后利用该信息激活在线好友的图标
				if(mess.getMessageType().equals(Message.message_OnlineFriend)){
					System.out.println("在线好友"+mess.getContent());
					
					//激活对应图标,首先要拿到好友列表对象
					
					FriendList FriendList=(FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
					//System.out.println((FriendList)ClientLogin.hmFriendList.get(mess.getReceiver()));
					FriendList.setEnableFriendIcon(mess.getContent());
					
				}
				if(mess.getMessageType().equals(Message.message_NewOnlineFriend)){
					System.out.println("新上线用户的名字是："+mess.getContent());
					FriendList friendList=(FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
					friendList.setEnabledOnlineFriend(mess.getContent());
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
				
			}
	  }
	  }
	