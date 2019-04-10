package mx.ipn.escom.sockets.datagram;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.FileHandler;

import mx.ipn.escom.files.handler.FileHandlerV;
import mx.ipn.escom.files.handler.GenericFileHandler;

public class MulticastS implements GenericSocket {

	protected InetAddress group;
	protected int port;
	protected MulticastSocket multicastSocket;
	protected String inetAddress;
	protected boolean reuseAddress;
	protected int ttl;
	protected int maxSize=65535;
	public MulticastS()
	{
		try
		{
			this.inetAddress="228.1.1.1";
			this.ttl=128;
			this.port =9999;
			this.reuseAddress=true;
			this.multicastSocket=new MulticastSocket(port);
			this.multicastSocket.setTimeToLive(ttl);
			
			this.multicastSocket.setReuseAddress(reuseAddress);
			this.group=InetAddress.getByName(inetAddress);
			this.multicastSocket.joinGroup(group);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public MulticastS(String inetAddress,int port)
	{
		try
		{
			this.inetAddress=inetAddress;
			this.port =port;
			this.reuseAddress=true;
			this.multicastSocket=new MulticastSocket(port);
			this.multicastSocket.setReuseAddress(reuseAddress);
			this.group=InetAddress.getByName(inetAddress);
			this.multicastSocket.joinGroup(group);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public MulticastS(String inetAddress,int port,boolean reuseAddress)
	{
		try
		{
			this.inetAddress=inetAddress;
			this.port =port;
			this.reuseAddress=reuseAddress;
			this.multicastSocket=new MulticastSocket(port);
			this.multicastSocket.setReuseAddress(reuseAddress);
			this.group=InetAddress.getByName(inetAddress);
			this.multicastSocket.joinGroup(group);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public MulticastS(String inetAddress,int port,boolean reuseAddress, int ttl)
	{
		try
		{
			this.inetAddress=inetAddress;
			this.ttl=ttl;
			this.port =port;
			this.reuseAddress=reuseAddress;
			
			this.multicastSocket=new MulticastSocket(port);
			this.multicastSocket.setTimeToLive(ttl);
			this.multicastSocket.setReuseAddress(reuseAddress);
			this.group=InetAddress.getByName(inetAddress);
			this.multicastSocket.joinGroup(group);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void sendObject(Object obj)
	{
		try 
		{
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
			byte[]b2=baos.toByteArray();
			DatagramPacket p=new DatagramPacket(b2,b2.length,group,port);
			multicastSocket.send(p);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Object receiveObject() throws ClassNotFoundException {
		Object obj=null;
		try 
		{
			DatagramPacket p = new DatagramPacket(new byte[maxSize],maxSize);
			multicastSocket.receive(p);//Bloqueante
			byte[] b=p.getData();
			ByteArrayInputStream bais=new ByteArrayInputStream(b);
			ObjectInputStream ois=new ObjectInputStream(bais);
			obj=ois.readObject();
		}
		catch(IOException iex)
		{
			
		}
		return obj;
	}

	@Override
	public void sendFile(File file) 
	{
		int sizeData=512;
		try
		{
			for(int k=0;k<3;k++)
			{
				DataInputStream dis=new DataInputStream(new FileInputStream(file.getAbsolutePath()));
				String name=file.getName()+";";
				long sizeFile=file.length();
				System.out.println("Tamanio de archivo en sendFile(MulticastS)::"+file.length());
				long  read=0,n=0;
				int i=0;
				int total=(int)sizeFile/sizeData;
				byte[] bfcad=name.getBytes();
				//sendBytes(bfcad);
				while(sizeFile>read)
				{
					byte[] b=new byte[sizeData];
					n=dis.read(b);
					System.out.println("Tamanio de b en MulticastS:sendFile::"+n);
					FileTransporter d=new FileTransporter(b,i,total,name,(int)n);
					System.out.println("Tamanio de b en MulticastS:FileTransport::"+d.getBytes().length);
					ByteArrayOutputStream baos=new ByteArrayOutputStream();
					ObjectOutputStream oos=new ObjectOutputStream(baos);
					oos.writeObject(d);
					oos.flush();
					byte[]b2=baos.toByteArray();
					sendBytes(b2);
					//System.out.println("Se envio "+i);
					read+=n;
					i++;
				}
				dis.close();
			}
			System.gc();
		}
		catch(IOException ex)
		{
			System.out.println("MulticastSError::"+ex.toString());
			ex.printStackTrace();
		}

	}
	@Override
	public void receiveFile(String pathToSave) {
		boolean[] pieces;
		try
		{
			FileTransporter[] filePieces=null;
			boolean isComplete=false;
			DatagramPacket p=new DatagramPacket(new byte[maxSize]/*Tamnio maximo de UDP*/,maxSize);
			multicastSocket.receive(p);//Bloqueante
			//Obtiene una cadena
			byte[] b;//=p.getData();
			String name="";//=new String(b);
			//String[] cads=name.split(";");
			//name=cads[0];
			
			while(!isComplete)
			{
				
				p=new DatagramPacket(new byte[maxSize]/*Tamnio maximo de UDP*/,maxSize);
				multicastSocket.receive(p);//Bloqueante
				b=p.getData();
				ByteArrayInputStream bais=new ByteArrayInputStream(b);
				ObjectInputStream ois=new ObjectInputStream(bais);
				
				FileTransporter d=(FileTransporter)ois.readObject();
				
				
				//Inicializamos el buffer si es la primera vez
				if( filePieces==null)
				{
					filePieces=new FileTransporter[d.getT()+1];
					name=d.getNombre();
					String[] cads=name.split(";");
					name=cads[0];
				}
				
				System.out.println("Paquete "+d.getN()+" de "+d.getT());
				//Guarda el paquete si no lo había recibido
				//if(filePieces[d.getN()]==null)
				//{
					filePieces[d.getN()]=d;
					//System.out.println("Recibe "+d.getN());
				//}
				//Si es el ultimo paquete esperado verifica que estén todos
				if(d.getN()==d.getT())
				{
					for(int k=0;k<filePieces.length;k++)
					{
						if(filePieces[k]==null)
						{
							isComplete=false;
							break;
						}
						else
						{
							isComplete=true;
							//System.out.println("Esta completo");
						}
					}
					//Si no faltal espacios rompe el ciclo infinito.
					if(isComplete)
					{
						System.out.println("Recibio todos. Generando archivo");
						break;
					}						
				}
				
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			
			for(int k=0;k<filePieces.length;k++) 
			{
				System.out.println("Aqui si llega con i="+k);
				//System.out.println("Tamainio archivo:"+filePieces[k].getBytes().length);
				outputStream.write(filePieces[k].getBytes(),0,filePieces[k].getSize());
			}
			byte[] completeFile=outputStream.toByteArray();
			
			//File file=new File(pathToSave+name);
			//System.out.println(file.getAbsolutePath());
			GenericFileHandler<byte[]> fh=new FileHandlerV();
			fh.writeFile(pathToSave, name, completeFile);
			//cads=null;
			b=null;
			System.out.println("Termino de recibir archivo:"+name);

			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
	private void sendBytes(byte[] b)
	{
		try
		{
			System.out.print(b.length);
			DatagramPacket p=new DatagramPacket(b,b.length,group,port);
			multicastSocket.send(p);
			System.out.println("Se envio");
			p=null;
			System.gc();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	

}
