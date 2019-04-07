package mx.ipn.escom.files.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandlerV implements GenericFileHandler<byte[]> {

	@Override
	public byte[] readFile(String path, String name) throws FileNotFoundException {
		byte[] b=null;
		try
		{
			File f=new File(path+name);
			if(!f.exists())
			{
				f=null;
				System.out.println("FileHnadlerV::El archivo no existe");
			}
			else
			{
				b=new byte[(int)f.length()];
				DataInputStream dis=new DataInputStream(new FileInputStream(f.getAbsolutePath()));
				dis.read(b);
				dis.close();
			}
			
		}
		catch(IOException iex)
		{
			
		}
		return b;
	}

	@Override
	public void writeFile(String path, String name, byte[] data) {
		try
		{
			File pathF=new File(path);
			if(!pathF.exists() || !pathF.isDirectory())
			{
				pathF.mkdir();
				
			}
			File file=new File(path+name);
			FileOutputStream fos=new FileOutputStream(file);
			DataOutputStream dos=new DataOutputStream(fos);
			dos.write(data);
			dos.close();
			fos.close();
		}
		catch(IOException iex)
		{
			iex.printStackTrace();
		}

	}

}
