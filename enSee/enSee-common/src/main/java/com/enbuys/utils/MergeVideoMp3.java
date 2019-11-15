package com.enbuys.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

	private String ffmpegEXE;
	
	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void convertor(String videoInputPath, String mp3InputPath,
			double seconds, String videoOutputPath) throws Exception {
//		ffmpeg.exe -i 苏州大裤衩.mp4 -i bgm.mp3 -t 7 -y 新的视频.mp4
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(videoInputPath);
		
		command.add("-i");
		command.add(mp3InputPath);

        command.add("-t");
        command.add(String.valueOf(seconds));
		
		command.add("-c:v copy -c:a aac -strict experimental -map 0:v:0 -map 1:a:0");
        // command.add("-y");
		command.add(videoOutputPath);
		String cmd = "";
		for (String c : command) {
            cmd += (c+" ");
		}

        Process process = Runtime.getRuntime().exec(cmd);
		/*ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();*/

		/// 防止出错，导致线程卡主，资源浪费
		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		
		String line = "";
        System.out.println();
		while ( (line = br.readLine()) != null ) {
            System.out.println(line);
		}
		
		if (br != null) {
			br.close();
		}
		if (inputStreamReader != null) {
			inputStreamReader.close();
		}
		if (errorStream != null) {
			errorStream.close();
		}
		
	}

	public static void main(String[] args) {
		MergeVideoMp3 ffmpeg = new MergeVideoMp3("D:\\Major\\ffmpeg\\bin\\ffmpeg.exe");
		try {
			ffmpeg.convertor("D:\\Major\\ffmpeg\\bin\\aaa.mp4", "D:\\Projects\\EnSee-vedios\\bgm\\告白之夜.mp3", 7.1, "D:\\Major\\ffmpeg\\bin\\合并音视频.mp4");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
