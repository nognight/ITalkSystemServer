package com.nognight.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class IoUtil {
	
	public static void close(ServerSocket ss) {
		if(ss != null) {
			try{
				ss.close();
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}

	public static void close(Socket s) {
		if(s != null) {
			try{
				s.close();
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}

	public static void close(InputStream is) {
		if(is != null) {
			try{
				is.close();// �ر��� ����
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}

	public static void close(OutputStream os) {
		if(os != null) {
			try{
				os.close();// �ر��� ����
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}

	public static void close(RandomAccessFile file) {
		if(file != null) {
			try{
				file.close();// �ر��� ����
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}

	public static void close(Reader file) {
		if(file != null) {
			try{
				file.close();// �ر��� ����
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}

	public static void close(Writer file) {
		if(file != null) {
			try{
				file.close();// �ر��� ����
			} catch(IOException e) {
				// ���ڱ�����û�д����ֵ��������쳣��������в����κδ���
				//������� ־�м�¼һ�µ�ǰ�� ���޷��ر�
			}
		}
	}
}
