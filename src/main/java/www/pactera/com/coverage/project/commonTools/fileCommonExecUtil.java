package www.pactera.com.coverage.project.commonTools;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class fileCommonExecUtil {
    /**
     * 创建文件夹
     *
     * @param filePath
     */
    public static void createFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void copyFolder(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        if(!new File(targetDir).exists()){
            (new File(targetDir)).mkdirs();
        }
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                FileCopyUtils.copy(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                String dir1 = sourceDir + File.separator + file[i].getName();
                String dir2 = targetDir + File.separator + file[i].getName();
                copyFolder(dir1, dir2);
            }
        }
    }

    public static void copyFile(String oldFilePath, String newFilePath) {
        if (StringUtils.isEmpty(oldFilePath) || StringUtils.isEmpty(newFilePath)) {
            return;
        }
        try {
            InputStream input = new FileInputStream(oldFilePath);
            FileOutputStream output = new FileOutputStream(newFilePath);
            FileCopyUtils.copy(input, output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String JointPath(String basePath, String project, String version,String separator_flag) {
        StringBuffer stringBuffer = new StringBuffer(basePath);
        stringBuffer.append(separator_flag);
        stringBuffer.append(project);
        stringBuffer.append(separator_flag);
        stringBuffer.append(version);
        stringBuffer.toString();
        String str = String.valueOf(stringBuffer);
        return str;
    }

    public static String generateRandomNum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dataUuid = simpleDateFormat.format(new Date());
        StringBuffer stringBuffer = new StringBuffer(30);
        stringBuffer.append(dataUuid).append(getRandom(16));
        String randomNumber = stringBuffer.toString();
        return randomNumber;
    }

    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min;
        return rangeLong;
    }

    public static void main(String[] args) {

        System.out.println(generateRandomNum());
    }

}
