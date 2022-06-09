package com.dicoding.diva.pimpledetectku.ui.hasil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.diva.pimpledetectku.databinding.ActivityHasilBinding
import com.dicoding.diva.pimpledetectku.ml.Generated
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class HasilActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHasilBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myFile = intent.getSerializableExtra("picture") as File
        getFile = myFile
        val result = BitmapFactory.decodeFile(getFile?.path)

        //load file txt
        val fileName = "List Barang.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use{it.readText()}.split("\n")

        var resized = Bitmap.createScaledBitmap(result, 600, 600, true)
        val model = Generated.newInstance(this)
        var tbuffer = TensorImage.fromBitmap(resized)
        var byteBuffer = tbuffer.buffer

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)

        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var max = getMax(outputFeature0.floatArray)

        binding.resultNameTv.text = inputString[max]
        // Releases model resources if no longer used.

        for (i in 0..49){
            Log.d("Hasil", outputFeature0.floatArray[i].toString() + "" + inputString[i])
        }
        Log.d("qwwqd", outputs.toString())


        model.close()

        binding.imageResultIv.setImageBitmap(result)
    }

    private fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..49)
        {
            if(arr[i] > min)
            {
                ind = i
                min = arr[i]
            }
        }
        return ind
    }
}