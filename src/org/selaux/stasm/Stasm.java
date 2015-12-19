package org.selaux.stasm;

import org.opencv.core.Mat;

public class Stasm {
  public static String NATIVE_LIBRARY_NAME = "stasm-jni";

  private String modelDirectory;

  public Stasm(String modelDirectory) {
    this.modelDirectory = modelDirectory;
  }

  public void setModelDirectory(String modelDirectory) {
    this.modelDirectory = modelDirectory;
  }

  public String getModelDirectory() {
    return this.modelDirectory;
  }

  public Mat search(Mat input) {
      Mat output = new Mat();

      this.nativeSearch(input.getNativeObjAddr(), output.getNativeObjAddr());

      return output;
  }

  protected native synchronized void nativeSearch(long input, long output);
}
