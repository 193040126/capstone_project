package com.dicoding.diva.pimpledetectku.ui.hasil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.diva.pimpledetectku.R
import com.dicoding.diva.pimpledetectku.databinding.ActivityHasilBinding
import com.dicoding.diva.pimpledetectku.ml.Model2
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.File


class HasilActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHasilBinding
    private var getFile: File? = null

    private val tfImageProcesor by lazy{
        ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }
    private val imageTensor = TensorImage(DataType.FLOAT32)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.hasil)

        val myFile = intent.getSerializableExtra("image") as File
        getFile = myFile
        val result = BitmapFactory.decodeFile(getFile?.path)

//        val extras = intent.extras
//        val byteArray = extras!!.getByteArray("image")
//        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
//        val image: ImageView = findViewById<View>() as ImageView
//        image.setImageBitmap(bmp)

        //load file txt
        val fileName = "label.txt"
        val labelFile = application.assets.open(fileName).bufferedReader().use{it.readText()}.split("\n")

        val resized = result.copy(Bitmap.Config.ARGB_8888, true)
        imageTensor.load(resized)

        val resultResized = tfImageProcesor.process(imageTensor)

        val model = Model2.newInstance(this)

        // Runs model inference and gets result.
        val outputs = model.process(resultResized.tensorBuffer)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray)
        binding.resultNameTv.text = labelFile[max]
        // Releases model resources if no longer used.

        for (i in 0..5){
            Log.d("Hasil", outputFeature0.floatArray[i].toString() + " " + labelFile[i])
        }
        Log.d("Output", outputs.toString())


        model.close()

        binding.imageResultIv.setImageBitmap(result)
    }

    private fun getMax(array:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..5)
        {
            if(array[i] > min)
            {
                min = array[i]
                ind = i;
            }
        }
        return ind
    }
}