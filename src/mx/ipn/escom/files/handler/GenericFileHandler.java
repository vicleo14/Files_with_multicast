package mx.ipn.escom.files.handler;

import java.io.FileNotFoundException;

public interface GenericFileHandler<T> {
	public T readFile(String path,String name) throws FileNotFoundException;
	public void writeFile(String path,String name,byte[] data);
}
