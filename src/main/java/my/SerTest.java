package my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class SerTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		GenericContainerEntity entity = new GenericContainerEntity();
		
//		entity.setAccountNumber("xyz123");
//		
//		List<DataField> dataFields = new ArrayList<>();
//		dataFields.add(new DataField("name", "kumar"));
//		dataFields.add(new DataField("age", "25"));
//		dataFields.add(new DataField("place", "pune"));
//		
//		entity.setDataFields(dataFields);
//		
//		storeResponse(entity);
		
		entity = getObjects("/home/kumar/kumar/objects/" + "kcdW9fCavGjX" + "/object/");
		
		System.out.println(entity.getAccountNumber());
		System.out.println(entity.getDataFields().size());
		System.out.println(entity.getDataFields());
		
	}
	
	private static GenericContainerEntity getObjects(String objectPath) {
		String filename = objectPath + "account.ser";

		FileInputStream fis = null;
		ObjectInputStream in = null;
		GenericContainerEntity account = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			account = (GenericContainerEntity) in.readObject();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return account;
	}
	
	
	private static void storeResponse(GenericContainerEntity entity) throws Exception {
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String objectPath =	"/home/kumar/kumar/objects/" + randomString + "/object/";
		String filename = objectPath + "account.ser";

		File directory = new File(objectPath	);
		if(!directory.exists()){
			directory.mkdirs();
		}

		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(entity);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		
	}

}
