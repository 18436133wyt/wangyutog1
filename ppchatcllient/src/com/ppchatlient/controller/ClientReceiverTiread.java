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
				String showMessage=mess.getSender()+"��"+mess.getReceiver()+"˵"+mess.getContent();
				System.out.println(showMessage);
				if(mess.getMessageType().equals(Message.message_Common)){
					//jta.append(showMessage+"\r\n");
					
					// �ں��ѽ�������ʾ������Ϣ
					//1.��εõ�������Ϣ
					FriendChat1 friendChat1=(FriendChat1)FriendList.hmfriendChat1.get(mess.getReceiver()+"to"+mess.getSender());
					//2.����ʾ��ϰ
					friendChat1.appendJta(showMessage);
				}
				//��3�����ͻ��˽��ܷ����������������ߺ�����Ϣ��Ȼ�����ø���Ϣ�������ߺ��ѵ�ͼ��
				if(mess.getMessageType().equals(Message.message_OnlineFriend)){
					System.out.println("���ߺ���"+mess.getContent());
					
					//�����Ӧͼ��,����Ҫ�õ������б�����
					
					FriendList FriendList=(FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
					//System.out.println((FriendList)ClientLogin.hmFriendList.get(mess.getReceiver()));
					FriendList.setEnableFriendIcon(mess.getContent());
					
				}
				if(mess.getMessageType().equals(Message.message_NewOnlineFriend)){
					System.out.println("�������û��������ǣ�"+mess.getContent());
					FriendList friendList=(FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
					friendList.setEnabledOnlineFriend(mess.getContent());
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
				
			}
	  }
	  }
	