package mx.ipn.escom.sockets.datagram;

import java.io.Serializable;

public class FileTransporter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int n;
	private byte[] bytes;
	private int t;
	private int size;
	private String nombre;
	/*
		bytes: Buffer de los datos.
		n: numero de archivo.
		t: numero de paquetes
		size: numero de bytes
	*/
	public FileTransporter(byte[] bytes, int n,int t,String nombre,int size)
	{
		this.n=n;
		this.t=t;
		this.bytes=bytes;
		this.nombre=nombre;
		this.size=size;
		//System.out.println("El archivo creado es el numero:"+n);
	}
	public byte[] getBytes()
	{
		return bytes;
	}
	public int getT()
	{
		return t;
	}
	public int getN()
	{
		return n;
	}
	public String getNombre	()
	{
		return nombre;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setN(int n) {
		this.n = n;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public void setT(int t) {
		this.t = t;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
