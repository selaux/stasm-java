import java.io.File

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.CvType
import org.opencv.core.Scalar
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

import org.selaux.stasm.Stasm

object SimpleSample {
  def main(args: Array[String]) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    System.loadLibrary(Stasm.NATIVE_LIBRARY_NAME);

    var filename = new File("./data/lena.jpg").getCanonicalPath()
    println(filename)

    var im = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    println(im)

    var stasm = new Stasm("/usr/share/opencv/haarcascades/")
    var landmarks = stasm.search(im)
    println(landmarks)

    for ( row <- 0 to landmarks.rows-1 ) {
      var landmark_x = scala.math.round(landmarks.get(row, 0)(0)).toInt
      var landmark_y = scala.math.round(landmarks.get(row, 1)(0)).toInt
      im.put(landmark_x, landmark_y, Array(255.toByte, 255.toByte, 255.toByte))
    }

    filename = new File("./data/landmarks.jpg").getCanonicalPath()
    Highgui.imwrite(filename, im)
  }
}
