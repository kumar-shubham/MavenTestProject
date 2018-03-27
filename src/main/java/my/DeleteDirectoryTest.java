package my;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class DeleteDirectoryTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String path = "/home/kumar/kumar/Softwares/ff/hello world";
		System.out.println(deleteDirectory(path, true));

	}
	
	/**
	 * This method deletes given directory. If the directory is empty then it will delete the directory right away
	 * but if it is not empty then make sure the <b>forceDelete</b> flag is true otherwise it will not delete the directory and will return false.
	 * @param path
	 * @param forceDelete
	 * @return <b>true if directory deleted otherwise false</b>
	 */
	public static boolean deleteDirectory(String path, boolean forceDelete) {
		
		System.out.println("directory to be deleted => " + path + " with forceDelete flag as " + forceDelete);
		
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		
		File directory = new File(path);
		if (directory != null && directory.isDirectory()) {
			File[] files = directory.listFiles();
			System.out.println("Total number of files in the directory is " + files.length);
			if (files.length > 0 && !forceDelete) {
				return false;
			}
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					boolean tempResult = deleteDirectory(file.getAbsolutePath(), forceDelete);
					if (!tempResult) {
						return false;
					}
				} else {
					file.delete();
				}
			}
			directory.delete();
			return true;
		}
		return false;
	}

}
