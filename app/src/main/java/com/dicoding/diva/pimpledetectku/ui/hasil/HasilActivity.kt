package com.dicoding.diva.pimpledetectku.ui.hasil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.diva.pimpledetectku.databinding.ActivityHasilBinding
import com.dicoding.diva.pimpledetectku.ml.Generated
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class HasilActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHasilBinding
//    private var getFile: File? = null
//
//    private val tfImageProcesor by lazy{
//        ImageProcessor.Builder()
//            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
//            .build()
//    }
//
//
//    private val imageTensor = TensorImage(DataType.FLOAT32)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val myFile = intent.getSerializableExtra("picture") as File
//        getFile = myFile
//        val result = BitmapFactory.decodeFile(getFile?.path)
//
//        //load file txt
//        val fileName = "label.txt"
//        val inputString = application.assets.open(fileName).bufferedReader().use{it.readText()}.split("\n")
//
//        var newBitmap = result.copy(Bitmap.Config.ARGB_8888, true)
//        imageTensor.load(newBitmap)
//
//        val resultResize = tfImageProcesor.process(imageTensor)
//
//        val model = Generated.newInstance(this)
//
//        // Runs model inference and gets result.
//        val outputs = model.process(resultResize.tensorBuffer)
//
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//        var max = getMax(outputFeature0.floatArray)
//
//        binding.resultNameTv.text = inputString[max]
//        // Releases model resources if no longer used.
//
//        for (i in 0..5){
//            Log.d("Hasil", outputFeature0.floatArray[i].toString() + "" + inputString[i])
//        }
//        Log.d("qwwqd", outputs.toString())
//
//
//        model.close()
//
//        binding.imageResultIv.setImageBitmap(result)
    }

//    private fun getMax(arr:FloatArray) : Int{
//        var ind = 0;
//        var min = 0.0f;
//
//        for(i in 0..5)
//        {
//            if(arr[i] > min)
//            {
//                ind = i
//                min = arr[i]
//            }
//        }
//        return ind
//    }
}