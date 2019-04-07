package mx.ipn.escom.multicast.main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import mx.ipn.escom.files.handler.FileHandlerV;
import mx.ipn.escom.files.handler.GenericFileHandler;

public class Main {

	public static void main(String Args[]) throws IOException
	{
		GenericFileHandler<byte[]> fileHandler=new FileHandlerV();
		byte[] b=fileHandler.readFile("/home/victor/Descargas/","AplicacionesComunicacionesRed-master.zip");
		fileHandler.writeFile("Archivos/", "prueba.zip", b);	
		System.out.println("Termino");
	}
}
