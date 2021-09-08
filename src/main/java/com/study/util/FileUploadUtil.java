package com.study.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/*@Component*/
public class FileUploadUtil {


    public static String fileUploadTitle(MultipartFile file,String path){
        /*获取当前项目存放的位置*/
        String projectPath = System.getProperty("user.dir");
        /*自定义的路径*/
        String filePath = "/upload/"+path+"/";
        /*获取文件名*/
        String fileName = file.getOriginalFilename();
        String[] split = fileName.split("\\.");
        BufferedOutputStream out = null;
        try{
            File file1 = new File(projectPath + filePath);
            /*判断当前文件加是否存在*/
            if(!file1.exists()){
                //不存在则创建
                file1.mkdirs();
            }
            /*通过UUID生成唯一的资源名，可以防止资源名重复*/
            UUID uuid = UUID.randomUUID();
            /*拼接图片的全路径名*/
            String fileFinalName = projectPath+filePath+uuid+"."+split[1];
            /*将图片持久化到磁盘*/
            out = new BufferedOutputStream(new FileOutputStream(new File(fileFinalName)));
            out.write(file.getBytes());
            out.flush();
            /*返回文件全限定名*/
            return fileFinalName;
        }catch(Exception e){
            System.out.println("上传文件出现异常");
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * fileIn :需要copy的文件
     * fileOut ：copy到的目标目录
     * @param fileIn
     * @param fileOut
     * @return
     */
    public static String copyFile(String fileIn,String fileOut)  {
        String[] fileName = fileIn.split("/");
        String file = fileIn.substring(2,fileIn.length());
        /*获取当前项目存放的位置*/
        String projectPath = System.getProperty("user.dir");
        /*自定义的路径*/
        String filePath = "/upload/";
        /*获取需要拷贝的文件*/
        String fileFinlPath = projectPath+filePath+file;
        /*拷贝的地址*/
        String fileOutPath = projectPath+filePath+fileOut+"/"+fileName[fileName.length-1];
        /*读取文件*/
        File fileInput = new File(fileFinlPath);
        /*输出文件*/
        File fileOutput = new File(fileOutPath);
        BufferedInputStream fis = null;
        BufferedOutputStream out = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(fileInput));
             out = new BufferedOutputStream(new FileOutputStream(fileOutPath));
            byte[] bytes = new byte[1024];
            int len =0;
            while ((len = fis.read(bytes))!=-1){
                out.write(bytes,0,len);
                out.flush();
            }
            out.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileOutPath;
    }


}
