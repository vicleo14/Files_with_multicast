package mx.ipn.escom.multicast.main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;

import mx.ipn.escom.files.handler.FileHandlerV;
import mx.ipn.escom.files.handler.GenericFileHandler;
import mx.ipn.escom.sockets.datagram.GenericSocket;
import mx.ipn.escom.sockets.datagram.MulticastS;

public class Main2 {

	public static void main(String Args[]) throws IOException
	{
		GenericSocket envia=new MulticastS();
		
		JFileChooser jfc=new JFileChooser();
		int r=jfc.showOpenDialog(null);
		if(r==JFileChooser.APPROVE_OPTION)
		{
			File f=jfc.getSelectedFile();
			envia.sendFile(f);
		}
		//		System.out.println("Termino");
	}
}
