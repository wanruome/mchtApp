/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月18日 下午5:19:06
 */
package com.ruomm.base.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {
	public final static String FILE_EXTENSION_SEPARATOR = ".";

	/**
	 * 文件读取
	 *
	 * @param filePath
	 *            -文件路径
	 * @param charsetName
	 *            -编码方式
	 * @return 文件读取结果
	 */
	public static String readFile(String filePath, String charsetName) {
		String content = null;
		try {
			File file = new File(filePath);
			if (null != file && file.exists() && file.isFile()) {
				content = readInputStream(new FileInputStream(file));
			}
			else {
				content = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			content = null;
		}
		return content;
	}

	public static List<String> readFileToList(String filePath, String charsetName) {

		try {
			return readInputStreamToList(new FileInputStream(new File(filePath)));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取文件
	 *
	 * @param is
	 *            -输入流
	 * @param isAutoClose
	 *            -是否自动关闭输入流
	 * @return 输入流读取读取结果
	 */
	public static String readInputStream(InputStream is) {
		if (null == is) {
			return null;
		}
		BufferedReader bufreader = null;
		String content = null;
		try {
			boolean isFirst = true;
			StringBuilder fileContent = new StringBuilder("");
			bufreader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = bufreader.readLine()) != null) {
				if (isFirst) {
					isFirst = false;
					fileContent.append(line);
				}
				else {
					fileContent.append("\n" + line);
				}

			}
			content = new String(fileContent);
		}
		catch (Exception ereader) {
			ereader.printStackTrace();
		}
		finally {
			if (bufreader != null) {
				try {
					bufreader.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return content;

	}

	public static List<String> readInputStreamToList(InputStream is) {
		if (null == is) {
			return null;
		}
		BufferedReader bufreader = null;
		List<String> realContent = null;
		try {
			List<String> fileContent = new ArrayList<String>();
			bufreader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = bufreader.readLine()) != null) {
				fileContent.add(line);
			}
			realContent = fileContent;

		}
		catch (Exception ereader) {
			ereader.printStackTrace();
		}
		finally {
			if (null != bufreader) {
				try {
					bufreader.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return realContent;
	}

	/**
	 * 写入文件
	 *
	 * @param 文件路径
	 * @param 内容
	 * @param 追加
	 *            -被追加，如果是真的，写文件的末尾，否则文件中明确的内容，写进去
	 * @return 返回true
	 * @throws RuntimeException
	 *             if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(String filePath, String content, boolean append) {
		FileWriter fileWriter = null;
		boolean flag = false;
		try {
			if (makeDirs(filePath)) {
				File file = new File(filePath);
				fileWriter = new FileWriter(file, append);
				fileWriter.write(content);
				flag = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (null != fileWriter) {
				try {
					fileWriter.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 写入文件
	 *
	 * @param 文件路径
	 * @param 内容
	 * @param 追加
	 *            -被追加，如果是真的，写文件的末尾，否则文件中明确的内容，写进去
	 * @return 返回true
	 * @throws RuntimeException
	 *             if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(File file, String content, boolean append) {
		if (null == file || file.isDirectory()) {
			return false;
		}
		FileWriter fileWriter = null;
		boolean flag = false;
		try {
			fileWriter = new FileWriter(file, append);
			fileWriter.write(content);
			flag = true;

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (null != fileWriter) {
				try {
					fileWriter.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static boolean writeFile(String filePath, InputStream stream, boolean append) {
		return writeFile(filePath != null ? new File(filePath) : null, stream, append);
	}

	/**
	 * 写入文件
	 *
	 * @param 文件
	 *            -
	 * @param 流
	 *            -
	 * @return
	 * @see {@link #writeFile(File, InputStream, boolean)}
	 */
	public static boolean writeFile(File file, InputStream stream) {
		return writeFile(file, stream, false);
	}

	/**
	 * 写入文件
	 *
	 * @param file
	 *            -要打开用于写入的文件。
	 * @param stream
	 *            -输入流
	 * @param append
	 *            -如果属实，那么字节将被写入到文件的末尾而不是开头
	 * @return 返回true
	 * @throws RuntimeException
	 *             if an error occurs while operator FileOutputStream
	 */
	public static boolean writeFile(File file, InputStream stream, boolean append) {
		// OutputStream outStream = null;
		BufferedOutputStream outStream = null;
		boolean flag = false;
		try {
			makeDirs(file.getParent());
			// outStream = new FileOutputStream(file, append);
			outStream = new BufferedOutputStream(new FileOutputStream(file, append));
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				outStream.write(data, 0, length);
			}
			outStream.flush();
			flag = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (null != outStream) {
				try {
					outStream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != stream) {
				try {
					stream.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 创建此文件的文件名结尾，包括创建该目录所需的完整目录路径的目录。 <br/>
	 * <ul>
	 * <strong>注意事项：</strong>
	 * <li>makeDirs("C:\\Users\\Tools") 只能创建的用户文件夹</li>
	 * <li>makeFolder("C:\\Users\\Tools\\") 可以创建Tools文件夹</li>
	 * </ul>
	 *
	 * @param 文件路径
	 *            -
	 * @return 如果必要的目录已创建或目标目录已经存在，目录假的不能创建。
	 *         <ul>
	 *         <li>如果getFolderName（字符串）返回null，返回false</li>
	 *         <li>如果目标目录已经存在，则返回true</li>
	 *         </ul>
	 */
	public static boolean makeDirs(String filePath) {
		String folderName = getFolderName(filePath);
		if (StringUtils.isEmpty(folderName)) {
			return false;
		}

		File folder = new File(folderName);
		return folder.exists() && folder.isDirectory() ? true : folder.mkdirs();
	}

	/**
	 * 从路径获取文件夹名称
	 *
	 * <pre>
	 *      getFolderName(null)               =   null
	 *      getFolderName("")                 =   ""
	 *      getFolderName("   ")              =   ""
	 *      getFolderName("a.mp3")            =   ""
	 *      getFolderName("a.b.rmvb")         =   ""
	 *      getFolderName("abc")              =   ""
	 *      getFolderName("c:\\")              =   "c:"
	 *      getFolderName("c:\\a")             =   "c:"
	 *      getFolderName("c:\\a.b")           =   "c:"
	 *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
	 *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
	 *      getFolderName("/home/admin")      =   "/home"
	 *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
	 * </pre>
	 *
	 * @param 文件路径
	 *            -
	 * @return
	 */
	public static String getFolderName(String filePath) {

		if (StringUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return filePosi == -1 ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * 从路径获取文件名，包括后缀
	 *
	 * <pre>
	 *      getFileName(null)               =   null
	 *      getFileName("")                 =   ""
	 *      getFileName("   ")              =   "   "
	 *      getFileName("a.mp3")            =   "a.mp3"
	 *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	 *      getFileName("abc")              =   "abc"
	 *      getFileName("c:\\")              =   ""
	 *      getFileName("c:\\a")             =   "a"
	 *      getFileName("c:\\a.b")           =   "a.b"
	 *      getFileName("c:a.txt\\a")        =   "a"
	 *      getFileName("/home/admin")      =   "admin"
	 *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	 * </pre>
	 *
	 * @param 文件路径
	 *            -
	 * @return 从路径文件名，包括后缀
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return filePosi == -1 ? filePath : filePath.substring(filePosi + 1);
	}

	/**
	 * 从路径获取文件名，不包括后缀
	 *
	 * <pre>
	 *      getFileNameWithoutExtension(null)               =   null
	 *      getFileNameWithoutExtension("")                 =   ""
	 *      getFileNameWithoutExtension("   ")              =   "   "
	 *      getFileNameWithoutExtension("abc")              =   "abc"
	 *      getFileNameWithoutExtension("a.mp3")            =   "a"
	 *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
	 *      getFileNameWithoutExtension("c:\\")              =   ""
	 *      getFileNameWithoutExtension("c:\\a")             =   "a"
	 *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
	 *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
	 *      getFileNameWithoutExtension("/home/admin")      =   "admin"
	 *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
	 * </pre>
	 *
	 * @param 文件路径
	 *            -
	 * @return 从路径文件名，不包括后缀
	 * @see
	 */
	public static String getFileNameWithoutExtension(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (filePosi == -1) {
			return extenPosi == -1 ? filePath : filePath.substring(0, extenPosi);
		}
		if (extenPosi == -1) {
			return filePath.substring(filePosi + 1);
		}
		return filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1);
	}

	/**
	 * 获取文件的后缀从路径
	 *
	 * <pre>
	 *      getFileExtension(null)               =   ""
	 *      getFileExtension("")                 =   ""
	 *      getFileExtension("   ")              =   "   "
	 *      getFileExtension("a.mp3")            =   "mp3"
	 *      getFileExtension("a.b.rmvb")         =   "rmvb"
	 *      getFileExtension("abc")              =   ""
	 *      getFileExtension("c:\\")              =   ""
	 *      getFileExtension("c:\\a")             =   ""
	 *      getFileExtension("c:\\a.b")           =   "b"
	 *      getFileExtension("c:a.txt\\a")        =   ""
	 *      getFileExtension("/home/admin")      =   ""
	 *      getFileExtension("/home/admin/a.txt/b")  =   ""
	 *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
	 * </pre>
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileExtension(String filePath) {
		if (StringUtils.isBlank(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (extenPosi == -1) {
			return "";
		}
		return filePosi >= extenPosi ? "" : filePath.substring(extenPosi + 1);
	}

	// 获取安卓路径
	public static String getPathContext(Context mContext) {
		String path = mContext.getFilesDir().getParent();
		return path;
	}

	public static String getPathExternal() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		else {
			return null;
		}
	}

	public static String getPathExternalFile(Context mContext) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			// return mContext.getExternalFilesDir(FILE_EXTENSION_SEPARATOR).getPath();
			String dirPath = mContext.getExternalFilesDir(FILE_EXTENSION_SEPARATOR).getPath();
			if (TextUtils.isEmpty(dirPath)) {
				dirPath = getPathExternal() + File.separator + "Android" + File.separator + "data" + File.separator
						+ mContext.getPackageName() + File.separator + "files";
			}

			return dirPath;
		}
		else {
			return null;
		}
	}

	public static String getPathExternalCache(Context mContext) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			String dirPath = mContext.getExternalCacheDir().getPath();
			if (TextUtils.isEmpty(dirPath)) {
				dirPath = getPathExternal() + File.separator + "Android" + File.separator + "data" + File.separator
						+ mContext.getPackageName() + File.separator + "cache";
			}

			return dirPath;
			// return Environment.getExternalStorageDirectory().getPath();
		}
		else {
			return null;
		}
	}

	public static String getPathContext(Context mContext, String filePath) {
		String pathParent = getPathContext(mContext);
		return pathParent + File.separator + filePath;
	}

	public static String getPathExternal(String filePath) {
		String pathParent = getPathExternal();
		if (TextUtils.isEmpty(pathParent)) {
			return null;
		}
		return pathParent + File.separator + filePath;

	}

	public static String getPathExternalCache(Context mContext, String filePath) {
		String pathParent = getPathExternalCache(mContext);
		if (TextUtils.isEmpty(pathParent)) {
			return null;
		}
		return pathParent + File.separator + filePath;
	}

	public static String getPathExternalFile(Context mContext, String filePath) {
		String pathParent = getPathExternalFile(mContext);
		if (TextUtils.isEmpty(pathParent)) {
			return null;
		}
		return pathParent + File.separator + filePath;
	}

	public static File createDir(String dirPath) {
		if (TextUtils.isEmpty(dirPath)) {
			return null;
		}
		File file = new File(dirPath);
		if (file.exists() && file.isDirectory()) {
			return file;
		}
		else if (file.exists()) {
			return null;
		}
		else {
			if (file.mkdirs()) {
				return file;
			}
			else {
				return null;
			}
		}
	}

	public static File createFile(String filepath) {
		if (TextUtils.isEmpty(filepath)) {
			return null;
		}
		if (filepath.endsWith(File.separator)) {
			return createDir(filepath);
		}

		int index = filepath.lastIndexOf(File.separator);
		if (index < 0) {
			return null;
		}
		else if (index == filepath.length() - 1) {
			return createDir(filepath);
		}

		String dirpath = filepath.substring(0, index);
		String filename = filepath.substring(index, filepath.length());
		File filedir = createDir(dirpath);
		if (null == filedir) {
			return null;
		}
		else {
			return new File(filedir.getPath() + File.separator + filename);
		}

	}

	public static File createDirInContext(Context mContext, String dirpath) {
		return createDir(getPathContext(mContext, dirpath));
	}

	public static File createDirInExternal(String dirpath) {
		return createDir(getPathExternal(dirpath));
	}

	public static File createDirInExternalCache(Context mContext, String filepath) {

		return createDir(getPathExternalCache(mContext, filepath));
	}

	public static File createDirInExternalFile(Context mContext, String filepath) {

		return createDir(getPathExternalFile(mContext, filepath));
	}

	public static File createFileInContext(Context mContext, String filepath) {
		return createFile(getPathContext(mContext, filepath));
	}

	public static File createFileInExternal(String filepath) {

		return createFile(getPathExternal(filepath));
	}

	public static File createFileInExternalCache(Context mContext, String filepath) {

		return createFile(getPathExternalCache(mContext, filepath));
	}

	public static File createFileInExternalFile(Context mContext, String filepath) {

		return createFile(getPathExternalFile(mContext, filepath));
	}

	public static boolean delFilesInContext(Context mContext, String filepath) {
		return delAllFile(getPathContext(mContext, filepath));
	}

	public static boolean delFilesInExternal(Context mContext, String filepath) {

		return delAllFile(getPathExternal(filepath));

	}

	public static boolean delFilesInExternalCache(Context mContext, String filepath, boolean isAbsPath) {
		return delAllFile(getPathExternalCache(mContext, filepath));
	}

	public static boolean delFilesInExternalFile(Context mContext, String filepath, boolean isAbsPath) {
		return delAllFile(getPathExternalFile(mContext, filepath));
	}

	public static boolean delAllFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		if (null == file || !file.exists()) {
			return false;
		}
		if (file.isFile()) {
			return file.delete();
		}
		boolean flag = false;
		String[] tempList = file.list();
		if (null == tempList || tempList.length < 1) {
			tempList = null;
			return true;
		}
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			}
			else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		temp = null;
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
			myFilePath = null;

		}
		catch (Exception e) {
		}
	}

	/**
	 * 计算文件或者文件夹的大小 ，单位 MB
	 *
	 * @param file
	 *            要计算的文件或者文件夹 ， 类型：java.io.File
	 * @return 大小，单位：Bytes
	 */
	public static long getSizeBytes(File file) {

		// 判断文件是否存在

		if (null != file && file.exists()) {
			// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
			if (!file.isFile()) {
				// 获取文件大小
				File[] fl = file.listFiles();
				long ss = 0;
				for (File f : fl) {
					ss += getSizeBytes(f);
				}
				return ss;
			}
			else {
				long ss = file.length();
				return ss;
			}
		}
		else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0;
		}
	}

	/**
	 * 计算文件或者文件夹的大小 ，单位 MB
	 *
	 * @param file
	 *            要计算的文件或者文件夹 ， 类型：java.io.File
	 * @return 大小，单位：MB
	 */
	public static double getSizeMB(File file) {

		// 判断文件是否存在

		if (null != file && file.exists()) {
			// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
			if (!file.isFile()) {
				// 获取文件大小
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl) {
					ss += getSizeMB(f);
				}
				return ss;
			}
			else {
				double ss = (double) file.length() / 1024 / 1024;
				// System.out.println(file.getName() + " : " + ss + "MB");
				return ss;
			}
		}
		else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getFilenameByTime(String filehead, String filetype) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String string = new String();
		string = filehead + dateFormat.format(date) + filetype;
		return string;
	}

	public static boolean saveBitmap(Bitmap mBitmap, File file, CompressFormat compressFormat) {
		BufferedOutputStream bufOut = null;
		boolean isTrue = false;
		try {
			bufOut = new BufferedOutputStream(new FileOutputStream(file));
			mBitmap.compress(compressFormat, 100, bufOut);
			bufOut.flush();
			isTrue = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {

			if (null != bufOut) {
				try {
					bufOut.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isTrue;
	}

	public static boolean saveBitmap(Bitmap mBitmap, File file) {
		return saveBitmap(mBitmap, file, CompressFormat.JPEG);
	}

	public static boolean saveBitmap(Bitmap bitMap, String filepath, CompressFormat compressFormat) {
		return saveBitmap(bitMap, new File(filepath), compressFormat);
	}

	public static boolean saveBitmap(Bitmap bitMap, String filepath) {
		return saveBitmap(bitMap, new File(filepath), CompressFormat.JPEG);
	}

	public static boolean copyFile(File srcfile, File desfile) {
		if (!srcfile.exists()) {
			return false;
		}
		BufferedInputStream bufis = null;
		BufferedOutputStream bufos = null;
		boolean isTrue = false;
		try {
			bufis = new BufferedInputStream(new FileInputStream(srcfile));
			bufos = new BufferedOutputStream(new FileOutputStream(desfile));
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = bufis.read(buffer)) > 0) {
				bufos.write(buffer, 0, count);
			}
			bufos.flush();
			isTrue = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (null != bufis) {
				try {
					bufis.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != bufos) {
				try {
					bufos.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isTrue;

	}
}
