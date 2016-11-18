package com.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 * 将svg转换为png格式的图片
 * 
 * @author zjn
 * @date 2016年11月17日
 */
public class SvgToPngUtil {

	public static void convertToPNG(String inputPath, String outputPath) {
		PNGTranscoder t = new PNGTranscoder();
		t.addTranscodingHint(PNGTranscoder.KEY_GAMMA, new Float(.8));
		String svgURI;
		OutputStream ostream = null;
		try {
			svgURI = new File(inputPath).toURI().toString();
			TranscoderInput input = new TranscoderInput(svgURI);
			try {
				ostream = new FileOutputStream(outputPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			TranscoderOutput output = new TranscoderOutput(ostream);
			try {
				t.transcode(input, output);
			} catch (TranscoderException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ostream.flush();
			ostream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		convertToPNG("E:/svg.svg", "E:/a.png");
	}
}