package com.dicoding.diva.pimpledetectku.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.diva.pimpledetectku.R
import com.dicoding.diva.pimpledetectku.databinding.ActivityHomeBinding
import com.dicoding.diva.pimpledetectku.ml.Generated
import com.dicoding.diva.pimpledetectku.ui.camera.CameraActivity
import com.dicoding.diva.pimpledetectku.ui.hasil.HasilActivity
import com.dicoding.diva.pimpledetectku.ui.rotateBitmap
import com.dicoding.diva.pimpledetectku.ui.uriToFile
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var getFile: File? = null
    private lateinit var bitmap: Bitmap

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.menu_home)
        actionBar.setDisplayHomeAsUpEnabled(true)



        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.cameraBtn.setOnClickListener { startCameraX() }
        binding.galeriBtn.setOnClickListener { startGallery() }
        binding.detectorBtn.setOnClickListener { classification()
//            val intent = Intent(this@HomeActivity, HasilActivity::class.java)
//            startActivity(intent)
//            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )


            binding.previewImage.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@HomeActivity)
            getFile = myFile
//            binding.previewImage.setImageURI(selectedImg)

            bitmap = BitmapFactory.decodeFile(getFile?.path)

//            bitmap = result.data?.extras?.get("data") as Bitmap
            binding.previewImage.setImageBitmap(bitmap)
        }
    }

    private fun classification() {
//        val myFile = intent.getSerializableExtra("picture") as File
//        getFile = myFile
        val result = BitmapFactory.decodeFile(getFile?.path)

        //load label
        val label = "label.txt"
        val labelFile = application.assets.open(label).bufferedReader().use{ it.readText() }.split("\n")


        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val model = Generated.newInstance(this)
        val tBuffer = TensorImage.fromBitmap(resized)
        val byteBuffer = tBuffer.buffer
        Log.d("shape", byteBuffer.toString())

        // creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)
        Log.d("shape", inputFeature0.toString())

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray)

        binding.resultTv.text = labelFile[max]
        // Releases model resources if no longer used.
        model.close()

        for (i in 0..5){
            Log.d("Hasil", outputFeature0.floatArray[i].toString() + "" + labelFile[i])
        }
        Log.d("qwwqd", outputs.toString())
    }

    private fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..5)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }

}
